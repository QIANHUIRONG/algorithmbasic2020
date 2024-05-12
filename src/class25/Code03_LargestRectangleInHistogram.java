package class25;

import java.util.Stack;

// 测试链接：https://leetcode.com/problems/largest-rectangle-in-histogram
// 时间：1：27

/**
 * 1、依次以每一个位置的数做高，扩出的最大矩形。客观上的答案，一定以某一个位置的数作高；
 * 2、扩出的最大矩形，就是利用单调栈求左右两边比我小的，就是我扩不到的
 * 3、同样的道理，有重复值，也结算，前面如果算错了，后序会有时刻算对的
 * 4、时间复杂度O（n)
 */
public class Code03_LargestRectangleInHistogram {

	public static int largestRectangleArea1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int ans = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < arr.length; i++) {
			while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) { // 有重复值也结算
				int j = stack.pop();
				int k = stack.isEmpty() ? -1 : stack.peek();
				int curArea = (i - k - 1) * arr[j];
				ans = Math.max(ans, curArea);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			int k = stack.isEmpty() ? -1 : stack.peek();
			int curArea = (arr.length - k - 1) * arr[j];
			ans = Math.max(ans, curArea);
		}
		return ans;
	}

	public static int largestRectangleArea2(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int N = height.length;
		int[] stack = new int[N];
		int si = -1;
		int maxArea = 0;
		for (int i = 0; i < height.length; i++) {
			while (si != -1 && height[i] <= height[stack[si]]) {
				int j = stack[si--];
				int k = si == -1 ? -1 : stack[si];
				int curArea = (i - k - 1) * height[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack[++si] = i;
		}
		while (si != -1) {
			int j = stack[si--];
			int k = si == -1 ? -1 : stack[si];
			int curArea = (height.length - k - 1) * height[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
