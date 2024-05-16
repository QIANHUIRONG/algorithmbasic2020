package class32;

// 测试链接：https://leetcode.com/problems/range-sum-query-2d-mutable
// 但这个题是付费题目
// 提交时把类名、构造函数名从Code02_IndexTree2D改成NumMatrix

/*
题意：1：17
流程：1：19
code：1：23
求左上角到右下角的累加和：1：27
 */

/*
【导图笔记】
indexTree改写多维要比线段树容易得多。

1.help[i][j]: 原数组中以1行1列做左上角，行列做右下角这一整块的累加和这个值填在这个格子里
2.当我某一个位置的值变了的时候
	行号管辖的范围：把最右侧的1抹掉+1~它自己
	列号管辖的范围：把最右侧的1抹掉+1~它自己
	help数组里受影跏向的范围：上面行，列影响到的所有范围
3.任意左上角，到任意一个右下角的点框住的累加和是多少？——1：27
4.时间复杂度:O(log行*log列)
 */
public class Code02_IndexTree2D {
	private int[][] help; // 二维index tree
	private int[][] nums; // 用来保存原数组的值
	private int N;
	private int M;

	public Code02_IndexTree2D(int[][] matrix) {
		if (matrix.length == 0 || matrix[0].length == 0) {
			return;
		}
		N = matrix.length;
		M = matrix[0].length;
		help = new int[matrix.length + 1][matrix[0].length];
		nums = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// 初始化index tree
				update(i, j, matrix[i][j]);
			}
		}
	}

	public void update(int row, int col, int val) {
		int add = val - nums[row][col]; // 要累加的值。因为题目是update()方法，所以就这么写了
		nums[row][col] = val;
		// 把原数组arr[row][col]变成val了，那么对应到help[]数组就是help[row+1][col+1]
		for (int i = row + 1; i <= N; i += i & (-i)) {
			for (int j = col + 1; j <= M; j += j & (-j)) {
				help[i][j] += add;
			}
		}
	}

	// 求[1,1]到[row,col]的累加和
	private int sum(int row, int col) {
		int sum = 0;
		for (int i = row + 1; i > 0; i -= i & (-i)) {
			for (int j = col + 1; j > 0; j -= j & (-j)) {
				sum += help[i][j];
			}
		}
		return sum;
	}

	// 求[row1,col1] 到 [row2,col2]的累加和
	public int sumRegion(int row1, int col1, int row2, int col2) {
		// 会多减掉一个sum(row1 - 1, col1 - 1)，所以还得加回来
		return sum(row2, col2) - sum(row1 - 1, col2) - sum(row2, col1 - 1) + sum(row1 - 1, col1 - 1);
	}

}
