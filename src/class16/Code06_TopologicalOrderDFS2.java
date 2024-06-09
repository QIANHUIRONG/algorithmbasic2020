package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;



/*
题意：
    给定一个有向图，图节点的拓扑排序定义如下:
    对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
    拓扑排序中的第一个节点可以是图中的任何一个没有其他节点指向它的节点.
    针对给定的有向图找到任意一种拓扑排序的顺序.
    OJ链接：https://www.lintcode.com/problem/topological-sorting
 */

/*
时间：
 */
/*
思维导图
1.  方法二：如果X点它所走过的最大深度大于Y，在拓扑排序中，X排在前面，所以我就需要算每个点能走过的最大深度

2. process(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> map) {}:当前来到cur节点，请返回cur点最深的节点
3.basecase：如果cur没有邻居了，就返回1个深度
4.普遍流程：当前节点求所有邻居节点的深度的最大值，+1，加上我的深度


 */
public class Code06_TopologicalOrderDFS2 {

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

	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> map = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			process(cur, map);
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

	public static Record process(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> map) {
		if (map.containsKey(cur)) {
			return map.get(cur);
		}
		if (cur.neighbors.isEmpty()) {
			Record ans = new Record(cur, 1);
			map.put(cur, ans);
			return ans;
		} else {
			int follow = 0;
			for (DirectedGraphNode next : cur.neighbors) {
				follow = Math.max(follow, process(next, map).deep); // 这里是最各个邻居的最大深度了，方法一是累加所有的节点
			}
			Record ans = new Record(cur, follow + 1);
			map.put(cur, ans);
			return ans;
		}
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
		process(cur1, map);
		for (Record value : map.values()) {
			System.out.println(value.node.label + ":" + value.deep);
		}
	}

}
