package class25;

import java.util.Stack;
/*
题意：子数组累加和*子数组中最小值的最大值
给定一个只包含正数的数组arr，arr中任何一个子数组sub，
一定都可以算出(sub累加和 )* (sub中的最小值)是什么，
那么所有子数组中，这个值最大是多少？
 */
/*
时间：52
 */
// O（N）
/*
子数组：连续的
1、必须以0位置的3做最小值，子数组扩到的最大范围，求一个答案；必须以1位置的4做最小值，子数组扩到的最大范围，求一个答案；客观上来说，答案一定以某个位置的数做最小值，答案比在其中
2、子数组扩到最大范围：利用单调栈求左右两边离我最近比我小的；就是我扩不到的
3、求一个范围上的累加和如何快：先预处理一个前缀累加和数组。sum[l...r] = sum[r] - sum[l - 1];
4、相等的时候，不用有重复值版本的单调栈。错了就错了，最后一个能算对。
	比如[1,2,2,2,1]
		 0 1 2 3 4
	遍历到4位置的1时，此时栈中是 3->2,2->2,1->2,0->1], (右边是栈底)，此时会把1，2，3位置的2都结算了，那么3位置的2，和2位置的2其实是错的，他们都
	没有找到左边离我最近比我小的0位置的1，但是没关系，等到结算1位置的2时，你不会错过全局的这个最大值；
5、时间复杂度：O(n)
 */
public class Code02_AllTimesMinToMax {

	// 对数器
	public static int max1(int[] arr) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				int minNum = Integer.MAX_VALUE;
				int sum = 0;
				for (int k = i; k <= j; k++) {
					sum += arr[k];
					minNum = Math.min(minNum, arr[k]);
				}
				max = Math.max(max, minNum * sum);
			}
		}
		return max;
	}

	// 最优解
	public static int max2(int[] arr) {
		int n = arr.length;

		// 1、预处理前缀累加和数组
		int[] sums = new int[n];
		sums[0] = arr[0];
		for (int i = 1; i < n; i++) {
			sums[i] = sums[i - 1] + arr[i];
		}

		int ans = Integer.MIN_VALUE;
		Stack<Integer> stack = new Stack<Integer>();
		// 2、依次以每一个位置做子数组的最小值，求答案
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) { // 等于也结算！错了就错了，会有算对的时候
				int j = stack.pop();
				// 此时j，左边离它最近比它小的：如果栈为空，就是-1；如果栈不为空，就是stack.peek()
				// 右边离它最近比它小的：i
				// 而流程是，以i位置的数最为子数组的最小值，所以左右两边离我最近比我小的我都到不了
				// l:0或者stack.peek() + 1
				// r:i - 1;
				// sum[l,r] = sum[r], 或者sum[r] - sum[l - 1]
				ans = Math.max(ans, (stack.isEmpty() ? sums[i - 1] : (sums[i - 1] - sums[stack.peek()])) * arr[j]);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			ans = Math.max(ans, (stack.isEmpty() ? sums[n - 1] : (sums[n - 1] - sums[stack.peek()])) * arr[j]);
		}
		return ans;
	}

	public static int[] gerenareRondomArray() {
		int[] arr = new int[(int) (Math.random() * 20) + 10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * 101);
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTimes = 2000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			int[] arr = gerenareRondomArray();
			if (max1(arr) != max2(arr)) {
				System.out.println("FUCK!");
				break;
			}
		}
		System.out.println("test finish");
	}

	// 本题可以在leetcode上找到原题
	// 测试链接 : https://leetcode.com/problems/maximum-subarray-min-product/
	// 注意测试题目数量大，要取模，但是思路和课上讲的是完全一样的
	// 注意溢出的处理即可，也就是用long类型来表示累加和
	// 还有优化就是，你可以用自己手写的数组栈，来替代系统实现的栈，也会快很多
	public static int maxSumMinProduct(int[] arr) {
		int size = arr.length;
		long[] sums = new long[size];
		sums[0] = arr[0];
		for (int i = 1; i < size; i++) {
			sums[i] = sums[i - 1] + arr[i];
		}
		long max = Long.MIN_VALUE;
		int[] stack = new int[size];
		int stackSize = 0;
		for (int i = 0; i < size; i++) {
			while (stackSize != 0 && arr[stack[stackSize - 1]] >= arr[i]) {
				int j = stack[--stackSize];
				max = Math.max(max,
						(stackSize == 0 ? sums[i - 1] : (sums[i - 1] - sums[stack[stackSize - 1]])) * arr[j]);
			}
			stack[stackSize++] = i;
		}
		while (stackSize != 0) {
			int j = stack[--stackSize];
			max = Math.max(max,
					(stackSize == 0 ? sums[size - 1] : (sums[size - 1] - sums[stack[stackSize - 1]])) * arr[j]);
		}
		return (int) (max % 1000000007);
	}

}
