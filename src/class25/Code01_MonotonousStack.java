package class25;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/*
题意：单调栈
一种特别设计的栈结构，为了解决如下的问题：
给定一个可能含有重复值的数组arr，i位置的数一定存在如下两个信息
1）arr[i]的左侧离i最近并且小于(或者大于)arr[i]的数在哪？
2）arr[i]的右侧离i最近并且小于(或者大于)arr[i]的数在哪？
 */
/*
时间：5
②暴力法：O(n^2)
③单调栈：O（N)
 */
/*
	单调栈
	1.定义：单调栈是一种特别的栈结构，为了解决两个问题：
		① arr[i]左边离i最近的且小于arr[i]的数在哪一个位置？（或者大于arr[i]的数在哪一个位置）
		② arr[i]右边离i最近的且小于arr[i]的数在哪一个位置？
	2.暴力解：如果每来到一个位置，都去左右两边遍历一下直到找到离i最近的且小于arr[i]的数，那时间复杂度就是O(N^2);
	利用单调栈可以做到O(N)
	3.流程：（为了简单起见，先假设数组没有重复值，然后实现的是arr[i]左边[右边]离i最近的且小于arr[i]的数在哪一个位置？）
		arr[3 4 2 6 1 7 0]
			0 1 2 3 4 5 6
		准备一个栈，存放数组的下标位置，这个栈栈底到栈顶的下标元素对应回原数组一定是从小到大的
		①遍历到0位置的3，栈中的元素是空，栈顶下标元素对应回原数组是不是比3大？->不是，直接入栈,栈变成:[0->3]
		②遍历到1位置的4，栈中的元素是[0->3],栈顶下标元素0对应回原数组是不是比4大？->不是，直接入栈，栈变成[1->4,0->3]
		③遍历到2位置的2，栈中的元素是[1->4,0->3],栈顶是不是比2大？->是->栈顶弹出1—>4，收集答案:
			I:1位置的4左边离它最近比它小的数是它下面压着的数，0->3;
			II:1位置的4右边离它最近比它小的数是让它弹出的那个数，2->2;
		接着栈中的元素是[0->3], 栈顶是不是比2大？->是->栈顶弹出0->3，收集答案:
			I:0位置的3左边离它最近比它小的数是它下面压着的数，没有用-1表示；
			II:0位置的3右边离它最近比它小的数是让它弹出的那个数，2->2;
		此时2位置的2就可以入栈了，栈变成[2->2]
		④...
		⑤最后arr[]已经遍历完了，再把栈中的元素单独弹出，设置答案。

	4.证明：证明收集的arr[i]左边离i最近的且小于arr[i]的数是正确的
		某一步，栈中是[b,a], 此时c的来临要让b收集答案了；
		I:左边离它最近比它小的：a
		II:右边离它最近比它小的：c
		数组大概是这样arr[a...b...c]
		先证明II: c要释放b，说明c<b;在c释放b之前，b没有被释放，说明b...c中间的数都>b;
		证明I: b没有释放a，说明a<b;a...b中间的数是大于b的：
			如果a...b中间的数不是大于b，而是a<?<b的，那a b在栈中不会挨着；
			如果a...b中间的数不是大于b，而是<a的，那么a早就被释放了；
			所以a...b中间的数一定>b,所以b左边离它最近比它小的就是a；

	5.时间复杂度：每个位置进栈1次，出栈1次，时间复杂度O(N);

	6.有重复值：数值中有重复值怎么办？
		[不想写了。。]
		反正就是栈中用一个链表表示所有的位置，结算的时候一起结算；
		算左边离你最近比你小的就看压着的链表中的最后一个位置；

 */

/*
1、求一个数组中，每个位置，左边离我最近的比我小的在哪；右边离我最近的比我小的在哪；
2、暴力解：来到每个位置，左边遍历求一下，右边遍历求一下，求一个位置就是O(N), 求所有位置就是O(N^2)
3、单调栈：可以做到求一个位置O(1),求所有位置就是O（N）。每个数字最多入栈1次，出栈1次。
4、准备一个栈，栈底到栈顶严格由小到大；
5、遍历到数据某个值，比栈顶元素小，弹出栈顶元素，此时就可以结算栈顶元素，它右边离它最近比它小的就是当前让我弹出的数；它左边离它最近比它小是就是它压着的数；
6、遍历完了数组，单独弹出栈，它右边离它最近比它小的就是-1，它左边离它最近比它小是就是它压着的数；
7、有重复值，栈中放一个List<Integer>。结算时结算整个链表，右边离它最近比它小的就是让我弹出的数；左边离它最近比它小是就是它压着的链表的最后一个值；
 */
public class Code01_MonotonousStack {

	// 无重复值版本
	// arr = [ 3, 1, 2, 3]
	//         0  1  2  3
	// 返回：
	//  [
	//     0 : [-1,  1]
	//     1 : [-1, -1]
	//     2 : [ 1, -1]
	//     3 : [ 2, -1]
	//  ]
	public static int[][] getNearLessNoRepeat(int[] arr) {
		int[][] res = new int[arr.length][2];
		// 只存位置！
		Stack<Integer> stack = new Stack<>();
		// 1.遍历结算
		for (int i = 0; i < arr.length; i++) { // 当遍历到i位置的数，arr[i]
			while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
				int j = stack.pop();
				int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
				res[j][0] = leftLessIndex;
				res[j][1] = i;
			}
			stack.push(i);
		}
		// 2.单独结算
		while (!stack.isEmpty()) {
			int j = stack.pop();
			int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
			res[j][0] = leftLessIndex;
			res[j][1] = -1;
		}
		return res;
	}

	// 有重复值版本
	public static int[][] getNearLess(int[] arr) {
		int[][] res = new int[arr.length][2];
		Stack<List<Integer>> stack = new Stack<>(); // 栈中存放的是一个list！
		// 1.遍历结算
		for (int i = 0; i < arr.length; i++) { // i -> arr[i] 进栈
			while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
				List<Integer> popIs = stack.pop(); // 结算整个链表
				int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1); // 左边离它最近比它小是就是它压着的链表的最后一个值；
				for (Integer popi : popIs) { // 结算整个链表
					res[popi][0] = leftLessIndex;
					res[popi][1] = i;
				}
			}
			if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) { // 如果栈中已经有我这个元素了，直接添加到链表的结尾
				stack.peek().add(Integer.valueOf(i));
			} else { // 栈中没有我这个元素，需要初始化链表
				ArrayList<Integer> list = new ArrayList<>();
				list.add(i);
				stack.push(list);
			}
		}
		// 2.单独结算
		while (!stack.isEmpty()) {
			List<Integer> popIs = stack.pop();
			int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
			for (Integer popi : popIs) {
				res[popi][0] = leftLessIndex;
				res[popi][1] = -1;
			}
		}
		return res;
	}

	// for test
	public static int[] getRandomArrayNoRepeat(int size) {
		int[] arr = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		for (int i = 0; i < arr.length; i++) {
			int swapIndex = (int) (Math.random() * arr.length);
			int tmp = arr[swapIndex];
			arr[swapIndex] = arr[i];
			arr[i] = tmp;
		}
		return arr;
	}

	// for test
	public static int[] getRandomArray(int size, int max) {
		int[] arr = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
		}
		return arr;
	}

	// for test
	public static int[][] rightWay(int[] arr) {
		int[][] res = new int[arr.length][2];
		for (int i = 0; i < arr.length; i++) {
			int leftLessIndex = -1;
			int rightLessIndex = -1;
			int cur = i - 1;
			while (cur >= 0) {
				if (arr[cur] < arr[i]) {
					leftLessIndex = cur;
					break;
				}
				cur--;
			}
			cur = i + 1;
			while (cur < arr.length) {
				if (arr[cur] < arr[i]) {
					rightLessIndex = cur;
					break;
				}
				cur++;
			}
			res[i][0] = leftLessIndex;
			res[i][1] = rightLessIndex;
		}
		return res;
	}

	// for test
	public static boolean isEqual(int[][] res1, int[][] res2) {
		if (res1.length != res2.length) {
			return false;
		}
		for (int i = 0; i < res1.length; i++) {
			if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
				return false;
			}
		}

		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int size = 10;
		int max = 20;
		int testTimes = 2000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			int[] arr1 = getRandomArrayNoRepeat(size);
			int[] arr2 = getRandomArray(size, max);
			if (!isEqual(getNearLessNoRepeat(arr1), rightWay(arr1))) {
				System.out.println("Oops!");
				printArray(arr1);
				break;
			}
			if (!isEqual(getNearLess(arr2), rightWay(arr2))) {
				System.out.println("Oops!");
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}
}
