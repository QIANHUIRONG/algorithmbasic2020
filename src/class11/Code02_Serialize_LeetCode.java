package class11;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author QIANHUIRONG
 * @date 2024-06-02 21:37
 */
// https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/description/
public class Code02_Serialize_LeetCode {

    // 按层方式序列化
    public String serialize(TreeNode root) {
        // 收集答案的
        LinkedList<String> ans = new LinkedList<>();
        if (root == null) {
            ans.addFirst(null); // 头进，尾出，模拟队列
        } else {
            ans.addFirst(String.valueOf(root.val)); // 先序列化头节点
            Queue<TreeNode> queue = new LinkedList<TreeNode>(); // 按层遍历需要的队列
            queue.add(root);
            while (!queue.isEmpty()) {
                root = queue.poll();
                if (root.left != null) {
                    ans.addFirst(String.valueOf(root.left.val));
                    queue.add(root.left);
                } else {
                    ans.addFirst(null);
                }
                if (root.right != null) {
                    ans.addFirst(String.valueOf(root.right.val));
                    queue.add(root.right);
                } else {
                    ans.addFirst(null);
                }
            }
        }
        // 把尾巴的null去掉（力扣要求的），但是如果ans里面只有一个null，就不用去掉
        while (!ans.isEmpty() && ans.peekFirst() == null) {
            ans.pollFirst();
        }
        // 用来将LinkedList<String> ans -> 转换成力扣要求的形式 [1,2,3,null,null,4,5]
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(ans.pollLast());
        while (!ans.isEmpty()) {
            builder.append("," + ans.pollLast());
        }
        builder.append("]");
        return builder.toString();
    }

    // 按层方式反序列化
    public TreeNode deserialize(String data) {
        String[] strs = data.substring(1, data.length() - 1).split(",");
        int index = 0; // strs专属指针
        TreeNode root = null;
        String value = strs[index++];
        if (value.equals("null")) {
            return root;
        }
        root = new TreeNode(Integer.valueOf(value)); // 先序列化头节点
        Queue<TreeNode> queue = new LinkedList<TreeNode>(); // 按层遍历需要的队列
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            // 左
            // 因为序列化时最后一个null去掉了，所以这里要这样写
            value = index == strs.length ? "null" : strs[index++];
            if (value.equals("null")) {
                node.left = null;
            } else {
                node.left = new TreeNode(Integer.valueOf(value));
                queue.add(node.left);
            }
            // 右
            value = index == strs.length ? "null" : strs[index++];
            if (value.equals("null")) {
                node.right = null;
            } else {
                node.right = new TreeNode(Integer.valueOf(value));
                queue.add(node.right);
            }
        }
        return root;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
