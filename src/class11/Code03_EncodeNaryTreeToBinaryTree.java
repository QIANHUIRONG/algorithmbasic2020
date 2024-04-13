package class11;

import java.util.ArrayList;
import java.util.List;

/*
本题测试链接：https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
将N叉树编码为二叉树：58
多叉树X节点的序列化方式：多叉树X节点序列化为二叉树X节点；多叉树X节点的孩子节点，放在二叉树X节点左树的右边界上。
二叉树的右树我压根不用
 */
public class Code03_EncodeNaryTreeToBinaryTree {

	// 提交时不要提交这个类
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
	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	// 只提交这个类即可
	class Codec {
		// 多叉树序列化为二叉树
		public TreeNode encode(Node root) {
			if (root == null) {
				return null;
			}
			TreeNode head = new TreeNode(root.val);
			head.left = en(root.children);
			return head;
		}

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
				// 这个循环是生成当前child的二叉树形式，当然需要继续递归child的子节点
				cur.left = en(child.children);
			}
			return head;
		}

		// 将二叉树反序列化为多叉树
		public Node decode(TreeNode root) {
			if (root == null) {
				return null;
			}
			return new Node(root.val, de(root.left));
		}

		// root是长兄，生成多叉树的孩子节点返回
		public List<Node> de(TreeNode root) {
			List<Node> children = new ArrayList<>();
			while (root != null) {
				Node cur = new Node(root.val, de(root.left));
				children.add(cur);
				// 序列化时，长兄放在左子树头节点，其他孩子放在左树右边界。
				// 要拿到所有孩子，就继续去右边界上搞
				root = root.right;
			}
			return children;
		}

	}

}
