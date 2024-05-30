package class08;

import java.util.Arrays;


/*
 [题意]

*/

/*
[时间]
1：16

 */

// 时复：
// 空复：

/*
[思维导图]
一、数据范围限制：非负的，能够表达成十进制的数。如果你的数组是对象，那么改起来就相当麻烦了

二、流程：
	1.先遍历一遍找最大值，看是几位的，把不足的高位用0补齐位数。然后准备10个桶，对应0~9，每一个桶是一个队列，先进先出
	2.从左往右，按个位数进桶；从左往右出桶，先进的先倒出来；
	3.然后再根据十位数进桶，再倒出。这次倒出来的数是按十位排序的，但是相同的十位，个位的顺序还是由小到大，没有改变
	4.从左往右，按百位数进桶，再倒出
	...

	5.如果数组有负数，先遍历一遍找到最小值，所有值加上最小值排序，排完再变回去

三、原理：我先按照各位排序，接下来按照十位数排序，十位数更能决定整个数的大小。十位数排完序之后，十位数相同的，个位数也是排序好的。这个流程玩到最高位，肯定对

四、无桶优化版本。优雅！
	1.假设现在根据个位数字进桶，我准备一个 count 数组，它的长度就是10，  0- 9下标。我去算每一个个位数字总共出现了几次，得到count数组[1,3,1,0,0,0,0,0,0,0]
	2.接下来他自己更新成count`。变成前缀累加和的形式[0,4,5,5,5,5,5,5,5,5,5]。就是我把每一个位数字出现的状况算个累累加和的形式。
	3.好的，我们这个 count` 现在是什么意思了？ count`[0] = 1表示所有数字中个位数字<=0的有1个。count `[1] = 4 表示所有数中个位数<=1的有4个，
 	4.接下来从右往左遍历，你这个040， 个位数字是0。所有数字小于等于 0 的一共有几个？1个。所以你的范围是从哪到哪？ 0 到0。，因为你是从右往左遍历，所以你这个0 40 就该填在原始数组0位置。
 	5.然后你既然已经使用过了这个0，count`[0]--, 变成0。其他位置都不用减减， 0 这个位置减减可以了。
 	6.我下一个数字是谁？031。所有数字中<=1一共有几个？4 个。小于等于1整体的范围，应该 0- 3，为啥？你个位数字小于等于一的有 4 个，所以你就应该占据 0- 3位置。又因为是从右往左遍历，你说你这个 031 填在哪？填在原始数组3位置范围。



 */
public class Code04_RadixSort {

	// only for no-negative value
	public static void radixSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			max = Math.max(max, arr[i]);
		}
		// 1、最大的位数
		int maxbits = getMaxbits(max);
		// 2、有几位，就入桶出桶几次。从1开始
		for (int d = 1; d <= maxbits; d++) {
			// 3、count[]数组，下标：0-9。 count[x] = y, 表示第d位是x的出现了y次
			int[] count = new int[10];
			for (int i = 0; i < arr.length; i++) {
				int digit = getDigit(arr[i], d);
				count[digit]++;
			}

			// 4、自我更新成count`
			for (int i = 1; i < count.length; i++) {
				count[i] = count[i - 1] + count[i];
			}
			// 4、有多少个数准备多少个辅助空间。这个是桶。代替队列
			int[] help = new int[arr.length];
			// 5、入桶。从右往左遍历
			for (int i = arr.length - 1; i >= 0; i--) {
				int digit = getDigit(arr[i], d); // 拿出arr[i]的第d位，比如是x。比如386，d=1，digit=6
				int countDigit = count[digit]; // <=x有几个. <=6出现了4次，那么当前数应该去3位置
				help[countDigit - 1] = arr[i];
				count[digit]--;
			}
			// 6、出桶
			for (int i = 0; i < arr.length; i++) {
				arr[i] = help[i];
			}
		}
	}


	/**
	 * num你看看除 10 除几回能给它除成 0
	 */
	public static int getMaxbits(int num) {
		int maxbits = 0;
		while (num != 0) {
			maxbits++;
			num /= 10;
		}
		return maxbits;
	}

	/**
	 * 提取num第i位上的数
	 * x: 123， d：3
	 * x / (int) Math.pow(10, d - 1) = 123 / 100 = 1
	 * 1 % 10 = 1；
	 *
	 * x:123, d:2 -> (123 / 10) % 10 = 12 % 10 = 2
	 * @param x
	 * @param d
	 * @return
	 */
	public static int getDigit(int x, int d) {
		int pow = (int) Math.pow(10, d - 1);
		x = x / pow;
		return x % 10;
//		return ((x / ((int) Math.pow(10, d - 1))) % 10);
	}


	// for test
	public static void comparator(int[] arr) {
		Arrays.sort(arr);
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random());
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
		int maxValue = 100000;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			radixSort(arr1);
			comparator(arr2);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");

		int[] arr = generateRandomArray(maxSize, maxValue);
		printArray(arr);
		radixSort(arr);
		printArray(arr);

	}

}
