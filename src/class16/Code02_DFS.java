package class16;

import java.util.HashSet;
import java.util.Stack;

public class Code02_DFS {

	/*
	题意：
		图的深度优先遍历
	 */
	/*
	 时间：42
	 */
	/*
	DFS——栈
	入栈就打印。一条路没走完就走到死，然后走完了就开始逐渐往上看哪个节点还有邻居节点
	要有一个set保证不重复进栈
	 */
	public static void dfs(Node node) {
		if (node == null) {
			return;
		}
		Stack<Node> stack = new Stack<>(); // 深度优先遍历需要的栈
		HashSet<Node> set = new HashSet<>(); // 图的遍历需要得到集合
		stack.add(node);
		set.add(node);
		System.out.println(node.value); // 入栈就打印！
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nodes) {
				if (!set.contains(next)) {
					/*
					当前弹出节点去枚举他的后代，没有进过栈的，先把父压回去，再把后代压回去。
					栈里面记录的其实是当前深度优先遍历的路径。因为还得回退看哪个节点还有没走过的邻居节点，继续深度优先遍历。
					 */
					stack.push(cur);
					stack.push(next);
					set.add(next);
					System.out.println(next.value);
					// 有一个不在set中，就跳出这个循环了！去回退继续深度优先遍历，如果继续循环，就是广度优先遍历的那种感觉了
					break;
				}
			}
		}
	}
	
	
	

}
