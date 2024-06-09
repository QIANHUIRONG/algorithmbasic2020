package class16;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/*
最小生成树——普利姆算法：1：43
算法描述：
	1)可以从任意节点出发来寻找最小生成树
	2)某个点加入到被选取的点中后，解锁这个点出发的所有新的边
	3)在所有解锁的边中选最小的边，然后看看这个边会不会形成环
	4)如果会，不要当前边，继续考察剩下解锁的边中最小的边，重复3)
	5)如果不会，要当前边，将该边的指向点加入到被选取的点中，重复2)
	6)当所有点都被选取，最小生成树就得到了
 */
/*
1、无向图，需要指定从哪个点出发
2、点解锁边，选一个最小的边（堆），如果不会形成环（set），要这条边，接着边解锁点
3、当所有的点都解锁了，停
4、不需要并查集：每次都是一个一个的点进入集合，不存在两大片集合的合并问题。表达解锁的点->一个正常的set就足够了

 */

/*
深度扩展：
为什么克鲁斯卡尔算法，判断是否会形成环用的并查集；
普利姆算法，判断是否会形成环用的Hashset？
    Kruskal 和 Prim 算法是两种不同的最小生成树（MST）算法，它们使用不同的数据结构来判断是否形成环是基于它们的工作方式和算法特性决定的。下面详细解释为什么 Kruskal 算法使用并查集而 Prim 算法使用 HashSet。

    Kruskal 算法
    工作方式：

    排序边：Kruskal 算法首先将所有边按权重从小到大排序。
    逐条选择边：依次选择最小权重的边，检查是否会形成环。
    判断环：如果当前边的两个顶点不在同一个集合中，则将这条边加入生成树，并将两个顶点所在的集合合并；如果在同一个集合中，则忽略这条边。
    为什么使用并查集：

    并查集的特点：并查集（Union-Find）数据结构特别适合处理连通性问题，能够高效地进行集合的合并（Union）和查找（Find）操作。
    判断环的效率：并查集通过路径压缩和按秩合并优化，可以在接近常数时间内完成合并和查找操作，因此非常适合在逐条边加入的过程中高效判断是否形成环。
    Prim 算法
    工作方式：

    选择起点：Prim 算法从一个任意顶点开始，将其加入生成树。
    逐步扩展：每次选择一条最小权重的边，该边连接一个已在生成树中的顶点和一个不在生成树中的顶点，将这条边和新的顶点加入生成树。
    判断环：由于 Prim 算法每次都是从已加入生成树的顶点出发，只选择连接未加入生成树的顶点的边，因此不会形成环。
    为什么使用 HashSet：

    保持已加入顶点的集合：在 Prim 算法中，用一个 HashSet 来记录已经加入生成树的顶点。
    检查顶点是否已加入：在每次选择边时，只需检查边的另一个顶点是否在 HashSet 中。由于 HashSet 提供常数时间的插入和查找操作，能够高效地判断顶点是否已在生成树中。
    不需要复杂的结构：因为 Prim 算法本质上是逐步扩展生成树的边，不需要像 Kruskal 算法那样频繁地合并和查找集合，只需判断顶点是否已在生成树中即可，因此 HashSet 已足够。
 */

// 12分钟

public class Code09_Prim {


    /*
    这个方法是课堂是老师讲的，但是是遍历了所有的点去跑一边普利姆算法
    经典的是会给一个出发点，自己写了，看下面primMSTHaveStart
     */
    public static Set<Edge> primMST(Graph graph) {
        // 1、小根堆。存放解锁的边
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());
        // 2、集合，存放解锁的点
        HashSet<Node> nodeSet = new HashSet<>();
        Set<Edge> res = new HashSet<>(); // 依次挑选的的边在result里

        // 这里是对所有的点都作为出发点，跑了一遍普利姆算法。经典的是会给一个出发点。
        for (Node node : graph.nodes.values()) { // 随便挑一个点作为开始点
            if (!nodeSet.contains(node)) {
                // 3、先解锁点，存入集合。解锁关联的边
                nodeSet.add(node);
                for (Edge edge : node.edges) { // 由一个点，解锁所有相连的边
                    priorityQueue.add(edge);
                }
                // 4、选一条最小的边，且不会形成环
                while (!priorityQueue.isEmpty()) {
                    Edge edge = priorityQueue.poll();
                    Node toNode = edge.to;
                    if (!nodeSet.contains(toNode)) { // 不会形成环，就要这条边。对应的点收集到res
                        nodeSet.add(toNode);
                        res.add(edge);
                        // 5、点再去解锁边
                        for (Edge nextEdge : toNode.edges) {
                            priorityQueue.add(nextEdge);
                        }
                    }
                }
            }
            // 加了break，就是从某个点出发，生成了一次普利姆算法，就退出循环了。
            // break;
        }
        return res;
    }

    /*
    指定了出发点的普利姆算法
     */
    public static Set<Edge> primMSTHaveStart(Node start) {
        // 1、小根堆。存放解锁的边
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());
        // 2、集合，存放解锁的点
        Set<Node> nodeSet = new HashSet<>();
        Set<Edge> res = new HashSet<>(); // 依次挑选的的边在result里
        // 3、出发点，不由分说，进入点集合
        nodeSet.add(start);
        // 4、解锁出发点相关的边
        for (Edge edge : start.edges) { // 由一个点，解锁所有相连的边
            priorityQueue.add(edge);
        }

        // 上面都是初始化阶段，接下来就开始玩循环。每次都堆里弹出最小边，不形成环就要，再去解锁点，再去解锁边
        // 5、选一条最小的边，且不会形成环
        while (!priorityQueue.isEmpty()) {
            Edge edge = priorityQueue.poll();
            Node toNode = edge.to;
            if (!nodeSet.contains(toNode)) { // 不会形成环，就要这条边。对应的点收集到res
                nodeSet.add(toNode);
                res.add(edge);
                // 6、点再去解锁边
                for (Edge nextEdge : toNode.edges) {
                    priorityQueue.add(nextEdge);
                }
            }
        }
        return res;
    }

    public static class EdgeComparator implements Comparator<Edge> {

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight - o2.weight;
        }

    }

    // 请保证graph是连通图
    // graph[i][j]表示点i到点j的距离，如果是系统最大值代表无路
    // 返回值是最小连通图的路径之和
    public static int prim(int[][] graph) {
        int size = graph.length;
        int[] distances = new int[size];
        boolean[] visit = new boolean[size];
        visit[0] = true;
        for (int i = 0; i < size; i++) {
            distances[i] = graph[0][i];
        }
        int sum = 0;
        for (int i = 1; i < size; i++) {
            int minPath = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] < minPath) {
                    minPath = distances[j];
                    minIndex = j;
                }
            }
            if (minIndex == -1) {
                return sum;
            }
            visit[minIndex] = true;
            sum += minPath;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] > graph[minIndex][j]) {
                    distances[j] = graph[minIndex][j];
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("hello world!");
    }

}
