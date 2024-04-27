package class14;

// 这个文件课上没有讲
// 原理和课上讲的完全一样
// 最大的区别就是这个文件实现的并查集是用数组结构，而不是map结构
// 请务必理解这个文件的实现，而且还提供了测试链接
// 提交如下的code，并把"Code06_UnionFind"这个类名改成"Main"
// 在测试链接里可以直接通过
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 测试链接 : https://www.nowcoder.com/questionTerminal/e7ed657974934a30b2010046536a5372


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code06_UnionFind {

	public static int MAXN = 1000001;
	public static int[] father = new int[MAXN];// father[i] = k, 表示i的父节点是k。
	public static int[] size = new int[MAXN]; // size[i]=k, 如果i是代表节点，那么i所在集合大小是k；如果不是代表节点，无意义。
	public static int setCount; // 一共有多少个集合

	// 初始化并查集
	public static void init(int n) {
		for (int i = 0; i <= n; i++) { // 集合初始元素是[0,n]
			father[i] = i; // 初始化时，i的父节点就是i
			size[i] = 1; // 初始化时，都是代表节点，size都是1
		}
		setCount = n; // 初始化时，每一个元素都是一个独立的集合
	}

	// 查询x和y是不是一个集合
	public static boolean isSameSet(int x, int y) {
		return find(x) == find(y);
	}

	// x所在的集合，和y所在的集合，合并成一个集合
	public static void union(int x, int y) {
		// 1、找到各自的代表节点
		int fx = find(x);
		int fy = find(y);
		if (fx != fy) {
			// 2、小挂大优化。这里也是一个大优化，可以使得合并完链长度短一点
			if (size[fx] >= size[fy]) { // y小，y挂x
				father[fy] = fx; // y的父亲变成a，表示y挂a
				size[fx] += size[fy]; // 累加y的size到x上
			} else {
				father[fx] = fy;
				size[fy] += size[fx];
			}
			setCount--; // 产生一次集合的合并，setCount--
		}
	}

	// 返回并查集有几个集合
	public int setCount() {
		return setCount;
	}

	// 从i开始寻找集合代表点
	/*
	这里会做链的扁平化优化
	从某个节点一直往上找到代表节点x，记录沿途经过的节点，最后把沿途节点的父亲节点都设置为x
	这里是一个大优化，单次看起来可能慢，但是痛就痛1次，调用频繁之后，均摊下来O（1）
	 */
	public static int find(int i) {
		int[] help = new int[MAXN];// 充当原本栈的结构
		int j = -1; // 这个变量用来表示沿途的长度，不然不知道help[]数组什么时候遍历完
		// i不等于自己的父亲，就一直往上。什么时候i和自己的父亲是相等，就是i到了代表节点的时候
		while (father[i] != i) {
			help[++j] = i;
			i = father[i]; // 我来到我的父
		}
		for (int k = 0; k < j; k++) {
			father[help[k]] = i;
		}
		return i;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			int n = (int) in.nval;
			init(n);
			in.nextToken();
			int m = (int) in.nval;
			for (int i = 0; i < m; i++) {
				in.nextToken();
				int op = (int) in.nval;
				in.nextToken();
				int x = (int) in.nval;
				in.nextToken();
				int y = (int) in.nval;
				if (op == 1) {
					out.println(isSameSet(x, y) ? "Yes" : "No");
					out.flush();
				} else {
					union(x, y);
				}
			}
		}
	}
}
