package class40;

/**
 * 【题意】正整数组子数组累加和等于K的最大长度
 * 给定一个正整数组成的无序数组arr ，给定一个正整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的
 * 返回其长度
 */
/*
【时间】
上节课讲错的一个小点：7
重新证明括号问题：8
题意：17
流程：19
解释为什么对：23
code：32
 */
// 时复：o(n)
/*
【思维导图】
1.大思路：求0位置开头的情况下，哪个子数组的累加和等于K，并且是长度最大的；
		  求1位置开头的情况下，哪个子数组的累加和等于K，并且是长度最大的；
		  。。。
	因为是正数数组，窗口扩大，累加和一定扩大，要求的东西和窗口的运动轨迹有单调性！
2.窗口内累加和<K,R右扩
3.窗口内累加和==K,收集答案，R++(L++,R++都可以，因为是正整数数组，但是如果数组含0，就必须让R++，看有没有可能扩得更远)；
4.窗口内累加和>K,L++,让数从窗口左侧出去，去求下一个位置做开头的情况下的答案
5.这个运动的过程中，R是不回退的。比如求0位置答案的时候，R扩到了7，那么接下来L++求1位置的答案，R只需要从7继续扩，因为L++来到1，窗口缩小，累加和一定缩小

5.为什么可以利用窗口？因为它有个重要的单调性，是非负数组范围变大累加和只可能变大或不变，不可能变小
范围变窄你的累加和值可能相等或变小不可能变大。窗口问题能解的那些题，一定都存在某种意义上的单调性。
这回是累加和这个指标下回可能换成别的。所以你看到有单调性，就往窗口上想

 */
public class Code01_LongestSumSubArrayLengthInPositiveArray {

	public static int getMaxLength(int[] arr, int K) {
		if (arr == null || arr.length == 0 || K <= 0) {
			return 0;
		}
		// 窗口：[L, R]，初始化的时候，窗口已经包含0位置了
		int L = 0;
		int R = 0;
		int sum = arr[0];
		int ans = 0;
		while (R < arr.length) {
			if (sum == K) {
				ans = Math.max(ans, R - L + 1);
				// 相等的时候，这里是正数数组，left++或者right++无所谓
				// 如果是非负数数组，就要right++，可能有0，看还能不能继续扩
				sum -= arr[L++];
			} else if (sum < K) {
				R++;
				if (R == arr.length) { // 必须要先判断一下，否则sum+=arr[right]会越界;其实循环只会从这里退出
					break;
				}
				sum += arr[R];
			} else {
				sum -= arr[L++]; // 先减完，L再++
			}
		}
		return ans;
	}

	// for test
	public static int right(int[] arr, int K) {
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				if (valid(arr, i, j, K)) {
					max = Math.max(max, j - i + 1);
				}
			}
		}
		return max;
	}

	// for test
	public static boolean valid(int[] arr, int L, int R, int K) {
		int sum = 0;
		for (int i = L; i <= R; i++) {
			sum += arr[i];
		}
		return sum == K;
	}

	// for test
	public static int[] generatePositiveArray(int size, int value) {
		int[] ans = new int[size];
		for (int i = 0; i != size; i++) {
			ans[i] = (int) (Math.random() * value) + 1;
		}
		return ans;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 50;
		int value = 100;
		int testTime = 500000;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generatePositiveArray(len, value);
			int K = (int) (Math.random() * value) + 1;
			int ans1 = getMaxLength(arr, K);
			int ans2 = right(arr, K);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println("K : " + K);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("test end");
	}

}
