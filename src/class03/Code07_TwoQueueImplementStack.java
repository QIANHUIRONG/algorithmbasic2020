package class03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 [题意]
 只使用队列实现栈结构

*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]
1.准备2个队列，一个queue，一个help，
	1.入队时，直接入queue
	2.出队时，queue导出队列里只剩一个数的时候停止，然后取出这个元素返回，就实现了先进先出
2.导完之后，queue和help要互换角色
 */
public class Code07_TwoQueueImplementStack {

	public static class TwoQueueStack {
		public Queue<Integer> queue;
		public Queue<Integer> help;

		public TwoQueueStack() {
			queue = new LinkedList<>();
			help = new LinkedList<>();
		}

		public void push(int value) {
			queue.offer(value);
		}

		public Integer pop() {
			while (queue.size() > 1) { // 往help导出，直到剩1个要弹出的元素
				help.add(queue.poll());
			}
			int ans = queue.poll();
			Queue<Integer> t = queue;
			queue = help;
			help = t;
			return ans;
		}

		public Integer peek() {
			while (queue.size() > 1) { // 往help导出，直到剩1个要弹出的元素
				help.add(queue.poll());
			}
			int ans = queue.poll();
			help.add(ans);
			Queue<Integer> t = queue;
			queue = help;
			help = t;
			return ans;
		}

		public boolean isEmpty() {
			return queue.isEmpty();
		}

	}

	public static void main(String[] args) {
		System.out.println("test begin");
		TwoQueueStack myStack = new TwoQueueStack();
		Stack<Integer> test = new Stack<>();
		int testTime = 1000000;
		int max = 1000000;
		for (int i = 0; i < testTime; i++) {
			if (myStack.isEmpty()) {
				if (!test.isEmpty()) {
					System.out.println("Oops");
				}
				int num = (int) (Math.random() * max);
				myStack.push(num);
				test.push(num);
			} else {
				if (Math.random() < 0.25) {
					int num = (int) (Math.random() * max);
					myStack.push(num);
					test.push(num);
				} else if (Math.random() < 0.5) {
					if (!myStack.peek().equals(test.peek())) {
						System.out.println("Oops");
					}
				} else if (Math.random() < 0.75) {
					if (!myStack.pop().equals(test.pop())) {
						System.out.println("Oops");
					}
				} else {
					if (myStack.isEmpty() != test.isEmpty()) {
						System.out.println("Oops");
					}
				}
			}
		}

		System.out.println("test finish!");

	}

}
