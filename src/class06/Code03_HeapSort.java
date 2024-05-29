package class06;

import java.util.Arrays;
import java.util.PriorityQueue;


/*
 [题意]
 堆排序
*/

/*
[时间]
时间：1:47。
heapInsert()方式建立大根堆时间复杂度：2：00
heapify()方法建堆：2:15
 */

// 时复：o(logn),没有稳定性。比如heapify，一下子就把最后1个数交换到0位置
// 空复：o(1)

/*
[思维导图]

1,先让整个数组都变成大根堆结构，建立堆的过程：
	 	1)从上到下的方法，heapInsert()方法，时间复杂度为O(N*ogN)
		2)从下到上的方法，heapify()方法，时间复杂度为O(N)
2,把堆的最大值和N-1位置的值交换，然后减少堆的大小之后，0位置做heapify，一直周而复始，时间复杂度为O(N*ogN)
3,堆的大小减小成0之后，排序完成
 */

/*
自顶向下建堆：使用heapInsert方式 时复分析：
1.heapInsert时间复杂度单次是logN,但是你一共要把N个数建立成大根堆,所以第一步建立大根堆的过程是n*logn的
2.但是仔细一想，我们会发现稍微有点问题，因为数据量是由小变到大的,当树还没长大时，前面的数据量，它可能每一次不是严格的logN,
只是它逐渐加最后几个节点它的复杂度才是logn, 不能说我加入的第一个数的时候，我承担了一个logn的代价，因为它没有到那么高那的高度
3.所以你就说加入N个数的过程中是N*logN,这件事，以乎它有点够呛。如果面对这种数量波动的时候，应该怎么去算它的时间角度？
可以用数据量增加常数法。

4.我既然是时间复杂度是忽略了常数项的，那么当你的数据量是N的时候，它的时间复杂度N*logN。
当我数据量只扩了一倍变成2N的时候，时间复杂度应该还是N*logN,因为时间复杂度他是忽略了常数项的。

5.当数量为N的时候，假设时间复杂度不会超过NlogN。你每一个数都承担logN的代价，它才是O(N*1ogN)。
所以我们知道O(NlogN)是我们上限，这明明是动态的og1+1og2+og3,最后1ogN,这个数
很明显比N*1ogN小的，所以我们说N个数搞成大根堆，它的上限是O(N*1ogN),下面我们只要在证明它的下限
也是O(N*ogN)就可以了.
6.它的下限怎么确定？2N的时候，可以分为把前N个数加到堆上去，后N的数加到同样的堆上去，
把前N个数加到堆上去，高度已经是logN,把后N个数加到堆上上去，每一个数的代价，其实下回就是logN+1, logN+2。。。
所以这后N个数要想加到同样的堆上去，他承担代价不会比O(N*ogN)要低，这就是下限.
再解释一下：前N个数加到堆里去，已经形成logN的高度，后N个数继续向同一个堆里加数，每次heapInsert的代价就是logN, N个数，就是N*logN，这是下限，
因为这只是后N个数的代价，前N个数的复杂度还没加上来呢。
7.我在数据量N的时候先假设出一个上限，数据量增了一个常数倍，我发现我的下限也是它，那我的复杂度不就是它O(N*1ogN)
 */

/**
 * 自底向上建堆，heapify的方式，时复分析
 * 1.数组是一股脑给我，我可以就把这个连续的数组当作完全二叉树，只不过还不是堆。接下来就是要利用heapify调整成堆
 * 2.先是最底层自己heapify，那么最底层就都是大根堆了；
 *   接着次底层节点heapify，那么次底层为头的树都是大根堆了；
 *   ...
 *   直到根节点heapify
 *  3.如果有N个节点，那么叶节点就有N/2个；这N/2个节点做heapify的时候，高度是1，heapify的复杂度就是高度，N/2 * O(1)
 *  次底层，N/4， 做heapify N/4 * 2；
 *  倒数第三层，N/8 * 3
 *  所以整个时间复杂度：T(n) = N/2*1 + N/4*2 + N/8*3...   -> 用2T(n)去做差，可以发现等比数列，o(n)!
 */

/*
两种建堆方式，时间复杂度有区别的本质
1.自顶向下建堆，大量的节点是高度增加后，才入堆的，大量节点做heapInsert就需要承担更高高度的代价
2.自底向上建堆，大量的节点是在高度低的时候入堆做heapify的
 */

/**
 * 1.对于排序，这个数组是一股脑给你的，只用heapify就行了
 * 2.但是对于堆结构，用户的数是一个个增加的，只能heapInsert建堆
 */

public class Code03_HeapSort {

	// 堆排序额外空间复杂度O(1)
	public static void heapSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		// O(N*logN)
//		for (int i = 0; i < arr.length; i++) { // O(N)
//			heapInsert(arr, i); // O(logN)
//		}
		// O(N)
		int heapSize = arr.length;
		for (int i = arr.length - 1; i >= 0; i--) {
			heapify(arr, i, heapSize);
		}
		swap(arr, 0, --heapSize);
		// O(N*logN)
		while (heapSize > 0) { // O(N)
			heapify(arr, 0, heapSize); // O(logN)
			swap(arr, 0, --heapSize); // O(1)
		}
	}

	// arr[index]刚来的数，往上
	public static void heapInsert(int[] arr, int index) {
		while (arr[index] > arr[(index - 1) / 2]) {
			swap(arr, index, (index - 1) / 2);
			index = (index - 1) / 2;
		}
	}

	// arr[index]位置的数，能否往下移动
	public static void heapify(int[] arr, int index, int heapSize) {
		int left = index * 2 + 1; // 左孩子的下标
		while (left < heapSize) { // 下方还有孩子的时候
			// 两个孩子中，谁的值大，把下标给largest
			// 1）只有左孩子，left -> largest
			// 2) 同时有左孩子和右孩子，右孩子的值<= 左孩子的值，left -> largest
			// 3) 同时有左孩子和右孩子并且右孩子的值> 左孩子的值， right -> largest
			int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
			// 父和较大的孩子之间，谁的值大，把下标给largest
			largest = arr[largest] > arr[index] ? largest : index;
			if (largest == index) {
				break;
			}
			swap(arr, largest, index);
			index = largest;
			left = index * 2 + 1;
		}
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// for test
	public static void comparator(int[] arr) {
		Arrays.sort(arr);
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// for test
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// for test
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {

		// 默认小根堆
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		heap.add(6);
		heap.add(8);
		heap.add(0);
		heap.add(2);
		heap.add(9);
		heap.add(1);

		while (!heap.isEmpty()) {
			System.out.println(heap.poll());
		}

		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			heapSort(arr1);
			comparator(arr2);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");

		int[] arr = generateRandomArray(maxSize, maxValue);
		printArray(arr);
		heapSort(arr);
		printArray(arr);
	}

}
