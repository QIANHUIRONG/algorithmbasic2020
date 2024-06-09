package class16;

/**
 * @author QIANHUIRONG
 * @date 2024-06-09 22:10
 */
import java.util.*;

public class  Code13_Dijkstra_GPT {
    // 定义图节点
    static class Node implements Comparable<Node> {
        int id;
        int distance;

        Node(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        // 按距离排序
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    // 迪杰斯特拉算法
    public static Map<Integer, Integer> dijkstra(Map<Integer, List<Node>> graph, int start) {
        // 初始化距离表
        Map<Integer, Integer> distances = new HashMap<>();
        for (int node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        // 优先队列
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Node(start, 0));

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            int currentDistance = currentNode.distance;

            // 遍历当前节点的邻居
            for (Node neighbor : graph.get(currentNode.id)) {
                int newDist = currentDistance + neighbor.distance;
                if (newDist < distances.get(neighbor.id)) {
                    distances.put(neighbor.id, newDist);
                    priorityQueue.add(new Node(neighbor.id, newDist));
                }
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        // 示例图（邻接表表示）
        Map<Integer, List<Node>> graph = new HashMap<>();
        graph.put(1, Arrays.asList(new Node(2, 1), new Node(3, 4)));
        graph.put(2, Arrays.asList(new Node(1, 1), new Node(3, 2), new Node(4, 5)));
        graph.put(3, Arrays.asList(new Node(1, 4), new Node(2, 2), new Node(4, 1)));
        graph.put(4, Arrays.asList(new Node(2, 5), new Node(3, 1)));

        // 计算从节点1到其他节点的最短路径
        Map<Integer, Integer> distances = dijkstra(graph, 1);

        // 输出结果
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            System.out.println("Distance from node 1 to node " + entry.getKey() + " is " + entry.getValue());
        }
    }
}

