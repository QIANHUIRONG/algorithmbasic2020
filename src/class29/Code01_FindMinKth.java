package class29;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
题目：在一个无序数组中求第k小的数
本题经典到用快排的方式都不新鲜了；笔试，工作用快排的方式就行，面试就可以聊一聊bfprt

题意：4
快排思路：
	无序数组中随机选一个数去做荷兰国旗问题，如果等于区命中了，就直接返回；
	否则要么在小于区继续递归下去，要么在大于区继续递归下去；两边只会走一边
	时间复杂度O(N)， (快排O(N*logN)是因为两边都要走)
	如果把快排改成迭代版本，可以做到时间复杂度O(n)，空间复杂度o(1)!!这无疑是最优解！

bfprt：
	1.题意【24】
	2.面试聊的时候可以装
	3.流程
	4.证明至少淘汰3/10N【40】
	5.时间复杂度【50】
	6.code[1:05]
时间复杂度：快排方法和bfprt算法都是O（N)
 */

/*
	 利用bfprt算法，时间复杂度O(N)
	 改写快排的方式是每次随机取一个数去做荷兰国旗划分，从概率上证明时间复杂度O(N);
	 而bfprt算法就是很讲究的取一个数去做荷兰国旗划分，保证每次至少淘汰3/10N规模，从而达到时间复杂度O(N)

	思路：
	 1.5个数一组，每组去排序，拿每个组的中位数组成新数组m[]
	 2.递归调用bfprt，得到m[]数组的中位数,这个中位数就是我们要找的天选之子p
	 3.拿着这个p去做荷兰国旗划分

	 这个p每次至少能淘汰3/10N的规模，证明如下：
	 我们想要求小于区最大规模是多大，不太好求，可以先去求大于区最小规模是多少：
	 假设这是原数组，每一行分成一组,每一组都已经从小到大排好，m[]数组是中间竖的[a,b,c,d,e],真名天子是c;
	[o o a o o
	 o o b o o
	 o o c o o
	 f g d o o
	 h i e o o] 规模总共是N，每一个点是N/20
	 总数N，每5个一组，每组排序完取中位数得到中位数数组，规模N/5;得到这个中位数数组的中位数c，
c > d、e; 也就是这个中位数在m中>N/10;
	 对应回原数组中，d 又 > f、g ; e 又 > h、i;所以c至少会大于f,g,d,h,i,e = 3N/10
	 综上所述，选出来的这个真命天子大于区至少3N/10,所以留下来的小于区最多7N/10;

	 时间复杂度：
	 1.每5个一组,O(1)
	 2.每组去排序，每组O(1),共O(N/5) = O(N)
	 3.取排完序的每一组的中位数得到中位数数组m O(N)
	 4.求中位数的中位数,T(N/5)
	 5.拿着真命天子去荷兰国旗，O(N)
	 6.递归每次至少淘汰T(3N/10),最多剩T(7N/10)
	 所以T(N) = T(N/5) + T(7N/10) + O(N)
	 -->数学上证明了这个递归式时间复杂度就是O(N)

	 为什么是5个一组？
	 人家bfprt就是5个人，他们就是喜欢5个一组，你喜欢的话4个一组，7个一组都行，递推式可能略有不同，
但是时间复杂度都是O(N)

	 bfprt启发了一种思想，我去选一个数确保每次规避最差的情况，而不用依靠概率去运行。


	 */
public class Code01_FindMinKth {

	public static class MaxHeapComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}

	}

	// 利用大根堆，时间复杂度O(N*logK)。不用看
	public static int minKth1(int[] arr, int k) {
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new MaxHeapComparator());
		for (int i = 0; i < k; i++) {
			maxHeap.add(arr[i]);
		}
		for (int i = k; i < arr.length; i++) {
			if (arr[i] < maxHeap.peek()) {
				maxHeap.poll();
				maxHeap.add(arr[i]);
			}
		}
		return maxHeap.peek();
	}

	// 改写快排，时间复杂度O(N)
	// k >= 1
	public static int minKth2(int[] array, int k) {
		int[] arr = copyArray(array);
		/*
		 index = k -1是因为：
		 k代表第几小的数，从1开始；
		 index代表找arr中排序后位于index位置的数，从0开始；
		 也就是 k = 1 -> index = 0 (找arr中第1小的数等于去找排序后位于0位置的数)
		 */
		return process2(arr, 0, arr.length - 1, k - 1);
	}

	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i != ans.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	// arr 第k小的数
	// process2(arr, 0, N-1, k-1)
	// arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
	// index [L..R]
	public static int process2(int[] arr, int L, int R, int index) {
		if (L == R) { // L = =R ==INDEX
			return arr[L];
		}
		// 不止一个数  L +  [0, R -L]
		int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
		int[] range = partition(arr, L, R, pivot);
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			return process2(arr, L, range[0] - 1, index);
		} else {
			return process2(arr, range[1] + 1, R, index);
		}
	}

	/*
	 经典的荷兰国旗问题
	 快排中的荷兰国旗，是直接拿最后一个元素做划分值去做荷兰国旗；
	 而本题因为bfprt直接选了一个划分值，所以直接给你划分值去划分。
	 */
	public static int[] partition(int[] arr, int L, int R, int pivot) {
		int less = L - 1;
		int more = R + 1;
		int cur = L;
		while (cur < more) {
			if (arr[cur] < pivot) {
				swap(arr, ++less, cur++);
			} else if (arr[cur] > pivot) {
				swap(arr, cur, --more);
			} else {
				cur++;
			}
		}
		return new int[] { less + 1, more - 1 };
	}

	public static void swap(int[] arr, int i1, int i2) {
		int tmp = arr[i1];
		arr[i1] = arr[i2];
		arr[i2] = tmp;
	}

	/*
	快排的迭代版本。时间复杂度O(n)，空间复杂度o(1)
	 */
	public static int minKth4(int[] array, int k) {
		int[] arr = copyArray(array);
		return minKthForLoop(arr, k - 1);
	}

	public static int minKthForLoop(int[] arr, int index) {
		int L = 0;
		int R = arr.length - 1;
		while (L < R) {
			int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
			int[] range = partition(arr, L, R, pivot);
			if (index >= range[0] && index <= range[1]) {
				return pivot;
			} else if (index < range[0]) {
				R = range[0] - 1;
			} else {
				L = range[1] + 1;
			}
		}
		return arr[L];
	}




	// 利用bfprt算法，时间复杂度O(N)
	public static int minKth3(int[] array, int k) {
		int[] arr = copyArray(array);
		return process3(arr, 0, arr.length - 1, k - 1);
	}

	// arr[L..R]  如果排序的话，位于index位置的数，是什么，返回
	public static int process3(int[] arr, int L, int R, int index) {
		if (L == R) {
			return arr[L];
		}
		// L...R  每五个数一组
		// 每一个小组内部排好序
		// 小组的中位数组成新数组
		// 这个新数组的中位数返回
		int pivot = bfprt(arr, L, R);
		int[] range = partition(arr, L, R, pivot);
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			return process3(arr, L, range[0] - 1, index);
		} else {
			return process3(arr, range[1] + 1, R, index);
		}
	}

	// arr[L...R]  五个数一组
	// 每个小组内部排序
	// 每个小组中位数领出来，组成marr
	// marr中的中位数，返回
	public static int bfprt(int[] arr, int L, int R) {
		int size = R - L + 1;
		int offset = size % 5 == 0 ? 0 : 1;
		int[] mArr = new int[size / 5 + offset];
		for (int team = 0; team < mArr.length; team++) { // 遍历原数组，每5个一组
			int teamFirst = L + team * 5;
			// 每一组排好序后返回中位数
			mArr[team] = getMedian(arr, teamFirst, Math.min(R, teamFirst + 4));
		}
		// marr中，找到中位数
		// marr(0, marr.len - 1,  mArr.length / 2 )
		return process3(mArr, 0, mArr.length - 1, mArr.length / 2);
	}

	public static int getMedian(int[] arr, int L, int R) {
		insertionSort(arr, L, R);
		return arr[(L + R) / 2];
	}

	public static void insertionSort(int[] arr, int L, int R) {
		for (int i = L + 1; i <= R; i++) {
			for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--) {
				swap(arr, j, j + 1);
			}
		}
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) (Math.random() * maxSize) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 1000000;
		int maxSize = 5;
		int maxValue = 100;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int k = (int) (Math.random() * arr.length) + 1;
			int ans1 = minKth1(arr, k);
			int ans2 = minKth2(arr, k);
			int ans3 = minKth3(arr, k);
			int ans4 = minKth4(arr, k);
			if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
