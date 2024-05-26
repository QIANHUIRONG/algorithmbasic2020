package class03;

import java.util.Stack;
/*
 [题意]
 只用栈实现队列
*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]
1.两个栈，push栈，pop栈
当用户想要数据的时候你把push里的数据导到pop栈里去，从pop钱里给用户返回
2.两个原则：
	1.push栈导数据到pop栈一定要一次性倒完
	2.pop栈里有数据，不能从push栈导数据（只有pop栈空了才可以导数）

3.在你要取数之前，来一次导数据的方法，再取数据。只要遵循上述2个原则，肯定对
 */
public class Code06_TwoStacksImplementQueue {

	public static class TwoStacksQueue {
		public Stack<Integer> stackPush;
		public Stack<Integer> stackPop;

		public TwoStacksQueue() {
			stackPush = new Stack<Integer>();
			stackPop = new Stack<Integer>();
		}

		// push栈向pop栈倒入数据
		private void pushToPop() {
			if (stackPop.isEmpty()) { // 为空才能导
				while (!stackPush.isEmpty()) { // 导的话所有数据都导
					stackPop.push(stackPush.pop());
				}
			}
		}

		public void offer(int pushInt) {
			stackPush.push(pushInt);
		}

		public int poll() {
			if (stackPop.isEmpty() && stackPush.isEmpty()) {
				throw new RuntimeException("Queue is isEmpty!");
			}
			pushToPop();
			return stackPop.pop();
		}

		public int peek() {
			if (stackPop.isEmpty() && stackPush.isEmpty()) {
				throw new RuntimeException("Queue is isEmpty!");
			}
			pushToPop();
			return stackPop.peek();
		}
	}

	public static void main(String[] args) {
		TwoStacksQueue test = new TwoStacksQueue();
		test.offer(1);
		test.offer(2);
		test.offer(3);
		System.out.println(test.peek());
		System.out.println(test.poll());
		System.out.println(test.peek());
		System.out.println(test.poll());
		System.out.println(test.peek());
		System.out.println(test.poll());
	}

}
