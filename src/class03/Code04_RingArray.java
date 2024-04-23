package class03;

/**
 * 数组实现栈：
 *  * 如果是数组实现栈：
 *  * 只需要一个数组，再加一个index指针指向下一个进来要添加的位置。
 *  * 入栈就放到arr[index]上，然后index++
 *  * 出栈就出arr[index-1],然后index--;
 *  * 一旦index == arr.length,报溢出错误
 */

/**
 * 通过pushi，polli，size完美解耦
 */
public class Code04_RingArray {

	public static class MyQueue {
		private int[] arr;
		private int pushi;// end
		private int polli;// begin
		private int size;
		private final int limit;

		public MyQueue(int limit) {
			arr = new int[limit];
			pushi = 0;
			polli = 0;
			size = 0;
			this.limit = limit;
		}

		public void push(int value) {
			if (size == limit) {
				throw new RuntimeException("队列满了，不能再加了");
			}
			size++;
			arr[pushi] = value;
			pushi = nextIndex(pushi);
		}

		public int pop() {
			if (size == 0) {
				throw new RuntimeException("队列空了，不能再拿了");
			}
			size--;
			int ans = arr[polli];
			polli = nextIndex(polli);
			return ans;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		// 如果现在的下标是i，返回下一个位置
		private int nextIndex(int i) {
			return i < limit - 1 ? i + 1 : 0;
		}

	}

}
