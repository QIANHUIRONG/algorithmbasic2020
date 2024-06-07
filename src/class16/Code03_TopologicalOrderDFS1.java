package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code03_TopologicalOrderDFS1 {

	// 不要提交这个类
	public static class DirectedGraphNode {
		public int label;
		public ArrayList<DirectedGraphNode> neighbors;

		public DirectedGraphNode(int x) {
			label = x;
			neighbors = new ArrayList<DirectedGraphNode>();
		}

		@Override
		public String toString() {
			return "DirectedGraphNode{" +
					"label=" + label +
					", neighbors=" + neighbors +
					'}';
		}
	}

	// 提交下面的
	public static class Record {
		public DirectedGraphNode node;
		public int deep;

		public Record(DirectedGraphNode n, int o) {
			node = n;
			deep = o;
		}

		@Override
		public String toString() {
			return "Record{" +
					"node=" + node +
					", deep=" + deep +
					'}';
		}
	}

	public static class MyComparator implements Comparator<Record> {

		@Override
		public int compare(Record o1, Record o2) {
			return o2.deep - o1.deep;
		}
	}

	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> order = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			f(cur, order);
		}
		ArrayList<Record> recordArr = new ArrayList<>();
		for (Record r : order.values()) {
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
		int follow = 0;
		for (DirectedGraphNode next : cur.neighbors) {
			follow = Math.max(follow, f(next, order).deep);
		}
		Record ans = new Record(cur, follow + 1);
		order.put(cur, ans);
		return ans;
	}

	public static void main(String[] args) {
		DirectedGraphNode cur1 = new DirectedGraphNode(1);
		DirectedGraphNode cur2 = new DirectedGraphNode(2);
		DirectedGraphNode cur3 = new DirectedGraphNode(3);
		DirectedGraphNode cur4 = new DirectedGraphNode(4);

		cur1.neighbors.add(cur2);
		cur1.neighbors.add(cur3);
		cur2.neighbors.add(cur4);

		// cur1->cur2->cur4
		// cur1->cur3
		HashMap<DirectedGraphNode, Record> map = new HashMap<>();
		f(cur1, map);
		for (Record value : map.values()) {
			System.out.println(value.node.label + ":" + value.deep);
		}
	}

}
