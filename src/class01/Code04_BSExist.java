package class01;

import java.util.Arrays;


/*
 [题意]

*/
/*
[时间]
流程：1:49
code:
 */
// 时复：o(n*logn)
/*
[思维导图]
1.找一个数，不需要全部的数看一遍，就是因为数据状况有规律，能二分

2.while(l < r) 和 while(l <= r)
这两种写法，啥时候写≤时候写<.我只能说都对你如果把你的大罗辑定成至少两个数的时候，你就有这种逻辑下的边界条件。如果你把
你的逻辑定成至少一个数字我就能二分，那就有这种逻辑下的边界条件，写Code扣边界条件这个过程是必不可少
的。
算法是一个大的思想，怎么实现这个大的思想，八仙过海各显神通
 */
public class Code04_BSExist {

	public static boolean exist(int[] arr, int num) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		int l = 0;
		int r = arr.length - 1;
		int m = 0;
		while (l <= r) {
			m = (l + r) / 2;
			if (arr[m] == num) {
				return true;
			} else if (arr[m] > num) {
				r = m - 1; // 这里不会越界吗？即使r=m-1来到了-1位置，越界了，那么下一轮while循环肯定进不去
			} else {
				l = m + 1;
			}
		}
		return false;
	}
	
	// for test
	public static boolean test(int[] sortedArr, int num) {
		for(int cur : sortedArr) {
			if(cur == num) {
				return true;
			}
		}
		return false;
	}
	
	
	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
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
			if (test(arr, value) != exist(arr, value)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}
