package class03;

import java.util.Stack;

/*
 [题意]
实现一个特殊的栈，在基本功能的基础上，再实现返回栈中最小元素的功能

1）pop、push、getMin操作的时间复杂度都是 O(1)。

2）设计的栈类型可以使用现成的栈结构。
*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]
1.getmin要求时复O(1), 那么就不能用遍历
2.
准备两个浅，数据栈眼最小栈：dataStack,minStack:
当前数比最小栈顶小，当前数放入最小浅
当前数不如最小栈栈顶小，重复压入最小栈顶
同步压入，同步弹出，最小栈栈顶就是此时最小的顶
 */
public class Code05_GetMinStack {


	public static class MyStack2 {
		private Stack<Integer> stackData;
		private Stack<Integer> stackMin;

		public MyStack2() {
			stackData = new Stack<Integer>();
			stackMin = new Stack<Integer>();
		}

		public void push(int newNum) {
			if (stackMin.isEmpty() || newNum < getmin()) {
				stackMin.push(newNum);
			} else {
				stackMin.push(stackMin.peek());
			}
			stackData.push(newNum);
		}

		public int pop() {
			if (stackData.isEmpty()) {
				throw new RuntimeException("Your stack is empty.");
			}
			stackMin.pop();
			return stackData.pop();
		}

		public int getmin() {
			if (stackMin.isEmpty()) {
				throw new RuntimeException("Your stack is empty.");
			}
			return stackMin.peek();
		}
	}



	public static void main(String[] args) {

		MyStack2 stack2 = new MyStack2();
		stack2.push(3);
		System.out.println(stack2.getmin());
		stack2.push(4);
		System.out.println(stack2.getmin());
		stack2.push(1);
		System.out.println(stack2.getmin());
		System.out.println(stack2.pop());
		System.out.println(stack2.getmin());
	}

}
