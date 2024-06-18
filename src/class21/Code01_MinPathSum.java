package class21;

/*
题意：
给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
说明：每次只能向下或者向右移动一步。
// 测试链接：64 https://leetcode.cn/problems/minimum-path-sum/description/
 */
/*
时间：5
空间压缩1.0版本：17
空间压缩推广：30
 */
/*
思维导图：
非常经典
	1.题意就是左上角->右下角，最小路径和是多少；
	dp[i][j]表示i,j位置到右下角的最小路径是多少？
	i,j位置只能往下走或者往右走，所以状态转移方程为
	dp[i][j] = min(dp[i + 1][j], dp[i][j + 1]) + matrix[i][j];

	2.空间压缩技巧：
	我们把dp[i][j]定义反一下，变成左上角到[i][j]的最短路径；
	i,j位置要么从左边来，要么从上边来，状态转移方程为：
	dp[i][j] = min(dp[i - 1][j],dp[i][j - 1]) + matrix[i][j];
	----> |
	----> |
	---->\|/
	dp[][]表从左往右填，从上往下填写，这是可以空间压缩的。

	m[][]:				dp[][]
	3	2	6	1		3	5	11	12	只需要第一行的值滚动下去，就可以填完整个dp表
	2	2	4	3		5	7	11	14	填每一行时，只需要上面一格的值和左边格子的值
	1	1	8	1		6	7	15	15

	只需要一个一维数组：
	[3	5	11	12],它就可以更新出第二行的：
	[5	7	11	14]
	比如第二行的5：依赖上一个格子，就是dp[0] = 3（还没自我更新，就代表上一个格子）,加上matrix[1][0] = 2，就自我更新出5；
	比如第二行的7，依赖左边格子，dp[0] = 5(已经自我更新了), 和上边格子dp[1] = 5（还没自我更新，就代表上一个格子），min(5, 5) + matrix[1][1] = 7
	...这样一行一行更新下去即可。


	3.空间压缩推广
	3.1
	如果发现一个动态规划，只依赖左边和上边，就是本题讲的，可以空间压缩
	如果发现一个动态规划，只依赖左边和上边和左上边，多一个临时变量记录，可以空间压缩（时间：34）
	如果发现一个动态规划，只依赖上边和左上边，可以从右往左遍历，也可以多一个临时变量记着
	3.2 如果dp是N*M大小的，M小，就纵向更新；N小就横向更新


	4.空间压缩技巧是面试装逼用的，可以先写非压缩版的，然后跟面试官聊空间压缩，笔试，刷题时用不用无所谓。
 */


public class Code01_MinPathSum {
	/*
	时间：5
	经典动态规划
	 */
	public static int minPathSum1(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[][] dp = new int[row][col];
		// dp[i][j]表示：从[0][0]位置走到[i][j]位置的最短距离是多少？
		dp[0][0] = m[0][0];
		for (int i = 1; i < row; i++) {
			// 第一行只能从左边来
			dp[i][0] = dp[i - 1][0] + m[i][0];
		}
		for (int j = 1; j < col; j++) {
			// 第一列只能从上边来
			dp[0][j] = dp[0][j - 1] + m[0][j];
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				// 普遍位置，我左边和我上边，谁小就从那边来
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
			}
		}
		return dp[row - 1][col - 1];
	}

	/**
	 * 空间压缩
	 * @param m
	 * @return
	 */
	public static int minPathSum2(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		/*只申请了一行
		原本那个dp[][]存在于脑海中 */
		int[] dp = new int[col];
		dp[0] = m[0][0];

		// 初始化第一行的值，只依赖左边。想象中二维表的第0行数据
		for (int j = 1; j < col; j++) {
			dp[j] = dp[j - 1] + m[0][j];
		}

		// dp此时是想象中二维表的第i-1行数据
		// 想更新成，想象中二维表的第i行数据
		// 普遍位置的值，依赖左边和上边。左边就是dp[j - 1]求好了，上边就是我自己还没更新的样子dp[j]
		for (int i = 1; i < row; i++) {
			dp[0] += m[i][0];
			for (int j = 1; j < col; j++) {

				dp[j] = Math.min(dp[j - 1], dp[j]) + m[i][j];
			}
		}
		return dp[col - 1];
	}

	// for test
	public static int[][] generateRandomMatrix(int rowSize, int colSize) {
		if (rowSize < 0 || colSize < 0) {
			return null;
		}
		int[][] result = new int[rowSize][colSize];
		for (int i = 0; i != result.length; i++) {
			for (int j = 0; j != result[0].length; j++) {
				result[i][j] = (int) (Math.random() * 100);
			}
		}
		return result;
	}

	// for test
	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int rowSize = 10;
		int colSize = 10;
		int[][] m = generateRandomMatrix(rowSize, colSize);
		System.out.println(minPathSum1(m));
		System.out.println(minPathSum2(m));

	}
}
