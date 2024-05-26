package class03;
/*
 [题意]
 删除链表中指定的值
*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]
跳过头部的指定值，跳过后面多次出现的指定值

 */
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
			if (head.value == num) {
				head = head.next;
			} else {
				break;
			}
		}
		// 方式1：
//		Node pre = head;
//		Node cur = head;
//		while (cur != null) {
//			if (cur.value == num) {
//				pre.next = cur.next;
//			} else {
//				pre = cur;
//			}
//			cur = cur.next;
//		}
		// 方式2：
		Node cur = head;
		while (cur.next != null) {
			if (cur.next.value != num) {
				cur = cur.next;
			} else {
				cur.next = cur.next.next;
			}

		}
		return head;
	}

	public static void main(String[] args) {
		// 示例：创建链表 1 -> 2 -> 2 -> 3 -> 3 -> 4 -> 2，删除值为 2 的所有节点
		Node head = new Node(1);
		head.next = new Node(2);
		head.next.next = new Node(2);
		head.next.next.next = new Node(3);
		head.next.next.next.next = new Node(3);
		head.next.next.next.next.next = new Node(4);
		head.next.next.next.next.next.next = new Node(2);
		Node cur = head;
		while (cur != null) {
			System.out.print(cur.value + " -> ");
			cur = cur.next;
		}
		System.out.println();

		Node newHead = removeValue(head, 2);

		// 打印新链表：1 -> 3 -> 3 -> 4
		while (newHead != null) {
			System.out.print(newHead.value + " -> ");
			newHead = newHead.next;
		}
	}
}
