package class12;

import java.util.ArrayList;
import java.util.List;


// 判断是否是搜索二叉树：36

/**
 * 搜索二叉树：任何一颗树都是左树的最大值比当前值小；右树的最小值比当前值大
 * 经典的搜索二叉树是没有重复值的。
 */
public class Code03_IsBST {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    /**
     * 方法一：中序遍历，然后看是不是递增的。
     * 这个方法也掌握下，在递归中收集ans
     *
     * @param head
     * @return
     */
    public static boolean isBST1(Node head) {
        if (head == null) {
            return true;
        }
        List<Node> ans = new ArrayList<>();
        // 中序遍历收集
        in(head, ans);
        for (int i = 1; i < ans.size(); i++) {
            if (ans.get(i).value <= ans.get(i - 1).value) {
                return false;
            }
        }
        return true;
    }

    // 中序遍历
    public static void in(Node head, List<Node> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    /**
     * 方法二：二叉树的递归套路
     * 1)X左树是BST
     * 2)X右树是BST
     * 3)X左树最大值max<X
     * 4)X右树最小值min>X
     *
     * @param head
     * @return
     */
    public static boolean isBST2(Node head) {
        if (head == null) {
            return true;
        }
        return process(head).isBST;
    }

    public static class Info {
        public boolean isBST;
        public int max;
        public int min;

        public Info(boolean i, int ma, int mi) {
            isBST = i;
            max = ma;
            min = mi;
        }

    }

    // 返回以x为头的整棵树是不是搜索二叉树
    public static Info process(Node x) {
        if (x == null) {
            // 空树最大值、最小值无法设置，可以返回null，让上游函数自己判断。上游拿到null，就知道子树是null。
            return null;
        }
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);
        int max = x.value;
        int min = x.value;
        if (leftInfo != null) {
            max = Math.max(leftInfo.max, max);
            min = Math.min(leftInfo.min, min);
        }
        if (rightInfo != null) {
            max = Math.max(rightInfo.max, max);
            min = Math.min(rightInfo.min, min);
        }
        boolean isBST = true;
        if (leftInfo != null && !leftInfo.isBST) {
            isBST = false;
        }
        if (rightInfo != null && !rightInfo.isBST) {
            isBST = false;
        }
        if (leftInfo != null && leftInfo.max >= x.value) {
            isBST = false;
        }
        if (rightInfo != null && rightInfo.min <= x.value) {
            isBST = false;
        }
        return new Info(isBST, max, min);
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
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (isBST1(head) != isBST2(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }

}
