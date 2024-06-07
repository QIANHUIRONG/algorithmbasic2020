package class16;

import java.util.HashMap;
import java.util.HashSet;

/*
数据结构：图
定义：
	1)由点的集合和边的集合构成
	2)虽然存在有向图和无向图的概念，但实际上都何以用有向图来表达
	3)边上可能带有权值
	4）向图跟无向图没有明显界限，无向图就是两个点双方都有有向边的有向图。可以认为就只有有向图

图的一些表示方法：
	教学课本里的邻接表法、邻接矩阵法。但是笔试面试很少出现这种结构
	笔试面试常见的: [[3,0,7],[5,1,2]],表示3节点到0节点有一条边，权重7

图的题目的难点：
	不是算法难，而是图的表示方式有多种，你需要在不同的表示结构上都练一遍图的常见算法
	所以我们要自己抽象出自己的图结构，遇到其他的图结构，写个转换器转换成自己的图结构
	这里的统一结构：就是Node点对象、Edge边对象、Node和Edge构成的Graph图对象。这种表示形式面向对象很强。
 */
public class Graph {
    // 点集。key：用户给的整数；value：对应建出来的Node
    public HashMap<Integer, Node> nodes;
    // 边集
    public HashSet<Edge> edges;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }
}
