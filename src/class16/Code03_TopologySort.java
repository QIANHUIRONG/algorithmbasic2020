package class16;

import java.util.*;

/*
题意：
    经典拓扑排序
 */
/*
时间：
 */
/*
思维导图
拓扑排序定义：
    在一个有向无环图中，拓扑排序是一个顶点的排列，使得对于每一条有向边 (u, v)，u 在 v 之前出现在排序序列中。
    一定是有向无环图才能做拓扑排序。无向图不行，无向图就是有2条指针，A<->B,就相当于有环了。
拓扑排序的作用举例：
    任务调度：
        如果任务之间存在依赖关系，可以用拓扑排序确定任务的执行顺序。
    编译依赖：
        在编译过程中，源文件之间的依赖关系可以通过拓扑排序确定编译顺序。
    课程安排：
        在课程安排中，如果某些课程有前置课程要求，可以使用拓扑排序确定课程学习顺序。

题解：
    1.找到入度为0的点，把他加到排序结果中去，再删掉这个点和它对后序点的影响；再去找入度为0的点，周而复始

 */
public class Code03_TopologySort {

    // graph：有向无环图
    public static List<Node> sortedTopology(Graph graph) {
        // 节点的入度哈希表。key 某个节点   value 剩余的入度
        Map<Node, Integer> inMap = new HashMap<>();
        // 入度为0的节点队列。只有剩余入度为0的点，才进入这个队列
        Queue<Node> zeroInQueue = new LinkedList<>();
        for (Node node : graph.nodes.values()) {
            inMap.put(node, node.in);
            if (node.in == 0) {
                zeroInQueue.add(node);
            }
        }

        List<Node> ans = new ArrayList<>();
        while (!zeroInQueue.isEmpty()) {
            // 弹出入度为0的点，加到res去
            Node cur = zeroInQueue.poll();
            ans.add(cur);
            // 删除这个点和它的影响
            for (Node next : cur.nodes) {
                inMap.put(next, inMap.get(next) - 1);
                if (inMap.get(next) == 0) {
                    zeroInQueue.add(next);
                }
            }
        }
        return ans;
    }
}
