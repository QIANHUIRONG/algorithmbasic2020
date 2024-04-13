package class13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
题意：给定一棵二叉树的头节点head，和另外两个节点a和b。返回a和b的最低公共祖先
题解：
什么是最低公共祖先：两个节点都往上跑，头结点碰上的最初汇聚点
 */
public class Code03_lowestAncestor {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	/*
	方法一：
		1、遍历二叉树，生成map记录每个节点的父节点
		2、x遍历找父节点，加入set,然后y找父，每到一个节点检查是不是在set里，第一次找到的就是最低公共祖先
		复杂度：O（N）
	 */
	public static Node lowestAncestor1(Node head, Node o1, Node o2) {
		if (head == null) {
			return null;
		}
		// 记录每个节点的父节点
		HashMap<Node, Node> parentMap = new HashMap<>();
		parentMap.put(head, null);
		fillParentMap(head, parentMap); // 遍历二叉树，生成所有节点的父节点

		// o1去找父节点，set存放经过的父节点
		HashSet<Node> o1Set = new HashSet<>();
		o1Set.add(o1);
		while (parentMap.get(o1) != null) {
			o1 = parentMap.get(o1);
			o1Set.add(o1);
		}

		// 轮到o2去找父节点，第一次在set出现过的节点就是最低公共祖先
		while (!o1Set.contains(o2)) {
			o2 = parentMap.get(o2);
		}
		return o2;
	}

	public static void fillParentMap(Node head, HashMap<Node, Node> parentMap) {
		if (head.left != null) {
			// 左孩子不为空，就收集到容器中
			parentMap.put(head.left, head);
			// 接着左孩子去收集
			fillParentMap(head.left, parentMap);
		}
		if (head.right != null) {
			parentMap.put(head.right, head);
			fillParentMap(head.right, parentMap);
		}
	}

	/*
	方法二：递归套路
	与X无关：
		X不是最低汇聚点：左树有答案 || 右树有答案 || x树上a、b不全
	与X有关：
		X就是最低汇聚点：
			左树发现a b中一个，右树发现另一个
			X本身是a,左树或右树发现b
			X本身是b,左树或右树发现a

	需要的信息：
		发现a否？发现b否？最低公共祖先是谁？
	 */
	public static Node lowestAncestor2(Node head, Node a, Node b) {
		return process(head, a, b).ans;
	}

	public static class Info {
		public boolean findA;
		public boolean findB;
		public Node ans;

		public Info(boolean fA, boolean fB, Node an) {
			findA = fA;
			findB = fB;
			ans = an;
		}
	}

	public static Info process(Node x, Node a, Node b) {
		if (x == null) {
			return new Info(false, false, null);
		}
		Info leftInfo = process(x.left, a, b);
		Info rightInfo = process(x.right, a, b);
		boolean findA = (x == a) || leftInfo.findA || rightInfo.findA;
		boolean findB = (x == b) || leftInfo.findB || rightInfo.findB;
		Node ans = null;
		if (leftInfo.ans != null) {
			ans = leftInfo.ans;
		} else if (rightInfo.ans != null) {
			ans = rightInfo.ans;
		} else {
			if (findA && findB) {
				ans = x;
			}
		}
		return new Info(findA, findB, ans);
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

	// for test
	public static Node pickRandomOne(Node head) {
		if (head == null) {
			return null;
		}
		ArrayList<Node> arr = new ArrayList<>();
		fillPrelist(head, arr);
		int randomIndex = (int) (Math.random() * arr.size());
		return arr.get(randomIndex);
	}

	// for test
	public static void fillPrelist(Node head, ArrayList<Node> arr) {
		if (head == null) {
			return;
		}
		arr.add(head);
		fillPrelist(head.left, arr);
		fillPrelist(head.right, arr);
	}

	public static void main(String[] args) {
		int maxLevel = 4;
		int maxValue = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			Node o1 = pickRandomOne(head);
			Node o2 = pickRandomOne(head);
			if (lowestAncestor1(head, o1, o2) != lowestAncestor2(head, o1, o2)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
