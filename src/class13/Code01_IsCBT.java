package class13;

import java.util.LinkedList;
import java.util.Queue;



/*
题意：判断一棵二叉树是不是完全二叉树
// 测试链接 : https://leetcode.com/problems/check-completeness-of-a-binary-tree/
 */
/*
时间：
 */
/*
思维导图
1.完全二叉树定义：一颗二叉树要吗是满的，要吗从左往右正在变满的过程中

方法一：按层遍历。在day12 Code01_IsCBT讲解过

方法二：方法二：二叉树递归套路
    1.以X为头的整棵树是不是完全二叉树，可能性怎么组织，要向左右子树要什么信息？
        四种情况是完全二叉树：
            1、左满 && 右满 && 左高 == 右高
            2、左完全 && 右满 && 左高 = 右高+1
            3、左满 && 右满 && 左高 == 右高 + 1
            4、左满 && 右完全 && 左高==右高
        除去这4种情况，都不是CBT

        需要的信息：
        是否是满
        是否是完全
        高度
 */
public class Code01_IsCBT {

    // 不要提交这个类
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 方法一：按层遍历。在day12 Code01_IsCBT讲解过
     */
    public static boolean isCompleteTree1(TreeNode head) {
        if (head == null) {
            return true;
        }
        // 是否遇到过左右两个孩子不双全的节点
        boolean leaf = false;
        // 按层遍历需要的队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            head = queue.poll();
            TreeNode l = head.left;
            TreeNode r = head.right;
            if (
                // 如果一个节点，有右无左，直接false
                // 如果遇到了不双全的节点之后，又发现当前节点不是叶节点，false
                    ((l == null && r != null) ||
                            (leaf && (l != null || r != null)))) {
                return false;
            }
            if (l != null) {
                queue.add(l);
            }
            if (r != null) {
                queue.add(r);
            }
            // 遇到第一个左右孩子不双全的节点，标记后面节点都得是叶节点
            if (l == null || r == null) {
                leaf = true;
            }
        }
        return true;
    }

    /*
    方法二：二叉树递归套路

     */
    public static boolean isCompleteTree2(TreeNode head) {
        return process(head).isCBT;
    }

    public static class Info {
        public boolean isFull;
        public boolean isCBT;
        public int height;

        public Info(boolean full, boolean cbt, int h) {
            isFull = full;
            isCBT = cbt;
            height = h;
        }
    }

    public static Info process(TreeNode x) {
        if (x == null) {
            return new Info(true, true, 0);
        }
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        // 判断一棵树是否是满二叉树，上节课讲过。左满&&右满&&左高==右高
        boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
        boolean isCBT = false;
        // 4种情况，看能不能改true
        if (leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height) {
            isCBT = true;
        } else if (leftInfo.isCBT && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
            isCBT = true;
        } else if (leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
            isCBT = true;
        } else if (leftInfo.isFull && rightInfo.isCBT && leftInfo.height == rightInfo.height) {
            isCBT = true;
        }
        return new Info(isFull, isCBT, height);
    }

    // for test
    public static TreeNode generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static TreeNode generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = generateRandomBST(maxLevel, maxValue);
            if (isCompleteTree1(head) != isCompleteTree2(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }

}
