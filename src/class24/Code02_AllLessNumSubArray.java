package class24;

import java.util.LinkedList;

/*
题意：整形数组中子数组最大值减最小值达标的子数组数量
给定一个整型数组arr，和一个整数num
某个arr中的子数组sub，如果想达标，必须满足：
sub中最大值 – sub中最小值 <= num，
返回arr中达标子数组的数量

 */
/*
时间：58
 */
//时复：o(n)
/*
思维导图：
	1.子数组是连续的
	2.暴力解：枚举所有的子数组，2个for循环；再来一个for循环求当前子数组的最大值和最小值，o(n^3)
	3.用窗口的最优解：O(n)
		0.为什么能用窗口：有单调性；窗口变大，窗口里面的最大值一定不变或者变大；窗口里面的最小值一定不变或者变小
		1.两个结论：
			1.假设某一个子数组从L…R范围已经达标，则内部所有子数组一定也都达标
			2.如果L...R不达标，则L往左扩，或者R往右扩出来的新的子数组一定也都不达标
		2.流程：
			1.依次求必须以i位置开头的达标子数组有多少个？所有位置累加
			2.准备2个队列，一个维护窗口内的最大值，一个维护窗口内的最小值；
			3.假设窗口从0开始，如果达标，窗口右边界R就继续扩展，扩到一旦不达标，停。因为l..r不达标，那么l往左扩或者r往右扩肯定也不达标
			收集此时0位置开头的答案，就是R-L(注意这里只收集0开头的答案，其他答案让当到了其他位置开头再收集)
			4.接口去求1位置的开头的达标子数组有多少个，注意：R是不回退的！l..r是达标的，那么内部所有子数组都是达标的。l=0,r扩到了10；接下来l=1，r不用从0开始扩，因为1-10一定是达标的，r只需要看能不能继续扩就行了
		3.时复：R是不回退的，每次看达不达标都是o(1)；求出所有位置，整体o（n)
 */
public class Code02_AllLessNumSubArray {

	// 暴力的对数器方法
	// O（N^3)
	public static int right(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		for (int L = 0; L < N; L++) {
			for (int R = L; R < N; R++) {
				int max = arr[L];
				int min = arr[L];
				for (int i = L + 1; i <= R; i++) {
					max = Math.max(max, arr[i]);
					min = Math.min(min, arr[i]);
				}
				if (max - min <= sum) {
					count++;
				}
			}
		}
		return count;
	}

	// O（N)
	public static int num(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		LinkedList<Integer> maxWindow = new LinkedList<>();
		LinkedList<Integer> minWindow = new LinkedList<>();
		int R = 0; // R是不回退的！l..r是达标的，那么内部所有子数组都是达标的。l=0,r扩到了10；接下来l=1，r不用从0开始扩，因为1-10一定是达标的，r只需要看能不能继续扩就行了
		for (int L = 0; L < N; L++) { // 依次求以每一个位置作为子数组的开头，达标子数组的数量
			// 窗口[L,R)
			while (R < N) { // L每到一个位置，R就尽可能去扩到初次不达标
				while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[R]) {
					maxWindow.pollLast();
				}
				maxWindow.addLast(R);
				while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
					minWindow.pollLast();
				}
				minWindow.addLast(R);
				if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
					break; // 初次不达标，停！因为l..r不达标，那么l往左扩或者r往右扩肯定也不达标
				} else {
					R++; // 达标就一直扩
				}
			}

			// R是初次不达标的位置，那么此时以L位置开头的达标子数组就是R-L
			count += R - L;

			// 马上L要++了
			if (maxWindow.peekFirst() == L) {
				maxWindow.pollFirst();
			}
			if (minWindow.peekFirst() == L) {
				minWindow.pollFirst();
			}
		}
		return count;
	}

	// for test
	public static int[] generateRandomArray(int maxLen, int maxValue) {
		int len = (int) (Math.random() * (maxLen + 1));
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 200;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxLen, maxValue);
			int sum = (int) (Math.random() * (maxValue + 1));
			int ans1 = right(arr, sum);
			int ans2 = num(arr, sum);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(sum);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");

	}

}
