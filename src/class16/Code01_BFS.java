package class16;

import java.util.*;

public class Code01_BFS {

    // 从node出发，进行宽度优先遍历
	/*
	BFS——队列；
	不同于二叉树，图里必须有一个set才能完成宽度优先扁历->因为二叉树没有环的问题
	遍历时，没有在set中，才放入队列；在set中，说明遍历过了，不放入队列
	 */
    public static void bfs(Node start) { // 一定要有一个开始节点
        if (start == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        Set<Node> set = new HashSet<>();
        queue.add(start);
        set.add(start);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.println(cur.value);
            for (Node node : cur.nodes) { // 遍历直接邻居，顺序无所谓，都对。
                if (!set.contains(node)) {
                    set.add(node);
                    queue.add(node);
                }
            }
        }
    }
}
