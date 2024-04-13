package class12;

// 判断一棵树是不是满二叉树：1：15

/**
 * 模板总结：1：18
 * 模板总结：
 * 首先解决刚才的所有问题，时间复杂度都是O（N），N为节点个数。看上去麻烦，其实全是最优解。
 * 为啥？　　递归序　　
 * 比如ｉｎｆｏ要３个信息，把左边的３个信息和右边的３个信息整合起来加工出我的３个信息往上给。
 * 所以整个过程其实是一种后序遍历
 * 在树上左动态规划，树形ｄｐ
 * 没有节点只会到３次，时间复杂度肯定是Ｏ（Ｎ）
 *
 * 总结：
 * １.思想上的提醒：都去想以ｘ为头的时候目标怎么实现，实现的手段就是我可以向左树要信息，再向右数要信息，再整合出我想要的目标。去想目标的可能性，常见的就是跟ｘ有关的时候，跟ｘ无关的时候各自会怎样
 * ２.写代码模板化
 * ３.一旦把可能性分析清楚，练习熟了以后，写这种题就很简单
 */
public class Code04_IsFull {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	// 第一种方法
	// 收集整棵树的高度h，和节点数n
	// 只有满二叉树满足 : 2 ^ h - 1 == n
	public static boolean isFull1(Node head) {
		if (head == null) {
			return true;
		}
		Info1 all = process1(head);
		return (1 << all.height) - 1 == all.nodes;
	}

	public static class Info1 {
		public int height;
		public int nodes;

		public Info1(int h, int n) {
			height = h;
			nodes = n;
		}
	}

	public static Info1 process1(Node head) {
		if (head == null) {
			return new Info1(0, 0);
		}
		Info1 leftInfo = process1(head.left);
		Info1 rightInfo = process1(head.right);
		int height = Math.max(leftInfo.height, rightInfo.height) + 1;
		int nodes = leftInfo.nodes + rightInfo.nodes + 1;
		return new Info1(height, nodes);
	}

	// 第二种方法
	// 收集子树是否是满二叉树
	// 收集子树的高度
	// 左树满 && 右树满 && 左右树高度一样 -> 整棵树是满的
	public static boolean isFull2(Node head) {
		if (head == null) {
			return true;
		}
		return process2(head).isFull;
	}

	public static class Info2 {
		public boolean isFull;
		public int height;

		public Info2(boolean f, int h) {
			isFull = f;
			height = h;
		}
	}

	public static Info2 process2(Node h) {
		if (h == null) {
			return new Info2(true, 0);
		}
		Info2 leftInfo = process2(h.left);
		Info2 rightInfo = process2(h.right);
		boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
		int height = Math.max(leftInfo.height, rightInfo.height) + 1;
		return new Info2(isFull, height);
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
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			if (isFull1(head) != isFull2(head)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}
