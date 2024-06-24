package class39;

/*
【题意】背包中有多少种零食放法
背包容量为w
一共有n袋零食, 第i袋零食体积为v[i]
总体积不超过背包容量的情况下，
一共有多少种零食放法？(总体积为0也算一种放法)。
 */
/*
【时间】
题意：46
用经典流程：48
用分治，code：52 【在下一个文件】
整合逻辑：1：11
总结：1：22
 */

/*
【思维导图】
经典流程：从左到右尝试模型
1、
	int process(int[] arr, int index, int rest) ：当前来到index位置，index及其之后的零食自由选择，还剩的容量是rest，返回选择方案
	basecase：i=arr.length,return rest < 0 ? 0 : 1; // 收集一种方法数，叫做我之前做过的决定，求方法数的都是这个套路
	主流程：
		可能性一：不要index位置的数
		可能性二：要index位置的数
		两种可能性的方法数累加

3、本题给的数据量是：
链接：https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
输入包括两行
第一行为两个正整数n和w(1 <= n <= 30, 1 <= w <= 2 * 10^9),表示零食的数量和背包的容量。
第二行n个正整数v[i](0 <= v[i] <= 10^9),表示每袋零食的体积。

这里的w这么大，经典流程就不行了-> 用分治，见Code02_SnacksWaysMain1
 */
public class Code02_SnacksWays {

	/*
	暴力递归
	 */
	public static int ways1(int[] arr, int w) {
		// arr[0...]
		return process(arr, 0, w);
	}

	// 从左往右的经典模型
	// 当前来到index位置，index及其之后的零食自由选择，还剩的容量是rest，返回选择方案
	public static int process(int[] arr, int index, int rest) {
//		if (rest < 0) { // 没有容量了
//			return -1; // -1 无方案的意思
//		}
		if (index == arr.length) { // 无零食可选
			return rest < 0 ? 0 : 1; // 收集一种方法数，叫做我之前做过的决定
		}
		// index号零食，要 or 不要
		int next1 = process(arr, index + 1, rest); // 不要
		int next2 = process(arr, index + 1, rest - arr[index]); // 要
		return next1 +  next2;
	}

	/*
	动态规划
	 */
	public static int ways2(int[] arr, int w) {
		int N = arr.length;
		int[][] dp = new int[N + 1][w + 1];
		for (int j = 0; j <= w; j++) {
			dp[N][j] = 1;
		}
		for (int i = N - 1; i >= 0; i--) {
			for (int j = 0; j <= w; j++) {
				dp[i][j] = dp[i + 1][j] + ((j - arr[i] >= 0) ? dp[i + 1][j - arr[i]] : 0);
			}
		}
		return dp[0][w];
	}

	/*
	不知道啥东西，没讲
	 */
	public static int ways3(int[] arr, int w) {
		int N = arr.length;
		int[][] dp = new int[N][w + 1];
		for (int i = 0; i < N; i++) {
			dp[i][0] = 1;
		}
		if (arr[0] <= w) {
			dp[0][arr[0]] = 1;
		}
		for (int i = 1; i < N; i++) {
			for (int j = 1; j <= w; j++) {
				dp[i][j] = dp[i - 1][j] + ((j - arr[i]) >= 0 ? dp[i - 1][j - arr[i]] : 0);
			}
		}
		int ans = 0;
		for (int j = 0; j <= w; j++) {
			ans += dp[N - 1][j];
		}
		return ans;
	}

	public static void main(String[] args) {
		int[] arr = { 4, 3, 2, 9 ,3,4,7,46,89,102,65,23,46,57,8,9,33,27,65,33,88};
		int w = 120;
		System.out.println(ways1(arr, w));
		System.out.println(ways2(arr, w));
		System.out.println(ways3(arr, w));

	}

}
