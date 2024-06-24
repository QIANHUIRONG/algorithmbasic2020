package class38;

/*
【题意】打怪兽需要花的最小钱数
int[] d，d[i]：i号怪兽的能力
int[] p，p[i]：i号怪兽要求的钱
开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。
如果你当前的能力，小于i号怪兽的能力，你必须付出p[i]的钱，贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上；
如果你当前的能力，大于等于i号怪兽的能力，你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪兽，然后怪兽就会加
入你，他的能力直接累加到你的能力上。
返回通过所有的怪兽，需要花的最小钱数。
 */

/*
时间
题意：1：26
题解1：1：30
code1：1：35
每个怪兽的能力值：10^8 :1:40
题解2：1：43
code2：1：55
两种方式分别适用于什么场景的数据量：2：09
再讲一遍方法二：2：15

 */

/*
【思维导图】
[从左往右的尝试模型]
思路一：
	1.
		process1(int ability, int index)：当前来到了index号怪兽的面前，你的能力是ability，如果要通过后续所有的怪兽，请返回需要花的最少钱数。
		basecase：index来到结尾，后续已经没有怪兽了，返回0
		主过程，根据能力值，可能性一：必须贿赂怪兽；可能性二：可以贿赂，也可以不贿赂
	2.如果改dp，index：0-N，ability：0-所有怪兽能力的累加和，如果每个怪兽能力都很大，比如都是10^8左右，那么ability就会
超过10^8，填完整张表，就会超时
	3.如果怪兽的能力值比较小，比如说100以内，怪兽的数量呢，100个以内，那我知道这即便是在最夸张的情况下，
我的能力也不会超过10000。累加和最大是10000，index的范围是100，这张表就是一个10^6的一张表，如果我能力比较小，
我可以知道我做动态规划有前途，在10^8以内，它能填完

思路二：
	1.
		process2(int index, int money):当前来到了index号怪兽的面前，花的钱数必须严格等于money，返回能通过的最大能力值。如果花money无法通过，返回-1.
		basecase:第1个怪兽，花的钱必须是p[0], 才有意义。此时返回的最大能力值d[0]
		主过程：可能性一：不贿赂怪兽，花money钱是能通过到index-1的，并且之前通过的最大能力值必须大于index号怪兽；
				可能性二：贿赂怪兽，花money-p[index]是能通过到index-1的
	2.index：0-n，money：所有怪兽的钱数累加和。如果你一个怪兽贿赂的钱非常大，这种方法不适用，改成动态规划这张表的规模超过
10^8不行。如果每一个怪兽贿赂它的钱数都是比较小。比如1~10，怪兽一共有100个，你写这张表的规模就很小就能通过了

三、腾讯考的时候的数据量：
	真实的考这道题的时候。怪兽数量：500,每个怪兽花的钱1~10，怪兽能力值10^6以上，选择第二种方法.
	如果怪兽的能力减小了，而钱数变大了，选择第一种方法
 */
public class Code04_MoneyProblem {

	/**
	 * 方法一
	 * @param d d[i]：i号怪兽的武力
	 * @param p p[i]：i号怪兽要求的钱
	 * @return
	 */
	public static long func1(int[] d, int[] p) {
		return process1(d, p, 0, 0);
	}

	// ability 当前你所具有的能力
	// index 来到了第index个怪兽的面前
	// 当前来到了index号怪兽的面前，你的能力是ability，如果要通过后续所有的怪兽，请返回需要花的最少钱数
	public static long process1(int[] d, int[] p, int ability, int index) {
		if (index == d.length) {
			return 0;
		}
		if (ability < d[index]) { // 必须贿赂怪兽
			return p[index] + process1(d, p, ability + d[index], index + 1);
		} else { // ability >= d[index] 可以贿赂，也可以不贿赂
			return Math.min(
					p[index] + process1(d, p, ability + d[index], index + 1),
					0 + process1(d, p, ability, index + 1));
		}
	}

	/**
	 * 方法一对应的动态规划
	 */
	public static long func2(int[] d, int[] p) {
		int sum = 0;
		for (int num : d) { // 所有怪兽能力的累加和
			sum += num;
		}
		// index: 0-n
		// abliity：0-所有怪兽能力累加和
		long[][] dp = new long[d.length + 1][sum + 1];
		for (int index = d.length - 1; index >= 0; index--) {
			for (int ability = 0; ability <= sum; ability++) {
				// 如果这种情况发生，那么这个hp必然是递归过程中不会出现的状态
				// 既然动态规划是尝试过程的优化，尝试过程碰不到的状态，不必计算
				if (ability + d[index] > sum) {
					continue;
				}
				if (ability < d[index]) {
					dp[index][ability] = p[index] + dp[index + 1][ability + d[index]];
				} else {
					dp[index][ability] = Math.min(p[index] + dp[index + 1][ability + d[index]], dp[index + 1][ability]);
				}
			}
		}
		return dp[0][0];
	}

	/**
	 * 方法二
	 * @param d d[i]：i号怪兽的武力
	 * @param p p[i]：i号怪兽要求的钱
	 * @return
	 */
	public static int minMoney2(int[] d, int[] p) {
		int allMoney = 0;
		for (int i = 0; i < p.length; i++) {
			allMoney += p[i]; // 贿赂所有怪兽花的钱，这是极限
		}
		int N = d.length;
		for (int money = 0; money < allMoney; money++) {
			// 从最小钱数开始试能通过所有怪兽，如果0元能通过，就是0元；如果1元能通过就是1元
			if (process2(d, p, N - 1, money) != -1) {
				return money;
			}
		}
		// 如果递归都无法通过，那么最多也就是花所有怪兽累加的钱
		return allMoney;
	}


	// 从0....index号怪兽，花的钱，必须严格==money
	// 如果通过不了，返回-1
	// 如果可以通过，返回能通过情况下的最大能力值
	public static long process2(int[] d, int[] p, int index, int money) {
		// 两种basecase都可以。basecase不知道怎么定义的时候，如果你清楚主流程，也可以反推basecase
		// 这里主流程，index会越来越小，那么basecase无非就是思考index=0，或者index=1怎么样

		// basecase1
//		if (index == -1) { // 一个怪兽也没遇到呢
//			return == 0 ? 0 : -1;
//		}
		// basecase2：第一个怪兽的情况
		if (index == 0) {
			return money == p[0] ? d[0] : -1; // 第1个怪兽，花的钱必须是p[0], 才有意义。此时返回的最大能力值d[0]
		}
		// 1) 不贿赂当前index号怪兽
		long preMaxAbility = process2(d, p, index - 1, money);
		long p1 = -1;
		if (preMaxAbility != -1 && preMaxAbility >= d[index]) {	// 花money的钱，之前是能通过到index-1的，并且之前通过的最大能力值必须大于index号怪兽

			p1 = preMaxAbility;
		}
		// 2) 贿赂当前的怪兽 当前的钱 p[index]
		long preMaxAbility2 = process2(d, p, index - 1, money - p[index]);
		long p2 = -1;
		if (preMaxAbility2 != -1) { // 我之前是能通过到index-1的
			p2 = d[index] + preMaxAbility2;
		}
		return Math.max(p1, p2);
	}

	/*
	方法二对应的动态规划
	 */
	public static long func3(int[] d, int[] p) {
		int sum = 0;
		for (int num : p) { // 所有怪兽花的钱的累加和
			sum += num;
		}
		long[][] dp = new long[d.length][sum + 1];
		for (int i = 0; i < dp.length; i++) { // 初始化所有的格子都是-1
			for (int j = 0; j <= sum; j++) {
				dp[i][j] = -1;
			}
		}

		// basecase
		dp[0][p[0]] = d[0];
		for (int index = 1; index < d.length; index++) {
			for (int money = 0; money <= sum; money++) {
				long preMaxAbility = dp[index - 1][money];
				long p1 = -1;
				if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
					p1 = preMaxAbility;
				}

				long p2 = -1;
				if (money - p[index] >= 0) {
					long preMaxAbility2 = dp[index - 1][money - p[index]];
					if (preMaxAbility2 != -1) {
						p2 = d[index] + preMaxAbility2;
					}
				}
				dp[index][money] = Math.max(p1, p2);
			}
		}

		int allMoney = 0;
		for (int i = 0; i < p.length; i++) {
			allMoney += p[i];
		}
		int N = d.length;
		for (int money = 0; money < allMoney; money++) {
			if (dp[N-1][money] != -1) {
				return money;
			}
		}
		return allMoney;
	}

	public static int[][] generateTwoRandomArray(int len, int value) {
		int size = (int) (Math.random() * len) + 1;
		int[][] arrs = new int[2][size];
		for (int i = 0; i < size; i++) {
			arrs[0][i] = (int) (Math.random() * value) + 1;
			arrs[1][i] = (int) (Math.random() * value) + 1;
		}
		return arrs;
	}

	public static void main(String[] args) {
		int len = 10;
		int value = 20;
		int testTimes = 10000;
		for (int i = 0; i < testTimes; i++) {
			int[][] arrs = generateTwoRandomArray(len, value);
			int[] d = arrs[0];
			int[] p = arrs[1];
			long ans1 = func1(d, p);
			long ans2 = func2(d, p);
			long ans3 = func3(d, p);
			long ans4 = minMoney2(d,p);
			if (ans1 != ans2 || ans2 != ans3 || ans1 != ans4) {
				System.out.println("oops!");
			}
		}

	}

}
