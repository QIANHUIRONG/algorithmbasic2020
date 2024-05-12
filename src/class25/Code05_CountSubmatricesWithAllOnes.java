package class25;

// 测试链接：https://leetcode.com/problems/count-submatrices-with-all-ones
// 时间：2：00
// 相等咋办：2：17

import java.util.Stack;

/*
1、第0行做底的子矩形，全是1的有多少个；第2行做底的子矩形，全是1的有多少个；累加。这样求解不会多也不会少
2、以某一行做底时：
	1.求必须以0位置的7做高的情况下，全是1的子矩形有多少个？求必须以1位置的8做高的情况下，全是1的子矩形有多少个？所有的累加
	2.0位置的7做高，利用单调栈，左右两边到不了的位置是l,r, 那么全是1的子矩形数量是 (1 + n) * n / 2。 （首项+尾项） * 项数 / 2
	3.5位置的7做高，利用单调栈，左边是3位置的1，右边是9位置的2，那么高度为7，6，5，4，3都要算，都是上一步的公式。而高度为2的让出来的时候自己算。
	4.（2：17）相等的时候，前面的不算，后面的统一算。
 */
public class Code05_CountSubmatricesWithAllOnes {

	public static int numSubmat(int[][] mat) {
		if (mat == null || mat.length == 0 || mat[0].length == 0) {
			return 0;
		}
		int nums = 0;
		int[] arr = new int[mat[0].length];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				arr[j] = mat[i][j] == 0 ? 0 : arr[j] + 1;
			}
			nums += countFromBottom(arr);
		}
		return nums;

	}


	// 系统栈
	// 比如
	//              1
	//              1
	//              1         1
	//    1         1         1
	//    1         1         1
	//    1         1         1
	//
	//    2  ....   6   ....  9
	// 如上图，假设在6位置，1的高度为6
	// 在6位置的左边，离6位置最近、且小于高度6的位置是2，2位置的高度是3
	// 在6位置的右边，离6位置最近、且小于高度6的位置是9，9位置的高度是4
	// 此时我们求什么？
	// 1) 求在3~8范围上，必须以高度6作为高的矩形，有几个？
	// 2) 求在3~8范围上，必须以高度5作为高的矩形，有几个？
	// 也就是说，<=4的高度，一律不求
	// 那么，1) 求必须以位置6的高度6作为高的矩形，有几个？
	// 3..3  3..4  3..5  3..6  3..7  3..8
	// 4..4  4..5  4..6  4..7  4..8
	// 5..5  5..6  5..7  5..8
	// 6..6  6..7  6..8
	// 7..7  7..8
	// 8..8
	// 这么多！= 21 = (9 - 2 - 1) * (9 - 2) / 2
	// 这就是任何一个数字从栈里弹出的时候，计算矩形数量的方式
	public static int countFromBottom(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int nums = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < arr.length; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) { // 等于的时候也必须出来，栈严格维持由小到大，否则单调栈就维持不住了
				int cur = stack.pop();
				if (arr[cur] > arr[i]) { // 只有严格大于才结算，相等的时候不算
					int left = stack.isEmpty() ? -1 : stack.peek();
					int down = Math.max(left == -1 ? 0 : arr[left], arr[i]); // 左右两边比我小的中的较大值
					int n = i - left - 1; // 扩出来的长度
					nums += (arr[cur] - down) * num(n); // height[cur] - down每一个都结算
				}
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int cur = stack.pop();
			int left = stack.isEmpty() ? -1 : stack.peek();
			int n = arr.length - left - 1;
			int down = left == -1 ? 0 : arr[left];
			nums += (arr[cur] - down) * num(n);
		}
		return nums;
	}


	// 数组实现的栈：
	public static int countFromBottom2(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int nums = 0;
		int[] stack = new int[height.length];
		int si = -1;
		for (int i = 0; i < height.length; i++) {
			while (si != -1 && height[stack[si]] >= height[i]) {
				int cur = stack[si--];
				if (height[cur] > height[i]) {
					int left = si == -1 ? -1 : stack[si];
					int n = i - left - 1;
					int down = Math.max(left == -1 ? 0 : height[left], height[i]);
					nums += (height[cur] - down) * num(n);
				}

			}
			stack[++si] = i;
		}
		while (si != -1) {
			int cur = stack[si--];
			int left = si == -1  ? -1 : stack[si];
			int n = height.length - left - 1;
			int down = left == -1 ? 0 : height[left];
			nums += (height[cur] - down) * num(n);
		}
		return nums;
	}

	public static int num(int n) {
		return ((n * (1 + n)) / 2);
	}

}
