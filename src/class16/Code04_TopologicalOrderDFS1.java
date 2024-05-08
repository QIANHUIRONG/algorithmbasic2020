package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

// 时间：1：06
// OJ链接：https://www.lintcode.com/problem/topological-sorting
/*
题意：
描述
给定一个有向图，图节点的拓扑排序定义如下:
对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
拓扑排序中的第一个节点可以是图中的任何一个没有其他节点指向它的节点.
针对给定的有向图找到任意一种拓扑排序的顺序.
 */
public class Code04_TopologicalOrderDFS1 {

    // 不要提交这个类
    // 本题的图结构。当前这个节点的value，当前这个节点的邻居节点。可以理解成邻接表法
    public static class DirectedGraphNode {
        public int label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }

    // 提交下面的
	/*
	主方法
	从X出发，能走到的所有节点都算上
	从y出发，能走到的所有节点都算上
	如果x能走到的点数大于Y，那么在拓扑序中x排在y之前。

	现在我要统计每个点能走到的节点，这里就可能会有大量重复计算的问题，如果算过了，放到缓存中去（对动态规划的提前预热）

	 */
	/*
	为什么这里没有写转换类：如果是新手，可以写转换器，转换成我们熟悉的结构去玩；
	如果是老手，就来啥结构玩啥结构，这里也是对这种方式进行练习
	 */
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Record> map = new HashMap<>();
        for (DirectedGraphNode cur : graph) {
            process(cur, map); // 生成所有点的点次表
        }
        ArrayList<Record> recordArr = new ArrayList<>();
        for (Record r : map.values()) {
            recordArr.add(r);
        }
        recordArr.sort(new MyComparator()); // 根据点次由大到小排序
        ArrayList<DirectedGraphNode> ans = new ArrayList<DirectedGraphNode>();
        for (Record r : recordArr) {
            ans.add(r.node);
        }
        return ans;
    }

    // 当前来到cur节点，请返回cur点所到之处，所有的点次！
    // 	map是一个缓存
    //  key : 某一个点的点次，之前算过了，那么node就会存在key上，value就是它的点次
    //  value : 点次是多少
    // 为什么这里返回值要封装Record对象？单从递归的角度，入参我给你传当前节点，出参你给我返回能走过的节点数。
    // 不过在主函数中，最终需要根据节点的点次排序，需要放到list中，排序之后我需要知道每个点次对应的节点是什么，所以这里需要封装成Record对象返回
    public static Record process(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> map) {
        if (map.containsKey(cur)) { // 算过了直接返回
            return map.get(cur);
        }
        // cur的点次之前没算过！
        long nodes = 1;
        for (DirectedGraphNode next : cur.neighbors) {
            nodes += process(next, map).nodes; // 累加上所有直接邻居的点次，就是我的点次
        }
        Record ans = new Record(cur, nodes);
        map.put(cur, ans); // 放到缓存中
        return ans;
    }

    public static class Record {
        public DirectedGraphNode node;
        public long nodes; // 点次。时间：1：17

        public Record(DirectedGraphNode n, long o) {
            node = n;
            nodes = o;
        }
    }

    public static class MyComparator implements Comparator<Record> {

        @Override
        public int compare(Record o1, Record o2) {
            // 因为nodes是long类型，这里就没写成o2.nodes-o1.nodes
            // 试了下把long nodes改成int，提交过不去
            return o1.nodes == o2.nodes ? 0 : (o1.nodes > o2.nodes ? -1 : 1);
        }
    }

}
