package class06;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Code04_SortArrayDistanceLessK {

	/**
	 * 已知一个几乎有序的数组。几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定<=k，并且k相对于数组长度来说是比较小的。
	 * 请选择一个合适的排序策略，对这个数组进行排序。
	 */
	// 时间复杂度：O(N*log k),一共弹出N个数，小根堆每一次调整log k，会比直接排序O(N*logN)好
	public static void sortedArrDistanceLessK(int[] arr, int k) {
		if (k == 0) {
			return;
		}
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int i = 0;
		while (i <= Math.min(arr.length - 1, k)) {
			heap.add(arr[i++]);
		}
		int j = 0;
		while (i < arr.length) {
			arr[j++] = heap.poll();
			heap.add(arr[i++]);
		}
		while (!heap.isEmpty()) {
			arr[j++] = heap.poll();
		}
	}

	// for test
	public static void comparator(int[] arr, int k) {
		Arrays.sort(arr);
	}

	// for test
	public static int[] randomArrayNoMoveMoreK(int maxSize, int maxValue, int K) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		// 先排个序
		Arrays.sort(arr);
		// 然后开始随意交换，但是保证每个数距离不超过K
		// swap[i] == true, 表示i位置已经参与过交换
		// swap[i] == false, 表示i位置没有参与过交换
		boolean[] isSwap = new boolean[arr.length];
		for (int i = 0; i < arr.length; i++) {
			int j = Math.min(i + (int) (Math.random() * (K + 1)), arr.length - 1);
			if (!isSwap[i] && !isSwap[j]) {
				isSwap[i] = true;
				isSwap[j] = true;
				int tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
			}
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
		System.out.println("test begin");
		int testTime = 50000;
		int maxSize = 100;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int k = (int) (Math.random() * maxSize) + 1;
			int[] arr = randomArrayNoMoveMoreK(maxSize, maxValue, k);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			sortedArrDistanceLessK(arr1, k);
			comparator(arr2, k);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				System.out.println("K : " + k);
				printArray(arr);
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}