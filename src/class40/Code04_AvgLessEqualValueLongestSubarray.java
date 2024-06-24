package class40;

import java.util.TreeMap;

/**
 * 【题意】 整数数组子数组平均值小于等于V的最大长度
 * 给定一个整型数组arr，和一个整数K，求平均值小于等于K的所有子数组中，最大长度是多少
 * 数据规模: 1000
 * arr[i]可能正可能负可能0
 */
/*
【时间】
题解：1：48

数组三连问题总结：1：51
 */
/*
【思维导图】
 题目要求平均值<=v的最长子数组。
 那么我们可以转化一下，
 比如数组如下：8,12,11,7
 v=10
 我们可以让每个数字减去10，得到如下转化数组：
 -2,2,1,-3
 原数组平均值<=10的最长子数组，就等同于：
 累加和<=0的最长子数组。

 比如最终求得最长的子数组是[0,3], 那么对应回原数组中[0,3]累加和一定<=20,进而可以得出对应原数组的平均值<=10

 */
/*
数组三连问题总结：
1.
题目一主要技巧：利用单调性优化
题目二主要技巧：利用预处理结构优化
题目三主要技巧：假设答案法+淘汰可能性（很难，以后还会见到）

2.窗口的三种模型
	L,R一起从左到右移动
	L,R从两侧向中间移动
	L,R从中间向两侧移动

第一个技巧（题目一给我们的启示）：
当有一个问题它描述的指标，有可能是累加和问题也有可能是别的问题，你就看看范围
变化了这个指标有没有单调性。当然它跟实际的数据状况比如全是正数，全是非负这种东西有关，它也和你问的标准有关
但总而言之是你看看范围变化，它有没有单调性，如果有的话，往窗口这想。

3.第二个技巧：
利用预处理结构的优化。我记录一个最早前缀和出现的位置，就不用遍历去搞定。我每次从这个最前缀和最早出现的位置直接定位答案。

3.同时还有一点就是子数组问题，甭管问的是什么，
你就把答案定成必须以0结尾的时候怎么样，必须以1结尾的时候怎么样，必须以2结尾的时候怎么样，
或者必须以0开头怎么样？以1开头怎么样？以2开头怎么样？你就这么去套，憋你也给他憋出来


4.第三个技巧：
假设答案法+淘汰可能性
淘汰可能性一旦哪个题目出现了，很难，因为它挑战了一种想法，它把我每个位置求一个答案，最后答案一定在其中的想法颠覆掉了。
我可能每一个位置求不出它正确的答案。我就要看我求出一个更短的，或者具体这个问题，我们要求的是小于等于K的最长问题，
所以我们精确求解每一个开头时的答案是否必要就变得不必要了。我只关注能够把最终答案推高的可能性。

技巧三一时之间很难掌握硬背这个题。技巧一跟技巧二好好想一想都是可以推广出去的。比如说你利用预处理结构，比如前缀和数
组，线段树，index Tree,都算预数据结构，你想得到累加和都很方便了，我们讲了很多预处理结构了这不新鲜。


 */
public class Code04_AvgLessEqualValueLongestSubarray {

	// 暴力解，时间复杂度O(N^3)，用于做对数器
	public static int ways1(int[] arr, int v) {
		int ans = 0;
		for (int L = 0; L < arr.length; L++) {
			for (int R = L; R < arr.length; R++) {
				int sum = 0;
				int k = R - L + 1;
				for (int i = L; i <= R; i++) {
					sum += arr[i];
				}
				double avg = (double) sum / (double) k;
				if (avg <= v) {
					ans = Math.max(ans, k);
				}
			}
		}
		return ans;
	}

	// 想实现的解法2，时间复杂度O(N*logN)
	// 题目要求平均值<=v的最长子数组。
	// 那么我们可以转化一下，
	// 比如数组如下：7，4，3，9，6
	// v=5
	// 我们可以让每个数字减去5，得到如下转化数组：
	// 2，-1，-2，4，1
	// 原数组平均值<=5的最长子数组，就等同于：
	// 累加和<=0的最长子数组。
	// 想通这个，就好办了。
	// 然后就是在转化数组里求：
	// 子数组必须以i结尾的情况下，累加和<=0的最长子数组。
	// 代码中的sum，就是每一步从原始数组的值先转化，然后把转化后的值累加起来的前缀和。
	// 比如，转化数组
	// 2，-1，-2，4，1
	// sum依次为 : 2、1、-1、3、4
	// 那么，比如当你来到一个位置i，
	// sum就是0...i的转化前缀和，假设是100
	// 那么就找>=100且距离100最近的转化前缀和最早出现在哪。
	public static int ways2(int[] arr, int v) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		for (int i = 0; i < arr.length; i++) {
			arr[i] -= v;
		}
		TreeMap<Integer, Integer> sortedMap = new TreeMap<>();
		sortedMap.put(0, -1);
		int sum = 0;
		int len = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
			Integer ceiling = sortedMap.ceilingKey(sum);
			if (ceiling != null) {
				len = Math.max(len, i - sortedMap.get(ceiling));
			} else {
				sortedMap.put(sum, i);
			}
		}
		return len;
	}

	// 想实现的解法3，时间复杂度O(N)，最优解
	public static int ways3(int[] arr, int v) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		for (int i = 0; i < arr.length; i++) {
			arr[i] -= v;
		}
		return maxLengthAwesome(arr, 0);
	}

	// 找到数组中累加和<=k的最长子数组
	public static int maxLengthAwesome(int[] arr, int k) {
		int N = arr.length;
		int[] sums = new int[N];
		int[] ends = new int[N];
		sums[N - 1] = arr[N - 1];
		ends[N - 1] = N - 1;
		for (int i = N - 2; i >= 0; i--) {
			if (sums[i + 1] < 0) {
				sums[i] = arr[i] + sums[i + 1];
				ends[i] = ends[i + 1];
			} else {
				sums[i] = arr[i];
				ends[i] = i;
			}
		}
		int end = 0;
		int sum = 0;
		int res = 0;
		for (int i = 0; i < N; i++) {
			while (end < N && sum + sums[end] <= k) {
				sum += sums[end];
				end = ends[end] + 1;
			}
			res = Math.max(res, end - i);
			if (end > i) {
				sum -= arr[i];
			} else {
				end = i + 1;
			}
		}
		return res;
	}

	// 用于测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int len = (int) (Math.random() * maxLen) + 1;
		int[] ans = new int[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (int) (Math.random() * maxValue);
		}
		return ans;
	}

	// 用于测试
	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	// 用于测试
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// 用于测试
	public static void main(String[] args) {
		System.out.println("测试开始");
		int maxLen = 20;
		int maxValue = 100;
		int testTime = 500000;
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(maxLen, maxValue);
			int value = (int) (Math.random() * maxValue);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			int[] arr3 = copyArray(arr);
			int ans1 = ways1(arr1, value);
			int ans2 = ways2(arr2, value);
			int ans3 = ways3(arr3, value);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("测试出错！");
				System.out.print("测试数组：");
				printArray(arr);
				System.out.println("子数组平均值不小于 ：" + value);
				System.out.println("方法1得到的最大长度：" + ans1);
				System.out.println("方法2得到的最大长度：" + ans2);
				System.out.println("方法3得到的最大长度：" + ans3);
				System.out.println("=========================");
			}
		}
		System.out.println("测试结束");
	}

}
