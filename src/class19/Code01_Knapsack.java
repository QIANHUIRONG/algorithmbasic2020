package class19;


/*
题意：
给定两个长度都为N的数组weights和values，
weights[i]和values[i]分别代表 i号物品的重量和价值。
给定一个正数bag，表示一个载重bag的袋子，
你装的物品不能超过这个重量。
返回你能装下最多的价值是多少?
 */

/**
 * 时间：7分
 */

/*
思维导图
[从左往右的尝试模型]
1.
	process(int[] w, int[] v, int index, int rest) {}：当前考虑到了index号货物，背包容量还剩rest，
	从index.往后及其所有的货物可以自由选择，做的选择不能超过背包容量，返回最大价值
	basecase：index来到结尾时候，已经没有货物可以选了，最大价值返回0
	普遍过程：当前index号货物要和不要两种情况，求max

 */
public class Code01_Knapsack {

	// 所有的货，重量和价值，都在w和v数组里
	// 为了方便，其中没有负数
	// bag背包容量，不能超过这个载重
	// 返回：不超重的情况下，能够得到的最大价值
	public static int maxValue(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		return process(w, v, 0, bag);
	}

	// index 0~N
	// rest 负~bag
	public static int process(int[] w, int[] v, int index, int rest) {
		// 设置无效解的basecase
		// 你可以在basecase去拦住，告诉上游你这么调用是无效的
		// 也可以上游自己控制，不产生无效解才调用递归。都可以。比如if(rest - w[index]) > 0才去调用递归
		// 这里是在basecase去拦住
		if (rest < 0) {
			return -1;
		}
		if (index == w.length) {
			return 0;
		}
		int p1 = process(w, v, index + 1, rest); // 不要index位置货物
		int p2 = 0;
		int next = process(w, v, index + 1, rest - w[index]); // 要了index位置的货物
		if (next != -1) { // 要了index位置的货物，下游不会返回无效解，就真的可以要
			p2 = v[index] + next;
		}
		return Math.max(p1, p2);
	}

	// index 0~N
	// rest 负~bag
	public static int dp(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		int N = w.length;
		int[][] dp = new int[N + 1][bag + 1];
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= bag; rest++) {
				int p1 = dp[index + 1][rest];
				int p2 = 0;
				int next = rest - w[index] < 0 ? -1 : dp[index + 1][rest - w[index]];
				if (next != -1) {
					p2 = v[index] + next;
				}
				dp[index][rest] = Math.max(p1, p2);
			}
		}
		return dp[0][bag];
	}

	public static void main(String[] args) {
		int[] weights = { 3, 2, 4, 7, 3, 1, 7 };
		int[] values = { 5, 6, 3, 19, 12, 4, 2 };
		int bag = 15;
		System.out.println(maxValue(weights, values, bag));
		System.out.println(dp(weights, values, bag));
	}

}
