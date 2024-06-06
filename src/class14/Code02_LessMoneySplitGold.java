package class14;

import java.util.PriorityQueue;

/*
题意：
金条分割的最小代价
一块金条切成两半，是需要花费和长度数值一样的铜板的。
比如长度为20的金条，不管怎么切，都要花费20个铜板。 一群人想整分整块金条，怎么分最省铜板?

例如,给定数组{10,20,30}，代表一共三个人，整块金条长度为60，金条要分成10，20，30三个部分。

如果先把长度60的金条分成10和50，花费60; 再把长度50的金条分成20和30，花费50;一共花费110铜板。
但如果先把长度60的金条分成30和30，花费60;再把长度30金条分成10和20， 花费30;一共花费90铜板。

输入一个数组，返回分割的最小代价。
 */

/*
思维导图
方法一：暴力递归
process(int[] arr, int pre) {} 等待合并的数都在arr里，pre之前的合并行为产生了多少总代价， arr中只剩一个数字的时候，停止合并，返回最小的总代价

方法二：贪心，哈夫曼树
	准备一个小根堆，每次从小根堆弹出2个数，合并成1个数，再放回去。周而复始，放回去的数就是答案

 */

/*
	小根堆，大根堆，排序是贪心策略最常用的三个工具
	堆：根据我指定的排序标准把数组组成堆，依次弹出，就有贪心的意思
 */


public class Code02_LessMoneySplitGold {

	/*
	方法一：暴力递归
	通过递归穷举所有可能的分割组合，计算每种组合的总代价，从中找出最小的分割代价。
	 */
	public static int lessMoney1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		return process(arr, 0);
	}

	// 等待合并的数都在arr里，pre之前的合并行为产生了多少总代价
	// arr中只剩一个数字的时候，停止合并，返回最小的总代价
	public static int process(int[] arr, int pre) {
		if (arr.length == 1) { // 如果数组中只剩一个元素，返回当前总代价
			return pre;
		}
		int ans = Integer.MAX_VALUE; // 初始化最小代价为最大整数值
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				// 合并arr[i]和arr[j]，递归调用process方法，并计算新的总代价
				ans = Math.min(ans, process(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
			}
		}
		return ans;
	}

	// 辅助方法，复制数组并合并指定索引的两个元素
	public static int[] copyAndMergeTwo(int[] arr, int i, int j) {
		int[] ans = new int[arr.length - 1];  // 创建一个新数组，长度为原数组长度减1
		int index = 0;  // 新数组的索引
		for (int arri = 0; arri < arr.length; arri++) {  // 遍历原数组
			if (arri != i && arri != j) {  // 跳过索引为i和j的元素
				ans[index++] = arr[arri];  // 将其他元素复制到新数组中
			}
		}
		ans[index] = arr[i] + arr[j];  // 将合并的元素添加到新数组的最后
		return ans;  // 返回新数组
	}

	/*
	方法二：贪心
	*/
	public static int lessMoney2(int[] arr) {
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int i = 0; i < arr.length; i++) {
			heap.add(arr[i]);
		}
		int sum = 0; // 需要的铜币数
		while (heap.size() > 1) {
			// 每次弹出2个数，再放回小根堆
			int cur = heap.poll() + heap.poll();
			heap.add(cur);
			sum += cur;
		}
		return sum;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 100000;
		int maxSize = 6;
		int maxValue = 1000;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			if (lessMoney1(arr) != lessMoney2(arr)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
