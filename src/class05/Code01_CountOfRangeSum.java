package class05;


/*
 [题意] // https://leetcode.cn/problems/count-of-range-sum/

 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。

输入：nums = [-2,5,-1], lower = -2, upper = 2
输出：3
解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。

*/

/*
[时间]
前缀和数组：8
题意 + 暴力解：15
转换1：21
转换2：24
merge改写流程：38
code：45
basecase重新解释：1：11
不回退的技巧：59
总结：1：21
用有序表解：1：24
/
 */

// 时复：
// 空复：

/*
[思维导图]
1.暴力解：枚举所有的子数组，再遍历求和 o(n^3)
2.优良一点的解法：枚举所有的子数组，使用前缀和数组求和 o(n^2)
3.最优解：1.归并排序做法 o(n*logn); 2.有序表做法：o(n*logn)

一、前缀和数组
1.求arr数组中[i...j]范围上的累加和，这种调用非常频繁，怎么算的快？
如果每次都遍历[i...j]去累加，每次都得o(n), 慢
生成前缀和数组o(n), 后续每次求[i,j]上的累加和都是o(1)

2.arr[i...j]上的累加和 = [0..j]的累加和 - [0-i-1]的累加和
遍历一遍数组，可以生成preSum, 前缀和数组， preSum[i] = [0...i]的累加和
arr[i...j]上的累加和 = preSum[j] - preSum[i - 1]

二、归并排序改法
1.转换1：把流程定成以数组中每一个位置做结尾的子数组达标的数量累加
2.转换2：求以i位置结尾的子数组，有多少落在[lower,upper]。假设i的前缀和preSum[i]=x，那么求以x位置结尾的子数组有多少达标
	等于求有多少前缀和落在在[x - upper, x-lower]之间。
3.例子：求必须以17位置结尾的子数组，有多少在[10,40]之间，preSum[17] = 100, 我就要看之前的前缀和中有多少落在[60,90]上
比如[0...2]前缀和preSum[2] = 70, 那么[3...17]的累加和就是达标的，因为sum[3...17] = preSum[17] - preSum[2] = 30,在[10, 40]内，达标
所以，每一个落在[x - upper, x-lower]的前缀和，都能转换成一个以i位置结尾的子数组，累加和在[lower, upper]之间


4.原题目就转换成：原始array处理成前缀和数组preSum(此时原数组可以忘记了)，
求preSum数组中出现的每一个数x,它左边有多少个数落在[x-upper,X-lower]上 --> 往归并排序上靠拢！！

5.左组的ans + 右组的ans + merge的ans
merge过程中，ans也是需要依赖窗口去滑动的。依次去求右组的每一个数的答案
左组定义[l,r)窗口
右组第一个数是x, 要求[x-upper, x-lower]，窗口滑动， l滑动到>=x-upper, r滑动到>x-lower
右组第二个数是y, 要求[y-upper, y-lower]，窗口滑动, l滑动到>=x-upper, r滑动到>x-lower
。。。
因为右组都是有序的，所以x-upper越来越大，x-lower也越来越大，l,r指针不回退。o(n)


三、有序表解法：需要学到有序表再来
 */


public class Code01_CountOfRangeSum {

	public static int countRangeSum(int[] nums, int lower, int upper) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		long[] preSum = new long[nums.length];
		preSum[0] = nums[0];
		for (int i = 1; i < nums.length; i++) {
			preSum[i] = preSum[i - 1] + nums[i];
		}
		return process(preSum, 0, preSum.length - 1, lower, upper);
	}

	// arr[L·.R]已经不传进来了，只传进来preSum(前缀和数组)
	//在原始的arr[L...R]中，有多少个子数组累加和在[lower,upper]上
	public static int process(long[] preSum, int l, int r, int lower, int upper) {
		// merge的过程中，求的是右组的每一个数，左组有多少个达标
		// 但是这慧忽略掉一种情况，就是0-i这个子数组，它要减去一个也没有的时候，这种忽略的情况就补在basecase
		// 在basecase中，其实就是看看0-我自己，达不达标。
		if (l == r) {
			return preSum[l] >= lower && preSum[l] <= upper ? 1 : 0;
		}
		int m = (l + r) / 2;
		return process(preSum, l, m, lower, upper) + process(preSum, m + 1, r, lower, upper)
				+ merge(preSum, l, m, r, lower, upper);
	}

	public static int merge(long[] arr, int l, int m, int r, int lower, int upper) {
		// 1.对于右组中的每个数，求左组中有多少个数，位于[x-upper,u-lower]之间
		int ans = 0;
		// 两个指针都不回退！！左右组都有序，越来越大的数字再-upper, -lower
		// 所以还是o(n)
		int windowL = l;
		int windowR = l;
		// [windowL, windowR) 。
		// 为什么不设计成[windowL,windowR],因为初始的时候，只能让windowL = l, windowR = l, 此时表示的是一个数也没有，
		// 如果此时设计成[windowL,windowR],那么就代表有一个数了
		for (int i = m + 1; i <= r; i++) {
			long min = arr[i] - upper;
			long max = arr[i] - lower;
			// 要求左组有多少个数落在[min,max]之间。min和max都是单调递增的
			while (windowL <= m && arr[windowL] < min) { // 出了while，windowL>=min
				windowL++; // l指针要滑到刚好>=min
			}
			while (windowR <= m && arr[windowR] <= max) { // 出了while，windowR > max
				windowR++; // r指针要滑动>max的地方
			}
			ans += windowR - windowL;
		}

		// 2.merge经典流程
		long[] help = new long[r - l + 1];
		int i = 0;
		int p1 = l;
		int p2 = m + 1;
		while (p1 <= m && p2 <= r) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[l + i] = help[i];
		}
		return ans;
	}





}
