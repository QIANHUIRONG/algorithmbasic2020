package class12;


/*
题意：是否是平衡二叉树
 */
/*
时间：
20
coding:26
 */
/**
 * [思维导图]
 * 二叉树递归套路：
 * 递归套路的本质：找左树要信息、找右树要信息；整合出我的信息。即左右头-->后序遍历！时间复杂度O（N），就是最优解
 *
 * 1.平衡二叉树的定义：在一个二叉树中，每一颗子树，左树的高度和右树的高度差不超过1。每颗子树都是平衡二叉树
 * 2.递归套路思考，以X为头的整棵树如果是满二叉树，需要左右子树满足什么条件？需要向左右子树要什么信息？
 *	需要向左右子树要的信息：是否是平衡二叉树；高度
 *
 */
public class Code02_IsBalanced {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	/**
	 * 方法一：常规方法：略
	 */
	public static boolean isBalanced1(Node head) {
		boolean[] ans = new boolean[1];
		ans[0] = true;
		process1(head, ans);
		return ans[0];
	}

	public static int process1(Node head, boolean[] ans) {
		if (!ans[0] || head == null) {
			return -1;
		}
		int leftHeight = process1(head.left, ans);
		int rightHeight = process1(head.right, ans);
		if (Math.abs(leftHeight - rightHeight) > 1) {
			ans[0] = false;
		}
		return Math.max(leftHeight, rightHeight) + 1;
	}

	/**
	 * 方法二、二叉树递归套路
	 */
	public static boolean isBalanced2(Node head) {
		return process(head).isBalanced;
	}
	
	public static class Info{
		public boolean isBalanced;
		public int height;
		
		public Info(boolean i, int h) {
			isBalanced = i;
			height = h;
		}
	}

	// 返回以X为头的二叉树是不是平衡二叉树
	public static Info process(Node x) {
		if(x == null) {
			// 空节点认为是平衡二叉树，高度0
			return new Info(true, 0);
		}
		Info leftInfo = process(x.left);
		Info rightInfo = process(x.right);
		int height = Math.max(leftInfo.height, rightInfo.height)  + 1;
		boolean isBalanced = true;
		if(!leftInfo.isBalanced) {
			isBalanced = false;
		}
		if(!rightInfo.isBalanced) {
			isBalanced = false;
		}
		if(Math.abs(leftInfo.height - rightInfo.height) > 1) {
			isBalanced = false;
		}
		// 返回我当前这个树的2个信息
		return new Info(isBalanced, height);
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
		int maxLevel = 5;
		int maxValue = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			if (isBalanced1(head) != isBalanced2(head)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
