package class21;

/*
题意：货币数组组成面值的方法数-同值认为不同
arr是货币数组，其中的值都是正数。再给定一个正数aim。
每个值都认为是一张货币，
即便是值相同的货币也认为每一张都是不同的，
返回组成aim的方法数
例如：arr = {1,1,1}，aim = 2
第0个和第1个能组成2，第1个和第2个能组成2，第0个和第2个能组成2
一共就3种方法，所以返回3
 */
/*
时间：46
 */
/*
思维导图：
[从左到右尝试模型]
1.
	process(int[] arr, int index, int rest) {}：index之后的货币自由选择，返回组成正好rest这么多的钱，有几种方法
	basecase:if (index == arr.length), 选完了，如果rest=0，返回1种方法否则返回0种
	普遍流程：当前位置要和不要两种情况，累加

2.dp

 */
public class Code02_CoinsWayEveryPaperDifferent {

	public static int coinWays(int[] arr, int aim) {
		return process(arr, 0, aim);
	}

	// index之后的货币自由选择，返回组成正好rest这么多的钱，有几种方法
	// 题解：
	// 货币值相同，也认为每一张都是不同的，本质就是每个位置都不同，我就去尝试每一个位置要和不要两种情况就行
	public static int process(int[] arr, int index, int rest) {
		if (rest < 0) { // 设置无效解
			return 0;
		}
		if (index == arr.length) { // 没钱了！
			return rest == 0 ? 1 : 0;
		} else {
			return process(arr, index + 1, rest) + process(arr, index + 1, rest - arr[index]);
		}
	}

	public static int dp(int[] arr, int aim) {
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				// rest - arr[index] 如果小于0，在递归中，basecase的 if (rest < 0) return 0 会返回0，所以这里就这么写
				dp[index][rest] = dp[index + 1][rest] + (rest - arr[index] >= 0 ? dp[index + 1][rest - arr[index]] : 0);
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		for (int i = 0; i < N; i++) {
			arr[i] = (int) (Math.random() * maxValue) + 1;
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
		int maxLen = 20;
		int maxValue = 30;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(maxLen, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = coinWays(arr, aim);
			int ans2 = dp(arr, aim);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
