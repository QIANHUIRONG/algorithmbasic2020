package class14;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

// 课上讲的并查集实现
// 请务必看补充的Code06_UnionFind
// 那是数组实现的并查集，并且有测试链接
// 可以直接通过
// 这个文件的并查集是用map实现的
// 但是笔试或者平时用的并查集一律用数组实现
// 所以Code06_UnionFind更具实战意义
// 一定要看！


/*
数据结构：并查集
定义：
	1.有若干个样本a、b、c、d.类型假设是V
	2.在并查集中开始认为每个样本都在单独的集合里
	3.用户可以在任何时候调用如下两个方法：
		boolean isSameSet(Vx,Vy):查询样本x和样本y是否属于个集合
		void union(Vx,Vy):把x和y各自所在集合的所有样本合并成一个集合
	4.isSameSet和union方法的代价成低诚好

时间复杂度：单次均难调用代价O(1)

连通性问题：想到并查集

简单讲解：
	1、一开始每个元素都认为是独立的集合，每个元素的代表节点都是自己
	2、判断2个元素是不是1个集合：2个元素都去找自己的代表节点，比较代表节点是不是1个
	3、合并2个元素：各自找到代表节点，比较小的集合代表节点挂到大的集合下，表示合并


 */
public class Code05_UnionFind {

	// 课上讲的时候
	// 包了一层
	// 其实不用包一层哦
	public static class UnionFind<V> {
		public HashMap<V, V> father; // key：某个节点，value：它的父节点。注意这里不是代表节点
		public HashMap<V, Integer> size;// key: 代表节点；value：集合大小

		public UnionFind(List<V> values) {
			father = new HashMap<>();
			size = new HashMap<>();
			for (V cur : values) {
				father.put(cur, cur);// 初始时，每个节点的代表节点都是自己
				size.put(cur, 1);// 每个代表节点的size都是1
			}
		}

		public boolean isSameSet(V a, V b) {
			return findFather(a) == findFather(b); // 看2个节点的代表节点是不是同一个
		}

		public void union(V a, V b) {
			// 1、找到各自的代表节点
			V aFather = findFather(a);
			V bFather = findFather(b);
			if (aFather != bFather) {
				int aSize = size.get(aFather);
				int bSize = size.get(bFather);
				// 2、小挂大优化。这里也是一个大优化，可以使得合并完链长度短一点
				if (aSize >= bSize) { // b小，b挂a
					father.put(bFather, aFather); // b的父节点变成a，就表示a挂a
					size.put(aFather, aSize + bSize); // 重新计算a的size
					size.remove(bFather); // b不是代表节点了，移除size
				} else {
					father.put(aFather, bFather);
					size.put(bFather, aSize + bSize);
					size.remove(aFather);
				}
			}
		}

		// 找到代表节点返回。给你一个节点，请你往上到不能再往上，把代表节点返回
		/*
		这里会做链的扁平化优化
		从某个节点一直往上找到代表节点x，记录沿途经过的节点，最后把沿途节点的父亲节点都设置为x
		这里是一个大优化，单次看起来可能慢，但是痛就痛1次，调用频繁之后，均摊下来O（1）
		 */
		public V findFather(V cur) {
			Stack<V> path = new Stack<>(); // 收集沿途的节点
			while (cur != father.get(cur)) {
				path.push(cur);
				cur = father.get(cur);
			}
			while (!path.isEmpty()) {
				father.put(path.pop(), cur); // 沿途节点的父节点都变成代表节点
			}
			return cur;
		}

		public int sets() {
			return size.size();
		}

	}
}
