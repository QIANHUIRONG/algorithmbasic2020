package class17;

import java.util.HashSet;
import java.util.Stack;

/*
题意：汉诺塔问题
有三根杆（A、B、C），其中A杆上有若干大小不同且自上而下按大小排列的圆盘。
每次只能移动一个圆盘，并且任何时候都不能将较大的圆盘放在较小的圆盘上。
目标是将所有圆盘从A杆移动到C杆，且保持原有顺序。
打印这个过程
 */
/*
时间：37
 */
/*
思维导图
1. 1-N-1个圆盘从左边移到中间
2. 第N个圆盘从左边移动到右边
3. 1-N-1个圆盘从中间移动到右边

方法一：用6个递归去搞；leftToRight(int n)
方法二：用增加参数的形式，来使得递归表示出更多含义 func(int N, String from, String to, String other)
方法三：迭代版本
 */
/*
做递归要有黑盒思维：
输入什么，达到什么效果，限制条件，basecase规定好，
接下来就是黑盒怎么使用的问题
比如说你要做一个递归函数，你就规定好这个黑盒，这个函数它满足什么样的条件。条件包括显示的条件和隐藏的条件潜台词，
就当你这个F函数它的含义固定了，你就把它作黑盒来用。
对于这个黑盒来说，最重要的是它的含义。输入什么，达到什么效果，遵循什么样的限制条件，规定好，basecase想好规定好，
(basecase就是什么时候就不用再分解问题了，直接就能出来规定好)，接下来就想我怎么用这个黑盒
就得设计基于函数不要太失于细节。先把黑盒规定好，接下来就是这个黑盒怎么用问题。
 */

public class Code02_Hanoi {

	/*
	方法一：用6个递归去搞
	 */
	public static void hanoi1(int n) {
		leftToRight(n);
	}

	// 请把1~N层圆盘 从左 -> 右
	public static void leftToRight(int n) {
		if (n == 1) { // base case
			System.out.println("Move 1 from left to right");
			return;
		}
		leftToMid(n - 1);
		System.out.println("Move " + n + " from left to right");
		midToRight(n - 1);
	}

	// 请把1~N层圆盘 从左 -> 中
	public static void leftToMid(int n) {
		if (n == 1) {
			System.out.println("Move 1 from left to mid");
			return;
		}
		leftToRight(n - 1);
		System.out.println("Move " + n + " from left to mid");
		rightToMid(n - 1);
	}

	public static void rightToMid(int n) {
		if (n == 1) {
			System.out.println("Move 1 from right to mid");
			return;
		}
		rightToLeft(n - 1);
		System.out.println("Move " + n + " from right to mid");
		leftToMid(n - 1);
	}

	public static void midToRight(int n) {
		if (n == 1) {
			System.out.println("Move 1 from mid to right");
			return;
		}
		midToLeft(n - 1);
		System.out.println("Move " + n + " from mid to right");
		leftToRight(n - 1);
	}

	public static void midToLeft(int n) {
		if (n == 1) {
			System.out.println("Move 1 from mid to left");
			return;
		}
		midToRight(n - 1);
		System.out.println("Move " + n + " from mid to left");
		rightToLeft(n - 1);
	}

	public static void rightToLeft(int n) {
		if (n == 1) {
			System.out.println("Move 1 from right to left");
			return;
		}
		rightToMid(n - 1);
		System.out.println("Move " + n + " from right to left");
		midToLeft(n - 1);
	}

	/*
	方法二：用增加参数的形式，来使得递归表示出更多含义
	 */
	public static void hanoi2(int n) {
		if (n > 0) {
			func(n, "left", "right", "mid");
		}
	}

	public static void func(int N, String from, String to, String other) {
		if (N == 1) { // base
			System.out.println("Move 1 from " + from + " to " + to);
		} else {
			func(N - 1, from, other, to);
			System.out.println("Move " + N + " from " + from + " to " + to);
			func(N - 1, other, to, from);
		}
	}

	public static class Record {
		public int level;
		public String from;
		public String to;
		public String other;

		public Record(int l, String f, String t, String o) {
			level = l;
			from = f;
			to = t;
			other = o;
		}
	}

	// GPT也是这么写的
	// 之前的迭代版本，很多同学表示看不懂
	// 所以我换了一个更容易理解的版本
	// 看注释吧！好懂！
	// 你把汉诺塔问题想象成二叉树
	// 比如当前还剩i层，其实打印这个过程就是：
	// 1) 去打印第一部分 -> 左子树
	// 2) 打印当前的动作 -> 当前节点
	// 3) 去打印第二部分 -> 右子树
	// 那么你只需要记录每一个任务 : 有没有加入过左子树的任务
	// 就可以完成迭代对递归的替代了
	public static void hanoi3(int N) {
		if (N < 1) {
			return;
		}
		// 每一个记录进栈
		Stack<Record> stack = new Stack<>();
		// 记录每一个记录有没有加入过左子树的任务
		HashSet<Record> finishLeft = new HashSet<>();
		// 初始的任务，认为是种子
		stack.add(new Record(N, "left", "right", "mid"));
		while (!stack.isEmpty()) {
			// 弹出当前任务
			Record cur = stack.pop();
			if (cur.level == 1) {
				// 如果层数只剩1了
				// 直接打印
				System.out.println("Move 1 from " + cur.from + " to " + cur.to);
			} else {
				// 如果不只1层
				if (!finishLeft.contains(cur)) {
					// 如果当前任务没有加入过左子树的任务
					// 现在就要加入了！
					// 把当前的任务重新压回去，因为还不到打印的时候
					// 再加入左子树任务！
					finishLeft.add(cur);
					stack.push(cur);
					stack.push(new Record(cur.level - 1, cur.from, cur.other, cur.to));
				} else {
					// 如果当前任务加入过左子树的任务
					// 说明此时已经是第二次弹出了！
					// 说明左子树的所有打印任务都完成了
					// 当前可以打印了！
					// 然后加入右子树的任务
					// 当前的任务可以永远的丢弃了！
					// 因为完成了左子树、打印了自己、加入了右子树
					// 再也不用回到这个任务了
					System.out.println("Move " + cur.level + " from " + cur.from + " to " + cur.to);
					stack.push(new Record(cur.level - 1, cur.other, cur.to, cur.from));
				}
			}
		}
	}











	public static void main(String[] args) {
		int n = 3;
		hanoi1(n);
		System.out.println("============");
		hanoi2(n);
		System.out.println("============");
		hanoi3(n);

	}

}
