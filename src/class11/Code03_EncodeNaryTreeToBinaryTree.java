package class11;

import java.util.ArrayList;
import java.util.List;

/*
题意：
本题测试链接：https://leetcode.cn/problems/encode-n-ary-tree-to-binary-tree
将N叉树编码为二叉树
设计一个算法，可以将 N 叉树编码为二叉树，并能将该二叉树解码为原 N 叉树。一个 N 叉树是指每个节点都有不超过 N 个孩子节点的有根树。
类似地，一个二叉树是指每个节点都有不超过 2 个孩子节点的有根树。你的编码 / 解码的算法的实现没有限制，你只需要保证一个 N 叉树可以编码为二叉树且该二叉树可以解码回原始 N 叉树即可。
 */

/*
时间：
题解：1：03
code：1：08
 */

/*
[思维导图]

多叉树X节点的序列化方式：多叉树X节点序列化为二叉树X节点；
多叉树X节点的孩子节点，放在二叉树X节点左树的右边界上。二叉树的右树我压根不用
比如多叉树：x的孩子abc, a的孩子d
		x
	a	b	c
d
二叉树表示：
		x
	a
d		b
			c
先建立x节点，然后把abc挂在x左树的右边界上；
然后a也会把他的孩子d挂在a的左树的右边界上；
 */
public class Code03_EncodeNaryTreeToBinaryTree {



	// 只提交这个类即可
	class Codec {
		// 多叉树序列化为二叉树
		public TreeNode encode(Node root) {
			if (root == null) {
				return null;
			}
			TreeNode head = new TreeNode(root.val); // 多叉树的头节点，肯定是我二叉树的头节点
			head.left = en(root.children); // 你所有孩子往我左树右边界上挂
			return head;
		}

		// 生成二叉树，挂孩子节点children，返回头节点；
		// 都往右边界挂
		private TreeNode en(List<Node> children) {
			if (children == null || children.size() == 0) {
				return null;
			}
			TreeNode head = null;
			TreeNode cur = null;
			// 遍历孩子节点
			for (Node child : children) {
				TreeNode tNode = new TreeNode(child.val);
				if (head == null) {
					// 第一个孩子，放在左树头节点
					head = tNode;
				} else {
					// 后面的孩子，放在左树右边界上
					cur.right = tNode;
				}
				cur = tNode;
				// 深度优先遍历。这个循环是生成当前child的二叉树形式，当然需要继续递归child的子节点。
				cur.left = en(child.children);
			}
			return head;
		}

		// 将二叉树反序列化为多叉树
		public Node decode(TreeNode root) {
			if (root == null) {
				return null;
			}
			return new Node(root.val, de(root.left)); // 我明确的知道，我的左树就是你的孩子
		}

		// root是长兄，生成多叉树的孩子节点返回
		public List<Node> de(TreeNode root) {
			List<Node> children = new ArrayList<>();
			while (root != null) {
				// 序列化是深度优先，反序列化也深度优先，去我左树拿孩子
				Node cur = new Node(root.val, de(root.left));
				children.add(cur);
				// 序列化时，长兄放在左子树头节点，其他孩子放在左树右边界。
				// 要拿到所有孩子，就继续去右边界上搞
				root = root.right;
			}
			return children;
		}

	}

	// 提交时不要提交这个类
	// 多叉树的节点对象
	public static class Node {
		public int val;
		public List<Node> children;

		public Node() {
		}

		public Node(int _val) {
			val = _val;
		}

		public Node(int _val, List<Node> _children) {
			val = _val;
			children = _children;
		}
	};

	// 提交时不要提交这个类
	// 二叉树的节点对象
	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

}
