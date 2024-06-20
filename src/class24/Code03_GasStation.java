package class24;

import java.util.LinkedList;

/*
题意：加油站的良好出发点问题
在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。
给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1 。如果存在解，则 保证 它是 唯一 的。
示例 1:
输入: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
输出: 3
解释:
从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
因此，3 可为起始索引。
// 测试链接：https://leetcode.cn/problems/gas-station/description/
 */

// 时间：1：30
// 时复：o(n)
/**
 * 思维导图：
 * 我们这里提升一下难度，题目只要求返回1个能走完的，我们求出所有能走完的
 *
 * 1、加工数组，汽油-花费，gas-cost. 问题就是求从每个点出发求累加和不掉到0以下，就能跑完一圈。
 * 2、暴力解，每个位置循环数组遍历，每个点转一圈，求累加和，O（N^2)
 * 3、生成2倍长度的累加和数组，累加和数组是N*2的长度，所有原始数组中出发累加和数组，在这个长数组中都能把它原始的累加和数组加工出来，想达成这个目的
 * 如何还原出原始的累加和数组：减去出发点的前一个数就行。（去掉前面累加和的影响）
 * 4、依次求每一个位置出发，是否能够跑完一圈：
 * 		准备一个长度为N的窗口，依次找到这个累加和数组在窗口中最薄弱点，如果这个最薄弱点出发的累加和，还原出原始的值的累加和，只要<0，
 * 		说明原始的值从这个点出发会有<0的时刻就无法走完一圈,那么当前求的这个位置出发，无法走完一圈
 *
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
		LinkedList<Integer> minWindow = new LinkedList<>(); // 窗口的最小值更新结构
		for (int i = 0; i < N; i++) {
			while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[i]) {
				minWindow.pollLast();
			}
			minWindow.addLast(i);
		}

		boolean[] ans = new boolean[N]; // 存放每个位置的结果
		int offset = 0; // 最弱的位置要减去的值，初始为0。初始窗口是[0,N-1], 它右边的数没有，就不用减少，初始置为0
		for (int L = 0, R = N; L < N; L++, R++) {
			// 窗口[L,R)
			if ((arr[minWindow.peekFirst()] - offset) >= 0) { // arr[minWindow.peekFirst()]当前窗口最薄弱的点
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

			offset = arr[L]; // 当前窗口是[L,R),接下来窗口马上要变成[L+1,R+1),offset应该就是窗口左边的一个值，就是arr[l]
		}
		return ans;
	}

}
