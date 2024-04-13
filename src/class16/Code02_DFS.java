package class16;

import java.util.HashSet;
import java.util.Stack;

public class Code02_DFS {

	/*
	DFS——栈
	入栈就打印
	一条路，走到不能再走了才返回
	一条路没走完就走到死，然后走完了就开始逐渐往上看哪个节点还有邻居节点
	要有一个set保证不重复进栈
	 */
	public static void dfs(Node node) {
		if (node == null) {
			return;
		}
		Stack<Node> stack = new Stack<>();
		HashSet<Node> set = new HashSet<>();
		stack.add(node);
		set.add(node);
		System.out.println(node.value); // 入栈就打印
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nexts) {
				if (!set.contains(next)) {
					/*
					当前弹出节点去枚举他的后代，没有进过栈的，先把父压回去，再把后代压回去
					栈里面记录的其实是当前深度优先遍历的路径。因为还得回退看哪个节点还有没走过的邻居节点，继续深度优先遍历。
					 */
					stack.push(cur);
					stack.push(next);
					set.add(next);
					System.out.println(next.value);
					break;
				}
			}
		}
	}
	
	
	

}
