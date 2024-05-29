package class07;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;


/*
 [题意]
给定很多线段，每个线段都有两个数组[start, end]，表示线段开始位置和结束位置，左右都是闭区间
规定：
1）线段的开始和结束位置一定都是整数值
2）线段重合区域的长度必须>=1
返回线段最多重合区域中，包含了几条线段
*/

/*
[时间]

code:55
复杂度：59
 */

// 时复：
// 空复：

/*
[思维导图]
一、暴力法
	1.遍历一遍，求出最大值最小值，所有线段都在这个范围
	2.枚举每一个0.5，看每一个.5包含了几个线段，答案一定必在其中
	3.复杂度：o((max-min) * n)。如果笔试的时候，max=100,min=100,n=10^4,那么这已经能通过了


二、最优解
	1.大流程：依次求出必须以每一条线段左边界做开始位置，假设它是重合区域的左边界，求重合线段树
	2.具体流程
	先根据线段开始位置由小到大排序
	当把线段开始位置由小到大拍完序之后，依次考察每一个线段，遍历到任何一个线段时，在小根堆里把所有<=开始位置的值都弹出
	然后把此时结束位置加入小根堆，此时小跟堆里有几个数就是这线段的答案
	所有线段的答案都求出来，最大的那个就是最大线段重合数
	3.例子：
		第一个数[1,7],<=1的全部弹出，结尾放入小根堆[7]，小根堆有几个数就是重合区域必须以当前线段的1做左边界的话的答案:1
		第二个数[2,3],<=2的全部弹出，结尾放入小根堆[7,3], 小根堆有几个数就是答案：2
		第三个数[4,6],<=4的弹出，结尾放入小根堆[7,6,3], 小根堆有几个数就是答案：3
		第四个数[5,8], <=5的弹出，结尾放入小根堆[8,7,6], 小根堆有几个数就是答案：3 --> 此时3已经出去了，因为3这个结尾穿不过去我现在的开头5

	4.流程为什么对？
		小根堆里面是之前出现的线段的结尾，如果当前数是[10, 17], 小根堆里面是[8,9,11,13,19], 因为是按照开始位置由小到大排序的，堆中的这些
	线段开始位置都是<=10, 如果结尾位置>10,就是能穿过10位置的线段了。

	5.时复：
		1.排序n*logN
		2.for循环，每个线段会考察1次，一共N次，每个线段结尾最多进1次小根堆，最多出1次小根堆，小根堆的代价logN, 所以一共n*logN
 */
public class Code01_CoverMax {

	/**
	 * 暴力法，用作对数器
	 * @param lines
	 * @return
	 */
	public static int maxCover1(int[][] lines) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < lines.length; i++) {
			min = Math.min(min, lines[i][0]);
			max = Math.max(max, lines[i][1]);
		}
		int cover = 0;
		for (double p = min + 0.5; p < max; p += 1) {
			int cur = 0;
			for (int i = 0; i < lines.length; i++) {
				if (lines[i][0] < p && lines[i][1] > p) {
					cur++;
				}
			}
			cover = Math.max(cover, cur);
		}
		return cover;
	}

	public static int maxCover2(int[][] m) {
		Line[] lines = new Line[m.length];
		for (int i = 0; i < m.length; i++) {
			lines[i] = new Line(m[i][0], m[i][1]);
		}
		// 按照线段开始位置排序
		Arrays.sort(lines, new StartComparator());
		// 小根堆，每一条线段的结尾数值，默认就是小根堆
		PriorityQueue<Integer> heap = new PriorityQueue<>();

		// 遍历数组，求以每一个线段开始位置作为重合区域左边界的答案
		int ans = 0;
		for (int i = 0; i < lines.length; i++) {
			// 把小根堆中结尾位置小于我开始位置的都弹出
			while (!heap.isEmpty() && heap.peek() <= lines[i].start) {
				heap.poll();
			}
			heap.add(lines[i].end);
			ans = Math.max(ans, heap.size());
		}
		return ans;
	}

	public static class Line {
		public int start;
		public int end;

		public Line(int s, int e) {
			start = s;
			end = e;
		}
	}

	public static class EndComparator implements Comparator<Line> {

		@Override
		public int compare(Line o1, Line o2) {
			return o1.end - o2.end;
		}

	}

	// 和maxCover2过程是一样的
	// 只是代码更短
	// 不使用类定义的写法
	public static int maxCover3(int[][] m) {
		// m是二维数组，可以认为m内部是一个一个的一维数组
		// 每一个一维数组就是一个对象，也就是线段
		// 如下的code，就是根据每一个线段的开始位置排序
		// 比如, m = { {5,7}, {1,4}, {2,6} } 跑完如下的code之后变成：{ {1,4}, {2,6}, {5,7} }
		Arrays.sort(m, (a, b) -> (a[0] - b[0]));
		// 准备好小根堆，和课堂的说法一样
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int max = 0;
		for (int[] line : m) {
			while (!heap.isEmpty() && heap.peek() <= line[0]) {
				heap.poll();
			}
			heap.add(line[1]);
			max = Math.max(max, heap.size());
		}
		return max;
	}

	// for test
	public static int[][] generateLines(int N, int L, int R) {
		int size = (int) (Math.random() * N) + 1;
		int[][] ans = new int[size][2];
		for (int i = 0; i < size; i++) {
			int a = L + (int) (Math.random() * (R - L + 1));
			int b = L + (int) (Math.random() * (R - L + 1));
			if (a == b) {
				b = a + 1;
			}
			ans[i][0] = Math.min(a, b);
			ans[i][1] = Math.max(a, b);
		}
		return ans;
	}

	public static class StartComparator implements Comparator<Line> {

		@Override
		public int compare(Line o1, Line o2) {
			return o1.start - o2.start;
		}

	}

	public static void main(String[] args) {

		Line l1 = new Line(4, 9);
		Line l2 = new Line(1, 4);
		Line l3 = new Line(7, 15);
		Line l4 = new Line(2, 4);
		Line l5 = new Line(4, 6);
		Line l6 = new Line(3, 7);

		// 底层堆结构，heap
		PriorityQueue<Line> heap = new PriorityQueue<>(new StartComparator());
		heap.add(l1);
		heap.add(l2);
		heap.add(l3);
		heap.add(l4);
		heap.add(l5);
		heap.add(l6);

		while (!heap.isEmpty()) {
			Line cur = heap.poll();
			System.out.println(cur.start + "," + cur.end);
		}

		System.out.println("test begin");
		int N = 100;
		int L = 0;
		int R = 200;
		int testTimes = 200000;
		for (int i = 0; i < testTimes; i++) {
			int[][] lines = generateLines(N, L, R);
			int ans1 = maxCover1(lines);
			int ans2 = maxCover2(lines);
			int ans3 = maxCover3(lines);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test end");
	}

}
