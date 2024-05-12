package class24;

import java.util.LinkedList;

// 测试链接：https://leetcode.cn/problems/gas-station/description/
// 时间：1：30

/**
 * 1、加工数组，gas-cost. 问题就是求从每个点出发求累加和不掉到0以下，就能跑完一圈。
 * 2、暴力解，每个位置循环数组遍历，每个点转一圈，O（N^2)
 * 3、生成2倍长度的累加和数组，累加和数组是N*2的长度，在这个长数组中都能把它原始的累加和数组加工出来
 * 4、准备一个长度为N的窗口，依次找到窗口的最薄弱0点，还原出最薄弱点原始的值，只要<0,这么这个位置无效
 */
public class Code03_GasStation {

	// 这个方法的时间复杂度O(N)，额外空间复杂度O(N)
	public static int canCompleteCircuit(int[] gas, int[] cost) {
		boolean[] good = goodArray(gas, cost);
		for (int i = 0; i < gas.length; i++) {
			if (good[i]) {
				return i;
			}
		}
		return -1;
	}

	public static boolean[] goodArray(int[] g, int[] c) {
		int N = g.length;
		int M = N * 2;
		int[] arr = new int[M];
		for (int i = 0; i < N; i++) {
			arr[i] = g[i] - c[i];
			arr[i + N] = g[i] - c[i];
		}
		// 生成2倍长度的累加和数组
		for (int i = 1; i < M; i++) {
			arr[i] += arr[i - 1];
		}

		// 接下来生成长度为N的窗口
		LinkedList<Integer> minWindow = new LinkedList<>();
		for (int i = 0; i < N; i++) {
			while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[i]) {
				minWindow.pollLast();
			}
			minWindow.addLast(i);
		}

		boolean[] ans = new boolean[N];
		int offset = 0; // 最弱的位置要减去的值，初始为0
		for (int L = 0, R = N; L < N; L++, R++) {
			// 窗口[L,R)
			if ((arr[minWindow.peekFirst()] - offset) >= 0) {
				ans[L] = true;
			}
			// 接下来窗口要扩了，R先扩1个，L再扩一个
			// 马上就要R++
			while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
				minWindow.pollLast();
			}
			minWindow.addLast(R);
			// 马上就要L++
			if (minWindow.peekFirst() == L) {
				minWindow.pollFirst();
			}

			offset = arr[L];
		}
		return ans;
	}

}
