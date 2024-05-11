package class22;

/*
时间：1：20
 */
public class 	Code02_MinCoinsNoLimit {

	public static int minCoins(int[] arr, int aim) {
		return process(arr, 0, aim);
	}

	// arr[index...]面值，每种面值张数自由选择，
	// 搞出rest正好这么多钱，返回最小张数
	// Integer.MAX_VALUE标记：怎么都搞定不了
	// 		和硬币找零专题第二题类似，第二题是每个面值可以使用无限张，要求方法数；本题是要求最少张数，递归把每种
	//可行的方法张数抓一下，全局最小就是要求的。
	public static int process(int[] arr, int index, int rest) {
		if (index == arr.length) {
			return rest == 0 ? 0 : Integer.MAX_VALUE;
		} else {
			int ans = Integer.MAX_VALUE;
			for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
				int next = process(arr, index + 1, rest - zhang * arr[index]);
				if (next != Integer.MAX_VALUE) {
					ans = Math.min(ans, zhang + next);
				}
			}
			return ans;
		}
	}

	// 没有斜率优化的动态规划
	public static int dp1(int[] arr, int aim) {
		if (aim == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 0;
		for (int j = 1; j <= aim; j++) {
			dp[N][j] = Integer.MAX_VALUE;
		}
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ans = Integer.MAX_VALUE;
				for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
					int next = dp[index + 1][rest - zhang * arr[index]];
					if (next != Integer.MAX_VALUE) {
						ans = Math.min(ans, zhang + next);
					}
				}
				dp[index][rest] = ans;
			}
		}
		return dp[0][aim];
	}

	/*
	假设当前来到9位置，arr[9] = 3元, rest = 14元，现在要求dp[9][14],表结构如下：
		2	5	8	11	14 （rest）
	9				y	x
	10	a	b	c	d	e
	（index）
	 dp[index][rest]表示arr[index...]往后的货币自由选择，凑出rest元的最少张数
	 x依赖于min(e,d + 1（还要多一张才能凑到rest元）,c + 2（还要多2张才能凑到rest元）,b + 3,a + 4);
	 y依赖于min(d,c + 1,b + 2,a + 3);
	 x = min(y + 1,e)
	 即dp[index][rest] = min(dp[index][rest-arr[index]] + 1, dp[index+1][rest])
	 */
	// 有斜率优化的动态规划
	public static int dp2(int[] arr, int aim) {
		if (aim == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 0;
		for (int j = 1; j <= aim; j++) {
			dp[N][j] = Integer.MAX_VALUE;
		}
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				dp[index][rest] = dp[index + 1][rest];
				if (rest - arr[index] >= 0 
						&& dp[index][rest - arr[index]] != Integer.MAX_VALUE) {
					dp[index][rest] = Math.min(dp[index][rest], dp[index][rest - arr[index]] + 1);
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
		int maxLen = 20;
		int maxValue = 30;
		int testTime = 300000;
		System.out.println("功能测试开始");
		for (int i = 0; i < testTime; i++) {
			int N = (int) (Math.random() * maxLen);
			int[] arr = randomArray(N, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = minCoins(arr, aim);
			int ans2 = dp1(arr, aim);
			int ans3 = dp2(arr, aim);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("功能测试结束");
	}

}
