package class03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


/**
 * Java中的栈：
 * 	Stack<Integer> stack = new Stack<>();
 *	方法：stack.push() / stack.add(); stack.pop();
 * Java中的队列：
 * 	Queue<Integer> queue = new LinkedList<>();
 * 	方法：queue.offer() / queue.add(); queue.poll();
 *
 */
public class Code03_DoubleEndsQueueToStackAndQueue {


	public static class Node {
		public int value;
		public Node last;
		public Node next;

		public Node(Integer data) {
			value = data;
		}
	}

	/**
	 * 版本1：不带泛型的版本。练习这个就够了
	 */
	public static class DoubleEndsQueue1 {
		public Node head;
		public Node tail;

		public void addFromHead(int value) {
			Node node = new Node(value);
			if (head == null) {
				head = node;
				tail = node;
			} else {
				node.next = head;
				head.last = node;
				head = node;
			}
		}

		public Integer popFromBottom() {
			if (head == null) {
				return null;
			}
			Node cur = tail;
			if (head == tail) {
				head = null;
				tail = null;
			} else {
				tail = tail.last;
				tail.next = null;
				cur.last = null;
			}
			return cur.value;
		}
	}



	public static class NodeT<T> {
		public T value;
		public NodeT<T> last;
		public NodeT<T> next;

		public NodeT(T data) {
			value = data;
		}
	}

	/**
	 *  版本2：带泛型的版本
	 * @param <T>
	 */
	public static class DoubleEndsQueue<T> {
		public NodeT<T> head;
		public NodeT<T> tail;

		public void addFromHead(T value) {
			NodeT<T> cur = new NodeT<T>(value);
			if (head == null) {
				head = cur;
				tail = cur;
			} else {
				cur.next = head;
				head.last = cur;
				head = cur;
			}
		}

		public void addFromBottom(T value) {
			NodeT<T> cur = new NodeT<T>(value);
			if (head == null) {
				head = cur;
				tail = cur;
			} else {
				cur.last = tail;
				tail.next = cur;
				tail = cur;
			}
		}

		public T popFromHead() {
			if (head == null) {
				return null;
			}
			NodeT<T> cur = head;
			if (head == tail) {
				head = null;
				tail = null;
			} else {
				head = head.next;
				cur.next = null;
				head.last = null;
			}
			return cur.value;
		}

		public T popFromBottom() {
			if (head == null) {
				return null;
			}
			NodeT<T> cur = tail;
			if (head == tail) {
				head = null;
				tail = null;
			} else {
				tail = tail.last;
				tail.next = null;
				cur.last = null;
			}
			return cur.value;
		}

		public boolean isEmpty() {
			return head == null;
		}

	}

	public static class MyStack<T> {
		private DoubleEndsQueue<T> queue;

		public MyStack() {
			queue = new DoubleEndsQueue<T>();
		}

		public void push(T value) {
			queue.addFromHead(value);
		}

		public T pop() {
			return queue.popFromHead();
		}

		public boolean isEmpty() {
			return queue.isEmpty();
		}

	}

	public static class MyQueue<T> {
		private DoubleEndsQueue<T> queue;

		public MyQueue() {
			queue = new DoubleEndsQueue<T>();
		}

		public void push(T value) {
			queue.addFromHead(value);
		}

		public T poll() {
			return queue.popFromBottom();
		}

		public boolean isEmpty() {
			return queue.isEmpty();
		}

	}

	public static boolean isEqual(Integer o1, Integer o2) {
		if (o1 == null && o2 != null) {
			return false;
		}
		if (o1 != null && o2 == null) {
			return false;
		}
		if (o1 == null && o2 == null) {
			return true;
		}
		return o1.equals(o2);
	}

	public static void main(String[] args) {
		int oneTestDataNum = 100;
		int value = 10000;
		int testTimes = 100000;
		for (int i = 0; i < testTimes; i++) {
			MyStack<Integer> myStack = new MyStack<>();
			MyQueue<Integer> myQueue = new MyQueue<>();
			Stack<Integer> stack = new Stack<>();
			Queue<Integer> queue = new LinkedList<>();
			for (int j = 0; j < oneTestDataNum; j++) {
				int nums = (int) (Math.random() * value);
				if (stack.isEmpty()) {
					myStack.push(nums);
					stack.push(nums);
				} else {
					if (Math.random() < 0.5) {
						myStack.push(nums);
						stack.push(nums);
					} else {
						if (!isEqual(myStack.pop(), stack.pop())) {
							System.out.println("oops!");
						}
					}
				}
				int numq = (int) (Math.random() * value);
				if (queue.isEmpty()) {
					myQueue.push(numq);
					queue.offer(numq);
				} else {
					if (Math.random() < 0.5) {
						myQueue.push(numq);
						queue.offer(numq);
					} else {
						if (!isEqual(myQueue.poll(), queue.poll())) {
							System.out.println("oops!");
						}
					}
				}
			}
		}
		System.out.println("finish!");
	}

}
