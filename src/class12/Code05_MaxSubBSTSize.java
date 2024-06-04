package class12;

import java.util.ArrayList;


/*
题目
给定一棵二叉树的头节点head，返回这颗二叉树中最大的二叉搜索子树的节点个数
线测试链接 : https://leetcode.com/problems/largest-bst-subtree
 */

/*
时间：
题意：1：18
题解：1：23
code：1：28
 */

/*
思维导图：
    以x为头的整棵树，怎么求出它的最大搜索二叉子树？
	1、与X无关：Math.max(左树的最大BST,右树的最大BST)
	2、与X有关：那么X整棵树必须是BST：左树是BST && 右树是BST && 左最大<=x<=右最小
	需要的信息：
	    1.最大BST size；
	    2.是否是BST；
	    3.max；
	    4.min；
	    5.树的节点树size(因为如果与X有关，x要整合出自己的最大BST size，需要子树的size。左子树size+右子树size+1)
	发现如果size == 最大BST size，那么就是BST，2条件可以省略。
 */
public class Code05_MaxSubBSTSize {

    // 提交时不要提交这个类
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int value) {
            val = value;
        }
    }

    // 提交如下的largestBSTSubtree方法，可以直接通过
    public static int largestBSTSubtree(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxBSTSize;
    }

    public static class Info {
        // 最大搜索子树的节点树
        public int maxBSTSize;
        // 整棵树的节点树
        public int allSize;
        public int max;
        public int min;

        public Info(int m, int a, int ma, int mi) {
            maxBSTSize = m;
            allSize = a;
            max = ma;
            min = mi;
        }
    }

    public static Info process(TreeNode x) {
        if (x == null) {
            // 空树无法设置最大最小值，直接设置null，上游自行判断处理
            return null;
        }
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);
        int max = x.val;
        int min = x.val;
        int allSize = 1;
        if (leftInfo != null) {
            max = Math.max(leftInfo.max, max);
            min = Math.min(leftInfo.min, min);
            allSize += leftInfo.allSize;
        }
        if (rightInfo != null) {
            max = Math.max(rightInfo.max, max);
            min = Math.min(rightInfo.min, min);
            allSize += rightInfo.allSize;
        }

        // 就差一个maxBSTSize属性
        int p1 = -1; // 左树的最大搜索二叉子树大小
        if (leftInfo != null) {
            p1 = leftInfo.maxBSTSize;
        }
        int p2 = -1; // 右树的最大搜索二叉子树大小
        if (rightInfo != null) {
            p2 = rightInfo.maxBSTSize;
        }
        int p3 = -1; // 我的最大搜索二叉子树大小
        boolean leftBST = leftInfo == null ? true : (leftInfo.maxBSTSize == leftInfo.allSize); // 判断左树是不是BST
        boolean rightBST = rightInfo == null ? true : (rightInfo.maxBSTSize == rightInfo.allSize); // 判断右树是不是BST
        boolean leftMaxLessX = leftInfo == null ? true : (leftInfo.max < x.val); // 判断左最大<=x
        boolean rightMinMoreX = rightInfo == null ? true : (x.val < rightInfo.min); // x<=右最小

        // 计算我的最大搜索二叉子树大小
        if (leftBST && rightBST && leftMaxLessX && rightMinMoreX) { // 左右子树都是BST的情况下 && 左最大<=x<=右最小
            int leftSize = leftInfo == null ? 0 : leftInfo.allSize; // 左树的节点总数
            int rightSize = rightInfo == null ? 0 : rightInfo.allSize; // 右树的节点总数
            p3 = leftSize + rightSize + 1;
        }
        return new Info(Math.max(p1, Math.max(p2, p3)), allSize, max, min);
    }

    // ==============================

    // 为了验证
    // 对数器方法
    public static int right(TreeNode head) {
        if (head == null) {
            return 0;
        }
        int h = getBSTSize(head);
        if (h != 0) {
            return h;
        }
        return Math.max(right(head.left), right(head.right));
    }

    // 为了验证
    // 对数器方法
    public static int getBSTSize(TreeNode head) {
        if (head == null) {
            return 0;
        }
        ArrayList<TreeNode> arr = new ArrayList<>();
        in(head, arr);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return 0;
            }
        }
        return arr.size();
    }

    // 为了验证
    // 对数器方法
    public static void in(TreeNode head, ArrayList<TreeNode> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    // 为了验证
    // 对数器方法
    public static TreeNode generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // 为了验证
    // 对数器方法
    public static TreeNode generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    // 为了验证
    // 对数器方法
    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = generateRandomBST(maxLevel, maxValue);
            if (largestBSTSubtree(head) != right(head)) {
                System.out.println("出错了！");
            }
        }
        System.out.println("测试结束");
    }

}
