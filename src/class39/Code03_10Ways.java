package class39;

import java.util.LinkedList;


/*
【时间】
卡特兰数定义和通项公式：1：28
有啥用？左右括号各N个组合出来的括号配对是否合法 1：33 （第40节，开头有一段重新证明！参考40节的时间）
括号如何算合法：1：36
怎么定义两个集合是否相等,在数学上叫等势：1：37
算出有多少个不合法的，就能知道有多少合法的：1：43
n个数进栈，有多少种进出栈的方式：2：00
公司股票波动：2：02
一共有n个节点，有多少种组成二叉树的方式：2：03
总结：2：06
所有整数和所有偶数是一样多的：2：09 推荐一个科普节目:希尔伯特的旅馆
5米的点数和10米的点数一样多：2：10



【思维导图】
一、卡特兰数
    1.k(0)=1,k(1)=1时，如果接下来的项满足：
      k(n)=k(0)*k(n-1)+k(1)*k(n-2)+...+k(n-2)*k(1)+k(n-1)*k(0)
      或者
      k(n)=c(2n,n)-c(2n,n-1)
      或者
      k(n)=c(2n,n)/(n+1)
      就说这个表达式，满足卡特兰数
	  卡特兰数前几项是：1，1，2，5，14，132...

     2.集合的等势（希尔伯特的旅馆，抖音李永乐讲的）
     	1.对于无穷大的集合，比较大小是没有意义的。而是比较集合的势
     	2.如果集合A中的一个元素通过映射关系能找到集合B中的一个元素，集合B中的一个元素也能通过映射关系能找到集合A中的一个元素，
     就说集合A和集合B的数量一样多，他俩是等势的。
     	3.一些等势的例子：整合和偶数一样多；直线和平面的点数一样多

     3.左右括号各N个组合出来的括号配对是否合法
     	1.怎么算合法：字符串的任意前缀右括号不能多于左括号，如果你保证每一个前缀右括号都不多于左括号，最后一定合法
     	2.直接算有多少合法比较困难，可以去求有多少不合法的：
     		1.怎么搞出不合法的：任何一个不合法一定存在一个最初最短的前缀，导致右括号数量比左括号多一个
     		2.规定两个集合：A集合：所有不合法的情况。
     		3.A集合中的每个元素，存在最短前缀，导致我右括号比左括号多一个，你总共的情况是N个左括号，N个右括号。
     		你在前缀上是右括号比左括号多一个。你又把后面所有的括号反转了，反转之后所形成的样子是右括号整体会比左括号多两个。
     		这就是B集合：N+1右括号，N-1个左括号所形成的的所有样子。这个映射关系就是A集合->B集合
     		4.规则反过来，B集合也能映射到A集合。所以A，B集合是等势的。所以N个左括号和N个右括号不合法的数量等于N+1右括号和
     		N-1个左括号形成的所有样子。----> c(2n,n-1), 2n个括号，选n-1个做左括号
     		5.合法的括号数量：c(2n,n)-c(2n,n-1)。c(2n,n)个括号，选n个做左括号，这是所有的情况

	4.一些类似的题目：
		1.N个0，N1,自由组合，一共2N个数，任何前缀上0的数量不要比1少，这样的达标结果有多少？
		2.有N个数字要进栈。一定得先进才能出。问你有多少种进出栈的方式？
	解：就是括号问题。进栈是左括号，出栈是右括号，有多种进浅的方式就在任何时候，任何一个前缀上右括号数量不能比左括号要多，
	就是合法的进出站方式，就是卡特兰数

		3.偶数个人排队, 假设找个数时8, 那么一定有8个人排队, 排队的时候排两行, 要求任何一个后面的人都不比前面排队的人要小,
		后面的一定要大于前面的, 一共有几种合理的排法?
	解：8个人，把1，2，3，4编号为0，5，6，7，8编号为1，自由组合这8个数字，合理的排法就是任何一个前缀0的数量>=1的数量。（
	任何一个前缀左括号的数量>=右括号的数量）


		4.一条坐标轴上, 一开始股票的变化在(0,0)点, 股票固定的要么往右上方,斜率45°涨, 要么往右下方斜率45°跌,
	股票的变化趋势可以有非常多种,如果跌到x轴以下叫无效, 如果你是一个可以完全控制股票涨跌的大亨, 问: 完全控制股票波动
	的可能性有多少种?
	解：在任何一个位置，上的次数要>=跌的次数.往上就是左括号往下右括号，就是在问左右括号合理的结合方式。

		5.一共有 N 个无差别节点, 有多少种组成二叉树的方式
			1.0个节点，只能空树：1种方法
			2.1个节点，1种方法
			3.N个节点：头节点1个，左树0个，右树N-1个。总体：0个的方法数*N-1方法数；
					   头节点1个，左树1个，右树N-2个，总体：1个的方法数*n-2方法数
					   ... ---> 卡特兰数通项公式2：k(n)=k(0)*k(n-1)+k(1)*k(n-2)+...+k(n-2)*k(1)+k(n-1)*k(0)

	5.总结：任何前缀上0的数量不少于1这个问题：每年面试中以各种形式出现，很多题目可以转化为这个问题处理
 */
public class Code03_10Ways {

	/**
	 * 方法一：暴力递归
	 * @param N
	 * @return
	 */
	public static long ways1(int N) {
		int zero = N;
		int one = N;
		LinkedList<Integer> path = new LinkedList<>();
		LinkedList<LinkedList<Integer>> ans = new LinkedList<>();
		process(zero, one, path, ans);
		long count = 0;
		for (LinkedList<Integer> cur : ans) {
			int status = 0;
			for (Integer num : cur) {
				if (num == 0) {
					status++;
				} else {
					status--;
				}
				if (status < 0) {
					break;
				}
			}
			if (status == 0) {
				count++;
			}
		}
		return count;
	}

	public static void process(int zero, int one, LinkedList<Integer> path, LinkedList<LinkedList<Integer>> ans) {
		if (zero == 0 && one == 0) {
			LinkedList<Integer> cur = new LinkedList<>();
			for (Integer num : path) {
				cur.add(num);
			}
			ans.add(cur);
		} else {
			if (zero == 0) {
				path.addLast(1);
				process(zero, one - 1, path, ans);
				path.removeLast();
			} else if (one == 0) {
				path.addLast(0);
				process(zero - 1, one, path, ans);
				path.removeLast();
			} else {
				path.addLast(1);
				process(zero, one - 1, path, ans);
				path.removeLast();
				path.addLast(0);
				process(zero - 1, one, path, ans);
				path.removeLast();
			}
		}
	}


	/*
	方法二：卡特兰数通项公式3，k(n) = c(2n,n) / (n+1)
	 */
	public static long ways2(int N) {
		if (N < 0) {
			return 0;
		}
		if (N < 2) {
			return 1;
		}
		long a = 1;
		long b = 1;
		long limit = N * 2;
		for (long i = 1; i <= limit; i++) {
			if (i <= N) {
				a *= i; // 1 * 2 * 3 * ... * n
			} else {
				b *= i; // 1 * (n+1) * (n+2) ... * 2n
			}
		}
		// b / a = C(2n,n)
		return (b / a) / (N + 1);
	}

	public static void main(String[] args) {
		System.out.println("test begin");
		for (int i = 0; i < 10; i++) {
			long ans1 = ways1(i);
			long ans2 = ways2(i);
			if (ans1 != ans2) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");

		System.out.println(ways2(4));
	}

}
