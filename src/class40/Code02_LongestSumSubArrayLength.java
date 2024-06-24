package class40;

import java.util.HashMap;

/**
 * 【题意】整数数组子数组累加和等于K的最大长度
 * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0
 * 给定一个整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的
 * 返回其长度
 */
/*
【时间】
流程：36
map里面一定要先加一条 0-> -1 ：49
code: 51
变种题目：54
 */
// 时复：o(n)
/*
【初代笔记】
题解：
	1.因为数组可能正、可能负、可能0，所以累加和不随着窗口的增大而具有单调性了，滑动窗口行不通；
	2.流程：子数组必须以i结尾，累加和 = K的最长子数组多长；
	3.例子：必须以100结尾，累加和为30且最长，假设当前0-100的累加和是200，如果我又直到0-17的累加和是170，那么从18-200的累加和一定是30；
	所以所谓的必须以100结尾的时候它往左边推出来的最长的子数组累加和得30这件事实际上就在找哪一个前缀和是最早出现170的，我就反着推出100结尾的情况下最长的子数组是谁。
	4.所以自然而然要维持一张前缀和第一次出现位置的表map和一个arr[0...i]的累加和sum；
	这张表一定要在初始化时添加一个(0, -1)的记录，表示前缀和为0第一次出现的位置是-1，这样才不会漏算！不然将错过0开头的答案。 比如数组[5,5], k = 10。如果map中没有提前放（0，-1），那么会错过[5,5]这个答案
	前缀和	前缀和第一次出现的位置
	0			-1

	例子：
	arr[-100, 2, 1, 3, 100]， k = 6;
	     0	  1  2  3   4
	遍历到0位置的-100，sum=-100,表中只有(0, -1),查询sum-k = -106这个前缀和有没有在表中出现过->没有，说明以0位置结尾的-100没有答案，把(-100, 0)添加到表里；
	遍历到1位置的2，sum=-98,表中有(0,-1)(-100,0),查询-104这个前缀和有没有在表中出现过->没有；把(-98,1)添加到表里；
	遍历到2位置的1，sum=-97,表中有(0,-1)(-100,0)(-104,1),查询-103这个前缀和有没有在表中出现过->没有；把(-97,2)添加到表里；
	遍历到3位置的3，sum=-94,表中有(0,-1)(-100,0)(-104,1)(-97,2),查询-100这个前缀和有没有在表中出现过->有!,抓一下答案，把(-94,3)添加到表里；
	遍历到4位置的100，sum=6，表中有(0,-1)(-100,0)(-104,1)(-97,2)(-94,3)，查询0这个前缀和有没有在表中出现过->有！如果没有初始一条(0,-1)，那这个答案就错过了！

	5.这个表只存这个前缀和第一次出现的位置，往后重复出现不用管；比如(8,3),前缀和为8第一次在3位置出现过，后面如果再遇到前缀和也为8的不用管了。

	6.变种[今日头条]：一个数组中，所有的值任一有正有负有0，我想找到有一个子数组，我要求在这个数组中-1的数量和1的数量必须是一样多的，哪一个子数组是最长的？
	题解：数组1和-1不变，其他的数变成0，问题就变成了求累加和等于0的情况下，子数组最长有多长？
 */
public class Code02_LongestSumSubArrayLength {

	public static int maxLength(int[] arr, int k) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		// key:前缀和
		// value : 0~value这个前缀和是最早出现key这个值的
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, -1); // important
		int ans = 0;
		int sum = 0;
		for (int i = 0; i < arr.length; i++) { // 子数组必须以i结尾，累加和 = K的最长子数组多长；
			sum += arr[i];
			if (map.containsKey(sum - k)) {
				ans = Math.max(i - map.get(sum - k), ans);
			}
			if (!map.containsKey(sum)) { //只维持最早出现这个前缀和的情况 只新加，不更新。
				map.put(sum, i);
			}
		}
		return ans;
	}

	// for test
	public static int right(int[] arr, int K) {
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				if (valid(arr, i, j, K)) {
					max = Math.max(max, j - i + 1);
				}
			}
		}
		return max;
	}

	// for test
	public static boolean valid(int[] arr, int L, int R, int K) {
		int sum = 0;
		for (int i = L; i <= R; i++) {
			sum += arr[i];
		}
		return sum == K;
	}

	// for test
	public static int[] generateRandomArray(int size, int value) {
		int[] ans = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value) - (int) (Math.random() * value);
		}
		return ans;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 50;
		int value = 100;
		int testTime = 500000;

		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(len, value);
			int K = (int) (Math.random() * value) - (int) (Math.random() * value);
			int ans1 = maxLength(arr, K);
			int ans2 = right(arr, K);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println("K : " + K);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("test end");

	}

}
