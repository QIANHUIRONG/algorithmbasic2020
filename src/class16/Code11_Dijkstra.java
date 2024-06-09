package class16;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/*
题意：
算法描述：给定一个出发点A，返回A点到其他所有节点的距离是多少？（如果到不了返回正无穷）

 */
/*
时间：
    迪杰斯特拉算法：1：57。
    流程：2：06
    code：2：15
    这个方法为什么不好？2:23


---加强堆优化：day17
时间：
题意：4
加强堆优化：9
code：15

 */
/*
思维导图：
一、定义，要求：
    1.迪杰斯特拉（Dijkstra）算法是一种用于计算单源最短路径的经典算法。它通常用于图论中的加权图，以找到从起始节点到所有其他节点的最短路径。
    2.经典的迪杰斯特拉算法要求没有负数的边

二、流程:
    1.初始化：将起始节点的距离设为0，其余节点的距离设为无穷大。将所有节点标记为未访问状态。
    2.选择当前节点：从未访问的节点中选择一个距离最小的节点作为当前节点。
    3.更新邻居节点的距离：对于当前节点的每个未访问的邻居节点，如果通过当前节点到该邻居节点的距离小于当前记录的距离，则更新该邻居节点的距离。
    4.标记已访问：将当前节点标记为已访问。
    5.重复：重复步骤2到4，直到所有节点都被访问过或者所有未访问节点的距离都是无穷大。


三、这个方法为什么不好?
    1.这个方法为什么不好？从未访问的节点中选择一个距离最小的节点作为当前节点，是遍历hash表去选择
    2.dijkstra2用加强堆优化
    3.dijkstra3是gpt实现的，我理解应该和dijkstra2是一样好的，但是实现更简单，只需要普通堆

流程:
以下是该算法的基本步骤：

 */
// no negative weight
public class Code11_Dijkstra {

    /*
    未用加强堆优化版本：
     */
    public static HashMap<Node, Integer> dijkstra1(Node start) {
        // 1、距离表。从开始节点出发到每个节点的最短距离，最终返回这个距离表。
        HashMap<Node, Integer> distanceMap = new HashMap<>();
        distanceMap.put(start, 0);
        // 2、已经锁定的点。也就是已经求出答案的点
        HashSet<Node> selectedNodes = new HashSet<>();
        // 3、从距离表中选一个，未被锁定 && 距离最短的点，作为中继点
        Node minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
        while (minNode != null) {
            int distance = distanceMap.get(minNode); //  原始点 到 中继点 此时的最小距离
            // 4、可以解锁这个中继点所有的边，看能不能更短
            for (Edge edge : minNode.edges) {
                Node toNode = edge.to;
                if (!distanceMap.containsKey(toNode)) { // 以前还到不了
                    distanceMap.put(toNode, distance + edge.weight);
                } else { // 以前到得了，选距离短的
                    distanceMap.put(edge.to, Math.min(distanceMap.get(toNode), distance + edge.weight));
                }
            }
            // 5、锁定当前节点，就是当前点已经求出答案了
            selectedNodes.add(minNode);
            // 6、拿到下一个中继点
            minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
        }
        return distanceMap;
    }

    /*
    这个方法现在就是遍历，可以用加强堆优化
     */
    public static Node getMinDistanceAndUnselectedNode(HashMap<Node, Integer> distanceMap, HashSet<Node> selectedNodes) {
        Node minNode = null; // 距离最短的点
        int minDistance = Integer.MAX_VALUE;
        for (Entry<Node, Integer> entry : distanceMap.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            if (!selectedNodes.contains(node) && distance < minDistance) {
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }




    // 改进后的dijkstra算法
    // 从head出发，所有head能到达的节点，生成到达每个节点的最小路径记录并返回
    public static HashMap<Node, Integer> dijkstra2(Node head, int size) {
        NodeHeap nodeHeap = new NodeHeap(size);  // 先生成一个堆，图有几个点，堆大小就搞多大
        nodeHeap.addOrUpdateOrIgnore(head, 0); // 如果节点从来没有生成过，就是add；如果是堆中已经有的，就是update；如果是之前已经访问过的，就是ignore
        HashMap<Node, Integer> result = new HashMap<>();
        while (!nodeHeap.isEmpty()) {
            NodeRecord record = nodeHeap.pop();
            Node cur = record.node;
            int distance = record.distance;
            for (Edge edge : cur.edges) {
                nodeHeap.addOrUpdateOrIgnore(edge.to, edge.weight + distance);
            }
            result.put(cur, distance);
        }
        return result;
    }


    /*
    参考GPT实现的dijkstra
     */
    /*
    时间复杂度：
    1.初始化距离表：需要对每个节点设置初始距离，这需要 O(V) 时间，其中 𝑉 是图中的节点数。
    2.主循环：执行n次，n最差是节点的个数；每次从堆中获取一个node，需要logN，共n*logn
    3.对于每个节点，遍历其所有邻居节点并更新距离。这部分操作的时间复杂度与节点的度数（该节点的邻居数量）成正比。在最坏情况下，
    所有边都可能被检查一次，总共需要O(E)时间，E是图的边数量；每次都要更新堆，logN, 所以这部分：E*logN
    总共：n*logn + E*logN
     */
    public static HashMap<Node, Integer> dijkstra3(Graph graph, Node head) {
        // 1.初始化
        HashMap<Node, Integer> distanceMap = new HashMap<>();
        distanceMap.put(head, 0);
        for (Node node : graph.nodes.values()) { // 初始化，到自己的距离是0，到其他节点都认为是无穷
            distanceMap.put(node, Integer.MAX_VALUE);
        }

        PriorityQueue<NodeRecord> heap = new PriorityQueue<>((o1, o2) -> o1.distance - o2.distance); // 存放当前已经遍历到的节点，封装成NodeRecord，按照距离组织的小根堆
        heap.add(new NodeRecord(head, 0));

        // 2.从未访问的节点中选择一个距离最小的节点作为当前节点
        while (!heap.isEmpty()) {
            NodeRecord nodeRecode = heap.poll();
            Integer distance = distanceMap.get(nodeRecode.node);

            // 更新邻居节点的距离
            for (Edge edge : nodeRecode.node.edges) {
                int newDistance = distance + edge.weight;
                if (newDistance < distanceMap.get(edge.to)) {
                    heap.add(new NodeRecord(edge.to, newDistance));
                }
            }
        }
        return distanceMap;
    }


    public static class NodeHeap {
        private Node[] nodes; // 实际的堆结构
        // key 某一个node， value 上面堆中的位置
        private HashMap<Node, Integer> heapIndexMap;
        // key 某一个节点， value 从源节点出发到该节点的目前最小距离
        private HashMap<Node, Integer> distanceMap;
        private int size; // 堆上有多少个点

        public NodeHeap(int size) {
            nodes = new Node[size];
            heapIndexMap = new HashMap<>();
            distanceMap = new HashMap<>();
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        // 有一个点叫node，现在发现了一个从源节点出发到达node的距离为distance
        // 判断要不要更新，如果需要的话，就更新
        public void addOrUpdateOrIgnore(Node node, int distance) {
            if (inHeap(node)) { // 如果在堆上，就是update
                distanceMap.put(node, Math.min(distanceMap.get(node), distance));
                heapInsert(heapIndexMap.get(node));  // 只需要heapInsert，因为距离只会变小，只需要网上做堆的调整
            }
            if (!isEntered(node)) { // 如果没有进来过，就是新增
                nodes[size] = node; // 先初始化
                heapIndexMap.put(node, size);
                distanceMap.put(node, distance);
                heapInsert(size++); // 玩一次heapInsert
            }
        }

        public NodeRecord pop() {
            NodeRecord nodeRecord = new NodeRecord(nodes[0], distanceMap.get(nodes[0]));
            swap(0, size - 1);
            heapIndexMap.put(nodes[size - 1], -1); // 不删除，值改为-1
            distanceMap.remove(nodes[size - 1]);
            // free C++同学还要把原本堆顶节点析构，对java同学不必
            nodes[size - 1] = null;
            heapify(0, --size);
            return nodeRecord;
        }

        private void heapInsert(int index) {
            while (distanceMap.get(nodes[index]) < distanceMap.get(nodes[(index - 1) / 2])) {
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        private void heapify(int index, int size) {
            int left = index * 2 + 1;
            while (left < size) {
                int smallest = left + 1 < size && distanceMap.get(nodes[left + 1]) < distanceMap.get(nodes[left])
                        ? left + 1
                        : left;
                smallest = distanceMap.get(nodes[smallest]) < distanceMap.get(nodes[index]) ? smallest : index;
                if (smallest == index) {
                    break;
                }
                swap(smallest, index);
                index = smallest;
                left = index * 2 + 1;
            }
        }

        // 是不是进来过。元素弹出了，不会从heapIndexMap remove，而是置为-1，表示这个节点曾经进来过
        private boolean isEntered(Node node) {
            return heapIndexMap.containsKey(node);
        }

        // 是不是还在堆上。必须进来过，并且不是-1，就是还没出去
        private boolean inHeap(Node node) {
            return isEntered(node) && heapIndexMap.get(node) != -1;
        }

        private void swap(int index1, int index2) {
            // 反向索引表要同步换
            heapIndexMap.put(nodes[index1], index2);
            heapIndexMap.put(nodes[index2], index1);
            Node tmp = nodes[index1];
            nodes[index1] = nodes[index2];
            nodes[index2] = tmp;
        }
    }

    public static class NodeRecord {
        public Node node;
        public int distance;

        public NodeRecord(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }


}
