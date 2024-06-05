package class13;

import java.util.ArrayList;
/*
题意：二叉树中最大的二叉搜索子树的头节点
 */

/*
思维导图：和day12 Code05_MaxSubBSTSize一样，只不过这道题要求的是取得最大搜索二叉子树的头节点
    以x为头的整棵树，怎么求出它的最大搜索二叉子树的头节点？
	1、与X无关：Math.max(左树的最大BST,右树的最大BST)的头节点
	2、与X有关：那么X整棵树必须是BST：左树是BST && 右树是BST && 左最大<=x<=右最小
	需要的信息：
		0.最大BST 的头节点
	    1.最大BST size；（要这个，是因为，与x无关时，我得比较，左右子树哪个最大搜索二叉子树更大，才能决定我用哪个的最大BST 的头节点）
	    2.是否是BST；
	    3.max；
	    4.min；
	    5.树的节点树size(因为如果与X有关，x要整合出自己的最大BST size，需要子树的size。左子树size+右子树size+1)
	发现如果size == 最大BST size，那么就是BST，2条件可以省略。
 */
public class Code02_MaxSubBSTHead {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	// 方法一
	public static int getBSTSize(Node head) {
		if (head == null) {
			return 0;
		}
		ArrayList<Node> arr = new ArrayList<>();
		in(head, arr);
		for (int i = 1; i < arr.size(); i++) {
			if (arr.get(i).value <= arr.get(i - 1).value) {
				return 0;
			}
		}
		return arr.size();
	}

	public static void in(Node head, ArrayList<Node> arr) {
		if (head == null) {
			return;
		}
		in(head.left, arr);
		arr.add(head);
		in(head.right, arr);
	}

	public static Node maxSubBSTHead1(Node head) {
		if (head == null) {
			return null;
		}
		if (getBSTSize(head) != 0) {
			return head;
		}
		Node leftAns = maxSubBSTHead1(head.left);
		Node rightAns = maxSubBSTHead1(head.right);
		return getBSTSize(leftAns) >= getBSTSize(rightAns) ? leftAns : rightAns;
	}


	/*
	方法二、二叉树递归套路
	 */
	public static Node maxSubBSTHead2(Node head) {
		if (head == null) {
			return null;
		}
		return process(head).maxSubBSTHead;
	}

	// 每一棵子树
	public static class Info {
		public Node maxSubBSTHead;
		public int maxSubBSTSize;
		public int allSize;
		public int min;
		public int max;

		public Info(Node h, int size, int n, int mi, int ma) {
			maxSubBSTHead = h;
			maxSubBSTSize = size;
			allSize = n;
			min = mi;
			max = ma;
		}
	}

	public static Info process(Node X) {
		if (X == null) {
			return null;
		}
		Info leftInfo = process(X.left);
		Info rightInfo = process(X.right);
		int min = X.value;
		int max = X.value;
		Node maxSubBSTHead = null;
		int maxSubBSTSize = 0;
		int allSize = 1;
		if (leftInfo != null) {
			min = Math.min(min, leftInfo.min);
			max = Math.max(max, leftInfo.max);
			maxSubBSTHead = leftInfo.maxSubBSTHead;
			maxSubBSTSize = leftInfo.maxSubBSTSize;
			allSize += leftInfo.allSize;
		}
		if (rightInfo != null) {
			min = Math.min(min, rightInfo.min);
			max = Math.max(max, rightInfo.max);
			allSize += rightInfo.allSize;
			if (rightInfo.maxSubBSTSize > maxSubBSTSize) {
				maxSubBSTHead = rightInfo.maxSubBSTHead;
				maxSubBSTSize = rightInfo.maxSubBSTSize;
			}
		}
		// 2种写法。左子树是BST，就是左子树的最大搜索子树的节点数==左子树总节点数 ， 或者 左子树的最大搜索子树的头节点 == X.left
		// boolean leftBST = leftInfo == null ? true : (leftInfo.maxSubBSTSize == leftInfo.allSize);
		boolean leftBST = leftInfo == null ? true : leftInfo.maxSubBSTHead == X.left; // 左子树是不是搜索二叉树
		boolean rightBST = rightInfo == null ? true : rightInfo.maxSubBSTHead == X.right; // 左子树是不是搜索二叉树
		boolean leftLess = leftInfo == null ? true : leftInfo.max < X.value; // 判断左最大<x
		boolean rightMax = rightInfo == null ? true : rightInfo.min > X.value; // 判断x<右最小
		if (leftBST && rightBST && leftLess && rightMax) {
			maxSubBSTHead = X;
			maxSubBSTSize = allSize;
		}
		return new Info(maxSubBSTHead, maxSubBSTSize, allSize, min, max);
	}

	// for test
	public static Node generateRandomBST(int maxLevel, int maxValue) {
		return generate(1, maxLevel, maxValue);
	}

	// for test
	public static Node generate(int level, int maxLevel, int maxValue) {
		if (level > maxLevel || Math.random() < 0.5) {
			return null;
		}
		Node head = new Node((int) (Math.random() * maxValue));
		head.left = generate(level + 1, maxLevel, maxValue);
		head.right = generate(level + 1, maxLevel, maxValue);
		return head;
	}

	public static void main(String[] args) {
		int maxLevel = 4;
		int maxValue = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			if (maxSubBSTHead1(head) != maxSubBSTHead2(head)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
