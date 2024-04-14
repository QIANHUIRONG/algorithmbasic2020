package class16;

import java.util.*;

//undirected graph only
public class Code06_Kruskal {

	/*
	最小生成树算法：1、克鲁斯卡尔算法；2、普利姆算法
	定义：在不影响所有点都连通的情况下，所有边的权值累加最小。
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

	一句话：把所有的边，根据权值由小到大排序，如果当前边不会形成环就要当前边会形成环就不要当前边
	怎么检查有无环：并查集！
		一开始所有的点都是单独的集合，选了那条边，就把边的from和to节点合并到一个集合。
	 */
	public static Set<Edge> kruskalMST(Graph graph) {
		UnionFind unionFind = new UnionFind();
		// 1、初始化并查集。一开始所有的点都是单独的集合
		unionFind.makeSets(graph.nodes.values());

		// 2、所有边入小根堆，每次弹出边最小的
		PriorityQueue<Edge> heap = new PriorityQueue<>(new EdgeComparator());
		for (Edge edge : graph.edges) {
			heap.add(edge);
		}
		Set<Edge> result = new HashSet<>();
		while (!heap.isEmpty()) {
			Edge edge = heap.poll();
			// 3、如果加入这条边不会形成环，就确定要这条边
			if (!unionFind.isSameSet(edge.from, edge.to)) {
				result.add(edge);
				unionFind.union(edge.from, edge.to);
			}
		}
		return result;
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

		private Node findFather(Node n) {
			Stack<Node> path = new Stack<>();
			while(n != fatherMap.get(n)) {
				path.add(n);
				n = fatherMap.get(n);
			}
			while(!path.isEmpty()) {
				fatherMap.put(path.pop(), n);
			}
			return n;
		}

		public boolean isSameSet(Node a, Node b) {
			return findFather(a) == findFather(b);
		}

		public void union(Node a, Node b) {
			if (a == null || b == null) {
				return;
			}
			Node aDai = findFather(a);
			Node bDai = findFather(b);
			if (aDai != bDai) {
				int aSetSize = sizeMap.get(aDai);
				int bSetSize = sizeMap.get(bDai);
				if (aSetSize <= bSetSize) {
					fatherMap.put(aDai, bDai);
					sizeMap.put(bDai, aSetSize + bSetSize);
					sizeMap.remove(aDai);
				} else {
					fatherMap.put(bDai, aDai);
					sizeMap.put(aDai, aSetSize + bSetSize);
					sizeMap.remove(bDai);
				}
			}
		}
	}
	

	public static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			return o1.weight - o2.weight;
		}

	}

}
