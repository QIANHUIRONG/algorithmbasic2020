// 不要拷贝包信息的内容
package class39;

// 课堂版本
// 本文件是Code02_SnacksWays问题的牛客题目解答
// 但是用的分治的方法
// 这是牛客的测试链接：
// https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交如下的代码，并把主类名改成"Main"
// 可以直接通过



/*
【题意】
背包容量为w
一共有n袋零食, 第i袋零食体积为v[i]
总体积不超过背包容量的情况下，
一共有多少种零食放法？(总体积为0也算一种放法)。
 */
/*
【时间】
题意：46
用经典流程：48
用分治，code：52
整合逻辑：1：11
总结：1：22
 */

/*
【思维导图】
1.用分治：决策无非就是0号零食要跟不要展开，1号零食要跟不要展开，还是从左到右尝试模型，2号零食要跟不要展开，一直展开下去，
2^30,10^8拿不下。所以用分治，左侧15个零食去算，右侧15个零食去算，收集2张表：左侧这15个零食产生的所有可能的累加和的方法数;右侧15个零食产生的所有可能的累加和的方法数
你干出这么一张表，就是2^15 + 2^15肯定在10^8以内

2.
	func(int[] arr, int index, int end, long sum, long bag, TreeMap<Long, Long> map){}
从index出发，到end结束。之前的选择，已经形成的累加和sum，零食[index....end]自由选择，出来的所有累加和，返回累加和不超过bag的方法数，每一种累加和对应的方法数，填在map里
	主流程:还是index位置要和不要两种情况，只不过要收集方法数到map种

4.主函数：
	1、只来自左侧的方法数+只来自右侧的方法数+即来自左侧，又来自右侧
	2、整合逻辑：
	通过右map生成一张前缀和表，右'map，累加和为1的7种，累加和既包括1也包括2的13种，累加和既包括1，也包括2，也包括6的16种，
生成一个类似于这样前缀和的表，但是你的Key只用记住1,2,6就可以了
	3、左侧我累加和1的时候有3种了，对右侧要求是你的累加和必须小于等于7，拿记录16。所以总数是3*16=48种
	4、对我左侧的累加和我要求严格的，生成前缀和的右表去适配左侧.左侧累加和严格为1所适配出来的所有东西都
	和左侧累加和严格为20适配上的所有东西不一样，它可以保证方法不重算

二、分治的总结：

1、10∧8的瓶颈：
如果C或C++给你一秒钟或者其它语言给你2~4秒，你知道自己一定要控制在这个范围内，否则这些肯定会超时
这是一个大限制，于是我们就我们就在众多可以脑补的方案中有了一条主线，
我们就可以根据输入数据的状况，启发自己从什么角度想，或者自己想了一个办法看看，如果超过了10^8就直接杀死，
这里面就要求你具备一定的方法多样性了，所以我给你补了一个技巧就是分治

2、分治的应用场景
往往是当我数据量的总体个数没有太夸张，但是你直接不切成左两半硬去搞每一个位置，每个位置两个分支，
每个位置两个分支的话会超时，那我就很自然的想，我就把我左侧砍一半它去跑暴力，右侧砍一半它去跑暴力
如果整个是30的长度，2八30很大，但是2^15+2^15没多大，这样一来我就可以单独拿下左侧的暴力和右侧的暴力，
不会超时，接下来我只要去硬憋左右两侧的整合逻辑，这事儿就搞定了。

3、题目中其他的值都特别大，除了数组长度n的这个长度它不大，但是整个玩暴力递归又拿不下，因为2^30 > 10^8,
那么就一定是分治，这也坚定了你去搞它整合逻辑的信心。有的时候你写不出来，是你觉得你写不出来，我告诉你一定能写出来，你就憋。
 */



import java.io.*;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Code02_SnacksWaysMain1 {

	public static void main(String[] args) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		StreamTokenizer in = new StreamTokenizer(br);
//		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
//		while (in.nextToken() != StreamTokenizer.TT_EOF) {
//			int n = (int) in.nval;
//			in.nextToken();
//			int bag = (int) in.nval;
//			int[] arr = new int[n];
//			for (int i = 0; i < n; i++) {
//				in.nextToken();
//				arr[i] = (int) in.nval;
//			}
//			long ways = ways(arr, bag);
//			out.println(ways);
//			out.flush();
//		}
		int[] arr = { 4, 3, 2, 9 ,3,4,7,46,89,102,65,23,46,57,8,9,33,27,65,33,88};
		int w = 120;
		System.out.println(ways(arr, w)); // 21130

	}

	public static long ways(int[] arr, int bag) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] <= bag ? 2 : 1;
		}
		int mid = (arr.length - 1) / 2;
		TreeMap<Long, Long> lmap = new TreeMap<>();
		// 只来自左侧的方法数
		long ways = process(arr, 0, 0, mid, bag, lmap);
		TreeMap<Long, Long> rmap = new TreeMap<>();
		// 只来自右侧的方法数，ways一直在累加！至此，ways已经求出了只来自左侧、只来自右侧的方法数了
		ways += process(arr, mid + 1, 0, arr.length - 1, bag, rmap);

		// 右侧前缀和表
		long pre = 0;
		for (Entry<Long, Long> entry : rmap.entrySet()) {
			pre += entry.getValue();
			rmap.put(entry.getKey(), pre);
		}

		// 即来自左侧，又来自右侧
		for (Entry<Long, Long> entry : lmap.entrySet()) {
			long lweight = entry.getKey();
			long lways = entry.getValue();
			Long floor = rmap.floorKey(bag - lweight);
			if (floor != null) { // 没有这个判断，有可能空指针。比如w=8，lweight=7，要到右边找<=1距离最近的，没有！没有这个判断直接rmap.get()就空指针
				long rways = rmap.get(floor);
				ways += lways * rways;
			}
		}
		// 最后+1，表示所有的都不拿
		return ways + 1;
	}


	// 从index出发，到end结束
	// 之前的选择，已经形成的累加和sum
	// 零食[index....end]自由选择，出来的所有累加和，返回累加和不超过bag的方法数，每一种累加和对应的方法数，填在map里
	// 最后不能什么货都没选
	// [3,3,3,3] bag = 6
	// 0 1 2 3
	// - - - -  -> （0 : 1）
	// - - - $  -> （0 : 1）(3, 1)
	// - - $ -  -> （0 : 1）(3, 2)
	public static long process(int[] arr, int index, long sum , int end, int bag, TreeMap<Long, Long> map) {
		if (sum > bag) { // 超过了背包容量，无效方法
			return 0;
		}
		if (index > end) {// 所有商品自由选择完了！
			if (sum != 0) {
				// 收集方法数
				if (!map.containsKey(sum)) {
					map.put(sum , 1L);
				} else {
					map.put(sum , map.get(sum) + 1);
				}
				return 1; // 返回一种有效方法，叫做之前做过的决定
			} else {
				// sum=0表示什么都没选，这其实也是1种方法，但是这里先不算，也不收集到m中，主函数最后会考虑这种情况
				return 0;
			}
		} else {
			// 1) 不要当前index位置的货
			long ways = process(arr, index + 1, sum , end, bag, map);
			// 2) 要当前index位置的货
			ways += process(arr, index + 1, sum  + arr[index], end, bag, map);
			return ways;
		}
	}

}