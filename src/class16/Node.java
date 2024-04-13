package class16;

import java.util.ArrayList;

// 点结构的描述
// 有时候题目不需要某些属性，就可以省略
public class Node {
	public int value;
	public int in; // 入度
	public int out; // 出度
	public ArrayList<Node> nexts; // 直接邻居点
	public ArrayList<Edge> edges; // 直接邻居边

	public Node(int value) {
		this.value = value;
		in = 0;
		out = 0;
		nexts = new ArrayList<>();
		edges = new ArrayList<>();
	}
}
