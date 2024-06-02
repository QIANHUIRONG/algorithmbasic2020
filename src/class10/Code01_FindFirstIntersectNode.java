package class10;


/**
 * 题意
 * 给定两个可能有环也可能无环的单链表，头节点head1和head2。请实现一个函数，如果两个链表相交，请返回相交的第一个节点。如果不相交，返回null
 * 【要求】
 * 如果两个链表长度之和为N,时间复杂度请达到O(N),额外空间复杂度请达到0(1)。
 */

/**
 * 时间：
 * 题意：8
 * 找到入环节点：9 （容器方法：11；不用容器方法：14 code：25）
 *
 */
public class Code01_FindFirstIntersectNode {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	public static Node getIntersectNode(Node head1, Node head2) {
		if (head1 == null || head2 == null) {
			return null;
		}
		Node loop1 = getLoopNode(head1);
		Node loop2 = getLoopNode(head2);
		// 都无环
		if (loop1 == null && loop2 == null) {
			return noLoop(head1, head2);
		}
		// 都有环
		if (loop1 != null && loop2 != null) {
			return bothLoop(head1, loop1, head2, loop2);
		}
		// 一个有环一个无环。不可能相交
		return null;
	}

	// 找到链表第一个入环节点，如果无环，返回null
	// 如果1->2->3->4， 头节点是1，那么第一个入环节点就是1
	public static Node getLoopNode(Node head) {
		// 记录一下2024年6月2日的一个疑惑：
		// 如果头节点就一个，它自己就形成环，那么应该是1->1, head.next不会是null的
		// 同理，如果头节点是2节点，head.next.next应该指回head，不会是null的
		if (head == null || head.next == null || head.next.next == null) {
			return null;
		}
		// n1 慢  n2 快
		Node slow = head.next;
		Node fast = head.next.next;
		while (slow != fast) {
			if (fast.next == null || fast.next.next == null) { // 如果快指针走着走着走到null了，就一定没有环
				return null;
			}
			fast = fast.next.next;
			slow = slow.next;
		}
		// 快慢指针相遇，快指针回头降速
		fast = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}
		return slow;
	}

	// 如果两个链表都无环，返回第一个相交节点，如果不想交，返回null
	public static Node noLoop(Node head1, Node head2) {
		if (head1 == null || head2 == null) {
			return null;
		}
		Node cur1 = head1;
		Node cur2 = head2;

		while (cur1.next != null) {
			cur1 = cur1.next;
		}
		while (cur2.next != null) {
			cur2 = cur2.next;
		}

		if (cur1 != cur2) { // 最后一个节点不一样，一定不相交
			return null;
		}

		cur1 = head1;
		cur2 = head2;
		int n = 0;
		while (cur1 != null) {
			n++;
			cur1 = cur1.next;
		}
		while (cur2 != null) {
			n--;
			cur2 = cur2.next;
		}

		// n  :  链表1长度减去链表2长度的值
		cur1 = n > 0 ? head1 : head2; // 谁长，谁的头变成cur1
		cur2 = cur1 == head1 ? head2 : head1; // 谁短，谁的头变成cur2
		n = Math.abs(n);
		while (n != 0) {
			n--;
			cur1 = cur1.next;
		}
		while (cur1 != cur2) {
			cur1 = cur1.next;
			cur2 = cur2.next;
		}
		return cur1;
	}

	// 两个有环链表，返回第一个相交节点，如果不想交返回null
	public static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
		Node cur1 = null;
		Node cur2 = null;
		// 情况二：入环节点相同
		if (loop1 == loop2) {
			cur1 = head1;
			cur2 = head2;
			int n = 0;
			while (cur1 != loop1) {
				n++;
				cur1 = cur1.next;
			}
			while (cur2 != loop2) {
				n--;
				cur2 = cur2.next;
			}
			cur1 = n > 0 ? head1 : head2;
			cur2 = cur1 == head1 ? head2 : head1;
			n = Math.abs(n);
			while (n != 0) {
				n--;
				cur1 = cur1.next;
			}
			while (cur1 != cur2) {
				cur1 = cur1.next;
				cur2 = cur2.next;
			}
			return cur1;
		} else {
			// 情况一、三
			// loop1绕一圈，能找到loop2，就是情况三，然会任意一个相交节点
			// 找不到loop2，就是情况一，没有相交节点
			cur1 = loop1.next;
			while (cur1 != loop1) {
				if (cur1 == loop2) {
					return loop1;
				}
				cur1 = cur1.next;
			}
			return null;
		}
	}

	public static void main(String[] args) {
		// 1->2->3->4->5->6->7->null
		Node head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		head1.next.next.next = new Node(4);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(6);
		head1.next.next.next.next.next.next = new Node(7);

		// 0->9->8->6->7->null
		Node head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next.next.next.next.next; // 8->6
		System.out.println(getIntersectNode(head1, head2).value);

		// 1->2->3->4->5->6->7->4...
		head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		head1.next.next.next = new Node(4);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(6);
		head1.next.next.next.next.next.next = new Node(7);
		head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

		// 0->9->8->2...
		head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next; // 8->2
		System.out.println(getIntersectNode(head1, head2).value);

		// 0->9->8->6->4->5->6..
		head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next.next.next.next.next; // 8->6
		System.out.println(getIntersectNode(head1, head2).value);

	}

}
