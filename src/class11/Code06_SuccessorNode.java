package class11;

/*
题意：
给你某个节点，返回该节点的后继节点
 */

/*
时间：
流程：1：45
coding:2:03
 */

/**
 * [思维导图]
 *
 * 1.
 * 一个节点的后继节点，就是中序遍历当前节点的下一个节点！
 * 所以经典方法：中序遍历一遍就行。O（N）
 *
 * 2.
 * 现在二叉树节点多了一条指向自己父亲节点的指针，要求O（K），K是到后继节点的距离
 * 1、X节点有右树，中序遍历的下一个节点就是右子树上的最左孩子；
 * 2、X节点没有右树：
 * 		如果X是它父节点的右孩子就一直往上找，直到X节点是他父节点的左孩子，则父亲节点就是后继
 * 		如果没有找到这种节点，那么这个节点是整棵树的最右节点，没有后继节点
 *
 */

public class Code06_SuccessorNode {

	public static class Node {
		public int value;
		public Node left;
		public Node right;
		// 有一个指向父节点的指针
		public Node parent;

		public Node(int data) {
			this.value = data;
		}
	}

	// 主函数
	public static Node getSuccessorNode(Node node) {
		if (node == null) {
			return node;
		}
		if (node.right != null) {
			// 有右子树，找到右树上的最左孩子
			Node cur = node.right;
			while (cur.left != null) {
				cur = cur.left;
			}
			return cur;
		} else { // 无右子树
			// 通过parent指针往上找，直到我是我父亲的左孩子或者我的父亲为null
			// 如果我的父亲不为空，并且我是我父亲的右孩子，就继续往上找
			while (node.parent != null && node == node.parent.right) {
				node = node.parent;
			}

			// 出while我的父亲为空，说明node是整棵树的最右节点，它的后继节点就是null
			if (node.parent == null) {
				return null;
			} else {
				// 或者找到了我是我父亲的左，返回我父亲
				return node.parent;
			}
		}
	}

//	public static NodeT getLeftMost(NodeT node) {
//		if (node == null) {
//			return node;
//		}
//		while (node.left != null) {
//			node = node.left;
//		}
//		return node;
//	}

	public static void main(String[] args) {
		Node head = new Node(6);
		head.parent = null;
		head.left = new Node(3);
		head.left.parent = head;
		head.left.left = new Node(1);
		head.left.left.parent = head.left;
		head.left.left.right = new Node(2);
		head.left.left.right.parent = head.left.left;
		head.left.right = new Node(4);
		head.left.right.parent = head.left;
		head.left.right.right = new Node(5);
		head.left.right.right.parent = head.left.right;
		head.right = new Node(9);
		head.right.parent = head;
		head.right.left = new Node(8);
		head.right.left.parent = head.right;
		head.right.left.left = new Node(7);
		head.right.left.left.parent = head.right.left;
		head.right.right = new Node(10);
		head.right.right.parent = head.right;

		Node test = head.left.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.left.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.right.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.left.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.right; // 10's next is null
		System.out.println(test.value + " next: " + getSuccessorNode(test));
	}

}
