package class15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/number-of-islands/
// 所有方法都可以直接通过
/*
题意：
给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
此外，你可以假设该网格的四条边均被水包围。
 */


public class Code02_NumberOfIslands {

	/*
	方法一：递归
	遍历所有的位置，把所有连成一片的'1'字符，变成0
	时间复杂度：O（N * M），也是最优解，看似暴力，但是递归中每个节点最多到自己4次。
	 */
	public static int numIslands3(char[][] board) {
		int islands = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == '1') {
					//如果当前是岛，岛数量++，并且把相邻的岛也感染了
					islands++;
					infect(board, i, j);
				}
			}
		}
		return islands;
	}

	// 从(i,j)这个位置出发，把所有连成一片的'1'字符，变成0
	public static void infect(char[][] board, int i, int j) {
		// 遇到边界、或者水、或者感染过了都直接return；
		if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != '1') {
			return;
		}
		// 感染了，下一次递归就不会走了
		board[i][j] = 0;
		// 感染上下左右
		infect(board, i - 1, j);
		infect(board, i + 1, j);
		infect(board, i, j - 1);
		infect(board, i, j + 1);
	}

	/*
	方法二：并查集。（这里是哈希表实现的并查集，去看下面还有效率更高的数组实现的）
	 */
	public static int numIslands1(char[][] board) {
		int n = board.length;
		int m = board[0].length;
		Dot[][] dots = new Dot[n][m]; // 包一层对象数组
		List<Dot> dotList = new ArrayList<>(); // 收集list，用于初始化并查集
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (board[i][j] == '1') {
					dots[i][j] = new Dot();
					dotList.add(dots[i][j]);
				}
			}
		}
		UnionFind1<Dot> uf = new UnionFind1<>(dotList); // 初始化并查集
		for (int j = 1; j < m; j++) {
			// (0,j)  (0,0)跳过了  (0,1) (0,2) (0,3)
			if (board[0][j - 1] == '1' && board[0][j] == '1') {
				uf.union(dots[0][j - 1], dots[0][j]);
			}
		}
		for (int i = 1; i < n; i++) {
			if (board[i - 1][0] == '1' && board[i][0] == '1') {
				uf.union(dots[i - 1][0], dots[i][0]);
			}
		}
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				if (board[i][j] == '1') {
					if (board[i][j - 1] == '1') {
						uf.union(dots[i][j - 1], dots[i][j]);
					}
					if (board[i - 1][j] == '1') {
						uf.union(dots[i - 1][j], dots[i][j]);
					}
				}
			}
		}
		return uf.sets();
	}


	/*
	1、如果当前位置是‘1’，看自己左边和上边，如果也是‘1’，就合并。（因为是从左到右，从上到下遍历，所以遍历完之后，该合并的都合并了）
	2、两个位置都是‘1’，怎么认为他们是不同的？包一层对象，也生成一个m*n的对象数组
	 */
	public static int numIslands2(char[][] board) {
		int n = board.length;
		int m = board[0].length;
		// 初始化并查集
		UnionFind2 uf = new UnionFind2(board);

		// 合并
		for (int j = 1; j < m; j++) { // 遍历第一行去合并
			if (board[0][j - 1] == '1' && board[0][j] == '1') {
				uf.union(0, j - 1, 0, j);
			}
		}
		for (int i = 1; i < n; i++) { // 遍历第一列去合并
			if (board[i - 1][0] == '1' && board[i][0] == '1') {
				uf.union(i - 1, 0, i, 0);
			}
		}
		for (int i = 1; i < n; i++) { // 遍历其他所有去合并
			for (int j = 1; j < m; j++) {
				if (board[i][j] == '1') {
					if (board[i][j - 1] == '1') { // 合并左
						uf.union(i, j - 1, i, j);
					}
					if (board[i - 1][j] == '1') { // 合并上
						uf.union(i - 1, j, i, j);
					}
				}
			}
		}
		return uf.setCount();
	}

	public static class UnionFind2 {
		private int[] parent;
		private int[] size;
		private int[] help;
		private int m;
		private int setCount;

		public UnionFind2(char[][] board) {
			// 初始化长度
			setCount = 0;
			int n = board.length; // 行数
			m = board[0].length; // 列数。使用成员变量是因为二位数组转一维需要用
			int len = n * m; // 总长
			parent = new int[len];
			size = new int[len];
			help = new int[len];

			// 初始化元素
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (board[i][j] == '1') { // 是岛才初始化元素放入并查集
						// 二维数组转一维
						// 原本是[i][j], 那么对应一维下标就是 i * 列数 + j
						int index = index(i, j);
						parent[index] = index;
						size[index] = 1;
						setCount++;
					}
				}
			}
		}

		// (r,c) -> i
		private int index(int i, int j) {
			return i * m + j;
		}

		// 原始位置 -> 下标
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

		public void union(int r1, int c1, int r2, int c2) {
			// 合并的时候，先计算对应的一维下标，再合并
			int i1 = index(r1, c1);
			int i2 = index(r2, c2);
			int f1 = find(i1);
			int f2 = find(i2);
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

		public int setCount() {
			return setCount;
		}

	}



	public static class Dot {

	}

	public static class Node<V> {

		V value;

		public Node(V v) {
			value = v;
		}

	}

	public static class UnionFind1<V> {
		public HashMap<V, Node<V>> nodes;
		public HashMap<Node<V>, Node<V>> parents;
		public HashMap<Node<V>, Integer> sizeMap;

		public UnionFind1(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		public void union(V a, V b) {
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				parents.put(small, big);
				sizeMap.put(big, aSetSize + bSetSize);
				sizeMap.remove(small);
			}
		}

		public int sets() {
			return sizeMap.size();
		}

	}

	// 为了测试
	public static char[][] generateRandomMatrix(int row, int col) {
		char[][] board = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				board[i][j] = Math.random() < 0.5 ? '1' : '0';
			}
		}
		return board;
	}

	// 为了测试
	public static char[][] copy(char[][] board) {
		int row = board.length;
		int col = board[0].length;
		char[][] ans = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				ans[i][j] = board[i][j];
			}
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int row = 0;
		int col = 0;
		char[][] board1 = null;
		char[][] board2 = null;
		char[][] board3 = null;
		long start = 0;
		long end = 0;

		row = 1000;
		col = 1000;
		board1 = generateRandomMatrix(row, col);
		board2 = copy(board1);
		board3 = copy(board1);

		System.out.println("感染方法、并查集(map实现)、并查集(数组实现)的运行结果和运行时间");
		System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

		start = System.currentTimeMillis();
		System.out.println("感染方法的运行结果: " + numIslands3(board1));
		end = System.currentTimeMillis();
		System.out.println("感染方法的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(map实现)的运行结果: " + numIslands1(board2));
		end = System.currentTimeMillis();
		System.out.println("并查集(map实现)的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
		end = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

		System.out.println();

		row = 10000;
		col = 10000;
		board1 = generateRandomMatrix(row, col);
		board3 = copy(board1);
		System.out.println("感染方法、并查集(数组实现)的运行结果和运行时间");
		System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

		start = System.currentTimeMillis();
		System.out.println("感染方法的运行结果: " + numIslands3(board1));
		end = System.currentTimeMillis();
		System.out.println("感染方法的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
		end = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

	}

}
