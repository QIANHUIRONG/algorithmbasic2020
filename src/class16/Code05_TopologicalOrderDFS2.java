package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code05_TopologicalOrderDFS2 {

	// 不要提交这个类
	public static class DirectedGraphNode {
		public int label;
		public ArrayList<DirectedGraphNode> neighbors;

		public DirectedGraphNode(int x) {
			label = x;
			neighbors = new ArrayList<DirectedGraphNode>();
		}
	}

	// 提交下面的

	/**
	 * 方法二：如果X点它所走过的最大深度大于Y，在拓扑排序中，X排在前面
	 * 所以我就需要算每个点能走过的最大深度
	 * @param graph
	 * @return
	 */
	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> map = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			f(cur, map);
		}
		ArrayList<Record> recordArr = new ArrayList<>();
		for (Record r : map.values()) {
			recordArr.add(r);
		}
		recordArr.sort(new MyComparator());
		ArrayList<DirectedGraphNode> ans = new ArrayList<DirectedGraphNode>();
		for (Record r : recordArr) {
			ans.add(r.node);
		}
		return ans;
	}

	public static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
		if (order.containsKey(cur)) {
			return order.get(cur);
		}
		int follow = 1;
		for (DirectedGraphNode next : cur.neighbors) {
			follow = Math.max(follow, f(next, order).deep); // 这里是最各个邻居的最大深度了，方法一是累加所有的节点
		}
		Record ans = new Record(cur, follow );
		order.put(cur, ans);
		return ans;
	}

	public static class Record {
		public DirectedGraphNode node;
		public int deep; // 这里换成深度了！！

		public Record(DirectedGraphNode n, int o) {
			node = n;
			deep = o;
		}
	}

	public static class MyComparator implements Comparator<Record> {

		@Override
		public int compare(Record o1, Record o2) {
			// 深度由大到小排序
			return o2.deep - o1.deep;
		}
	}

}
