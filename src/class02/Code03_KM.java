package class02;

import java.util.HashMap;
import java.util.HashSet;
/*
 [题意]
一个数组中有一种数出现K次，其他数都出现了M次，
M > 1, K < M
找到，出现了K次的数，
要求，额外空间复杂度O(1)，时间复杂度O(N)
*/

/*
[时间]
题目：1:06
流程：1：09
code: 1: 24
写对数器：1：41
题目加了点限制：2：04

 */

// 时复：o(n), 空间复杂度:o(1)

/*
[思维导图]
1.int型整数是32位的，准备一个长度为32的数组help，用来存储整型的位信息，比如整数3，在数组中就是arr[0 0 ... 0 1 1]
2.遍历原数组，把所有数字的二进制状态都保存在help中，当前数如果某一位上是1，就累加到arr中
3.最后比如0位置上的1出现了12次，k = 1， m = 5，那就表示这个出现了k次的数0位置上有1；
原因：
	k=2表示只有一种数出现了2次，m=5表示可能有很多种数出现的5次；
	0位置上的1出现了12次，12 % m != 0, 表示这12个1全部由出现了5次的数来构成是凑不出来的，所以0位置上的1一定有出现了2次的数帮忙凑出来的；
	例子：
	1出现了2次， 3（11）出现了7次， 5（101）出现了7次，那0位置上的1一共就是7 + 7 + 2 = 16次， 16 % 7 ！= 0， 就表示那个出现了k次的数0位置上一定是1；

4.可以最后通过每一位上是否是M的整数倍这件事就把那个出现了K次的数给他依次给弄出来
如果我们在某一个变量发现1的数量不能够被M整除，说明我要找的那个数，在这一位它一定是1状态。
-如果我们在某一个变量发现1的数量能够被M整除，说明我要找的那个数，在这一位它一定是0状态。
*/

public class Code03_KM {


	/**
	 * 经典解法
	 */
	// 请保证arr中，只有一种数出现了K次，其他数都出现了M次，1 <= K < M
	public static int km(int[] arr, int k, int m) {
		// help[0] 0位置的1出现了几个
		// help[i] i位置的1出现了几个
		int[] help = new int[32]; // 下标：0-31
		for (int num : arr) {
			for (int i = 0; i < 32; i++) {
				if ((num >> i & 1) == 1) { // num的二进制形式第i位置上是1，就累加到help数组中
					help[i]++;
				}
			}
		}

		// 如果这个出现了K次的数，就是0，那么if (help[i] % m != 0) 就进不去，ans一直维持0，直到返回，也是对的！
		int ans = 0;
		for (int i = 0; i < 32; i++) {
			if (help[i] % m != 0) { // %m之后不是0，说明出现k次的这个数在第i位置上是1
				ans |= (1 << i); // 1左移i位置 或 到ans里，那么这个位置就设置好了
			}
		}
		return ans;
	}


	// 用于测试
	// 哈希表统计词频的方法
	public static int test(int[] arr, int k, int m) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int num : arr) {
			if (map.containsKey(num)) {
				map.put(num, map.get(num) + 1);
			} else {
				map.put(num, 1);
			}
		}
		int ans = 0;
		for (int num : map.keySet()) {
			if (map.get(num) == k) {
				ans = num;
				break;
			}
		}
		return ans;
	}


	// 为了测试
	public static int[] randomArray(int maxKinds, int range, int k, int m) {
		int ktimeNum = randomNumber(range);
		// 真命天子出现的次数
		int times = k;
		// 2
		int numKinds = (int) (Math.random() * maxKinds) + 2;
		// k * 1 + (numKinds - 1) * m
		int[] arr = new int[times + (numKinds - 1) * m];
		int index = 0;
		for (; index < times; index++) {
			arr[index] = ktimeNum;
		}
		numKinds--;
		HashSet<Integer> set = new HashSet<>();
		set.add(ktimeNum);
		while (numKinds != 0) {
			int curNum = 0;
			do {
				curNum = randomNumber(range);
			} while (set.contains(curNum));
			set.add(curNum);
			numKinds--;
			for (int i = 0; i < m; i++) {
				arr[index++] = curNum;
			}
		}
		// arr 填好了
		for (int i = 0; i < arr.length; i++) {
			// i 位置的数，我想随机和j位置的数做交换
			int j = (int) (Math.random() * arr.length);// 0 ~ N-1
			int tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
		return arr;
	}

	// 为了测试
	// [-range, +range]
	public static int randomNumber(int range) {
		return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
	}

	// 为了测试
	public static void main(String[] args) {
		int kinds = 5;
		int range = 30;
		int testTime = 100000;
		int max = 9;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {

			// 保证有效的k和m
			int a = (int) (Math.random() * max) + 1; // a 1 ~ 9
			int b = (int) (Math.random() * max) + 1; // b 1 ~ 9
			int k = Math.min(a, b);
			int m = Math.max(a, b);
			// k < m
			if (k == m) {
				m++;
			}

			int[] arr = randomArray(kinds, range, k, m);
			int ans1 = test(arr, k, m);
			int ans2 = km(arr, k, m);
			if (ans1 != ans2) {
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println("出错了！");
			}
		}
		System.out.println("测试结束");
	}

}
