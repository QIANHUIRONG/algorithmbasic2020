package class40;

/**
 * 【题意】
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
/*
【思维导图】
1.大思路：求0位置开头的情况下，哪个子数组的累加和等于K，并且是长度最大的；
		  求1位置开头的情况下，哪个子数组的累加和等于K，并且是长度最大的；
		  。。。
	因为是正数数组，窗口扩大，累加和一定扩大，要求的东西和窗口的运动轨迹有单调性！
2.窗口内累加和<K,R右扩
3.窗口内累加和==K,收集答案，R++
4.窗口内累加和>K,L++,让数从窗口左侧出去

5.为什么可以利用窗口？因为它有个重要的单调性，是非负数组范围变大累加和只可能变大或不变，不可能变小
范围变窄你的累加和值可能相等或变小不可能变大。窗口问题能解的那些题，一定都存在某种意义上的单调性。
一定存在某一个范围对应指标的单调性，这回是累加和这个指标下回可能换成别的。所以你看到有单调性，就往窗口上想

 */
/*
【初代笔记】
题解：
	1.利用滑动窗口，因为是正整数数组，窗口增大，窗口累加和一定变大；滑动滑动窗口能解的题目一定存在某一个指标上的单调性，换句话说，有单调性，就往窗口上想。
	2.大流程：
		窗口内累加和 < K, R++;
		窗口内累加和 == K，收集答案，L++(L++,R++都可以，因为是正整数数组，但是如果数组含0，就必须让R++，看有没有可能扩得更远)；
		窗口内累加和 > K, L++;
		本质就求出了以每一个位置作为开头的子数组中累加和等于k长度最大的。
 */
public class Code01_LongestSumSubArrayLengthInPositiveArray {

	public static int getMaxLength(int[] arr, int K) {
		if (arr == null || arr.length == 0 || K <= 0) {
			return 0;
		}
		// 窗口：[left, right]
		int left = 0;
		int right = 0;
		int sum = arr[0];
		int ans = 0;
		while (right < arr.length) {
			if (sum == K) {
				ans = Math.max(ans, right - left + 1);
				// 相等的时候，这里是正数数组，left++或者right++无所谓
				// 如果是非负数数组，就要right++，可能有0，看还能不能继续扩
				sum -= arr[left++];
			} else if (sum < K) {
				right++;
				if (right == arr.length) { // 必须要先判断一下，否则sum+=arr[right]会越界;其实循环只会从这里退出
					break;
				}
				sum += arr[right];
			} else {
				sum -= arr[left++];
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
