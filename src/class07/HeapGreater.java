package class07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
/*
 [题意]
 手写加强堆
*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]
一、系统堆的局限性
	1.堆中已经添加了很多元素，某一个元素大小发生变化后，系统堆无法做到实时调整，它就是一个无效堆了
	2.当堆上某个元素的大小改变了，此时如果要重新调整成堆，因为没有反向索引表，只能顺序遍历找到修改值的地方，再依次heapInsert(),heapify()
，遍历的代价是O(N),而当有反向索引表时候，直接可以找到该值在堆上的位置，没有了这个O(N)的代价

	3.这里面所有方法的代价都是 log n 的，很高效。这种结构在笔试里是大量出现的，面试题也会问到你给面试官扯这个他会很开心。为啥你有改写底层能力
 */

// 必须练习带泛型的！因为使用的时候，经常添加的元素不是整数，而是对象
public class HeapGreater<T> {

	private ArrayList<T> heap; // 充当原本的数组，因为容量是需要动态扩容的，想想系统的PriorityQueue, 最多传入泛型和比较器，不会传入大小。 set(i,v)是O(1)的；insert(i,v)是O(n)的。
	private HashMap<T, Integer> indexMap; // 反向索引表，某一个元素放在堆的什么位置。如果T是基础类型，就包一层对象，防止被覆盖了。
	private int heapSize; // 最后一个元素的下一个位置
	private Comparator<T> comp; // 因为是带泛型的，所以需要传入比较器告诉我泛型T怎么比大小

	public HeapGreater(Comparator<T> c) {
		heap = new ArrayList<>();
		indexMap = new HashMap<>();
		heapSize = 0;
		comp = c;
	}

	public boolean isEmpty() {
		return heapSize == 0;
	}

	public int size() {
		return heapSize;
	}

	// 系统堆是没有这个方法的，因为没有反向索引表，如果想知道在不在堆里，只能遍历
	public boolean contains(T obj) {
		return indexMap.containsKey(obj);
	}

	public T peek() {
		return heap.get(0);
	}

	public void push(T obj) {
		heap.add(obj);
		// 多了这一行。维护反向索引表。heapSiz就是当前元素进来放的位置
		indexMap.put(obj, heapSize);
		heapInsert(heapSize);
		heapSize++;
	}

	public T pop() {
		T ans = heap.get(0);
		// swap里面，还封装了对反向索引表重定向的过程
		swap(0, heapSize - 1);
		indexMap.remove(ans);
		heap.remove(heapSize - 1);
		heapSize--;
		heapify(0);
		return ans;
	}

	// 原来系统的堆里它绝对不支持这种方法。你给我一个对象，我告诉你它内部值发生变化了，而且我不告诉你变大还是变小了，
	// 请你把整个堆继续调有序，这个方法系统提供的绝对没有。为啥？它没有反向索引表
	// 但我们此时就可以做到了。你给我一个对象，它变大还是变小？我先通过反向索引表拿到他的位置，然后依次玩一下heapInsert(), heapify()
	public void resign(T obj) {
		heapInsert(indexMap.get(obj));
		heapify(indexMap.get(obj));
	}

	// 移除元素
	// 拿堆中的最后1个元素和obj换，然后玩一次resign
	public void remove(T obj) {
		int index = indexMap.get(obj);
		indexMap.remove(obj);

		T replace = heap.get(heapSize - 1);
		heapSize--;
		heap.remove(heapSize);
		if (obj != replace) {
			heap.set(index, replace);
			indexMap.put(replace, index);
			resign(replace);
		}
	}


	// 请返回堆上的所有元素
	public List<T> getAllElements() {
		List<T> ans = new ArrayList<>();
		for (T c : heap) {
			ans.add(c);
		}
		return ans;
	}

	private void heapInsert(int index) {
		// 我：index，比如是2
		// 父：(index - 1)  / 2  比如是9
		// compare(2，9)，比较器假设传入的是由小到大的，此时就会返回-1，我需要和我的父换
		// 可以这么理解，返回负数，就是第一个参数优先，说明第一个参数要和第二个参数交换才能维持堆
		while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
			swap(index, (index - 1) / 2);
			index = (index - 1) / 2;
		}
	}

	private void heapify(int index) {
		int left = index * 2 + 1;
		while (left < heapSize) {
			int largest = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? (left + 1) : left;
			largest = comp.compare(heap.get(largest), heap.get(index)) < 0 ? largest : index;
			if (largest == index) {
				break;
			}
			swap(largest, index);
			index = largest;
			left = index * 2 + 1;
		}
	}

	private void swap(int i, int j) {
		T o1 = heap.get(i);
		T o2 = heap.get(j);
		heap.set(i, o2);
		heap.set(j, o1);
		// 反向索引表也得换！！
		indexMap.put(o2, i);
		indexMap.put(o1, j);
	}

}
