package class08;

import java.util.Arrays;


/*
 [题意]
 桶排序
 计数排序
*/

/*
[时间]
 1：05
 */

// 时复：O(n)
// 空复：

/*
[思维导图]
一、桶排序说明
	1.所有桶排序思想下的排序，都要对数据状况有要求，除非特殊说明，否则算法复杂度排序必须按基于比较的排序做复杂度估算
	2.
		1）一般来讲，计数排序要求，样本是整数，且范围比较窄
		2）一般来讲，基数排序要求，样本是10进制的正整数
		3）一旦要求稍有升级，改写代价增加是显而易见的

	3.不基于比较的排序，它的极限O(N)；基于比较的排序，目前来讲O(NlogN)是极限

二、计数排序

	好，那么接下来我们就要讲一个基数排序跟计数排序了，先说一下这个题目，假设有一个数组，这个数组里面的每一个值，代表一个员工的年龄，代表个员工的年纪。整形数组里面每个值的含义是员工的年纪，请你把它从小到大排序

	1.这个数组中所有的数都是员工的年龄，员工年龄我可以认为从 0 到。200 可以。所以这个数的范围是被限制的，员工年龄人很少能活我 200 岁
	2.你搞一个统计数组嘛。0 岁的员工有多少人？ 1 岁的员工有多少人？两岁的员工有多少人？ 16 岁的员工有多少人？ 23 岁的员工。有多少人？你遍历原始数组？你准备一个辅助数组，他这个辅助数组管他姑且叫help。
	它的长度就是201，下标从0-200。然后你要遇到第一个值是是16，你就把 16 上面的这个员工年龄从0 变成1。如果你又遇到 16 岁了，那就变成2。如果你遇到 23 了，那 23 岁就从 0 变成一。如果你加又遇到 16 的，那他就从 2 变成3。
	你遍历数组一遍就可以把arr数组每一个年龄出现多少人统计好。O(n)
	3.那么你怎么得到一个排序后的数组？那你就依次看吧，如果 0 岁的员工 0 个人，1岁的员工有2人，那前面两个就是1，两岁的员工有 4 人，那接下来4个4，
	 你再遍历这个 help 数组一遍。绝对可以把排完序的结果生成好。它的数组长度，它是个常数的，你生成这样一个数组又是O（n） 的


 */
public class Code03_CountSort {

	// only for 0~200 value
	public static void countSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			max = Math.max(max, arr[i]);
		}
		// bucket[i] = x, 表示i出现了x次
		int[] bucket = new int[max + 1];
		for (int i = 0; i < arr.length; i++) {
			bucket[arr[i]]++;
		}
		int i = 0;
		for (int j = 0; j < bucket.length; j++) {
			while (bucket[j] > 0) {
				arr[i] = j;
				i++;
				j--;
			}
		}
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
		int maxValue = 150;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			countSort(arr1);
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
		countSort(arr);
		printArray(arr);

	}

}
