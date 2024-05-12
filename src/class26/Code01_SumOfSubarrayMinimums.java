package class26;

// 测试链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
// subArrayMinSum1是暴力解
// subArrayMinSum2是最优解的思路
// sumSubarrayMins是最优解思路下的单调栈优化
// Leetcode上不要提交subArrayMinSum1、subArrayMinSum2方法，因为没有考虑取摸
// Leetcode上只提交sumSubarrayMins方法，时间复杂度O(N)，可以直接通过

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
1、以i位置的数字作为子数组的最小值，所有子数组的数量 * i。每个位置都这么求，累加
2、以10位置的7做最小值，扩出来的范围[6,14]。那么6-14中有多少子数组以7做最小值？6-10，6-11，6-12，6-13，6-14；7-10，7-11，7-12，7-13，7-14...。总共 (10-6 + 1) * (14-10 + 1) = 25;
3、抽象化：i位置，左边离我最近比我小的x，右边离我最近比我小的y，那么产生的答案：(i - x ) * (y - i) * x
4、（20：00）相等时：7位置是3，12位置也是3，那么求7位置时，是算全的，但是求12位置时，开始下标从8开始！所以我就需要得到：我左边离我最近小于等于我的；右边离我最近严格小于我的；
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
//		// left[i] = x : arr[i]左边，离arr[i]最近，<=arr[i]，位置在x。
//		int[] left = nearLessEqualLeft(arr);
//		// right[i] = y : arr[i]右边，离arr[i]最近，< arr[i],的数，位置在y
//		int[] right = nearLessRight(arr);


		List<int[]> list = nearLessEqualLeftAndNearLessRight(arr); // 自己搞的方法
		int[] left = list.get(0);
		int[] right = list.get(1);

		long ans = 0;
		for (int i = 0; i < arr.length; i++) {
			long start = i - left[i];
			long end = right[i] - i;
			ans += start * end * (long) arr[i];
			ans %= 1000000007; // 题目要求的，结果太大了，要%= 1000000007
		}
		return (int) ans;
	}

	// 找左边离我最近，小于等于我的
	public static int[] nearLessEqualLeft(int[] arr) {
//		int N = arr.length;
//		int[] left = new int[N];
//		Stack<Integer> stack = new Stack<>();
//		for (int i = N - 1; i >= 0; i--) {
//			while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
//				left[stack.pop()] = i;
//			}
//			stack.push(i);
//		}
//		while (!stack.isEmpty()) {
//			left[stack.pop()] = -1;
//		}
//		return left;
		int n = arr.length;
		int[] left = new int[n];
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < n; i++) {
			// arr[i]严格小于栈顶，才会弹出栈顶结算；
			// 如果arr[i]等于栈顶，栈顶不会弹出。
			while (!stack.isEmpty() && arr[i] < arr[stack.peek()]) {
				left[stack.pop()] = stack.isEmpty() ? -1 : stack.peek();
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			left[stack.pop()] = stack.isEmpty() ? -1 : stack.peek();
		}
		return left;
	}


	/**
	 * 找左边离我最近，小于等于我的
	 * 找到右边离我最近，严格小于我的
	 * @param arr
	 * @return
	 */
	public static List<int[]> nearLessEqualLeftAndNearLessRight(int[] arr) {
		int n = arr.length;
		int[] left = new int[n];
		int[] right = new int[n];
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && arr[i] < arr[stack.peek()]) {
				int cur = stack.pop();
				// 结算左边：如果arr[7] = 3, 栈顶是5->3, 此时不会弹出栈顶结算，会直接放入栈中。就是7->3; 5->3。因为5->3一定要等着将来7->3结算时，作为7->3左边离他最近且小于等于它
				left[cur] = stack.isEmpty() ? -1 : stack.peek();
				right[cur] = i; // 这里arr[i]严格小于arr[stack.peek]], 所以结算右边一定对
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int cur = stack.pop();
			left[cur] = stack.isEmpty() ? -1 : stack.peek();
			right[cur] = n;
		}
		List<int[]> ans = new ArrayList<>();
		ans.add(left);
		ans.add(right);
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
