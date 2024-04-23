package class01;

public class Code06_BSAwesome {

	/**
	 * 题意：
		 * 在一个无序数组中, 值有可能正, 负, 或者零, 数组中任由两个相邻的数一定不相等.
		 * 定义局部最小:
		 * 1.长度为1，arr[0]就是局部最小；
		 * 2.数组的开头，如果arr[0] < arr[1] ，arr[0]被定义为局部最小。
		 * 3.数组的结尾，如果arr[N-1] < arr[N-2] ，arr[N-1]被定义为局部最小。
		 * 任何一个中间位置i, 即数组下标1~N-2之间, 必须满足arr[i-1] > arr[i]  && arr[i] <arr[i+1] ,叫找到一个局部最小。
		 * 请找到任意一个局部最小并返回。
	 * 时间：02：11
	 * 启发：二分不一定要有序
	 *
	 * 题解：
	 * 	先看arr[0]和arr[N-1]是不是局部最小，如果是直接返回，如果不是，那么中间一定存在局部最小
	 * 	如果arr[0]<arr[1], 且arr[m-1] < arr[m]， 那么在arr[0]和arr[m]之间一定有局部最小值。
	 * @param arr
	 * @return
	 */
	public static int getLessIndex(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		if (arr.length == 1 || arr[0] < arr[1]) {
			return 0;
		}
		if (arr[arr.length - 1] < arr[arr.length - 2]) {
			return arr.length - 1;
		}
		int l = 1;
		int r = arr.length - 2;
		int m = 0;
		while (l < r) {
			m = (l + r) / 2;
			if (arr[m] < arr[m - 1] && arr[m] < arr[m + 1]) {
				return m;
			} else if (arr[m] >= arr[m - 1]) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return l;
	}

	// 验证得到的结果，是不是局部最小
	public static boolean isRight(int[] arr, int index) {
		if (arr.length <= 1) {
			return true;
		}
		if (index == 0) {
			return arr[index] < arr[index + 1];
		}
		if (index == arr.length - 1) {
			return arr[index] < arr[index - 1];
		}
		return arr[index] < arr[index - 1] && arr[index] < arr[index + 1];
	}

	// 为了测试
	// 生成相邻不相等的数组
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) (Math.random() * maxSize) + 1];
		arr[0] = (int) (Math.random() * maxValue) - (int) (Math.random() * maxValue);
		for (int i = 1; i < arr.length; i++) {
			do {
				arr[i] = (int) (Math.random() * maxValue) - (int) (Math.random() * maxValue);
			} while (arr[i] == arr[i - 1]);
		}
		return arr;
	}

	// 为了测试
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 30;
		int maxValue = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int ans = getLessIndex(arr);
			if (!isRight(arr, ans)) {
				System.out.println("出错了！");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
