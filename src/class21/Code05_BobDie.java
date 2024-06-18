package class21;

/*
题意：Bob生还的概率
给定5个参数，N，M，row，col，k
表示在N*M的区域上，醉汉Bob初始在(row,col)位置
Bob一共要迈出k步，且每步都会等概率向上下左右四个方向走一个单位
任何时候Bob只要离开N*M的区域，就直接死亡
返回k步之后，Bob还在N*M的区域的概率
 */
/*
1：53
 */

/*
思维导图：
1.一共有多少种走的情况：假设棋盘完全的无限大。你一步就等概率上下左右四个方向，所以总的情况数4^K
那我走完K步之后，我只要还在棋盘里，我就收集他的一个生存点数。生存的点数除以4^K就是他活下来的概率
2.
	process(int row, int col, int rest, int N, int M) {}:目前在row，col位置，还有rest步要走，走完了如果还在棋盘中就获得1个生存点，返回总的生存点数
	basecase:
		越界了，直接返回0
		没有越界，rest已经等于0了，返回1个生存点数
	普遍流程：可以往上下左右4个方向走，累加方法数返回
3.改dp：和象棋跳马问题一样，3维的

 */
public class Code05_BobDie {

	public static double livePosibility1(int row, int col, int k, int N, int M) {
		return (double) process(row, col, k, N, M) / Math.pow(4, k);
	}

	// 目前在row，col位置，还有rest步要走，走完了如果还在棋盘中就获得1个生存点，返回总的生存点数
	public static long process(int row, int col, int rest, int N, int M) {
		if (row < 0 || row == N || col < 0 || col == M) {
			return 0;
		}
		// 还在棋盘中！
		if (rest == 0) {
			return 1;
		}
		// 还在棋盘中！还有步数要走
		long up = process(row - 1, col, rest - 1, N, M);
		long down = process(row + 1, col, rest - 1, N, M);
		long left = process(row, col - 1, rest - 1, N, M);
		long right = process(row, col + 1, rest - 1, N, M);
		return up + down + left + right;
	}

	public static double livePosibility2(int row, int col, int k, int N, int M) {
		long[][][] dp = new long[N + 1][M + 1][k + 1];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j <= M; j++) {
				dp[i][j][0] = 1;
			}
		}
		for (int rest = 1; rest <= k; rest++) {
			for (int r = 0; r <= N; r++) {
				for (int c = 0; c <= M; c++) {
					dp[r][c][rest] = pick(dp, N + 1, M + 1, r - 1, c, rest - 1);
					dp[r][c][rest] += pick(dp, N + 1, M + 1, r + 1, c, rest - 1);
					dp[r][c][rest] += pick(dp, N + 1, M + 1, r, c - 1, rest - 1);
					dp[r][c][rest] += pick(dp, N + 1, M + 1, r, c + 1, rest - 1);
				}
			}
		}
		return (double) dp[row][col][k] / Math.pow(4, k);
	}

	public static long pick(long[][][] dp, int N, int M, int r, int c, int rest) {
		if (r < 0 || r == N || c < 0 || c == M) {
			return 0;
		}
		return dp[r][c][rest];
	}

	public static void main(String[] args) {
		System.out.println(livePosibility1(6, 6, 10, 50, 50));
		System.out.println(livePosibility2(6, 6, 10, 50, 50));
	}

}
