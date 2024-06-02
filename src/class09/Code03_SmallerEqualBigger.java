package class09;

/*
 [题意]
	将单向链表按某值划分成左边小、中间相等、右边大的形式
*/

// 笔记看语雀，很全！！！
public class Code03_SmallerEqualBigger {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	/**
	 * 方法1：使用容器
	 * 放到数组中玩partition
	 * 时间复杂度：O（n), 空间复杂度：O(N)
	 * @param head
	 * @param pivot
	 * @return
	 */
	public static Node listPartition1(Node head, int pivot) {
		if (head == null) {
			return head;
		}
		Node cur = head;
		// 先看有多少个节点
		int i = 0;
		while (cur != null) {
			i++;
			cur = cur.next;
		}
		// 放到数组中
		Node[] nodeArr = new Node[i];
		i = 0;
		cur = head;
		for (i = 0; i != nodeArr.length; i++) {
			nodeArr[i] = cur;
			cur = cur.next;
		}
		// 玩partition
		arrPartition(nodeArr, pivot);
		// 重新组装
		for (i = 1; i != nodeArr.length; i++) {
			nodeArr[i - 1].next = nodeArr[i];
		}
		nodeArr[i - 1].next = null; // 最后一个节点的next指针指向null，不然人家原本可能有指向的
		return nodeArr[0];
	}

	public static void arrPartition(Node[] nodeArr, int pivot) {
		int small = -1;
		int big = nodeArr.length;
		int index = 0;
		while (index != big) {
			if (nodeArr[index].value < pivot) {
				swap(nodeArr, ++small, index++);
			} else if (nodeArr[index].value == pivot) {
				index++;
			} else {
				swap(nodeArr, --big, index);
			}
		}
	}

	public static void swap(Node[] nodeArr, int a, int b) {
		Node tmp = nodeArr[a];
		nodeArr[a] = nodeArr[b];
		nodeArr[b] = tmp;
	}


	/**
	 * 方法2：不使用容器
	 * 时间复杂度：O（n), 空间复杂度：O(1）
	 * @param head
	 * @param pivot
	 * @return
	 */
	public static Node listPartition2(Node head, int pivot) {
		Node sH = null; // small head
		Node sT = null; // small tail
		Node eH = null; // equal head
		Node eT = null; // equal tail
		Node mH = null; // big head
		Node mT = null; // big tail
		Node next = null; // save next node
		while (head != null) {
			next = head.next;
			head.next = null; // 锻连
			if (head.value < pivot) {
				if (sH == null) {
					sH = head;
					sT = head;
				} else {
					sT.next = head;
					sT = head;
				}
			} else if (head.value == pivot) {
				if (eH == null) {
					eH = head;
					eT = head;
				} else {
					eT.next = head;
					eT = head;
				}
			} else {
				if (mH == null) {
					mH = head;
					mT = head;
				} else {
					mT.next = head;
					mT = head;
				}
			}
			head = next;
		}
		if (sH!= null) {
			if (eT != null) {
				sT.next = eH;
				eT.next = mT;
			} else {
				sT.next = mH;
			}
			return sH;
		} else {
			if (eH != null) {
				eT.next = mH;
				return eH;
			} else {
				return mH;
			}
		}

//		// 小于区域的尾巴，连等于区域的头，等于区域的尾巴连大于区域的头
//		if (sT != null) { // 如果有小于区域
//			sT.next = eH;
//			eT = eT == null ? sT : eT; // 下一步，谁去连大于区域的头，谁就变成eT
//		}
//		// 下一步，一定是需要用eT 去接 大于区域的头
//		// 有等于区域，eT -> 等于区域的尾结点
//		// 无等于区域，eT -> 小于区域的尾结点
//		// eT 尽量不为空的尾巴节点
//		if (eT != null) { // 如果小于区域和等于区域，不是都没有
//			eT.next = mH;
//		}
//		return sH != null ? sH : (eH != null ? eH : mH);
	}

	public static void printLinkedList(Node node) {
		System.out.print("Linked List: ");
		while (node != null) {
			System.out.print(node.value + " ");
			node = node.next;
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Node head1 = new Node(7);
		head1.next = new Node(9);
		head1.next.next = new Node(1);
		head1.next.next.next = new Node(8);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(2);
		head1.next.next.next.next.next.next = new Node(5);
		System.out.println("划分之前：");
		printLinkedList(head1);
		 head1 = listPartition1(head1, 5);
//		head1 = listPartition2(head1, 5);
		System.out.println("划分之后：");
		printLinkedList(head1);

	}

}
