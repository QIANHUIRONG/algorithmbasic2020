package class03;

public class Code02_DeleteGivenValue {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	// head = removeValue(head, 2);

	/**
	 * 题解：跳过头部的指定值，跳过后面多次出现的指定值
	 * @param head
	 * @param num
	 * @return
	 */
	public static Node removeValue(Node head, int num) {
		// 1、head来到第一个不需要删的位置
		while (head != null) {
			if (head.value != num) {
				break;
			}
			head = head.next;
		}
		Node pre = head;
		Node cur = head;
		while (cur != null) {
			if (cur.value == num) {
				pre.next = cur.next;
			} else {
				pre = cur;
			}
			cur = cur.next;
		}
		return head;
	}

}
