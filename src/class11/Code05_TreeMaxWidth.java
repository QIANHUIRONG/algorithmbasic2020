package class11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


/*
题意：
求二叉树最宽的层有多少个节点
 */

/*
时间
1：26
 */

/*
思维导图：
	按层遍历时，直接遍历一整层。就可以拿到每一个层的宽度
 */
public class Code05_TreeMaxWidth {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	// 使用容器的做法
	public static int maxWidthUseMap(Node head) {
		if (head == null) {
			return 0;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		// key 在 哪一层，value
		HashMap<Node, Integer> levelMap = new HashMap<>();
		levelMap.put(head, 1);
		int curLevel = 1; // 当前你正在统计哪一层的宽度
		int curLevelNodes = 0; // 当前层curLevel层，宽度目前是多少
		int max = 0;
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			int curNodeLevel = levelMap.get(cur);
			if (cur.left != null) {
				levelMap.put(cur.left, curNodeLevel + 1);
				queue.add(cur.left);
			}
			if (cur.right != null) {
				levelMap.put(cur.right, curNodeLevel + 1);
				queue.add(cur.right);
			}
			if (curNodeLevel == curLevel) {
				curLevelNodes++;
			} else {
				max = Math.max(max, curLevelNodes);
				curLevel++;
				curLevelNodes = 1;
			}
		}
		max = Math.max(max, curLevelNodes);
		return max;
	}

	// 不用容器，老师课堂讲的，比较复杂，下面有自己的优化版本
	public static int maxWidthNoMap(Node head) {
		if (head == null) {
			return 0;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		Node curEnd = head; // 当前层，最右节点是谁
		Node nextEnd = null; // 下一层，最右节点是谁
		int max = 0;
		int curLevelNodes = 0; // 当前层的节点数
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			if (cur.left != null) {
				queue.add(cur.left);
				nextEnd = cur.left;
			}
			if (cur.right != null) {
				queue.add(cur.right);
				nextEnd = cur.right;
			}
			curLevelNodes++;
			if (cur == curEnd) {
				max = Math.max(max, curLevelNodes);
				curLevelNodes = 0;
				curEnd = nextEnd;
			}
		}
		return max;
	}


	// 不用容器，自己的优化版本
	// 按层遍历时，直接遍历一整层。就可以拿到每一个层的宽度
	// O（N）：一个节点最多进入一次队列，出一次队列
	public static int maxWidthNoMap1(Node head) {
		if (head == null) {
			return 0;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		int ans = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			int curLevelNodes = 0;
			// 直接遍历一整层
			for (int i = 0; i < size; i++) {
				Node cur = queue.poll();
				curLevelNodes++;
				if (cur.left != null) {
					queue.add(cur.left);
				}
				if (cur.right != null) {
					queue.add(cur.right);
				}
			}
			ans = Math.max(ans, curLevelNodes);
		}
		return ans;
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
		int maxLevel = 10;
		int maxValue = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			if (maxWidthUseMap(head) != maxWidthNoMap1(head)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");

	}

}
