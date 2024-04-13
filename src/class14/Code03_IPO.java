package class14;

import java.util.Comparator;
import java.util.PriorityQueue;

// 做项目获得的最大钱数
/*
输入: 正数数组costs[]、正数数组profits[]、正数K、正数M
costs[i]表示i号项目的花费
profits[i]表示i号项目在扣除花费之后还能挣到的钱(利润)
K表示你只能串行的最多做k个项目
M表示你初始的资金
说明: 每做完一个项目，马上获得的收益，可以支持你去做下一个项目。不能并行的做项目。
输出：你最后获得的最大钱数。
 */
/*
题解：
打游戏：我挑我能打过的中收益最大的！这里也一样，挑能做的，利润最高的。
能做的：准备一个小根堆，按照花费组织。把能做的都吐出来，放到大根堆，大根堆按照利润组织。
从大根堆中拿出来做，就是挑能做的，利润最高的。
 */
public class Code03_IPO {

	// 最多K个项目
	// M是初始资金
	// Profits[] Capital[] 一定等长
	// 返回最终最大的资金
	public static int findMaximizedCapital(int K, int M, int[] Profits, int[] Capital) {
		PriorityQueue<Program> minCostQ = new PriorityQueue<>(new MinCostComparator());
		PriorityQueue<Program> maxProfitQ = new PriorityQueue<>(new MaxProfitComparator());
		for (int i = 0; i < Profits.length; i++) { // 所有项目先放到小根堆
			minCostQ.add(new Program(Profits[i], Capital[i]));
		}
		for (int i = 0; i < K; i++) {
			while (!minCostQ.isEmpty() && minCostQ.peek().c <= M) { // 挑出能做的。花费小于我的资金，入大根堆
				maxProfitQ.add(minCostQ.poll());
			}
			if (maxProfitQ.isEmpty()) { // 没项目可做了，直接返回
				return M;
			}
			M += maxProfitQ.poll().p; // 能做项目，累计金额
		}
		return M;
	}

	public static class Program {
		// 利润
		public int p;
		// 花费
		public int c;

		public Program(int p, int c) {
			this.p = p;
			this.c = c;
		}
	}

	public static class MinCostComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o1.c - o2.c;
		}

	}

	public static class MaxProfitComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o2.p - o1.p;
		}

	}

}
