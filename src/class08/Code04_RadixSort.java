package class08;

import java.util.Arrays;

// 1：16
public class Code04_RadixSort {

	// only for no-negative value
	public static void radixSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			max = Math.max(max, arr[i]);
		}
		// 1、最大的位数
		int maxbits = maxbits(max);
		// 2、有几位，就入桶出桶几次
		for (int d = 1; d <= maxbits; d++) {
			// 3、count[]数组，从0-9，帮助入桶出桶
			// count[0] 当前位(d位)是0的数字有多少个
			// count[1] 当前位(d位)是(0和1)的数字有多少个
			// count[2] 当前位(d位)是(0、1和2)的数字有多少个
			// count[i] 当前位(d位)是(0~i)的数字有多少个
			int[] count = new int[10];
			for (int i = 0; i < arr.length; i++) {
				int digit = getDigit(arr[i], d);
				count[digit]++;
			}
			for (int i = 1; i < count.length; i++) {
				count[i] = count[i - 1] + count[i];
			}
			// 4、有多少个数准备多少个辅助空间。这个是桶。代替队列
			int[] help = new int[arr.length];
			// 5、入桶
			for (int i = arr.length - 1; i >= 0; i--) {
				int digit = getDigit(arr[i], d);
				help[count[digit] - 1] = arr[i];
				count[digit]--;
			}
			// 6、出桶
			for (int i = 0; i < arr.length; i++) {
				arr[i] = help[i];
			}
		}
	}



	public static int maxbits(int num) {
		int res = 0;
		while (num != 0) {
			res++;
			num /= 10;
		}
		return res;
	}

	/**
	 * 提取num第i位上的数
	 * x: 123， d：3
	 * x / (int) Math.pow(10, d - 1) = 123 / 100 = 1
	 * 1 % 10 = 1；
	 *
	 * x:123, d:2 -> (123 / 10) % 10 = 12 % 10 = 2
	 * @param x
	 * @param d
	 * @return
	 */
	public static int getDigit(int x, int d) {
		int pow = (int) Math.pow(10, d - 1);
		x = x / pow;
		return x % 10;
//		return ((x / ((int) Math.pow(10, d - 1))) % 10);
	}


	// for test
	public static void comparator(int[] arr) {
		Arrays.sort(arr);
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random());
		}
		return arr;
	}

	// for test
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
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

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100000;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			radixSort(arr1);
			comparator(arr2);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");

		int[] arr = generateRandomArray(maxSize, maxValue);
		printArray(arr);
		radixSort(arr);
		printArray(arr);

	}

}
