package class40;

/**
 * 【题意】
 * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0
 * 给定一个整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和<=K，并且是长度最大的
 * 返回其长度
 */

/*
【时间】
先玩2个辅助概念：59
流程：1：10
可能性舍弃：1：18
可能性舍弃后为什么对：1：21
code：1：29


 */
/*
【思维导图】 时间复杂度：O(n)
1.引入2个辅助概念：
    1.sum[]:如果子数组必须以i开头，取得最小的累加和是多少
      minSumEnd[]：如果子数组必须以i开头，取得最小的累加和的右边界
    2.从右往左遍历一遍，生成2个辅助数组
    3.填到i位置，如果sum[i+1]是负数，说明扩进来会让我i位置更小，有利可图，扩
    填到i位置，如果sum[i+1]是正数，说明扩进来会让我i位置更更大，不扩

2.主流程：用窗口。求子数组必须以每一个位置开头的情况下的答案（1：10）
假设扩出的第一块是0~6位置。这一块就是零开头的情况下所取得的最小累加和设为a且a<=k(如果a>k则扩结束了)，
如果a是小于等于K的，下面我把7开头能够取得最小累加和算做b我看看ā+b是不是小于等于K的。如果还是我接着扩，看加上
C之后是不是小于等干K的，我一直扩到大干K的时候

3.假设必须从0开头的a这块，b这一块，c这一块，d这一块得到的
整体累加和都是小于等于k的，但是一旦算上e这一块，就不再小于等于k,>K了，那么到e之前停止
必须以零开头的情况下，小于等于k的最长子数组是谁？->a,b,c,d推到的范围

4.0开头的时候你重新扩，1开头的时候你重新扩，2开头的时候你重新扩，能求出正确答案的，
但是它时间复杂度O(N^2),回退了

5.不回退的技巧：可能性舍弃
假设我从0开始扩了4块。最后一块的最末尾的位置是13，然后14块扩不动了
此时得到一个答案0开头的情况下扩出来的小于等于K的最长子数组是14个。0~13累加和是sum,
下来我把这个窗口变成从1变成13不回退看看调整完之后这块能不能把14这一块吸进来也不超过K
如果吸进来超了，那就不扩了，我收集一个答案，然后计算2~13，看能不能把14吸进来
就是你每一个开头的时候，你就看你整个下一块能不能进来。窗口的右边界不会退！

这样子有可能从1开始其实正确的答案根本也扩不到13，有可能这个位置的答案是错的，错就错了，我只关心
能不能推高答案的可能性，而不去关心每个位置的具体答案。

其实我们这个流程没有正确地求出0位置时的答案，1位置的时候的准确答案2位置的时候的
准确答案，我们就关注能不能把答案推高的可能性。
我0已经推到13位置了，我看看0退出的情况下一块能不能进来，如果它不能进来，甚至于1
的答案还比原来的要短。本来就应该舍弃这个答案，我就看下一块能不能进来就这件事。

6.这道题难就难在，它居然是有某些可能性的舍弃的。
原理就是我已经找到L出发到R的一个解了。你现在告诉我L加1的答案比它短，
这个答案可以舍弃，因为我关心的是全局最长。
 */

   /*
   【初代笔记】
   题解：
	1.概念引入：
		1.1 minSum[i]:如果子数组必须以i开头，哪一个以i开头的子数组能取得最小的累加和，最小的累加和填入到minSum[i]中
		1.2 minSumEnd[i]: 必须以i开头的子数组，当它取得最小累加和时的右边界位置填入到minSumEnd[i]中；
	先从右往左遍历生成这两个数组，例子：
		  arr[1,2,-4,3,7,-2,4,-3]
			  0 1  2 3 4  5 6  7
	 minsum  [			 -2 1 -3]
	minsumEnd[			  5 7  7]
	①来到7位置的3，以7位置的3开头最小的累加和是3，最小累加和的右边界是7
	②来到6位置的4，以6位置的4开头最小的累加和，首先是当前位置的4，然后发现往右扩，minsum[7]=-3,能让我累加和变得更小，就吸收进来得到更小的累加和，minsum[6] = 4+（-3） = 1；
	③来到5位置的-2，以5位置-2开头最小的累加和，首先是当前位置的-2，然后发现往右扩，minsum[6]=1,不能让我累加和变得更小，就不往右扩了，minsum[5]就等于-2；
	...以此类推

	为什么从右往左遍历？——因为当minsum[i]和minSumEnd[i]定义确定了后，发现i位置的信息依赖于i+1位置的信息，意味着右边的东西得先算。

	2.大思路：用窗口
		2.1 先求必须以0开头的子数组累加和<=k的最长度：
			2.1.1 如果minSum[0] > k,那么以0位置开头的所有子数组累加和不可能<=k,0位置开头的子数组没有答案；
			2.1.1 如果minSum[0] <= k, 假设minSum[0] = x, minSumEnd[0] = a；
			接下来就要看一下以(a+1)位置开头的最小累加和是多少，假设minSum[a+1] = y, minSumEnd[a+1] = b, 如果两块累加和x+y还是小于k,
			那就要继续去看b+1位置开头的最小累加和是多少，再看看三块的累加和是不是还小于k
			2.2.2 直到某一块的累加和加上去之后，发现>k了，就停，收集答案。
		2.2 太抽象，举个形象一点的例子：【例子不好描述，看不懂就看视频，1：10】
		[a] [b] [c] [d] | [e] , 假设a+b+c+d这4块的累加和<=k,加上e这一块后就大于k了，那么0位置开头的答案就是从[a]到[d]的长度
		2.3 以0位置开头的子数组答案就求出来了，接下来求以1开头的，以2开头的...；但是这样窗口右边界每次都从头开始，时间复杂度就O(N^2)了；

		2.4 这就需要可能性舍弃了，涉及到可能性舍弃的都是非常难的！
			2.4.1 先明确一件事，我要求的是整体的最长子数组,那么如果我求出一个答案是14，那么长度<14的子数组其实我都可以舍弃了；
		也就是说，关注点从求每一个位置开头的情况下，子数组累加和<=k的最长长度 变成了 能不能把全局的最长长度推高；
			2.4.2 流程：从0开始扩了4块，[0..3][3..7][7...12][13],[14...18];
			[14...18]这一块加进来累加和就大于k了，此时先收集0位置的答案0-13;
			接下来来到1位置，累加和sum-arr[0], 我直接看1位置能不能把[14...18]这一块扩进来！窗口右边界不回退，只关注能不能把答案推高。

		2.5 右边界不回退，复杂度O(N),最优解
    */
public class Code03_LongestLessSumSubArrayLength {

    public static int maxLengthAwesome(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] minSums = new int[arr.length]; // 以i位置开头的情况下，取得的最小累加和是多少
        int[] minSumEnds = new int[arr.length]; // i位置开头情况下取得最小累加和的右边界
        minSums[arr.length - 1] = arr[arr.length - 1];// 从右往左填，最后1个位置直接填，就是自己
        minSumEnds[arr.length - 1] = arr.length - 1;
        // 算好minSums和minSumEnds
        for (int i = arr.length - 2; i >= 0; i--) {
            //minSums[i + 1] < 0，扩进来我i位置能变小，就扩进来
            if (minSums[i + 1] < 0) {
                minSums[i] = arr[i] + minSums[i + 1];
                minSumEnds[i] = minSumEnds[i + 1];
            } else {
                minSums[i] = arr[i];
                minSumEnds[i] = i;
            }
        }
        int end = 0; // 迟迟扩不进来那一块儿的开头位置。不回退。扩进来的位置[i,end)
        int sum = 0; // 目前扩进来的数的累加和
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            // while循环结束之后：
            // 1) 如果以i开头的情况下，累加和<=k的最长子数组是arr[i..end-1]，看看这个子数组长度能不能更新ans；
            // 2) 如果以i开头的情况下，累加和<=k的最长子数组比arr[i..end-1]短，更新还是不更新ans都不会影响最终结果；
            while (end < arr.length && sum + minSums[end] <= k) {
                sum += minSums[end];
                end = minSumEnds[end] + 1;
            }
            // end-i是i开头的时候标准的答案吗？不一定 可能是也可能不是，如果它不是长度一定较短，本来也不用关心，如果是就有可能推高最大值
            ans = Math.max(ans, end - i);
            if (i < end) { // 还有窗口，哪怕窗口没有数字 [i~end) [4,4)。只需要i++
                sum -= arr[i];
            } else { // i == end,  即将 i++, i > end, 此时窗口概念维持不住了，所以end跟着i一起走。end也要扩1个，i再++
                end++;
            }
        }
        return ans;
    }

    public static int maxLength(int[] arr, int k) {
        int[] h = new int[arr.length + 1];
        int sum = 0;
        h[0] = sum;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        sum = 0;
        int res = 0;
        int pre = 0;
        int len = 0;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            pre = getLessIndex(h, sum - k);
            len = pre == -1 ? 0 : i - pre + 1;
            res = Math.max(res, len);
        }
        return res;
    }

    public static int getLessIndex(int[] arr, int num) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        int res = -1;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] >= num) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    // for test
    public static int[] generateRandomArray(int len, int maxValue) {
        int[] res = new int[len];
        for (int i = 0; i != res.length; i++) {
            res[i] = (int) (Math.random() * maxValue) - (maxValue / 3);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 10000000; i++) {
            int[] arr = generateRandomArray(10, 20);
            int k = (int) (Math.random() * 20) - 5;
            if (maxLengthAwesome(arr, k) != maxLength(arr, k)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }

}
