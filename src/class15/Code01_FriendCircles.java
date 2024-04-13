package class15;

// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/friend-circles/
// 可以直接通过
/*
题意：
n个人，给你一个 n x n 的矩阵 isFriends ，其中 isFriends[i][j] = 1 表示第 i 个人和第 j 个人认识，
而 isFriends[i][j] = 0 表示二者不认识
返回矩阵中朋友圈数量
 */

/*
题解：使用并查集数据结构，认识就合并，最终返回有几个集合。
 */
public class Code01_FriendCircles {

	public static int findCircleNum(int[][] M) {
		int N = M.length;
		UnionFind unionFind = new UnionFind(N);
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (M[i][j] == 1) { // i和j互相认识
					unionFind.union(i, j);
				}
			}
		}
		return unionFind.sets();
	}

	public static class UnionFind {
		// parent[i] = k ： i的父亲是k
		private int[] parent;
		// size[i] = k ： 如果i是代表节点，size[i]才有意义，否则无意义
		// i所在的集合大小是多少
		private int[] size;
		// 辅助结构
		private int[] help;
		// 一共有多少个集合
		private int setCount;

		public UnionFind(int N) {
			parent = new int[N];
			size = new int[N];
			help = new int[N];
			setCount = N;
			for (int i = 0; i < N; i++) {
				parent[i] = i;
				size[i] = 1;
			}
		}

		// 从i开始一直往上，往上到不能再往上，代表节点，返回
		// 这个过程要做路径压缩
		private int find(int i) {
			int hi = 0;
			while (i != parent[i]) {
				help[hi++] = i;
				i = parent[i];
			}
			for (hi--; hi >= 0; hi--) {
				parent[help[hi]] = i;
			}
			return i;
		}

		public void union(int i, int j) {
			int f1 = find(i);
			int f2 = find(j);
			if (f1 != f2) {
				if (size[f1] >= size[f2]) {
					size[f1] += size[f2];
					parent[f2] = f1;
				} else {
					size[f2] += size[f1];
					parent[f1] = f2;
				}
				setCount--;
			}
		}

		public int sets() {
			return setCount;
		}
	}

}
