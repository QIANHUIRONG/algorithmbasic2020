package class39;

import java.util.HashSet;
import java.util.TreeSet;

/*
// 题意：非负数组子序列中累加和%m的最大值
// 给定一个非负数组arr，和一个正数m。 返回arr的所有子序列中累加和%m之后的最大值。
 */

/*
时间
题意：12
数据量1：数组值都不大，%m的m很大：13  code：17
数据量2：数组值巨大，%m的m不大：19
数据量2 code：30
数据量3：数据值巨大，m巨大，数组长度n不大，30以内：33
什么时候用分治：44
code：45
 */

/*
【思维导图】
解法一：数组值都不大,数组长度n也不大，%m的m很大，m:10^10
	1.背包解法。process(int index, int sum, HashSet<Integer> set)，当前来到index位置，之前做的选择累加和是sum，
继续在index及其后面的数字做选择，收集所有可能的累加和，到set里面去。主函数在去%m，看哪一个最大
	2.主流程：index位置的数要和不要两种情况
	3.index的取值：0-n，sum的取值：所有数字的累加和，不大


解法二：数组值巨大，%m的m不大
	1.dp[i][j]:arr0.i所有的数字自由选择，所搞出来的所有累加和再模m之后有没有余数j
	2.主流程：
		1.可能性一：完全不使用i位置数字dp[i-1][j]，arr从0~i-1所有的数字搞出来的累加和能不能模完M之后搞出j
		2.可能性二：使用i位置的数：【23】
            情况一：i = 4, arr[i] = 5,j=7,m = 8,cur = 5 % 8 = 3
            3 <= 8,那么如果要凑出dp[4][7], 就需要凑出dp[3][4], 就是dp[i - 1][j - cur]
            宏观理解：要搞出余数7，我当前这个数余数是3，那只要之前能给我搞出余数4就行了。
            这里已经包括了，cur==j的情况，这时候会拿dp[i-1][0]。

            情况二：i = 4, arr[i] = 11,j = 3, m = 7, cur = 11 % 7 = 4
            4 > 3, 如果要凑出dp[4][3],  （11 + ？） % 7 = 3， ？ = 6， 6 = 7 + 3 - 4， 依赖dp[i-1][m + j - cur]
            宏观理解：要搞出余数3，我当前这个数的余数的4，那只要之前能给我搞出余数6就行了，我的余数加上之前的余数 = 10，10%7=3
            int cur = arr[i] % m;

            if (cur <= j) {
                dp[i][j] |= dp[i - 1][j - cur];
            } else {
                dp[i][j] |= dp[i - 1][j - cur + m]; // 相比上一个，就相当于再加一个m
            }


解法三：
	1.arr每个位置值巨大，m的值巨大，但是arr长度30以内，这种情况下用分治。如果直接暴力展开，2^30 > 10^8, 所以可以拆成2份1
	2.所有可能性的展开就两个：0位置的或要跟不要两个分支，1位置的或要跟不要两个分支，2位置的或要跟不要两个分支，
这样分治下去每个位置的或就两种情况，要么要要么不要。最疯就是2^30 > 10^8，纯暴力是拿不下的，如果我把这30个数砍两半，
每边15个数，求左边15个数所有的累加和在模完M得到的所有余数。我再求右边15个数所有的累加和在模完M得到的所有余数。
2^15+2^15，远远没有到10^8的规模
	3.后续答案无非就3种情况：
		1.只来自左侧
		2.只来自右侧
		3.即来自左侧，又来自右侧。整合逻辑：遍历左侧的余数，如果是7，而m = 100, 那么就去找<=92距离92最近的->有序表


什么时候用分治:
	1,数据量整体做尝试可能性太多了，跑不完
	2,数据分成多个块（常见是两块）之后，各自的可能性并不算多，跑得完
	3,合并多个块各自信息的整合过程不复杂
 */
public class Code01_SubsquenceMaxModM {

    /*
    数据量1：数组值都不大，n很大
     */
	public static int max1(int[] arr, int m) {
		HashSet<Integer> set = new HashSet<>();
		// arr从0位置开始自由选择，递归收集所有的累加和，再去%m求最大
		process(arr, 0, 0, set);
		int max = 0;
		for (Integer sum : set) {
			max = Math.max(max, sum % m);
		}
		return max;
	}

	public static void process(int[] arr, int index, int sum, HashSet<Integer> set) {
		if (index == arr.length) {
			set.add(sum);
		} else {
			process(arr, index + 1, sum, set);
			process(arr, index + 1, sum + arr[index], set);
		}
	}

	// 和上面思想一样，只不过容器换成了treeSet
//    public static int max1(int[] arr, int m) {
//        if (arr == null || arr.length == 0) {
//            return 0;
//        }
//        TreeSet<Integer> set = new TreeSet<>();
//        // arr从0位置开始自由选择，递归收集所有的累加和，再去%m求最大
//        process(arr, 0, 0, m, set);
//        // floor: 找到<=x，最接近x的
//        return set.floor(m);
//    }
//
//    public static void process(int[] arr, int index, int sum, int m, TreeSet<Integer> treeSet) {
//        if (index == arr.length) {
//            treeSet.add(sum % m);
//        } else {
//            process(arr, index + 1, sum, m, treeSet);
//            process(arr, index + 1, sum + arr[index], m, treeSet);
//        }
//    }

    /*
    数据量一对应的动态规划
     */
    public static int max2(int[] arr, int m) {
        int sum1 = 0;
        int N = arr.length;
        for (int i = 0; i < N; i++) {
            sum1 += arr[i]; // 所有数组值的累加和
        }
        // dp[index][sum]表示 arr[index..]之后的数字自由选择，累加和能否搞出sum？
        // 因为暴力递归中，没有返回值，basecase是收集答案到set中，所以这里的dp就这么改写
        boolean[][] dp = new boolean[N][sum1 + 1];
        // 第0列
        for (int i = 0; i < N; i++) {
            dp[i][0] = true; // 累加和都能凑出0.都不选就行
        }
        // 第0行，只能搞出arr[0],其他都是false
        dp[0][arr[0]] = true;
        for (int index = 1; index < N; index++) {
            for (int sum = 1; sum <= sum1; sum++) {
                // 不选index位置的数
                dp[index][sum] = dp[index - 1][sum];
                // 选了index位置的数
                if (sum - arr[index] >= 0) {
                    dp[index][sum] |= dp[index - 1][sum - arr[index]]; // dp[index][sum]初始是false，如果dp[index - 1][sum - arr[index]]为true就是true，为false就是false
                }
            }
        }
        int ans = 0;
        for (int j = 0; j <= sum1; j++) {
            if (dp[N - 1][j]) {
                ans = Math.max(ans, j % m);
            }
        }
        return ans;
    }




    /*
    数据量2：数组值巨大，n不大
    数据量2对应的dp。没有写暴力递归->尝试了下，好像搞不定啊，又变成max1了
     */
    public static int max3(int[] arr, int m) {
        int N = arr.length;
        // 行：0-n-1
        // 列：0...m-1
        // dp[i][j]:arr0.i所有的数字自由选择，所搞出来的所有累加和再%m之后有没有余数j
        boolean[][] dp = new boolean[N][m];
        for (int i = 0; i < N; i++) {
            dp[i][0] = true; // 搞出余数0，就是什么数都不选就是余数0
        }
        dp[0][arr[0] % m] = true; // 只能选arr[0], 它搞出来的余数是啥，那个格子就是true
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < m; j++) {
                // 可能性一：不使用i位置的数，arr从0~i-1所有的数字搞出来的累加和能不能模完M之后搞出j
                dp[i][j] = dp[i - 1][j];

				/*
				可能性二：使用i位置的数
				1、i = 4, arr[i] = 5,j=7,m = 8,cur = 5 % 8 = 3
				3 <= 8,那么如果要凑出dp[4][7], 就需要凑出dp[3][4], 就是dp[i - 1][j - cur]
				宏观理解：要搞出余数7，我当前这个数余数是3，那只要之前能给我搞出余数4就行了。

				2、i = 4, arr[i] = 11,j = 3, m = 7, cur = 11 % 7 = 4
				4 > 3, 如果要凑出dp[4][3],  （11 + ？） % 7 = 3， ？ = 6， 6 = 7 + 3 - 4， 依赖dp[i-1][m + j - cur]
				宏观理解：要搞出余数3，我当前这个数的余数的4，那只要之前能给我搞出余数6就行了，我的余数加上之前的余数 = 10，10%7=3
				 */
                int cur = arr[i] % m;
                if (cur <= j) {
                    dp[i][j] |= dp[i - 1][j - cur];
                } else {
                    dp[i][j] |= dp[i - 1][j - cur + m]; // 相比上一个，就相当于再加一个m
                }
            }
        }
        for (int j = m - 1; j >= 0; j--) {
            if (dp[N - 1][j]) {
                return j;
            }
        }
        return 0;
    }

    // 如果arr的累加和很大，m也很大
    // 但是arr的长度相对不大
    // 用分治法
    public static int max4(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        int mid = (arr.length - 1) / 2;
        TreeSet<Integer> sortSet1 = new TreeSet<>();
        process4(arr, 0, 0, mid, m, sortSet1);
        TreeSet<Integer> sortSet2 = new TreeSet<>();
        process4(arr, mid + 1, 0, arr.length - 1, m, sortSet2);
        int ans = 0;
        ans = Math.max(ans, sortSet1.floor(m)); // 先抓一下，答案可能只来自左侧
        ans = Math.max(ans, sortSet2.floor(m)); // 先抓一下，答案可能只来自右侧
        for (Integer leftMod : sortSet1) {
            // m=8， leftMod = 3， 右边应该去找<=4距离4最近的。treeSet.floor(x), 就是找到<=x距离x最近的！
            ans = Math.max(ans, leftMod + sortSet2.floor(m - 1 - leftMod));
        }
        for (Integer rightMod : sortSet2) { // 原本是没有这段代码的，没有这段也行，但是加了好像好理解一点，不然我总感觉漏了一边
            ans = Math.max(ans, rightMod + sortSet1.floor(m - 1 - rightMod));
        }
        return ans;
    }

    // 从index出发，最后有边界是end+1，arr[index...end]
    public static void process4(int[] arr, int index, int sum, int end, int m, TreeSet<Integer> sortSet) {
        if (index == end + 1) {
            sortSet.add(sum % m);
        } else {
            process4(arr, index + 1, sum, end, m, sortSet);
            process4(arr, index + 1, sum + arr[index], end, m, sortSet);
        }
    }

    public static int[] generateRandomArray(int len, int value) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * value);
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 10;
        int value = 100;
        int m = 76;
        int testTime = 500000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(len, value);
            int ans1 = max1(arr, m);
            int ans2 = max2(arr, m);
            int ans3 = max3(arr, m);
            int ans4 = max4(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }

}
