package class30;

// 本题测试链接 : https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
// 时间：1：23
// 方法一：二叉树的递归套路
// 方法二：Morris的版本


/*
题意【1：23】：
二叉树dp的做法【1：24】：
morris遍历改法【1：27】
	1.遍历cur的时候，正确更新level
	2.正确的发现叶节点
	因为我们会改指针，所以level的更新就很麻烦
	正确发现叶子节点【1：36】
code【1：49】

二叉树递归套路，morris总结【1：50】

面试的时候，思考的东西要念出来，絮絮叨叨的，

 */

/*
导图整理的笔记：
一、怎么知道当前节点在第几层

二、怎么知道当前节点是不是叶子节点？
	有左子树的，第二次到达自己的时候，反过来看是不是叶子节点
	最后需要单独检查整棵树的最右节点是不是叶子节点，因为这个节点

三、二叉树技巧总结
	1.二叉树的递归套路是一个解决二叉树最优解的一个一个大的技巧。
	2.Morrisi遍历，把空间也做到极致

四、如果判断是否能改morris
	X节点为头的信息：如果严格需要左树，右树收集信息，才能整合出自己的信息，不用想morris，因为递归序很强，任何节点都可以回到自己3次；而morris有些只能回到1次，有些2次,用二叉树递归讨论
	如果不严格要求左树，右树的完整信息，可能只需要左树的信息或者右树的信息，Morris遍历就是最优解
 */
public class Code02_MinDepth {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int x) {
			val = x;
		}
	}

	// 下面的方法是一般解
	public static int minDepth1(TreeNode head) {
		if (head == null) {
			return 0;
		}
		return p(head);
	}

	// 返回x为头的树，最小深度是多少
	public static int p(TreeNode x) {
		if (x.left == null && x.right == null) {
			return 1;
		}
		// 左右子树起码有一个不为空
		int leftH = Integer.MAX_VALUE;
		if (x.left != null) {
			leftH = p(x.left);
		}
		int rightH = Integer.MAX_VALUE;
		if (x.right != null) {
			rightH = p(x.right);
		}
		return 1 + Math.min(leftH, rightH);
	}

	/**
	 * 下面的方法是morris遍历的解
	 * @param head
	 * @return
	 */
	public static int minDepth2(TreeNode head) {
		if (head == null) {
			return 0;
		}
		TreeNode cur = head;
		TreeNode mostRight = null;
		int curLevel = 0;
		int minHeight = Integer.MAX_VALUE;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				int rightBoardSize = 1;
				while (mostRight.right != null && mostRight.right != cur) {
					rightBoardSize++;
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) { // 第一次到达
					curLevel++;
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次到达
					if (mostRight.left == null) {
						minHeight = Math.min(minHeight, curLevel);
					}
					curLevel -= rightBoardSize;
					mostRight.right = null;
				}
			} else { // 只有一次到达
				curLevel++;
			}
			cur = cur.right;
		}
		int finalRight = 1;
		cur = head;
		while (cur.right != null) {
			finalRight++;
			cur = cur.right;
		}
		if (cur.left == null && cur.right == null) {
			minHeight = Math.min(minHeight, finalRight);
		}
		return minHeight;
	}

}
