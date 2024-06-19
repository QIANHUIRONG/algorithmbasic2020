package class23;

/*
题意：正数数组分割为累加和接近的两个集合
给定一个正数数组arr，
请把arr中所有的数分成两个集合，尽量让两个集合的累加和接近
返回：
最接近的情况下，较小集合的累加和
 */
/*
时间：08
 */
/*
思维导图：
[从左到右的尝试模型]
1.题解：其实就是改背包问题。总和不要超过累加和/2（向下取整），最接近是多少
2.
	process(int[] arr, int i, int rest) {}：arr[i...]可以自由选择，请返回累加和尽量接近rest，但不能超过rest的情况下，最接近的累加和是多少？（背包问题是求=rest）
	basecase：i来到了arr的结尾，没得选了，返回最接近rest的累加和就是0
	普遍流程：
		1.可能性一：不要i位置的数
		2.可能性二：如果要了i位置的数，不会超过rest，就有可能性二
		两种求max，就是最接近rest的
3.dp
 */
public class Code01_SplitSumClosed {

	public static int right(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int sum = 0;
		for (int num : arr) {
			sum += num;
		}
		return process(arr, 0, sum / 2);
	}

	// arr[i...]可以自由选择，请返回累加和尽量接近rest，但不能超过rest的情况下，最接近的累加和是多少？
	// 本质是求的较小集合最大能推到多大
	public static int process(int[] arr, int i, int rest) {
		if (i == arr.length) {
			return 0;
		} else { // 还有数，arr[i]这个数
			// 可能性1，不使用arr[i]
			int p1 = process(arr, i + 1, rest);
			// 可能性2，要使用arr[i]
			int p2 = 0;
			if (arr[i] <= rest) {
				p2 = arr[i] + process(arr, i + 1, rest - arr[i]);
			}
			return Math.max(p1, p2);
		}
	}

	public static int dp(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int sum = 0;
		for (int num : arr) {
			sum += num;
		}
		sum /= 2;
		int N = arr.length;
		int[][] dp = new int[N + 1][sum + 1];
		for (int i = N - 1; i >= 0; i--) {
			for (int rest = 0; rest <= sum; rest++) {
				// 可能性1，不使用arr[i]
				int p1 = dp[i + 1][rest];
				// 可能性2，要使用arr[i]
				int p2 = 0;
				if (arr[i] <= rest) {
					p2 = arr[i] + dp[i + 1][rest - arr[i]];
				}
				dp[i][rest] = Math.max(p1, p2);
			}
		}
		return dp[0][sum];
	}

	public static int[] randomArray(int len, int value) {
		int[] arr = new int[len];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * value);
		}
		return arr;
	}

	public static void printArray(int[] arr) {
		for (int num : arr) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int maxLen = 20;
		int maxValue = 50;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * maxLen);
			int[] arr = randomArray(len, maxValue);
			int ans1 = right(arr);
			int ans2 = dp(arr);
			if (ans1 != ans2) {
				printArray(arr);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
