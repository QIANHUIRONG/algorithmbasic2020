package class30;

// 笔记是完整的，不需要看思维导图！！！

/*
时间：8
 */
/*
传统的二叉树的三序遍历【8】
Morris【15】：时间复杂度o(n), 空间复杂度o(1)。利用了一棵树中叶子大量的空闲指针
流程【17】
流程为什么对【30】
morris加工加工先序中序后序【36】
morris code【40】
morris 加工先序 code【48】
morris 加工中序 code【52】
morris 时间复杂度 【54】:我每来到一个节点，都要过一遍左树的右边界，时间复杂度会不会因为这个升高？
morris 加工后序【1：00】
笔试不用，面试装逼【1：16】
如何判断一棵树是搜索二叉树【1：17】：把中序遍历的打印行为变成对比行为
 */

/*
二叉树的三序遍历，递归的方式，时间复杂度O(N),空间复杂度O(H),H是树的高度;
使用Morris改写的三序遍历，可以做到时间复杂度O(N),空间复杂度O(1)!
*/

/*
Morris遍历：
开始时，cur来到头节点：
1.如果cur没有左孩子，cur直接去右树,cur = cur.right;
2.如果cur有左孩子，先找到左子树上的最右节点MostRight,
	2.1.如果MostRight的右指针指向空，也就是第一次来到当前节点，让MostRight的右指针指向cur，然后cur指向左子树，cur=cur.left;
	2.2.如果MostRight的右指针指向cur，也就是第二次来到当前节点，让MostRight的右指针指向空，然后cur指向右子树，cur=cur.right;
3.当cur为空时，morris遍历结束
*/

/*
Morris序：
没有左树的只会经过1次；有左树的会经过两次
我每来到一个节点，都要过一遍左树的右边界，时间复杂度会不会因为这个升高？——不会，每一次过的左树右边界都是不同的，整棵树可以被左树的右边界分解
 */


/*
morris遍历加工先序遍历
没有左树的只会经过1次；有左树的会经过两次
没有左树只经过1次的就在经过1次那里直接打印；
有左树会经过两次的就在两次中的第一次打印；
 */

/*
morris遍历加工中序遍历
没有左树的只会经过1次；有左树的会经过两次
没有左树只经过1次的就在经过1次那里直接打印；
有左树会经过两次的就在两次中的第2次打印；
 */

/*
morris遍历加工后序遍历
加工后序遍历没有那么简单了，规则如下：
1.没有左树的只会经过1次不鸟它
2.有左树的会经过2次的在第2次经过时逆序打印当前节点的左树的右边界
3.Morris遍历完后，逆序打印整棵树的右边界，注意是整棵树

怎么逆序打印？
千万不要用栈！好不容易就是要让额外空间复杂度O(1),还用栈？
->链表反转，打印，打印完，记得再反转回来；
 */

/*
时间复杂度o(n), 空间复杂度(1).
 */

/*
笔试的时候用递归，面试的时候先说递归，再装逼说morris
 */

/*
Morris改写判断是否是搜索二叉树
	本题也可以用二叉树的递归套路
	利用Morris判断一棵树是不是搜索二叉树：Morris遍历的时候，把中序遍历的打印行为变为对比值大小的行为
	中序遍历判断每次前一个节点是否小于后一个节点
 */

public class Code01_MorrisTraversal {

	public static class Node {
		public int value;
		Node left;
		Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	public static void process(Node root) {
		if (root == null) {
			return;
		}
		// 1
		process(root.left);
		// 2
		process(root.right);
		// 3
	}

	// morris遍历
	public static void morris(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null; // 左子树上的最右节点
		while (cur != null) {  // cur为空时，遍历结束
			mostRight = cur.left;
			if (mostRight != null) { // 要去找左树上的最右孩子，如果连左树都没有，直接指向右子树，cur = cur.right;
				// 有左树，会经过两次
				// 找左树上的最右孩子；mostRight.right != cur这个条件确保第一次标记过后，第二次拿到的mostRight节点是对的
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}

				if (mostRight.right == null) {
					// MostRight的右指针指向空，也就是第一次来到当前节点
					// 让MostRight的右指针指向cur，然后cur指向左子树
					mostRight.right = cur;
					cur = cur.left;
				} else {
					// MostRight的右指针指向cur，也就是第二次来到当前节点
					// 让MostRight的右指针指向null，然后cur指向右子树
					mostRight.right = null;
					cur = cur.right;
				}
			} else {
				// 根本没有左树，直接指向右子树
				cur = cur.right;
			}
		}
	}

	// morris遍历加工先序遍历
	/*
	没有左树的只会经过1次；有左树的会经过两次
	没有左树只经过1次的就在经过1次那里直接打印；
	有左树会经过两次的就在两次中的第一次打印；
	 */
	public static void morrisPre(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					// 有左树会经过两次的就在两次中的第一次打印；
					System.out.print(cur.value + " ");
					mostRight.right = cur;
					cur = cur.left;
				} else {
					mostRight.right = null;
					cur = cur.right;
				}
			} else {
				// 	没有左树只经过1次的就在经过1次那里直接打印；
				System.out.print(cur.value + " ");
				cur = cur.right;
			}
		}
		System.out.println();
	}

	// morris遍历加工中序遍历
	/*
	没有左树的只会经过1次；有左树的会经过两次
	没有左树只经过1次的就在经过1次那里直接打印；
	有左树会经过两次的就在两次中的第2次打印；
	 */
	public static void morrisIn(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
				} else {
					// 	有左树会经过两次的就在两次中的第2次打印；
					System.out.print(cur.value + " ");
					mostRight.right = null;
					cur = cur.right;
				}
			} else {
				// 没有左树只经过1次的就在经过1次那里直接打印；
				System.out.print(cur.value + " ");
				cur = cur.right;
			}
		}
		System.out.println();
	}

	// morris遍历加工后序遍历
	/*
	加工后序遍历没有那么简单了，规则如下：
	1.没有左树的只会经过1次不鸟它
	2.有左树的会经过2次的在第2次经过时逆序打印当前节点左树右边界
	3.Morris遍历完后，逆序打印整棵树的右边界，注意是整棵树

	怎么逆序打印？
	千万不要用栈！好不容易就是要让额外空间复杂度O(1),还用栈？
	->链表反转，打印，打印完，记得再反转回来；
	 */
	public static void morrisPos(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
				} else {
					mostRight.right = null;
					// 有左树的会经过2次的在第2次经过时逆序打印左树右边界
					/*
					注意1，一定要在mostRight的右指针指向空了再能去逆序打印，否则因为右指针还存在，会打印重复
					注意2，一定要在cur = cur.right之前去打印，否则就找不到原本cur的左树了;
					如果是先序遍历和中序遍历，直接拿值，就无所谓顺序了；
					 */
					printEdge(cur.left);
					cur = cur.right;
				}
			} else {
				// 没有左树的只会经过1次不鸟它
				cur = cur.right;
			}
		}
		// Morris遍历完后，逆序打印整棵树的右边界，注意是整棵树
		printEdge(head);
		System.out.println();
	}

	// 打印，后序遍历使用
	public static void printEdge(Node head) {
		Node tail = reverseEdge(head);
		Node cur = tail;
		while (cur != null) {
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		reverseEdge(tail);
	}

	// 反转链表，后序遍历使用
	public static Node reverseEdge(Node head) {
		Node pre = null;
		Node next = null;
		while (head != null) {
			next = head.right; // 注意是right指针
			head.right = pre;
			pre = head;
			head = next;
		}
		return pre;
	}

	// Morris改写判断是否是搜索二叉树
	// 本题也可以用二叉树的递归套路
	// 中序遍历判断每次前一个节点是否小于后一个节点
	public static boolean isBST1(Node head) {
		if (head == null) {
			return true;
		}
		Node cur = head;
		Node mostRight = null;
		Integer pre = null; // 保存前一个节点的value
		boolean ans = true; // 是否是搜索二叉树
		while (cur != null) {
			mostRight = cur.left;
			if (cur.left != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					// 第1次
					mostRight.right = cur;
					cur = cur.left;
				} else {
					// 第2次
					// 中序遍历，判断前一个数是否小于后一个数
					if (pre != null && pre >= cur.value) {
						ans = false; // 千万不要直接返回fasle了，因为你改过了指针，要让morris跑完，让指针复原
					}
					pre = cur.value;
					mostRight.right = null;
					cur = cur.right;
				}
			} else {
				// 没左树
				// 中序遍历，判断前一个数是否小于后一个数
				if (pre != null && pre >= cur.value) {
					ans = false;
				}
				pre = cur.value;
				cur = cur.right;
			}
		}
		return ans;
	}





	/*
	以下代码是老师原版的，比较优雅，上面是自己改了一版更好理解的
	 */
//	public static void morris(Node head) {
//		if (head == null) {
//			return;
//		}
//		Node cur = head;
//		Node mostRight = null;
//		while (cur != null) {
//			mostRight = cur.left;
//			if (mostRight != null) {
//				while (mostRight.right != null && mostRight.right != cur) {
//					mostRight = mostRight.right;
//				}
//				if (mostRight.right == null) {
//					mostRight.right = cur;
//					cur = cur.left;
//					continue;
//				} else {
//					mostRight.right = null;
//				}
//			}
//			cur = cur.right;
//		}
//	}
//
//	public static void morrisPre(Node head) {
//		if (head == null) {
//			return;
//		}
//		Node cur = head;
//		Node mostRight = null;
//		while (cur != null) {
//			mostRight = cur.left;
//			if (mostRight != null) {
//				while (mostRight.right != null && mostRight.right != cur) {
//					mostRight = mostRight.right;
//				}
//				if (mostRight.right == null) {
//					System.out.print(cur.value + " ");
//					mostRight.right = cur;
//					cur = cur.left;
//					continue;
//				} else {
//					mostRight.right = null;
//				}
//			} else {
//				System.out.print(cur.value + " ");
//			}
//			cur = cur.right;
//		}
//		System.out.println();
//	}
//
//	public static void morrisIn(Node head) {
//		if (head == null) {
//			return;
//		}
//		Node cur = head;
//		Node mostRight = null;
//		while (cur != null) {
//			mostRight = cur.left;
//			if (mostRight != null) {
//				while (mostRight.right != null && mostRight.right != cur) {
//					mostRight = mostRight.right;
//				}
//				if (mostRight.right == null) {
//					mostRight.right = cur;
//					cur = cur.left;
//					continue;
//				} else {
//					mostRight.right = null;
//				}
//			}
//			System.out.print(cur.value + " ");
//			cur = cur.right;
//		}
//		System.out.println();
//	}
//
//	public static void morrisPos(Node head) {
//		if (head == null) {
//			return;
//		}
//		Node cur = head;
//		Node mostRight = null;
//		while (cur != null) {
//			mostRight = cur.left;
//			if (mostRight != null) {
//				while (mostRight.right != null && mostRight.right != cur) {
//					mostRight = mostRight.right;
//				}
//				if (mostRight.right == null) {
//					mostRight.right = cur;
//					cur = cur.left;
//					continue;
//				} else {
//					mostRight.right = null;
//					printEdge(cur.left);
//				}
//			}
//			cur = cur.right;
//		}
//		printEdge(head);
//		System.out.println();
//	}
//
//	public static void printEdge(Node head) {
//		Node tail = reverseEdge(head);
//		Node cur = tail;
//		while (cur != null) {
//			System.out.print(cur.value + " ");
//			cur = cur.right;
//		}
//		reverseEdge(tail);
//	}
//
//	public static Node reverseEdge(Node from) {
//		Node pre = null;
//		Node next = null;
//		while (from != null) {
//			next = from.right;
//			from.right = pre;
//			pre = from;
//			from = next;
//		}
//		return pre;
//	}

	// for test -- print tree
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

	/**
	 * 判断是不是搜索二叉树
	 * @param head
	 * @return
	 */
	public static boolean isBST(Node head) {
		if (head == null) {
			return true;
		}
		Node cur = head;
		Node mostRight = null;
		Integer pre = null;
		boolean ans = true;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			if (pre != null && pre >= cur.value) {
				ans = false;
			}
			pre = cur.value;
			cur = cur.right;
		}
		return ans;
	}

	public static void main(String[] args) {
		Node head = new Node(4);
		head.left = new Node(2);
		head.right = new Node(6);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.right.left = new Node(5);
		head.right.right = new Node(7);
		printTree(head);
		morrisIn(head);
		morrisPre(head);
		morrisPos(head);
		printTree(head);

	}

}
