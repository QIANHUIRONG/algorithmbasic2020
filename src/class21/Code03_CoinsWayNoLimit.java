package class21;

/*
题意：面值数组组成面值的方法数-张数不限
给定数组arr，arr中所有的值都为正数且不重复
每个值代表一种面值的货币，每种面值的货币可以使用任意张
再给定一个整数 aim，代表要找的钱数
求组成 aim 的方法数

// 测试链接：力扣39-> 看看Problem_0039_CombinationSum
// https://leetcode.cn/problems/combination-sum/
 */
/*
时间；59
 */
/*
思维导图：
1.和上一题相比，这次每个位置的货币可以选多次，我就需要一个循环来枚举当前位置选的所有可能的张数
2.dp
3.斜率优化：
	 * 斜率优化
	 * 	假设当前来到9位置，arr[9] = 3元，rest = 14元，现在要求dp[9][14],表结构如下：
	 * 		2	5	8	11	14 （rest）
	 * 	9				y	x
	 * 	10	a	b	c	d	e
	 *（index）
	 *  	现在要求x位置，根据递归函数，它依赖于e（3元取0张）,d（3元取1张）,c,b,a这些位置， x = e + d + c + b + a;
	 *  	找附近的依赖关系：
	 *  	y位置依赖于d,c,b,a，并且y位置已经求过了（整个表从下到上，从左到右），y = d + c + b + a;
	 *  	则x = y + e
	 * 	也就是dp[index][rest] = dp[index][rest - arr[index]] + dp[index+1][rest]
 */



/*
暴力递归、记忆化搜索、动态规划：
暴力递归是有两个分支，就是2^N,有三个分支就是3^N等
动态规划是严格的表结构，这张表由简单的格子填到复杂的格子，如果每个格子都只依赖有限个格子，那就是O(m*n),但是如果有枚举行为，
就需要看能不能斜率优化，不然就不是O(m*n)了
记忆化搜索是算过的就不用算了，没算过的就去跑递归，如果没有枚举行为，它和动态规划一样优秀，时间复杂度一样好，但是如果有枚举行为，
就不如斜率优化过后的动态规划了。


笔试上，先写暴力递归
没有枚举，改记忆化搜索结束
有枚举行为，需要用严格表结构的动态规划，来观察是否可以斜率优化

面试上，先写暴力递归
没有枚举行为，记忆化搜索和动态规划问面试官他想要哪个
有枚举行为，动态规划，然后考虑是否能斜率优化

如果动态规划很难搞，搞记忆化搜索就够了！
 */

public class Code03_CoinsWayNoLimit {

	/**
	 * 暴力递归
	 * @param arr
	 * @param aim
	 * @return
	 */
	public static int coinsWay(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		return process(arr, 0, aim);
	}

	// 和上一题相比，这次每个位置的货币可以选多次，我就需要一个循环来枚举当前位置选的所有可能的张数
	// arr[index....] 所有的面值，每一个面值都可以任意选择张数，组成正好rest这么多钱，方法数多少？
	public static int process(int[] arr, int index, int rest) {
		// if (rest < 0) { // 递归中 zhang * arr[index] <= rest 上游控制着，永远走不到这个if (rest < 0)
//			return -1;
//		}
		if (index == arr.length) { // 没钱了
			return rest == 0 ? 1 : 0;
		}
		int ways = 0;
		for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
			ways += process(arr, index + 1, rest - (zhang * arr[index]));
		}
		return ways;
	}

	/**
	 * 动态规划
	 * @param arr
	 * @param aim
	 * @return
	 */
	public static int dp1(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ways = 0;
				for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
					ways += dp[index + 1][rest - (zhang * arr[index])];
				}
				dp[index][rest] = ways;
			}
		}
		return dp[0][aim];
	}

	/**
	 * 斜率优化
	 * 	假设当前来到9位置，arr[9] = 3元，rest = 14元，现在要求dp[9][14],表结构如下：
	 * 		2	5	8	11	14 （rest）
	 * 	9				y	x
	 * 	10	a	b	c	d	e
	 *（index）
	 *  	现在要求x位置，根据递归函数，它依赖于e（3元取0张）,d（3元取1张）,c,b,a这些位置， x = e + d + c + b + a;
	 *  	找附近的依赖关系：
	 *  	y位置依赖于d,c,b,a，并且y位置已经求过了（整个表从下到上，从左到右），y = d + c + b + a;
	 *  	则x = y + e
	 * 	也就是dp[index][rest] = dp[index][rest - arr[index]] + dp[index+1][rest]
	 * @param arr
	 * @param aim
	 * @return
	 */
	public static int dp2(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				dp[index][rest] = dp[index + 1][rest];
				if (rest - arr[index] >= 0) {
					dp[index][rest] += dp[index][rest - arr[index]];
				}
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		boolean[] has = new boolean[maxValue + 1];
		for (int i = 0; i < N; i++) {
			do {
				arr[i] = (int) (Math.random() * maxValue) + 1;
			} while (has[arr[i]]);
			has[arr[i]] = true;
		}
		return arr;
	}

	// 为了测试
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// 为了测试
	public static void main(String[] args) {
		int maxLen = 10;
		int maxValue = 30;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(maxLen, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = coinsWay(arr, aim);
			int ans2 = dp1(arr, aim);
			int ans3 = dp2(arr, aim);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
