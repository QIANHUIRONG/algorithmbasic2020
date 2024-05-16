package class32;

/*
时间
回顾线段树[12]
index tree解决了什么问题[13]？
一维的arr[]求[l,r]上的累加和怎么算的快？:13
前缀和存在的问题：如果arr[]中的某个值改一下，那么前缀和数组help[]就存在大量更新：18
indexTree解决了什么问题:19
规则：22
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
一、前缀和数组：可以迅速得到arr[L...R]上的累加和（o(1)）。如果你把arr中某个数字改一下，help数组大量存在更新的问题。

二、indexTree：
	1.也是解决arr[L...R]上累加和问题的，并且支持某个位置上的数更新之后，能继续维持求累加和快。
	2.相比于线段树，它不能解决把L...R上的数都变成一个值的情况。但是代码容易得多，并且从一维改多维容易得多

三、规则：结对子

四、规律
	1.假设index二进制形式：010111000，那么它管理的范围[抹掉最后一个1+1，自己],[010110001-010111000]
	2.求原数组1-i位置的前缀和，i=0101100110, 那么前缀和就是help[i] + help[抹掉最后一个1],直到抹成0.

五、复杂度：O(logn)。index有几个位信息就最多做几次操作，而位数就是log(n)

六、你有了前缀和，就可以容易的得到[l...r]上的累加和了

 */
public class Code01_IndexTree {

	// 下标从1开始！
	public static class IndexTree {
		private int[] help;

		public IndexTree(int size) {
			// 0位置弃而不用！
			help = new int[size + 1];
		}

		// index位置上的数字加了d，这时候需要维持好我的help[]数组
		// 初始话的时候，也调用这个方法，相当于原数组是arr[0]=1, 就调用add(0,1), 然后这个方法会把关联的位置都刷成1
		public void add(int index, int d) {
			while (index < help.length) {
				help[index] += d;
				index += index & -index; // 提取出index最右侧的1出来，一直累加
			}
		}

		// 1~index 累加和是多少？
		public int sum(int index) {
			int ans = 0;
			while (index > 0) {
				ans += help[index];
				index -= index & -index; // 提取出index最右侧的1出来，抹掉
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
