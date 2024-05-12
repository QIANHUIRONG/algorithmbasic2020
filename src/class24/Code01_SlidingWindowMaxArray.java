package class24;

import java.util.LinkedList;

/*
时间：34
 */

/*
怎么想到窗口？
发现要求的指标和窗口的运动轨迹具有相似性，窗口变大，某个指标也会跟着变大。

本节课是窗口的最大值和最小值更新结构，本质是窗口，只不过要时时刻刻能够返回窗口的最大值和最小值才使用到了双端队列，
而不是窗口一定会有双端队列。
 */

// 测试链接：力扣239 https://leetcode.cn/problems/sliding-window-maximum/submissions/531193479/
public class Code01_SlidingWindowMaxArray {

	// 暴力的对数器方法
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
		// qmax 窗口最大值的更新结构
		// 放下标
		LinkedList<Integer> qmax = new LinkedList<Integer>();
		int[] res = new int[arr.length - w + 1]; // 收集答案的 [0 1 2 3 4]5个元素，w=3，总共会产生3个答案。就是5-3+1
		int index = 0; // 给res专用的
		for (int R = 0; R < arr.length; R++) { // R位置要进入窗口
			while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {
				qmax.pollLast();
			}
			qmax.addLast(R);
			if (qmax.peekFirst() == R - w) { // R-w: 窗口的过期位置。窗口的左边界。比如R=4，w=3， 此时窗口是[2,3,4], 1要出去
				qmax.pollFirst();
			}
			if (R >= w - 1) { // 形没形成窗口
				res[index++] = arr[qmax.peekFirst()];
			}
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
