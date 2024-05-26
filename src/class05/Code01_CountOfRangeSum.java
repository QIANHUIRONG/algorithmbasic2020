package class05;


/*
 [题意]

*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]

 */


// 这道题直接在leetcode测评：
// https://leetcode.com/problems/count-of-range-sum/
public class Code01_CountOfRangeSum {

	public static int countRangeSum(int[] nums, int lower, int upper) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		long[] sum = new long[nums.length];
		sum[0] = nums[0];
		for (int i = 1; i < nums.length; i++) {
			sum[i] = sum[i - 1] + nums[i];
		}
		return process(sum, 0, sum.length - 1, lower, upper);
	}

	public static int process(long[] sum, int l, int r, int lower, int upper) {
		if (l == r) {
			return sum[l] >= lower && sum[l] <= upper ? 1 : 0;
		}
		int m = (l + r) / 2;
		return process(sum, l, m, lower, upper) + process(sum, m + 1, r, lower, upper)
				+ merge(sum, l, m, r, lower, upper);
	}

	public static int merge(long[] arr, int l, int m, int r, int lower, int upper) {
		int ans = 0;
		int windowL = l;
		int windowR = l;
		// [windowL, windowR)
		for (int i = m + 1; i <= r; i++) {
			long min = arr[i] - upper;
			long max = arr[i] - lower;
			while (windowR <= m && arr[windowR] <= max) {
				windowR++;
			}
			while (windowL <= m && arr[windowL] < min) {
				windowL++;
			}
			ans += windowR - windowL;
		}
		long[] help = new long[r - l + 1];
		int i = 0;
		int p1 = l;
		int p2 = m + 1;
		while (p1 <= m && p2 <= r) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[l + i] = help[i];
		}
		return ans;
	}

}
