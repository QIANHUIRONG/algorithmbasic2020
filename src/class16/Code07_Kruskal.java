package class16;

import java.util.*;

//undirected graph only
public class Code07_Kruskal {

    /*
    题意：
    克鲁斯卡尔算法

     */
    /*
    时间：1：29
    code：1：37
     */
    /*
      时间复杂度：N*log(N),n是边
     */

    /*
    最小生成树算法：1、克鲁斯卡尔算法；2、普利姆算法
    最小生成树定义：在不影响所有点都连通的情况下，所有边的权值累加最小。
    提到最小生成树一般都是无向图。当然有向图也可以，但是需要给出发点。
     */
	/*
	克鲁斯卡尔算法
	算法描述：
		1)总是从权值最小的边开始考虑，依次考察权值依次变大的边
		2)当前的边要么进入最小生成树的集合，要么丢弃
		3)如果当前的边进入最小生成树的集合中不会形成环，就要当前边
		4)如果当前的边进入最小生成树的集合中会形成环，就不要当前边
		5)考察完所有边之后，最小生成树的集合也得到了

	一句话：把所有的边，根据权值由小到大排序，如果当前边不会形成环就要当前边会形成环就不要当前边。其实就是贪心
	怎么检查有无环：并查集！
		一开始所有的点都是单独的集合，选了那条边，就把边的from和to节点合并到一个集合。
	 */

    // 用了18分钟

    public static Set<Edge> kruskalMST(Graph graph) {
        UnionFind unionFind = new UnionFind();
        // 1、初始化并查集。一开始所有的点都是单独的集合
        unionFind.makeSets(graph.nodes.values());

        // 2、所有边入小根堆，每次弹出边最小的。
        // 其实这里放到数组中，排序一下就行
        PriorityQueue<Edge> heap = new PriorityQueue<>(new EdgeComparator());
        for (Edge edge : graph.edges) { // n*log(n)，n是边的数量
            heap.add(edge);
        }
        Set<Edge> ans = new HashSet<>();
        while (!heap.isEmpty()) {
            Edge edge = heap.poll(); // log(n)
            // 3、如果加入这条边不会形成环，就确定要这条边
            if (!unionFind.isSameSet(edge.from, edge.to)) { // o(1)
                ans.add(edge);
                unionFind.union(edge.from, edge.to);
            }
        }
        return ans;
    }


    // Union-Find Set
    public static class UnionFind {
        // key 某一个节点， value key节点往上的节点
        private HashMap<Node, Node> fatherMap;
        // key 某一个集合的代表节点, value key所在集合的节点个数
        private HashMap<Node, Integer> sizeMap;

        public UnionFind() {
            fatherMap = new HashMap<Node, Node>();
            sizeMap = new HashMap<Node, Integer>();
        }

        public void makeSets(Collection<Node> nodes) {
            fatherMap.clear();
            sizeMap.clear();
            for (Node node : nodes) {
                fatherMap.put(node, node);
                sizeMap.put(node, 1);
            }
        }

        public boolean isSameSet(Node x, Node y) {
            return findFather(x) == findFather(y);
        }

        public void union(Node x, Node y) {
            if (x == null || y == null) {
                return;
            }
            Node xFather = findFather(x);
            Node yFather = findFather(y);
            if (xFather != yFather) {
                int xSize = sizeMap.get(xFather);
                int ySize = sizeMap.get(yFather);
                if (xSize <= ySize) {
                    fatherMap.put(xFather, yFather);
                    sizeMap.put(yFather, xSize + ySize);
                    sizeMap.remove(xFather);
                } else {
                    fatherMap.put(yFather, xFather);
                    sizeMap.put(xFather, xSize + ySize);
                    sizeMap.remove(yFather);
                }
            }
        }

        private Node findFather(Node x) {
            Stack<Node> path = new Stack<>();
            while (x != fatherMap.get(x)) {
                path.add(x);
                x = fatherMap.get(x);
            }
            while (!path.isEmpty()) {
                fatherMap.put(path.pop(), x);
            }
            return x;
        }
    }


    public static class EdgeComparator implements Comparator<Edge> {

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight - o2.weight;
        }

    }

}
