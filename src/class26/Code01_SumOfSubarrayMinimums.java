package class26;

import java.util.Stack;
/*
题意：子数组最小值的累加和
给定一个数组arr，返回所有子数组最小值的累加和
 */
// 测试链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
// subArrayMinSum1是暴力解
// subArrayMinSum2是最优解的思路
// sumSubarrayMins是最优解思路下的单调栈优化
// Leetcode上不要提交subArrayMinSum1、subArrayMinSum2方法，因为没有考虑取摸
// Leetcode上只提交sumSubarrayMins方法，时间复杂度O(N)，可以直接通过


/*
时间：
 */
/*
思维导图：
1、以i位置的数字作为子数组的最小值，所有子数组的数量 * i。每个位置都这么求，累加
2、以10位置的7做最小值，扩出来的范围[6,14]。那么6-14中有多少子数组以7做最小值？6-10，6-11，6-12，6-13，6-14；7-10，7-11，7-12，7-13，7-14...。总共 (10-6 + 1) * (14-10 + 1) = 25;
3、抽象化：i位置，左边离我最近比我小的x，右边离我最近比我小的y，那么产生的答案：(i - x ) * (y - i) * x
4、（20：00）相等时：7位置是3，12位置也是3，那么求7位置时，是算全的，但是求12位置时，开始下标从8开始！所以我就需要得到：我左边离我最近小于等于我的；右边离我最近严格小于我的；
经典的单调栈代码是Code01_MonotonousStack，是没有重复值的版本，找到左右两边离我最近的严格比我小的；而如果数组有重复值，那么求出来的结果就是：我左边离我最近小于等于我的；右边离我最近严格小于我的！！！

 */
public class Code01_SumOfSubarrayMinimums {

	public static int subArrayMinSum1(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				int min = arr[i];
				for (int k = i + 1; k <= j; k++) {
					min = Math.min(min, arr[k]);
				}
				ans += min;
			}
		}
		return ans;
	}

	// 没有用单调栈，但是思路是一样的
	public static int subArrayMinSum2(int[] arr) {
		// left[i] = x : arr[i]左边，离arr[i]最近，<=arr[i]，位置在x
		int[] left = leftNearLessEqual2(arr);
		// right[i] = y : arr[i]右边，离arr[i]最近，< arr[i],的数，位置在y
		int[] right = rightNearLess2(arr);
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			int start = i - left[i];
			int end = right[i] - i;
			ans += start * end * arr[i];
		}
		return ans;
	}

	public static int[] leftNearLessEqual2(int[] arr) {
		int N = arr.length;
		int[] left = new int[N];
		for (int i = 0; i < N; i++) {
			int ans = -1;
			for (int j = i - 1; j >= 0; j--) {
				if (arr[j] <= arr[i]) {
					ans = j;
					break;
				}
			}
			left[i] = ans;
		}
		return left;
	}

	public static int[] rightNearLess2(int[] arr) {
		int N = arr.length;
		int[] right = new int[N];
		for (int i = 0; i < N; i++) {
			int ans = N;
			for (int j = i + 1; j < N; j++) {
				if (arr[i] > arr[j]) {
					ans = j;
					break;
				}
			}
			right[i] = ans;
		}
		return right;
	}

	/**
	 * 最优解
	 * @param arr
	 * @return
	 */
	public static int sumSubarrayMins(int[] arr) {
		int[][] list = nearLessEqualLeftAndNearLessRight2(arr);
		long ans = 0;
		for (int i = 0; i < arr.length; i++) {
			long start = i - list[i][0];
			long end = list[i][1] - i;
			ans += start * end * (long) arr[i];
			ans %= 1000000007;
		}
		return (int) ans;
	}


	/**
	 * 找左边离我最近，小于等于我的
	 * 找到右边离我最近，严格小于我的
	 * @param arr
	 * @return
	 */
	public static int[][] nearLessEqualLeftAndNearLessRight2(int[] arr) {
		int n = arr.length;
		int[][] ans = new int[n][2];
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < n; i++) {
			// 这里会发现和单调栈Code01_MonotonousStack的代码一样
			// 解释一下，如果有重复值，那么经典的Code01_MonotonousStack代码，执行的结果就是：找左边离我最近，小于等于我的；找到右边离我最近，严格小于我的
			// 为什么？因为我遍历到7位置的2，要去结算栈顶的5位置的4，因为arr[i] < arr[stack.peek()]，是严格小于才结算栈顶的，所以右边一定是严格小于我的；
			// 左边呢？如果相等的时候，不结算，所以就有可能出现栈中是 3->2, 4->2], 等到5位置的1来的时候，会结算4->2, 此时4->2左边就是小于等于我的，离我最近的
			while (!stack.isEmpty() && arr[i] < arr[stack.peek()]) {
				int cur = stack.pop();
				// 结算左边：如果arr[7] = 3, 栈顶是5->3, 此时不会弹出栈顶结算，会直接放入栈中。就是7->3; 5->3。因为5->3一定要等着将来7->3结算时，作为7->3左边离他最近且小于等于它
				ans[cur][0] = stack.isEmpty() ? -1 : stack.peek();
				ans[cur][1] = i; // 这里arr[i]严格小于arr[stack.peek]], 所以结算右边一定对
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int cur = stack.pop();
			ans[cur][0] = stack.isEmpty() ? -1 : stack.peek();
			ans[cur][1] = n;
		}
		return ans;
	}

	// 找到右边离我最近，严格小于我的
	public static int[] nearLessRight(int[] arr) {
		int N = arr.length;
		int[] right = new int[N];
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < N; i++) {
			while (!stack.isEmpty() && arr[i] < arr[stack.peek()]) {
				right[stack.pop()] = i;
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			right[stack.pop()] = N;
		}
		return right;
	}
	public static int[] randomArray(int len, int maxValue) {
		int[] ans = new int[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (int) (Math.random() * maxValue) + 1;
		}
		return ans;
	}

	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 50;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * maxLen);
			int[] arr = randomArray(len, maxValue);
			int ans1 = subArrayMinSum1(arr);
			int ans2 = subArrayMinSum2(arr);
			int ans3 = sumSubarrayMins(arr);
			if (ans1 != ans2 || ans1 != ans3) {
				printArray(arr);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println("出错了！");
				break;
			}
		}
		System.out.println("测试结束");


	}

}
