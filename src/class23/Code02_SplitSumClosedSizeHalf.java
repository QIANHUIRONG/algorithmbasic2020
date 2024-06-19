package class23;

/*
题意：正数数组分割为个数跟累加和接近的两个集合
给定一个正数数组arr，请把arr中所有的数分成两个集合
如果arr长度为偶数，两个集合包含数的个数要一样多
如果arr长度为奇数，两个集合包含数的个数必须只差一个
请尽量让两个集合的累加和接近
返回:
最接近的情况下，较小集合的累加和
（较大集合的累加和一定是所有数累加和减去较小集合的累加和）
 */
/*
时间：52
   */

/*
思维导图：
1.上一题多加一个选定个数的参数
2.
	比如累加和100，有8个数，是偶数，那么就是必须拿4个数，凑出<=50最近的
	如果只有7个数，第一个可能性我必须拿够3个数<=50最近的；第二个可能性，我必须拿够4个数，<=50最近的；我应该返回这两种可能性中，更接近50的那个，也就是求max
3.在递归中多补一个参数picks，表示挑选的个数一定要是picks个
	process(int[] arr, int i, int picks, int rest) {}：arr[i....]自由选择，挑选的个数一定要是picks个，累加和<=rest, 离rest最近的返回
	basecase：
		i==arr.length的时候，如果picks为0，正要挑了picks个，那么之前做的决定是有效的，返回离rest最近的0；如果picks不是0，那么之前做的决定无效，返回-1；
		这里在递归主流程中只控制了不能超过rest，没有考虑必须挑选picks个，所以在basecase只需要考虑i==arr.length的时候，picks的情况
	普遍流程：
		1.可能性一：不要i位置的数
		2.可能性二：如果要了i位置的数，不会超过rest，并且下游给我返回的不是-1，不是一个无效解，就有可能性二
		两种求max，就是最接近rest的

4.dp：3维的
 */
public class Code02_SplitSumClosedSizeHalf {

	public static int right(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int sum = 0;
		for (int num : arr) {
			sum += num;
		}
		if ((arr.length % 2) == 0) { // 偶数
			return process(arr, 0, arr.length / 2, sum / 2);
		} else { // 奇数
			return Math.max(process(arr, 0, arr.length / 2, sum / 2), process(arr, 0, arr.length / 2 + 1, sum / 2));
		}
	}

	// arr[i....]自由选择，挑选的个数一定要是picks个，累加和<=rest, 离rest最近的返回
	public static int process(int[] arr, int i, int picks, int rest) {
		if (i == arr.length) {
			return picks == 0 ? 0 : -1;
		} else {
			// 可能性1，不使用arr[i]
			int p1 = process(arr, i + 1, picks, rest);
			// 可能性2，要使用arr[i]
			int p2 = -1;
			int next = -1;
			if (arr[i] <= rest) {
				next = process(arr, i + 1, picks - 1, rest - arr[i]);
			}
			if (next != -1) {
				p2 = arr[i] + next;
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
		int M = (N + 1) / 2; // 向上取整！！

		// i:0~N；picks:0~ M； rest: 0~sum
		int[][][] dp = new int[N + 1][M + 1][sum + 1];

		// basecase：第N层
		for (int picks = 0; picks <= M; picks++) {
			for (int rest = 0; rest <= sum; rest++) {
				if (picks == 0) {
					dp[N][picks][rest] = 0;
				} else {
					dp[N][picks][rest] = -1;
				}
			}
		}

		// 普遍位置
		for (int i = N - 1; i >= 0; i--) {
			for (int picks = 0; picks <= M; picks++) {
				for (int rest = 0; rest <= sum; rest++) {
					int p1 = dp[i + 1][picks][rest];
					// 就是要使用arr[i]这个数
					int p2 = -1;
					int next = -1;
					if (picks - 1 >= 0 && arr[i] <= rest) {
						next = dp[i + 1][picks - 1][rest - arr[i]];
					}
					if (next != -1) {
						p2 = arr[i] + next;
					}
					dp[i][picks][rest] = Math.max(p1, p2);
				}
			}
		}
		if ((arr.length & 1) == 0) {
			return dp[0][arr.length / 2][sum];
		} else {
			return Math.max(dp[0][arr.length / 2][sum], dp[0][(arr.length / 2) + 1][sum]);
		}
	}

//	public static int right(int[] arr) {
//		if (arr == null || arr.length < 2) {
//			return 0;
//		}
//		int sum = 0;
//		for (int num : arr) {
//			sum += num;
//		}
//		return process(arr, 0, 0, sum >> 1);
//	}
//
//	public static int process(int[] arr, int i, int picks, int rest) {
//		if (i == arr.length) {
//			if ((arr.length & 1) == 0) {
//				return picks == (arr.length >> 1) ? 0 : -1;
//			} else {
//				return (picks == (arr.length >> 1) || picks == (arr.length >> 1) + 1) ? 0 : -1;
//			}
//		}
//		int p1 = process(arr, i + 1, picks, rest);
//		int p2 = -1;
//		int next2 = -1;
//		if (arr[i] <= rest) {
//			next2 = process(arr, i + 1, picks + 1, rest - arr[i]);
//		}
//		if (next2 != -1) {
//			p2 = arr[i] + next2;
//		}
//		return Math.max(p1, p2);
//	}
//
//	public static int dp1(int[] arr) {
//		if (arr == null || arr.length < 2) {
//			return 0;
//		}
//		int sum = 0;
//		for (int num : arr) {
//			sum += num;
//		}
//		sum >>= 1;
//		int N = arr.length;
//		int M = (arr.length + 1) >> 1;
//		int[][][] dp = new int[N + 1][M + 1][sum + 1];
//		for (int i = 0; i <= N; i++) {
//			for (int j = 0; j <= M; j++) {
//				for (int k = 0; k <= sum; k++) {
//					dp[i][j][k] = -1;
//				}
//			}
//		}
//		for (int k = 0; k <= sum; k++) {
//			dp[N][M][k] = 0;
//		}
//		if ((arr.length & 1) != 0) {
//			for (int k = 0; k <= sum; k++) {
//				dp[N][M - 1][k] = 0;
//			}
//		}
//		for (int i = N - 1; i >= 0; i--) {
//			for (int picks = 0; picks <= M; picks++) {
//				for (int rest = 0; rest <= sum; rest++) {
//					int p1 = dp[i + 1][picks][rest];
//					int p2 = -1;
//					int next2 = -1;
//					if (picks + 1 <= M && arr[i] <= rest) {
//						next2 = dp[i + 1][picks + 1][rest - arr[i]];
//					}
//					if (next2 != -1) {
//						p2 = arr[i] + next2;
//					}
//					dp[i][picks][rest] = Math.max(p1, p2);
//				}
//			}
//		}
//		return dp[0][0][sum];
//	}

	public static int dp2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int sum = 0;
		for (int num : arr) {
			sum += num;
		}
		sum >>= 1;
		int N = arr.length;
		int M = (arr.length + 1) >> 1;
		int[][][] dp = new int[N][M + 1][sum + 1];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j <= M; j++) {
				for (int k = 0; k <= sum; k++) {
					dp[i][j][k] = Integer.MIN_VALUE;
				}
			}
		}
		for (int i = 0; i < N; i++) {
			for (int k = 0; k <= sum; k++) {
				dp[i][0][k] = 0;
			}
		}
		for (int k = 0; k <= sum; k++) {
			dp[0][1][k] = arr[0] <= k ? arr[0] : Integer.MIN_VALUE;
		}
		for (int i = 1; i < N; i++) {
			for (int j = 1; j <= Math.min(i + 1, M); j++) {
				for (int k = 0; k <= sum; k++) {
					dp[i][j][k] = dp[i - 1][j][k];
					if (k - arr[i] >= 0) {
						dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - 1][k - arr[i]] + arr[i]);
					}
				}
			}
		}
		return Math.max(dp[N - 1][M][sum], dp[N - 1][N - M][sum]);
	}

	// for test
	public static int[] randomArray(int len, int value) {
		int[] arr = new int[len];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * value);
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int num : arr) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int maxLen = 10;
		int maxValue = 50;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * maxLen);
			int[] arr = randomArray(len, maxValue);
			int ans1 = right(arr);
			int ans2 = dp(arr);
			int ans3 = dp2(arr);
			if (ans1 != ans2 || ans1 != ans3) {
				printArray(arr);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}