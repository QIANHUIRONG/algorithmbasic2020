package class04;



/*
 [题意]

*/

/*
[时间]

非递归实现归并排序流程：1：28
非递归方式时间复杂度估计：1：35
code: 36

 */

// 时复：o(logn), 有稳定性。相等的时候先拷贝左边就行
// 空复：o(n)

/*
[思维导图]
1.整体流程
	1）整体是递归，左边排好序+右边排好序+merge让整体有序
	2）利用master公式来求解时间复杂度
	3）当然可以用非递归实现


2.Merge过程
	1.谁小拷贝谁，相等先拷贝左边的（稳定）
	2.merge的过程中，左指针p1不回退，右指针p2也不回退，单独merge过程是o(n)的

3.时复：
	1.递归用master公式可以得到o(n*logn)。
递归是：T(N) = 2 * T(N / 2) + O(N), a = 2, b = 2, d = 1, log(b,a) = 1 = d, 时复 = o(n^1 * logn)
	2.改成迭代可以直接看出o(n*logn)

3.为什么比O(n^2)的排序好？
	1.最本质的原因是所有O(N2)的排序，大量浪费比较行为
比较行为都是独立的.上一次发生的比较行为，丝毫不能够加速底下的比较行，大量浪费比较行为
	2.而归并排序我左组内部的比较行为浪费了吗？没浪费，都变成了排序好的结果，右侧部分也变成了排序好的结果。
当你左侧跟右侧merge的过程中，左组数之间是根本没有比较的，要比较是左组某一个指针对右组某一个指针在比较
你们此时比较行为也没有浪费，变成了你们共同的一个排好序的结果，所以比较行为每一次都变成结果在传递，
所以复杂度是O(N*logN)


4.非递归实现归并排序
	1.设计变量步长，步长为1：左组1个，右组1个去merge；
	2.然后步长*=2, 左组2个，右组2个去merge
	3.... 直到步长>=整个数组长度，就停止

	4.步长的变化次数：logn次，每次调整步长遍历一遍去merge o(n), 总体o(n*logn)
	5.如果左组都凑不够，那就不用merge了；如果右组凑不够，不够也行，去merge


 */

public class Code01_MergeSort {

	// 递归方法实现
	public static void mergeSort1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process(arr, 0, arr.length - 1);
	}

	public static void process(int[] arr, int L, int R) {
		if (L == R) { // base case
			return;
		}
		int mid = (L + R) / 2;
		process(arr, L, mid);
		process(arr, mid + 1, R);
		merge(arr, L, mid, R);
	}

	public static void merge(int[] arr, int L, int M, int R) {
		int[] help = new int[R - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = M + 1;
		while (p1 <= M && p2 <= R) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		// 要么p1越界了，要么p2越界了
		while (p1 <= M) {
			help[i++] = arr[p1++];
		}
		while (p2 <= R) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
	}

	/**
	 * 非递归方法实现
	 * @param arr
	 */
	public static void mergeSort2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		int N = arr.length;
		// 步长。先左组1个右组1个去merge；再左组2个右组2个merge
		int mergeSize = 1;
		while (mergeSize < N) { // log N
			// 当前左组的，第一个位置
			int L = 0;
			while (L < N) {
				int M = L + mergeSize - 1; // 中间的位置
				if (M >= N) { // 左组都不够，直接跳出循环
					break;
				}

				// 到此有左组，右组无论多少，都会去merge

				int R = Math.min(M + mergeSize, N - 1); // R如果够的话就是M + mergeSize，如果不够，就是N-1
				merge(arr, L, M, R);
				L = R + 1;
			}
			mergeSize *= 2;
		}
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
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			mergeSort1(arr1);
			mergeSort2(arr2);
			if (!isEqual(arr1, arr2)) {
				System.out.println("出错了！");
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
