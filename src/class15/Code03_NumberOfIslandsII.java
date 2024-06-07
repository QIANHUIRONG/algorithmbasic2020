package class15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
题意：
假设你设计一个游戏，用一个 m 行 n 列的 2D 网格来存储你的游戏地图。
起始的时候，每个格子的地形都被默认标记为「水」。我们可以通过使用 addLand 进行操作，将位置 (row, col) 的「水」变成「陆地」。
你将会被给定一个列表，来记录所有需要被操作的位置，然后你需要返回计算出来 每次 addLand 操作后岛屿的数量。
注意：一个岛的定义是被「水」包围的「陆地」，通过水平方向或者垂直方向上相邻的陆地连接而成。你可以假设地图网格的四边均被无边无际的「水」所包围。
注意：一个岛的定义是被「水」包围的「陆地」，通过水平方向或者垂直方向上相邻的陆地连接而成。你可以假设地图网格的四边均被无边无际的「水」所包围。

请仔细阅读下方示例与解析，更加深入了解岛屿的判定。

示例:

输入: m = 3, n = 3, positions = [[0,0], [0,1], [1,2], [2,1]]
输出: [1,1,2,3]
解析:

起初，二维网格 grid 被全部注入「水」。（0 代表「水」，1 代表「陆地」）

0 0 0
0 0 0
0 0 0
操作 #1：addLand(0, 0) 将 grid[0][0] 的水变为陆地。

1 0 0
0 0 0   Number of islands = 1
0 0 0
操作 #2：addLand(0, 1) 将 grid[0][1] 的水变为陆地。

1 1 0
0 0 0   岛屿的数量为 1
0 0 0
操作 #3：addLand(1, 2) 将 grid[1][2] 的水变为陆地。

1 1 0
0 0 1   岛屿的数量为 2
0 0 0
操作 #4：addLand(2, 1) 将 grid[2][1] 的水变为陆地。

1 1 0
0 0 1   岛屿的数量为 3
0 1 0
拓展：

你是否能在 O(k log mn) 的时间复杂度程度内完成每次的计算？（k 表示 positions 的长度）

// 测试链接：https://leetcode.cn/problems/number-of-islands-ii/   -> 带锁的。。
*/

/*
时间：
1：32
 */
/*
时间复杂度：
	o(m*n) + o(k)， o(m*n)是并查集初始化m*n的parent[]，size[], o(k)是position的对数，一共k对，1对在并查集中都是o(1)的调整
 */
/*
思维导图：
	1.其实要是是每一步的岛的数量
	2.动态初始化的技巧：并查集一般都是一次性给你一堆集合，直接都放到并查集中去；本题是来一个，我往并查集塞一个
	3.构造器初始化的时候，只是初始化parent，help的长度，并没有塞值
	4.int connect(i, j)方法，[i][j]位置来了一个陆地，如果没有初始化，先初始化，然后请你把上下左右的陆地都连起来，返回岛屿数量，主函数遍历positions对数，收集每一次的答案。
	5.用size[i]是否等于0来标记i有没有被初始化，size[i]=0,从来没被初始化；size[i]=1,初始化过！正常情况：union的时候，小挂大，挂完之后，size[小的]清0；现在的情况：不清0，用size[i]是否等于0来标记是否初始化过
 */
public class Code03_NumberOfIslandsII {

	public static List<Integer> numIslands21(int m, int n, int[][] positions) {
		UnionFind1 uf = new UnionFind1(m, n);
		List<Integer> ans = new ArrayList<>(); // 收集每一次的答案
		for (int[] position : positions) {
			// 来一对我就收集一个答案
			ans.add(uf.connect(position[0], position[1]));
		}
		return ans;
	}

	public static class UnionFind1 {
		private int[] parent;
		// 用size[i] 等不等于0来标记i有没有被初始化。size[i]=0,从来没被初始化；size[i]=1,初始化过！
		// 正常情况：union的时候，小挂大，挂完之后，size[小的]清0
		// 现在的情况：不清空，用size[i]是否等于0来标记是否初始化过
		private int[] size;
		private int[] help;
		private final int row;
		private final int col;
		private int setCount;

		// 初始化的时候都是‘0’，所以只设置长度，没有做其他的初始化，因为现在一个'1'也没有
		public UnionFind1(int m, int n) {
			row = m;
			col = n;
			setCount = 0;
			int len = row * col;
			parent = new int[len];
			size = new int[len];
			help = new int[len];
		}


		/*
		[i][j]位置来了一个陆地，请你把上下左右的陆地都连起来，返回岛屿数量
		 */
		public int connect(int i, int j) {
			int index = index(i, j);
			if (size[index] == 0) { // 表示从来没有被初始化过，此时才需要初始化
				// 初始化index
				parent[index] = index;
				size[index] = 1;
				setCount++;
				// 上下左右合并
				union(i - 1, j, i, j);
				union(i + 1, j, i, j);
				union(i, j - 1, i, j);
				union(i, j + 1, i, j);
			}
			// 否则，就是重复初始化，不用管，不需要去动态合并
			return setCount;
		}

		private int index(int r, int c) {
			return r * col + c;
		}

		public int find(int i) {
			int j = -1; // 这个变量用来表示沿途的长度，不然不知道help[]数组什么时候遍历完
			// i不等于自己的父亲，就一直往上。什么时候i和自己的父亲是相等，就是i到了代表节点的时候
			while (parent[i] != i) {
				help[++j] = i;
				i = parent[i]; // 我来到我的父
			}
			for (int k = 0; k < j; k++) {
				parent[help[k]] = i;
			}
			return i;
		}

		private void union(int r1, int c1, int r2, int c2) {
			if (r1 < 0 || r1 == row || r2 < 0 || r2 == row || c1 < 0 || c1 == col || c2 < 0 || c2 == col) {
				return;
			}
			int x = index(r1, c1);
			int y = index(r2, c2);
			if (size[x] == 0 || size[y] == 0) { // 2个要连的如果有1个还没有被初始化，那么它就是水，不用连了
				return;
			}
			int fx = find(x);
			int fy = find(y);
			if (fx != fy) {
				if (size[fx] >= size[fy]) {
					size[fx] += size[fy];
					parent[fy] = fx;
					// size[fy]是不清空的！
				} else {
					size[fy] += size[fx];
					parent[fx] = fy;
				}
				setCount--;
			}
		}


	}

	// 课上讲的如果m*n比较大，会经历很重的初始化，而k比较小，怎么优化的方法
	public static List<Integer> numIslands22(int m, int n, int[][] positions) {
		UnionFind2 uf = new UnionFind2();
		List<Integer> ans = new ArrayList<>();
		for (int[] position : positions) {
			ans.add(uf.connect(position[0], position[1]));
		}
		return ans;
	}

	public static class UnionFind2 {
		private HashMap<String, String> parent;
		private HashMap<String, Integer> size;
		private ArrayList<String> help;
		private int sets;

		public UnionFind2() {
			parent = new HashMap<>();
			size = new HashMap<>();
			help = new ArrayList<>();
			sets = 0;
		}

		private String find(String cur) {
			while (!cur.equals(parent.get(cur))) {
				help.add(cur);
				cur = parent.get(cur);
			}
			for (String str : help) {
				parent.put(str, cur);
			}
			help.clear();
			return cur;
		}

		private void union(String s1, String s2) {
			if (parent.containsKey(s1) && parent.containsKey(s2)) {
				String f1 = find(s1);
				String f2 = find(s2);
				if (!f1.equals(f2)) {
					int size1 = size.get(f1);
					int size2 = size.get(f2);
					String big = size1 >= size2 ? f1 : f2;
					String small = big == f1 ? f2 : f1;
					parent.put(small, big);
					size.put(big, size1 + size2);
					sets--;
				}
			}
		}

		public int connect(int r, int c) {
			String key = String.valueOf(r) + "_" + String.valueOf(c);
			if (!parent.containsKey(key)) {
				parent.put(key, key);
				size.put(key, 1);
				sets++;
				String up = String.valueOf(r - 1) + "_" + String.valueOf(c);
				String down = String.valueOf(r + 1) + "_" + String.valueOf(c);
				String left = String.valueOf(r) + "_" + String.valueOf(c - 1);
				String right = String.valueOf(r) + "_" + String.valueOf(c + 1);
				union(up, key);
				union(down, key);
				union(left, key);
				union(right, key);
			}
			return sets;
		}

	}

}
