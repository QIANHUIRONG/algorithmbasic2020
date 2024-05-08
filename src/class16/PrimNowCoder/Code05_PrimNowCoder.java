package class16.PrimNowCoder;



// 测试链接 : https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
// 想自己写一个先转换成我们的图接口，再搞普利姆。但是没搞出来。。。先留在这里。
import java.io.*;
import java.util.*;


public class Code05_PrimNowCoder {

    public static int MAXN = 10001;

    public static int MAXM = 100001;

    public static int m;

    public static int n;

    public static int[][] graph = new int[MAXM][3];



    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                graph[i][0] = (int) in.nval;
                in.nextToken();
                graph[i][1] = (int) in.nval;
                in.nextToken();
                graph[i][2] = (int) in.nval;
            }

            // 转换
            Graph graph1 = createGraph(graph);

            // 普利姆算法
            int res = primMSTHaveStart(graph1.nodes.get(1));

            // 写答案
            out.println(res);
            out.flush();
        }
        br.close();
        out.close();
//        int[][] graph = {{1, 3, 3}, {1, 2, 1}, {2, 3, 1}};
//        Graph graph1 = createGraph(graph);
//        System.out.println(primMSTHaveStart(graph1.nodes.get(1)));


    }


    public static Graph createGraph(int[][] matrix) {
        Graph graph = new Graph();
        for (int i = 0; i < matrix.length; i++) {
            // 拿到每一条边， matrix[i]
            int from = matrix[i][0];
            int to = matrix[i][1];
            int weight = matrix[i][2];
            if (!graph.nodes.containsKey(from)) { // 没有这个节点，new一个，放到点集
                graph.nodes.put(from, new Node(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node(to));
            }
            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);
            Edge newEdge = new Edge(weight, fromNode, toNode); // 边
            graph.edges.add(newEdge);

            fromNode.nodes.add(toNode);
            fromNode.out++;
            toNode.in++;
            fromNode.edges.add(newEdge);
        }
        return graph;
    }


    public static int primMSTHaveStart(Node start) {
        // 1、小根堆。存放解锁的边
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());
        // 2、集合，存放解锁的点
        Set<Node> nodeSet = new HashSet<>();
        int res = 0;
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
                res += edge.weight;
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


    public static class Node {
        public int value;
        public int in; // 入度
        public int out; // 出度
        public ArrayList<Node> nodes; // 直接邻居点
        public ArrayList<Edge> edges; // 直接邻居边

        public Node(int value) {
            this.value = value;
            in = 0;
            out = 0;
            nodes = new ArrayList<>();
            edges = new ArrayList<>();
        }
    }

    public static class Graph {
        // 点集。key：用户给的整数；value：对应建出来的Node
        public HashMap<Integer, Node> nodes;
        // 边集
        public HashSet<Edge> edges;

        public Graph() {
            nodes = new HashMap<>();
            edges = new HashSet<>();
        }
    }


    public static class Edge {
        public int weight; // 权重
        public Node from;
        public Node to;

        public Edge(int weight, Node from, Node to) {
            this.weight = weight;
            this.from = from;
            this.to = to;
        }

    }

}




