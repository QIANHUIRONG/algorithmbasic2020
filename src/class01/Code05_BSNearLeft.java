package class01;

import java.util.Arrays;

public class Code05_BSNearLeft {

	/**
	 * 题意：在arr上，找满足>=value的最左位置
	 * 如果中点>=num,那就临时保存下，但是有可能左边还有更好的选择，继续二分
	 * @param arr
	 * @return
	 */
	public static int nearestIndex(int[] arr, int num) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		int l = 0, r = arr.length - 1, m = 0;
		int t = -1;
		while (l <= r) {
			m = (l + r) / 2;
			if (arr[m] >= num) {
				t = m; // 如果中点>=num,那就临时保存下
				r = m - 1; // 但是有可能左边还有更好的选择，继续二分
			} else {
				l = m + 1;
			}
		}
		return t;
	}

	// for test
	public static int test(int[] arr, int value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= value) {
				return i;
			}
		}
		return -1;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}
	
	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 10;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			Arrays.sort(arr);
			int value = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
			if (test(arr, value) != nearestIndex(arr, value)) {
				printArray(arr);
				System.out.println(value);
				System.out.println(test(arr, value));
				System.out.println(nearestIndex(arr, value));
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}
