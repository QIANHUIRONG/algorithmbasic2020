package class16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
经典拓扑排序。
一定是有向无环图才能做拓扑排序。
找到入度为0的点，把他加到排序结果中去，再删掉这个点和它的影响；再去找入度为0的点

 */
public class Code03_TopologySort {

	// graph：有向无环图
	public static List<Node> sortedTopology(Graph graph) {
		// key 某个节点   value 剩余的入度
		HashMap<Node, Integer> inMap = new HashMap<>();
		// 只有剩余入度为0的点，才进入这个队列
		Queue<Node> zeroInQueue = new LinkedList<>();
		for (Node node : graph.nodes.values()) {
			inMap.put(node, node.in);
			if (node.in == 0) {
				zeroInQueue.add(node);
			}
		}
		List<Node> res = new ArrayList<>();
		while (!zeroInQueue.isEmpty()) {
			// 弹出入度为0的点，加到res去
			Node cur = zeroInQueue.poll();
			res.add(cur);
			// 删除这个点和它的影响
			for (Node next : cur.nexts) {
				inMap.put(next, inMap.get(next) - 1);
				if (inMap.get(next) == 0) {
					zeroInQueue.add(next);
				}
			}
		}
		return res;
	}
}
