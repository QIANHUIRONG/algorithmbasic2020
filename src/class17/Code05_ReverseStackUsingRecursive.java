package class17;

import java.util.Stack;

/*
题意：
只使用递归逆序你一个栈
 */
/*
时间：1小时49分
 */
/*
题解：2个递归函数
1.f(Stack<Integer> stack) {} 栈底元素移除掉；上面的元素盖下来；返回移除掉的元素
basecase：先从栈中弹出一个元素，如果弹出之后，栈为空，就说明是最后一个元素了，直接返回
普遍过程：拿到栈底元素，再把我自己的元素重新压回栈，返回栈底元素

2.reverse(Stack<Integer> stack) {} 逆序一个栈
basecase：如果栈为空了，就不用逆序了
普遍过程：栈底元素拿出来，反转剩下的元素，拿出来的栈底元素入栈
 */
public class Code05_ReverseStackUsingRecursive {

	public static void reverse(Stack<Integer> stack) {
		// basecase：如果栈为空了，就不用逆序了
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
			// basecase：如果弹出result之后，栈为空，那么这个result就是要返回的移除掉的元素，直接return
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
