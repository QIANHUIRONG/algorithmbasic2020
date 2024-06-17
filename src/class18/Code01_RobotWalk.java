package class18;

/*
题意：
假设有排成一行的N个位置，记为1~N，N 一定大于或等于 2
开始时机器人在其中的M位置上(M 一定是 1~N 中的一个)
如果机器人来到1位置，那么下一步只能往右来到2位置；
如果机器人来到N位置，那么下一步只能往左来到 N-1 位置；
如果机器人来到中间位置，那么下一步可以往左走或者往右走；
规定机器人必须走 K 步，最终能来到P位置(P也是1~N中的一个)的方法有多少种
给定四个参数 N、M、K、P，返回方法数。
 */
/**
 * 时间：21。改动态规划：57
 */
/*
题解：
一、暴力递归
1.process1(int cur, int rest, int aim, int N) {}：机器人当前来到的位置是cur，机器人还有rest步需要去走，最终的目标是aim，有哪些位置？1~N
返回：机器人从cur出发，走过rest步之后，最终停在aim的方法数，是多少？
2.basecase：如果rest=0，已经走完了，如果当前位置就是目标位置，返回1种方法，否则返回0种
3.普遍流程：机器人要么往左走，要么往右走；

二、傻缓存



三、动态规划
1.改动态规划的过程已经不需要原题意了，看着暴力递归就足够改出DP了
2.暴力递归的分析过程抽象出来，就是动态规划的转移过程，任何一个动态规划，都是由暴力递归尝试的种子改过来的；
有些暴力递归不能改成动态规划的原因：暴力递归没有重复过程；

改动态规划的过程：
	1.先分析dp表的大小
	2.填basecase
	3.看最终想要的，也就是最终返回的递归
	3.分析普遍位置怎么依赖的


 */
public class Code01_RobotWalk {

	/**
	 * 暴力递归版本
	 * @param N
	 * @param start
	 * @param aim
	 * @param K
	 * @return
	 */
	public static int ways1(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		return process1(start, K, aim, N);
	}

	// 机器人当前来到的位置是cur，
	// 机器人还有rest步需要去走，
	// 最终的目标是aim，
	// 有哪些位置？1~N
	// 返回：机器人从cur出发，走过rest步之后，最终停在aim的方法数，是多少？
	public static int process1(int cur, int rest, int aim, int N) {
		if (rest == 0) { // 如果已经不需要走了，走完了！
			return cur == aim ? 1 : 0;
		}
		// (cur, rest)
		if (cur == 1) { // 1 -> 2
			return process1(2, rest - 1, aim, N);
		}
		// (cur, rest)
		if (cur == N) { // N-1 <- N
			return process1(N - 1, rest - 1, aim, N);
		}
		// (cur, rest)
		return process1(cur - 1, rest - 1, aim, N) + process1(cur + 1, rest - 1, aim, N);
	}

	/**
	 * 记忆化搜索版本
	 * @param N
	 * @param start
	 * @param aim
	 * @param K
	 * @return
	 */
	public static int ways2(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		int[][] dp = new int[N + 1][K + 1];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j <= K; j++) {
				dp[i][j] = -1;
			}
		}
		// dp就是缓存表
		// dp[cur][rest] == -1 -> process1(cur, rest)之前没算过！
		// dp[cur][rest] != -1 -> process1(cur, rest)之前算过！返回值，dp[cur][rest]
		// N+1 * K+1
		return process2(start, K, aim, N, dp);
	}

	// cur 范: 1 ~ N
	// rest 范：0 ~ K
	public static int process2(int cur, int rest, int aim, int N, int[][] dp) {
		if (dp[cur][rest] != -1) {
			return dp[cur][rest];
		}
		// 之前没算过！
		int ans = 0;
		if (rest == 0) {
			ans = cur == aim ? 1 : 0;
		} else if (cur == 1) {
			ans = process2(2, rest - 1, aim, N, dp);
		} else if (cur == N) {
			ans = process2(N - 1, rest - 1, aim, N, dp);
		} else {
			ans = process2(cur - 1, rest - 1, aim, N, dp) + process2(cur + 1, rest - 1, aim, N, dp);
		}
		dp[cur][rest] = ans;
		return ans;

	}

	/**
	 * 动态规划版本
	 * 1.先看basecase
	 * 2.看最终想要的，也就是最终返回的递归
	 * 3.分析普遍位置怎么依赖的
	 * @param N
	 * @param start
	 * @param aim
	 * @param K
	 * @return
	 */
	public static int ways3(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		int[][] dp = new int[N + 1][K + 1];
		dp[aim][0] = 1; // 其他位置默认都是0
		for (int rest = 1; rest <= K; rest++) {
			dp[1][rest] = dp[2][rest - 1];
			for (int cur = 2; cur < N; cur++) {
				dp[cur][rest] = dp[cur - 1][rest - 1] + dp[cur + 1][rest - 1];
			}
			dp[N][rest] = dp[N - 1][rest - 1];
		}
		return dp[start][K];
	}

	public static void main(String[] args) {
		System.out.println(ways1(5, 2, 4, 6));
		System.out.println(ways2(5, 2, 4, 6));
		System.out.println(ways3(5, 2, 4, 6));
	}

}
