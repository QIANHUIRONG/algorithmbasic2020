package class11;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

// 时间：20
public class Code02_SerializeAndReconstructTree {
    /*
     * 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化，
     * 以下代码全部实现了。
     * 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
     * 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
     * 比如如下两棵树
     *         __2
     *        /
     *       1
     *       和
     *       1__
     *          \
     *           2
     * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
     *       
     * */
	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	/**
	 * 无论是先序、后序、按层方式序列化，都是在遍历的过程中，不要忽略空节点，该占位就占位。
	 */

	/**
	 * 一、先序方式序列化
	 * Queue<String> ans = new LinkedList<>();只是存储的容器，你用其他的也行,
	 * 像力扣297题就要求序列化成String类型。
	 * 注意：不能忽略空节点
	 */
	public static Queue<String> preSerial(Node head) {
		Queue<String> ans = new LinkedList<>();
		pres(head, ans);
		return ans;
	}

	public static void pres(Node head, Queue<String> ans) {
		if (head == null) {
			// 空节点也需要存起来，才能唯一对应一颗二叉树
			ans.add(null);
		} else {
			ans.add(String.valueOf(head.value));
			pres(head.left, ans);
			pres(head.right, ans);
		}
	}

	/**
	 * 一、先序方式返序列化
	 */
	public static Node buildByPreQueue(Queue<String> queue) {
		if (queue == null || queue.size() == 0) {
			return null;
		}
		return preb(queue);
	}

	public static Node preb(Queue<String> queue) {
		String value = queue.poll();
		if (value == null) {
			return null;
		}
		Node head = new Node(Integer.valueOf(value));
		head.left = preb(queue);
		head.right = preb(queue);
		return head;
	}

	/**
	 * 二、后序方式序列化
	 */
	public static Queue<String> posSerial(Node head) {
		Queue<String> ans = new LinkedList<>();
		poss(head, ans);
		return ans;
	}

	public static void poss(Node head, Queue<String> ans) {
		if (head == null) {
			ans.add(null);
		} else {
			poss(head.left, ans);
			poss(head.right, ans);
			ans.add(String.valueOf(head.value));
		}
	}

	/**
	 * 二、后序方式反序列化
	 */
	public static Node buildByPosQueue(Queue<String> poslist) {
		if (poslist == null || poslist.size() == 0) {
			return null;
		}
		// 左右头  ->  stack(头右左) -> 和先序类似，先建立头，再建右，再建左
		Stack<String> stack = new Stack<>();
		while (!poslist.isEmpty()) {
			stack.push(poslist.poll());
		}
		return posb(stack);
	}

	public static Node posb(Stack<String> posstack) {
		String value = posstack.pop();
		if (value == null) {
			return null;
		}
		Node head = new Node(Integer.valueOf(value));
		head.right = posb(posstack);
		head.left = posb(posstack);
		return head;
	}



	/**
	 * 三、按层方式序列化
	 *
	 *  本质就是宽度优先遍历->宽度优先遍历需要一个队列
	 *     也是不能忽略空节点
	 *     序列化的规则:
	 *         遇到一个节点，序列化它的左右孩子。
	 *         如果孩子为空，就只序列化不往队列里去
	 *         如果孩子不为空，就即序列化又往队列里去。（宽度优先遍历的体现罢了）
	 */
	public static Queue<String> levelSerial(Node head) {
		if (head == null) {
			return null;
		}
		// 收集答案的队列
		Queue<String> ans = new LinkedList<>();
		// 先序列化头节点
		ans.add(String.valueOf(head.value));
		// 按层遍历需要的队列
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(head);
		while (!queue.isEmpty()) {
			head = queue.poll(); // head 父   子
			if (head.left != null) {
				ans.add(String.valueOf(head.left.value));
				queue.add(head.left);
			} else {
				ans.add(null);
			}
			if (head.right != null) {
				ans.add(String.valueOf(head.right.value));
				queue.add(head.right);
			} else {
				ans.add(null);
			}
		}
		return ans;
	}

	/**
	 * 三、按层方式返序列化
	 */
//	public static NodeT buildByLevelQueue(Queue<String> levelList) {
//		if (levelList == null || levelList.size() == 0) {
//			return null;
//		}
//		NodeT head = generateNode(levelList.poll());
//		Queue<NodeT> queue = new LinkedList<NodeT>();
//		if (head != null) {
//			queue.add(head);
//		}
//		NodeT node = null;
//		while (!queue.isEmpty()) {
//			node = queue.poll();
//			node.left = generateNode(levelList.poll());
//			node.right = generateNode(levelList.poll());
//			if (node.left != null) {
//				queue.add(node.left);
//			}
//			if (node.right != null) {
//				queue.add(node.right);
//			}
//		}
//		return head;
//	}

	// 自己的反序列化版本，更好理解一点
	// 按层方式序列化和反序列化归根结底都是按层方式，都需要一个队列
	public static Node buildByLevelQueue(Queue<String> levelList) {
		if (levelList == null || levelList.size() == 0) {
			return null;
		}
		// 序列化时先序列化头节点，反序列化同样道理，先建出头节点
		String value = levelList.poll();
		if (value == null) {
			return null;
		}
		Node head = new Node(Integer.valueOf(value));
		// 按层遍历需要的队列
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		while (!queue.isEmpty()) {
			Node node = queue.poll();
			// 返序列化左子树。序列化是让头节点去序列化左右子树，返序列化就让头节点去返序列化左右子树
			value = levelList.poll();
			if (value != null) {
				// 左树不为空，即反序列化又往队列里去；为空，只反序列化不往队列里去
				node.left = new Node(Integer.valueOf(value));
				queue.add(node.left);
			} else {
				node.left = null;
			}
			// 右
			value = levelList.poll();
			if (value != null) {
				node.right = new Node(Integer.valueOf(value));
				queue.add(node.right);
			} else {
				node.right = null;
			}
		}
		return head;
	}

	public static Node generateNode(String val) {
		if (val == null) {
			return null;
		}
		return new Node(Integer.valueOf(val));
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
	public static boolean isSameValueStructure(Node head1, Node head2) {
		if (head1 == null && head2 != null) {
			return false;
		}
		if (head1 != null && head2 == null) {
			return false;
		}
		if (head1 == null && head2 == null) {
			return true;
		}
		if (head1.value != head2.value) {
			return false;
		}
		return isSameValueStructure(head1.left, head2.left) && isSameValueStructure(head1.right, head2.right);
	}

	// for test
	public static void printTree(Node head) {
		System.out.println("Binary Tree:");
		printInOrder(head, 0, "H", 17);
		System.out.println();
	}

	public static void printInOrder(Node head, int height, String to, int len) {
		if (head == null) {
			return;
		}
		printInOrder(head.right, height + 1, "v", len);
		String val = to + head.value + to;
		int lenM = val.length();
		int lenL = (len - lenM) / 2;
		int lenR = len - lenM - lenL;
		val = getSpace(lenL) + val + getSpace(lenR);
		System.out.println(getSpace(height * len) + val);
		printInOrder(head.left, height + 1, "^", len);
	}

	public static String getSpace(int num) {
		String space = " ";
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			buf.append(space);
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		int maxLevel = 5;
		int maxValue = 100;
		int testTimes = 1000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			Queue<String> pre = preSerial(head);
			Queue<String> pos = posSerial(head);
			Queue<String> level = levelSerial(head);
			Node preBuild = buildByPreQueue(pre);
			Node posBuild = buildByPosQueue(pos);
			Node levelBuild = buildByLevelQueue(level);
			if (!isSameValueStructure(preBuild, posBuild) || !isSameValueStructure(posBuild, levelBuild)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");
		
	}
}
