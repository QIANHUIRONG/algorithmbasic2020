package class06;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
 [题意]
堆结构
*/

/*
[时间]

 */

// 时复：heapInsert()和heapify()都是logN, 因为最多调整数的高度，而节点数N，树的高度logN
// 空复：o(1)

/*
[思维导图]
一、完全二叉树
	1.定义：一个树要么这层是满的，在不满的层（最后一层）也是从左往右依次变满的状态中.空树也认为是
	2.怎么表示完全二叉树？从0出发的一段连续的数组可以被认为是一颗完全二叉树
		i位置：
			左孩子：2*i+1
			右孩子：2*i+2
			父：(i-1)/2
		再用一个size变量表示想象中的二叉树大小

二、堆定义
	1.堆在结构上是一颗完全二叉树。大根堆：每一颗子树最大值都是自己头结点的值。反之就是小根堆
	2.堆结构的调整代价o(logn)
	3.系统堆就是优先级队列Priority，默认是小根堆。如果要用大根堆，自己传入比较器

三、堆的两个核心玩法 heapInsert(), heapify()
	1.添加。用户依次给出数字，把所有数字组织成大根堆：
		新加进来数字，heapSize++，如果>父节点，就往上移动；直到<=父节点或者根节点，这个过程就是heapInsert()
	2.返回堆顶，剩下的数继续调整成大根堆:
		返回0位置给用户，heapSize--, 用末尾的位置去替换0位置，需要做heapify(), 往下做堆的调整。
		0位置看左右两个孩子，找到较大孩子，如果较大孩子比自己大，交换；继续往下看；直到孩子的下标越界了


四、大跟堆中的7位置发生了变化，由x变成了x`,不知道x是变大变小了，这种情况下怎样依然调整成堆的样子
	从7位置开始做heapinsert,heapify:
	如果7位置数变大了，只可能发生heapinsert往上移动
	如果7位置数变小了，只可能发生heapify往下移动
	同时调用neapinsert,,heapify检查7位置该怎么移动两个其实只能发生一个，一定能把整个堆都调对



 */
public class Code02_Heap {

	// 大根堆X
	public static class MyMaxHeap {
		private int[] arr;
		private final int limit;
		private int heapSize; // 下一个数进来要放的位置

		public MyMaxHeap(int limit) {
			arr = new int[limit]; // 数组下标0-limit-1, limit是到不了的下标
			this.limit = limit;
			heapSize = 0;
		}

		public boolean isEmpty() {
			return heapSize == 0;
		}

		public boolean isFull() {
			return heapSize == limit;
		}

		public void push(int value) {
			if (heapSize == limit) {
				throw new RuntimeException("heap is full");
			}
			arr[heapSize] = value;
			heapInsert(arr, heapSize++);
		}

		// 用户此时，让你返回最大值，并且在大根堆中，把最大值删掉
		// 剩下的数，依然保持大根堆组织
		public int pop() {
			int ans = arr[0];
			swap(arr, 0, --heapSize);
			heapify(arr, 0, heapSize);
			return ans;
		}

		// 新加进来的数，现在停在了index位置，请依次往上移动，
		// 移动到0位置，或者干不掉自己的父亲了，停！
		private void heapInsert(int[] arr, int index) {
			// 我：index; 父亲：(index - 1) / 2
			// while有2种退出循环的情况：1.我<=我的父；2.我来到了根节点0位置
			while (arr[index] > arr[(index - 1) / 2]) {
				swap(arr, index, (index - 1) / 2);
				index = (index - 1) / 2;
			}
		}

		// 从index位置，往下看，不断的下沉
		private void heapify(int[] arr, int index, int heapSize) {
			int left = index * 2 + 1;
			// while有2种退出循环的情况：1.较大的孩子都不再比index位置的数大 2.已经没左孩子了
			while (left < heapSize) { // 如果有左孩子，有没有右孩子，可能有可能没有！
				// 把较大孩子的下标，给largest
				int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
				largest = arr[largest] > arr[index] ? largest : index;
				if (largest == index) {
					break;
				}
				// index和较大孩子，要互换
				swap(arr, largest, index);
				index = largest;
				left = index * 2 + 1;
			}
		}

		private void swap(int[] arr, int i, int j) {
			int tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}

	}

	// 用作对数器
	public static class RightMaxHeap {
		private int[] arr;
		private final int limit;
		private int size;

		public RightMaxHeap(int limit) {
			arr = new int[limit];
			this.limit = limit;
			size = 0;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean isFull() {
			return size == limit;
		}

		public void push(int value) {
			if (size == limit) {
				throw new RuntimeException("heap is full");
			}
			arr[size++] = value;
		}

		public int pop() {
			int maxIndex = 0;
			for (int i = 1; i < size; i++) {
				if (arr[i] > arr[maxIndex]) {
					maxIndex = i;
				}
			}
			int ans = arr[maxIndex];
			arr[maxIndex] = arr[--size];
			return ans;
		}

	}

	public static class MyComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}

	}

	public static void main(String[] args) {
		// 小根堆
		PriorityQueue<Integer> heap = new PriorityQueue<>(new MyComparator());
		heap.add(5);
		heap.add(5);
		heap.add(5);
		heap.add(3);
		// 5 , 3
		System.out.println(heap.peek());
		heap.add(7);
		heap.add(0);
		heap.add(7);
		heap.add(0);
		heap.add(7);
		heap.add(0);
		System.out.println(heap.peek());
		while (!heap.isEmpty()) {
			System.out.println(heap.poll());
		}

		int value = 1000;
		int limit = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			int curLimit = (int) (Math.random() * limit) + 1;
			MyMaxHeap my = new MyMaxHeap(curLimit);
			RightMaxHeap test = new RightMaxHeap(curLimit);
			int curOpTimes = (int) (Math.random() * limit);
			for (int j = 0; j < curOpTimes; j++) {
				if (my.isEmpty() != test.isEmpty()) {
					System.out.println("Oops!");
				}
				if (my.isFull() != test.isFull()) {
					System.out.println("Oops!");
				}
				if (my.isEmpty()) {
					int curValue = (int) (Math.random() * value);
					my.push(curValue);
					test.push(curValue);
				} else if (my.isFull()) {
					if (my.pop() != test.pop()) {
						System.out.println("Oops!");
					}
				} else {
					if (Math.random() < 0.5) {
						int curValue = (int) (Math.random() * value);
						my.push(curValue);
						test.push(curValue);
					} else {
						if (my.pop() != test.pop()) {
							System.out.println("Oops!");
						}
					}
				}
			}
		}
		System.out.println("finish!");

	}

}
