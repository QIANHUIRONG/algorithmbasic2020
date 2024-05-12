package class25;

import java.util.Stack;

// 测试链接：https://leetcode.com/problems/maximal-rectangle/
// 时间：1：34

/*
1、暴力解：枚举所有的子矩阵，再遍历判断每个子矩阵是不是都是1，O（n^4) * O(N^2) = O(n^6)
2、子数组必须以第0行做地基，求一个答案；子数组必须以第1行做地基，求一个答案；客观上答案一定以某一行做地基。每一行求答案就是上一题！
3、压缩数组，遇到1累加，遇到0高度归0
4、复杂度：o(n*m)。求一次压缩数组：O(m),求一个压缩数组的答案：O(m), 一共求n次
 */
public class Code04_MaximalRectangle {

	public static int maximalRectangle(char[][] map) {
		if (map == null || map.length == 0 || map[0].length == 0) {
			return 0;
		}
		int ans = 0;
		int[] arr = new int[map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				arr[j] = map[i][j] == '0' ? 0 : arr[j] + 1;
			}
			ans = Math.max(maxRecFromBottom(arr), ans);
		}
		return ans;
	}

	// height是正方图数组
	public static int maxRecFromBottom(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int maxArea = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < arr.length; i++) {
			while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
				int j = stack.pop();
				int k = stack.isEmpty() ? -1 : stack.peek();
				int curArea = (i - k - 1) * arr[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			int k = stack.isEmpty() ? -1 : stack.peek();
			int curArea = (arr.length - k - 1) * arr[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
