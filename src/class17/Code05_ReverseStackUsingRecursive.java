package class17;

import java.util.Stack;

/*
时间：1小时49分
 */
public class Code05_ReverseStackUsingRecursive {

	public static void reverse(Stack<Integer> stack) {
		if (stack.isEmpty()) {
			return;
		}
		// 栈底元素拿出来
		int i = f(stack);
		// 反转剩下的元素
		reverse(stack);
		// 拿出来的栈底元素入栈
		stack.push(i);
	}

	// 栈底元素移除掉
	// 上面的元素盖下来
	// 返回移除掉的栈底元素
	public static int f(Stack<Integer> stack) {
		int result = stack.pop();
		if (stack.isEmpty()) {
			// basecash：如果弹出result之后，栈为空，那么这个result就是要返回的移除掉的元素，直接return
			return result;
		} else {
			// 普遍过程：拿到栈底元素，再把我自己的元素重新压回栈，返回栈底元素
			int last = f(stack);
			stack.push(result);
			return last;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> test = new Stack<Integer>();
		test.push(1);
		test.push(2);
		test.push(3);
		test.push(4);
		test.push(5);
		reverse(test);
		while (!test.isEmpty()) {
			System.out.println(test.pop());
		}

	}

}
