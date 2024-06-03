package class09;

/*
题目
给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。

https://leetcode.cn/problems/reverse-linked-list-ii/description/
 */
/*
题解：
    1.剥离出要反转的区域（需要遍历找到left节点的上一个节点，left节点；right节点；right节点的下一个节点）
    2.将待反转的区域反转
    3.重新链接

 */
/*
虚拟头节点
引入虚拟头结点的主要目的是简化代码逻辑，避免处理头结点的特殊情况，使代码更加简洁和易于维护。
当你需要对链表的头部进行插入、删除或其他操作时，可以考虑使用虚拟头结点来简化这些操作。
 */
public class Code05_ReverseBetween {


    public ListNode reverseBetween(ListNode head, int left, int right) {
        // 因为头节点有可能发生变化，使用虚拟头节点可以避免复杂的分类讨论
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;

        ListNode leftPre = dummyNode;
        // 找到left节点的前一个节点、left节点
        for (int i = 1; i <= left - 1; i++) {
            leftPre = leftPre.next;
        }
        ListNode leftNode = leftPre.next;

        // 找到right节点、right节点的下一个节点
        ListNode rightNode = dummyNode;
        for (int i = 1; i <= right; i++) {
            rightNode = rightNode.next;
        }
        ListNode rightNodeNext = rightNode.next;

        // 切断链接
        leftPre.next = null;
        rightNode.next = null;

        // 反转
        reverseLinkedList(leftNode);

        // 接回到原来的链表中
        leftPre.next = rightNode;
        leftNode.next = rightNodeNext;
        return dummyNode.next;
    }

    private void reverseLinkedList(ListNode head) {
        ListNode pre = null;
        ListNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
