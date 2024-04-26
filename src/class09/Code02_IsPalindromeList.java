package class09;

import java.util.Stack;

public class Code02_IsPalindromeList {

    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    /**
     * 方法1：使用容器的做法
     * O（n）的空间复杂度
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome1(Node head) {
        Stack<Node> stack = new Stack<Node>();
        Node cur = head;
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        while (head != null) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 方法2：不用看。需要n/2的额外空间复杂度
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome2(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node right = head.next;
        Node cur = head;
        while (cur.next != null && cur.next.next != null) {
            right = right.next;
            cur = cur.next.next;
        }
        Stack<Node> stack = new Stack<Node>();
        while (right != null) {
            stack.push(right);
            right = right.next;
        }
        while (!stack.isEmpty()) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 方法3：最优解。
     * 时间复杂度O（N), 空间复杂度O（1）
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome3(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        if (head.next.next == null) {
            return head.value == head.next.value;
        }
        // 1.快慢指针求中点
        Node slow = head.next;
        Node fast = head.next.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2.链表反转
        Node cur = slow.next;
        Node pre = slow;
        pre.next = null;
        Node next = null;

        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }

        // 3.对比
        Node start = head;
        Node end = pre;
        while (start != null && end != null) {
            if (start.value != end.value) {
                return false;
            }
            start = start.next;
            end = end.next;
        }

        // 4.恢复链表
        cur = pre.next;
        pre.next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return true;
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

        Node head = null;
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(2);
        head.next.next.next = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        printLinkedList(head);
//		System.out.print(isPalindrome1(head) + " | ");
//		System.out.print(isPalindrome2(head) + " | ");
//		System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        if (isPalindrome1(head) == isPalindrome2(head) && isPalindrome1(head) == isPalindrome3(head)) {
            System.out.println("nice");
        } else {
            System.out.println("fuck");
        }
        System.out.println("=========================");

    }

}
