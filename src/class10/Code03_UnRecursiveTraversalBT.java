package class10;

import java.util.Stack;

public class Code03_UnRecursiveTraversalBT {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	// 先序
	public static void pre(Node head) {
		System.out.print("pre-order: ");
		if (head != null) {
			Stack<Node> stack = new Stack<Node>();
			stack.push(head);
			while (!stack.isEmpty()) {
				head = stack.pop();
				System.out.print(head.value + " ");
				if (head.right != null) {
					stack.push(head.right);
				}
				if (head.left != null) {
					stack.push(head.left);
				}
			}
		}
		System.out.println();
	}

	// 中序 1：48

	/**
	 * 原理：整棵树可以被子树的左边界分解
	 * 既然遇到cur，整个左边界入栈，那么弹出打印的时候一定是先左再当前节点；弹出当前节点时，cur会来到右树进行这个过程。所以完成了左头右。
	 * @param cur
	 */
	public static void in(Node cur) {
		System.out.print("in-order: ");
		if (cur != null) {
			Stack<Node> stack = new Stack<Node>();
			// 栈不为空或者当前节点不为空，就继续
			while (!stack.isEmpty() || cur != null) {
				if (cur != null) {
					// cur不为空，整条左边界入栈
					stack.push(cur);
					cur = cur.left;
				} else {
					// cur为空弹出节点并打印
					cur = stack.pop();
					System.out.print(cur.value + " ");
					// cur来到弹出节点右边位置
					cur = cur.right;
				}
			}
		}
		System.out.println();
	}

	// 后序
	public static void pos1(Node head) {
		System.out.print("pos-order: ");
		if (head != null) {
			Stack<Node> s1 = new Stack<Node>();
			Stack<Node> s2 = new Stack<Node>();
			s1.push(head);
			while (!s1.isEmpty()) {
				head = s1.pop(); // 头 右 左
				s2.push(head);
				if (head.left != null) {
					s1.push(head.left);
				}
				if (head.right != null) {
					s1.push(head.right);
				}
			}
			// 左 右 头
			while (!s2.isEmpty()) {
				System.out.print(s2.pop().value + " ");
			}
		}
		System.out.println();
	}

	// 后序：只使用1个栈
	public static void pos2(Node h) {
		System.out.print("pos-order: ");
		if (h != null) {
			Stack<Node> stack = new Stack<Node>();
			stack.push(h);
			Node c = null;
			while (!stack.isEmpty()) {
				c = stack.peek();
				if (c.left != null && h != c.left && h != c.right) {
					stack.push(c.left);
				} else if (c.right != null && h != c.right) {
					stack.push(c.right);
				} else {
					System.out.print(stack.pop().value + " ");
					h = c;
				}
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Node head = new Node(1);
		head.left = new Node(2);
		head.right = new Node(3);
		head.left.left = new Node(4);
		head.left.right = new Node(5);
		head.right.left = new Node(6);
		head.right.right = new Node(7);

		pre(head);
		System.out.println("========");
		in(head);
		System.out.println("========");
		pos1(head);
		System.out.println("========");
		pos2(head);
		System.out.println("========");
	}

}
