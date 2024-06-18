package class20;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
/*
 题目
 数组arr代表每一个咖啡机冲一杯咖啡的时间，每个咖啡机只能串行的制造咖啡。
 现在有n个人需要喝咖啡，只能用咖啡机来制造咖啡。
 认为每个人喝咖啡的时间非常短，冲好的时间即是喝完的时间。
 每个人喝完之后咖啡杯可以选择洗或者自然挥发干净，只有一台洗咖啡杯的机器，只能串行的洗咖啡杯。
 洗杯子的机器洗完一个杯子时间为a，任何一个杯子自然挥发干净的时间为b。
 四个参数：arr, n, a, b
 假设时间点从0开始，返回所有人喝完咖啡并洗完咖啡杯的全部过程结束后，至少来到什么时间点。
 */
/*
  时间：
  题意：1：40
  先不考虑洗杯子，只先去求每个人最快什么时间点能喝到咖啡：1：43
  每个人最快什么时间点喝完的数组我已经有了，考虑洗杯子，最快什么时候都洗完:2：00
  code:2:14
 */
/*
思维导图：
[寻找业务限制的尝试模型]
把这道题拆分成2道题：
	一.先不考虑洗杯子，只先去求每个人最快什么时间点能喝到咖啡。一共N个人，那你给我生成一个drink[N]的数组
		1.用小根堆。
		小根堆存放的对象：咖啡机能用的时间，咖啡机泡一杯咖啡的时间；
		小根堆组织：咖啡机能用的时间+咖啡机泡一杯咖啡的时间，小根堆去组织，表示的意义就是用户最快能够喝到咖啡的时间


	二.每个人最快什么时间点喝完的数组我已经有了，考虑洗杯子，最快什么时候都洗完:
		1.这里就存在一个选择的问题，我这个杯子几个去自然挥发，几个去让洗的机器洗
		2.
			bestTime(int[] drinks, int wash, int air, int index, int free) {}:
				drinks[]所有杯子可以开始洗的时间。喝咖啡的时间为0，能喝就可以开始洗！
				 wash 单杯洗干净的时间（串行）
				 air 挥发干净的时间(并行)
				 free 洗的机器什么时候可用。洗的机器只有1台
				 返回drinks[index.....]都变干净，最早的结束时间
			basecase：没有杯子了，0号时间点结束
			普遍流程：
				1.index号杯子决定洗：index号杯子洗干净的时间、后续杯子洗干净的时间，求max！木桶原理，我和我的后续必须要都洗完了，才叫做从index往后的所有杯子都洗完了，递归才能返回
				2.index号杯子决定挥发：index号杯子挥发干净的时间、后续杯子洗干净的时间，求max！
				两种情况返回最小值！表示两种选择都洗完了，都已经求出了各自的最短时间，我去求两种中时间快的

		3.改dp
		改动态规划的时候，free：变化范围无法估计-> 业务限制模型。你的可变参数不能直观的得到变化范围，我们需要从业务上，人为的想限制来把free的变化范围估计出来
	 	free最大是什么：就是所有杯子都去洗，冲到的最大时间点
	 	一句话：限制不够，业务来凑
	 	如果你实在改不出严格表结构的动态规划，咱就用记忆化搜索！！！
 */


public class Code03_Coffee {

	/**
	 * 1、验证的方法
	 * 彻底的暴力
	 * 很慢但是绝对正确
	 */
	public static int right(int[] arr, int n, int a, int b) {
		int[] times = new int[arr.length];
		int[] drink = new int[n];
		return forceMake(arr, times, 0, drink, n, a, b);
	}

	// 每个人暴力尝试用每一个咖啡机给自己做咖啡
	public static int forceMake(int[] arr, int[] times, int kth, int[] drink, int n, int a, int b) {
		if (kth == n) {
			int[] drinkSorted = Arrays.copyOf(drink, kth);
			Arrays.sort(drinkSorted);
			return forceWash(drinkSorted, a, b, 0, 0, 0);
		}
		int time = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			int work = arr[i];
			int pre = times[i];
			drink[kth] = pre + work;
			times[i] = pre + work;
			time = Math.min(time, forceMake(arr, times, kth + 1, drink, n, a, b));
			drink[kth] = 0;
			times[i] = pre;
		}
		return time;
	}

	public static int forceWash(int[] drinks, int a, int b, int index, int washLine, int time) {
		if (index == drinks.length) {
			return time;
		}
		// 选择一：当前index号咖啡杯，选择用洗咖啡机刷干净
		int wash = Math.max(drinks[index], washLine) + a;
		int ans1 = forceWash(drinks, a, b, index + 1, wash, Math.max(wash, time));

		// 选择二：当前index号咖啡杯，选择自然挥发
		int dry = drinks[index] + b;
		int ans2 = forceWash(drinks, a, b, index + 1, washLine, Math.max(dry, time));
		return Math.min(ans1, ans2);
	}

	// 以下为贪心+优良暴力
	public static class Machine {
		public int timePoint; // 能用的时间点
		public int workTime; // 泡一杯咖啡需要的时间

		public Machine(int t, int w) {
			timePoint = t;
			workTime = w;
		}
	}

	public static class MachineComparator implements Comparator<Machine> {

		@Override
		public int compare(Machine o1, Machine o2) {
			return (o1.timePoint + o1.workTime) - (o2.timePoint + o2.workTime);
		}

	}

	/**
	 * 2、优良一点的暴力尝试的方法（课堂上讲解的）
	 * 把这道题拆分成2道题：
	 * 	1.先不考虑洗杯子，只先去求每个人最快什么时间点能喝到咖啡。一共N个人，那你给我生成一个drink[N]的数组
	 * 	2.每个人最快什么时间点喝完的数组我已经有了，考虑洗杯子，最快什么时候都洗完。
	 */
	public static int minTime1(int[] arr, int n, int a, int b) {
		// 1、生成drink[]数组
		PriorityQueue<Machine> heap = new PriorityQueue<Machine>(new MachineComparator()); // 小根堆按照，每个咖啡机能用的时间点+泡一杯咖啡需要的时间，去组织
		for (int i = 0; i < arr.length; i++) {
			heap.add(new Machine(0, arr[i])); // 初始的时候，每个咖啡机能用的时间点都是0时间点
		}
		int[] drinks = new int[n];
		for (int i = 0; i < n; i++) {
			Machine cur = heap.poll();
			cur.timePoint += cur.workTime;
			drinks[i] = cur.timePoint; // i号人能喝上咖啡的时间，就是咖啡机能用的时间+泡一杯需要的时间
			heap.add(cur);
		}
		// 2、洗杯子
		return bestTime(drinks, a, b, 0, 0);
	}

	// drinks 所有杯子可以开始洗的时间。喝咖啡的时间为0，能喝就可以开始洗！
	// wash 单杯洗干净的时间（串行）
	// air 挥发干净的时间(并行)
	// free 洗的机器什么时候可用。洗的机器只有1台
	// drinks[index.....]都变干净，最早的结束时间（返回）
	public static int bestTime(int[] drinks, int wash, int air, int index, int free) {
		if (index == drinks.length) { // 没有杯子了，0号时间点结束
			return 0;
		}
		// 情况一：index号杯子 决定洗
		// 我自己洗干净的时间
		int selfClean1 = Math.max(drinks[index], free) + wash; // drinks[index]：我喝到咖啡的时间；free： 洗的机器什么时候可用；Math.max(drinks[index], free)：index号杯子什么时候能洗
		// 后序杯子干净的时间
		int restClean1 = bestTime(drinks, wash, air, index + 1, selfClean1);
		int p1 = Math.max(selfClean1, restClean1);

		// 情况二：index号杯子 决定挥发
		// 我自己挥发干净的时间
		int selfClean2 = drinks[index] + air;
		// 后序杯子干净的时间
		int restClean2 = bestTime(drinks, wash, air, index + 1, free);
		int p2 = Math.max(selfClean2, restClean2);

		// 最终两种情况取时间点短的
		return Math.min(p1, p2);
	}

	/**
	 * 3、优良一点的暴力尝试的方法改动态规划。课上讲解的
	 * 	改动态规划的时候，free：变化范围无法估计-> 业务限制模型。你的可变参数不能直观的得到变化范围，我们需要从业务上，人为的想限制来把time的变化范围估计出来
	 * 	free最大是什么：就是所有杯子都去洗，冲到的最大时间点
	 * 	一句话：限制不够，业务来凑
	 * 	如果你实在改不出严格表结构的动态规划，咱就用记忆化搜索！！！
	 * @param arr
	 * @param n
	 * @param a
	 * @param b
	 * @return
	 */
	public static int minTime2(int[] arr, int n, int a, int b) {
		PriorityQueue<Machine> heap = new PriorityQueue<Machine>(new MachineComparator());
		for (int i = 0; i < arr.length; i++) {
			heap.add(new Machine(0, arr[i]));
		}
		int[] drinks = new int[n];
		for (int i = 0; i < n; i++) {
			Machine cur = heap.poll();
			cur.timePoint += cur.workTime;
			drinks[i] = cur.timePoint;
			heap.add(cur);
		}
		return bestTimeDp(drinks, a, b);
	}

	public static int bestTimeDp(int[] drinks, int wash, int air) {
		int N = drinks.length;
		// free能冲到的最大值
		int maxFree = 0;
		for (int i = 0; i < drinks.length; i++) {
			maxFree = Math.max(maxFree, drinks[i]) + wash;
		}
		int[][] dp = new int[N + 1][maxFree + 1];
		for (int index = N - 1; index >= 0; index--) {
			for (int free = 0; free <= maxFree; free++) {
				/**
				 * selfClean1：这个东西有可能越界，这里的free是要逼近maxFree的，这里再+wash，就越界了。在业务上，free就是最大值，超出了，这个格子业务上是没有意义的，递归也调用不到。
				 * 其实就是类似，范围尝试模型中，dp右下角部分不用填，因为L不会跑到R右边去
				 */
				int selfClean1 = Math.max(drinks[index], free) + wash;
				if (selfClean1 > maxFree) {
					break; // 因为后面的也都不用填了
				}
				// index号杯子 决定洗
				int restClean1 = dp[index + 1][selfClean1];
				int p1 = Math.max(selfClean1, restClean1);
				// index号杯子 决定挥发
				int selfClean2 = drinks[index] + air;
				int restClean2 = dp[index + 1][free];
				int p2 = Math.max(selfClean2, restClean2);
				dp[index][free] = Math.min(p1, p2);
			}
		}
		return dp[0][0];
	}

	// for test
	public static int[] randomArray(int len, int max) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * max) + 1;
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		System.out.print("arr : ");
		for (int j = 0; j < arr.length; j++) {
			System.out.print(arr[j] + ", ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 10;
		int max = 10;
		int testTime = 10;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(len, max);
			int n = (int) (Math.random() * 7) + 1;
			int a = (int) (Math.random() * 7) + 1;
			int b = (int) (Math.random() * 10) + 1;
			int ans1 = right(arr, n, a, b);
			int ans2 = minTime1(arr, n, a, b);
			int ans3 = minTime2(arr, n, a, b);
			if (ans1 != ans2 || ans2 != ans3) {
				printArray(arr);
				System.out.println("n : " + n);
				System.out.println("a : " + a);
				System.out.println("b : " + b);
				System.out.println(ans1 + " , " + ans2 + " , " + ans3);
				System.out.println("===============");
				break;
			}
		}
		System.out.println("测试结束");

	}

}
