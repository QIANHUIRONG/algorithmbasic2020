package class32;

/*
题意：实现Index Tree
 */
/*
时间
回顾线段树[12]
index tree解决了什么问题[13]？
一维的arr[]求[l,r]上的累加和怎么算的快？:13
前缀和存在的问题：如果arr[]中的某个值改一下，那么前缀和数组help[]就存在大量更新：18
indexTree解决了什么问题:19
流程：22
规律：
	每个位置管的范围：31
	想求1-i的前缀和：42
	求前缀和为什么对：48
code：51
	add(int index, int d)：54

复杂度： logn  1：09

 */

/*
【导图笔记】
一、前提引入
	1.回顾线段树：解决区间上统一加一个值，或者统一更新更一个值，或者求区间上的累加和，做到log(n)
	2.index tree解决了什么问题：也是解决累加和问题。一般来说，我们求arr[l..r]上的累加和，可以用前缀和数组。先生成前缀和数组sum，然后sum[l...r] = sum[r] - sum[l - 1]
	但是如果arr中的某个值修改了，那么这个前缀和数组会存在大量更新的问题。
	所以我们需要一个结构，能够支持更新update(i, v)方法，还能求范围上的累加和很快-> index tree
	4.注意index tree只能做到单点更新，不能像线段树那样做到把一个范围上的数都更新。那么index tree相比于线段树的优势是：代码简单；改多维好改

二、index tree 流程
	1.下标从1开始，0位置弃而不用
	2.凑对子：
		1位置管1
		2位置管1，2
		3位置管3
		4位置管1，2，3，4
		5位置管5
		6位置管5，6
		7位置管7
		8位置管1，2，3，4，5，6，7，8
		....

	抽象化：i位置，二进制形式假设010111000，那么这个i位置管的范围：010110001（去掉最后一个1，再+1） - 010111000（自己）
	比如8位置管1，2，3，4，5，6，7，8；
	8的二进制：1000，那么它管的范围：0001-1000，就是1-8

	3.求1-i位置的累加和
	i假设33，二进制形式：100001，
	累加和 = help[100001] + help[10000(抹掉最后一个1)] = help[33] + help[32], 33位置只管自己，32位置管1-32，就是对的

	再举个长的例子：
	i = 10011；sum[1-i] = help[10011] + help[10010] + help[10000],每次抹掉1个1，直到抹成0

	求前缀和为什么对：因为每个位置管的范围是那么限定的，就有这种规律

	4.add(int index, int d) {}， index位置加了一个d，那么help数组中哪些位置会受到牵连呢？
		1位置管1
		2位置管1，2
		3位置管3
		4位置管1，2，3，4
		5位置管5
		6位置管5，6
		7位置管7
		8位置管1，2，3，4，5，6，7，8
		比如add(3,5),3位置加了一个5，那么3位置，4位置，8位置，16位置。。。都需要更新
		什么规律？3的二进制：11，
		把最右侧的1再加一遍：100->4
		把最右侧的1再加一遍：1000->8
		把最右侧的1再加一遍：10000->16
		。。。
		这些位置都统一给我加一个d，这些位置都需要变

三、总结：
	1.add(index, d)：不断提取最右侧的1，累加到index，这些index都需要加上d
	2.sum(index)：不断提取最右侧1，抹掉，这些index都需要拿出help的值累加
	3.求sum[l,r] = sum[r] - sum[l-1]

四、时间复杂度：每次都和二进制形式的1玩，一个数字n，它的二进制形式最多有logn个1，所以时复o(logn)
 */
public class Code01_IndexTree {

	// 0位置弃而不用！下标从1开始！
	public static class IndexTree {
		private int[] help;

		public IndexTree(int size) {
			help = new int[size + 1];
		}

		// index位置上的数字加了d，这时候需要维持好我的help[]数组
		// 初始话的时候，也调用这个方法，相当于原数组是arr[0]=1, 就调用add(0,1), 然后这个方法会把关联的位置都刷成1
		public void add(int index, int d) {
			while (index < help.length) {
				help[index] += d; // 先把我自己位置加d
				index += index & -index; // 提取出index最右侧的1出来，一直累加，这些位置都要加d
			}
		}

		// 1~index 累加和是多少？
		public int sum(int index) {
			int ans = 0;
			while (index > 0) {  // 不能取到0位置
				ans += help[index]; // 先累加上当前位置
				index -= index & -index; // 提取出index最右侧的1出来，抹掉，再累加
			}
			return ans;
		}
	}


	// 对数器
	public static class Right {
		private int[] nums;
		private int N;

		public Right(int size) {
			N = size + 1;
			nums = new int[N + 1];
		}

		public int sum(int index) {
			int ret = 0;
			for (int i = 1; i <= index; i++) {
				ret += nums[i];
			}
			return ret;
		}

		public void add(int index, int d) {
			nums[index] += d;
		}

	}

	public static void main(String[] args) {
		int N = 100;
		int V = 100;
		int testTime = 2000000;
		IndexTree tree = new IndexTree(N);
		Right test = new Right(N);
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int index = (int) (Math.random() * N) + 1;
			if (Math.random() <= 0.5) {
				int add = (int) (Math.random() * V);
				tree.add(index, add);
				test.add(index, add);
			} else {
				if (tree.sum(index) != test.sum(index)) {
					System.out.println("Oops!");
				}
			}
		}
		System.out.println("test finish");
	}

}
