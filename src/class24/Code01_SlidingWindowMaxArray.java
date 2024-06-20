package class24;

import java.util.LinkedList;

/*
题意：滑动窗口最大值
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回 滑动窗口中的最大值 。
示例 1：
输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
输出：[3,3,5,5,6,7]
解释：
滑动窗口的位置                最大值
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
// 测试链接：力扣239 https://leetcode.cn/problems/sliding-window-maximum/submissions/531193479/
 */
/*
时间：34
 */

/*
思维导图
	1.在经典结构的基础上，窗口每次进一个，出一个，收集1个最大值
	2.有一些烦人的边界：
		1.arr长度N,窗口W,会收集多少个数作为结果：N-W+1个数。所以初始化res的大小是N-W+1
		2.这里只维护了窗口的右边界R，那么此时窗口的左边界应该是R-W，L应该是R-W+1，此时R-W应该出去
		3.只要R>=W-1, 才会形成长度为W的窗口，才需要开始收集答案
 */

public class Code01_SlidingWindowMaxArray {

	/**
	 * 最优解
	 * @param arr
	 * @param w
	 * @return
	 */
	public static int[] maxSlidingWindow(int[] arr, int w) {
		if (arr == null || w < 1 || arr.length < w) {
			return null;
		}
		// qmax：双端队列，放下标
		LinkedList<Integer> qmax = new LinkedList<Integer>();
		int[] res = new int[arr.length - w + 1]; // 收集答案的 [0 1 2 3 4]5个元素，w=3，总共会产生3个答案。就是5-3+1
		int index = 0; // 给res专用的
		for (int R = 0; R < arr.length; R++) { // R位置要进入窗口
			while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {  // 一直弹出，直到队列尾部比我大
				qmax.pollLast();
			}
			qmax.addLast(R); // 我进入窗口
			// 进一个，要伴随出一个，才能收集1个答案。要出去的位置就是R-W
			if (qmax.peekFirst() == R - w) { // R-w: 窗口的过期位置。窗口的左边界。比如R=4，w=3， 此时窗口是[1,2,3,4], 1要出去
				qmax.pollFirst();
			}
			if (R >= w - 1) { // 形没形成窗口。w是3的话，R得来到2位置，形成窗口[0,1,2]共3个
				res[index++] = arr[qmax.peekFirst()];
			}
		}
		return res;
	}

	// 暴力的对数器方法
	// 遍历窗口的内容，求最大值
	public static int[] right(int[] arr, int w) {
		if (arr == null || w < 1 || arr.length < w) {
			return null;
		}
		int N = arr.length;
		int[] res = new int[N - w + 1];
		int index = 0;
		int L = 0;
		int R = w - 1;
		while (R < N) {
			int max = arr[L];
			for (int i = L + 1; i <= R; i++) {
				max = Math.max(max, arr[i]);

			}
			res[index++] = max;
			L++;
			R++;
		}
		return res;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	// for test
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int testTime = 100000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int w = (int) (Math.random() * (arr.length + 1));
			int[] ans1 = maxSlidingWindow(arr, w);
			int[] ans2 = right(arr, w);
			if (!isEqual(ans1, ans2)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
