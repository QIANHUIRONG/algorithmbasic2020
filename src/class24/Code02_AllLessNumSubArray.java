package class24;

import java.util.LinkedList;

/*
时间：58
子数组是连续的
 */

public class Code02_AllLessNumSubArray {

	// 暴力的对数器方法
	// O（N^3)
	public static int right(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		for (int L = 0; L < N; L++) {
			for (int R = L; R < N; R++) {
				int max = arr[L];
				int min = arr[L];
				for (int i = L + 1; i <= R; i++) {
					max = Math.max(max, arr[i]);
					min = Math.min(min, arr[i]);
				}
				if (max - min <= sum) {
					count++;
				}
			}
		}
		return count;
	}

	// O（N)
	public static int num(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		LinkedList<Integer> maxWindow = new LinkedList<>();
		LinkedList<Integer> minWindow = new LinkedList<>();
		int R = 0; // R是不回退的！l..r是达标的，那么内部所有子数组都是达标的。l=0,r扩到了10；接下来l=1，r不用从0开始扩，因为1-10一定是达标的，r只需要看能不能继续扩就行了
		for (int L = 0; L < N; L++) { // 依次求以每一个位置作为子数组的开头，达标子数组的数量
			// 窗口[L,R)
			while (R < N) { // L每到一个位置，R就尽可能去扩到初次不达标
				while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[R]) {
					maxWindow.pollLast();
				}
				maxWindow.addLast(R);
				while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
					minWindow.pollLast();
				}
				minWindow.addLast(R);
				if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
					break; // 初次不达标，停！因为l..r不达标，那么l往左扩或者r往右扩肯定也不达标
				} else {
					R++;
				}
			}

			// R是初次不达标的位置，那么此时以L位置开头的达标子数组就是R-L
			count += R - L;

			// 马上L要++了
			if (maxWindow.peekFirst() == L) {
				maxWindow.pollFirst();
			}
			if (minWindow.peekFirst() == L) {
				minWindow.pollFirst();
			}
		}
		return count;
	}

	// for test
	public static int[] generateRandomArray(int maxLen, int maxValue) {
		int len = (int) (Math.random() * (maxLen + 1));
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 200;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxLen, maxValue);
			int sum = (int) (Math.random() * (maxValue + 1));
			int ans1 = right(arr, sum);
			int ans2 = num(arr, sum);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(sum);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");

	}

}
