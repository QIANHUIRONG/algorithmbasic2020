package class02;
/*
 [题意]
 本页有4道题目

*/

/*
[时间]

 */

// 时复：

/*
[思维导图]

 */
public class Code02_EvenTimesOddTimes {



	/*
	题目一：怎么把一个int类型的数，最右侧的1提取出来
	题解：
		X & (~X + 1)就是答案；
		而(~X + 1) = -X, 所以X & -X也一样
	 */

	/**
	 * 题目二：
	 * 	统计一个int数二进制有多少个1
	 *
	 * 题解：每次提取最右侧的1，然后再消掉最右侧的1，直到数为0
	 * 怎么提取：x & -x;
	 * 怎么消除：最右侧的1用1个变量抓住，和原来的数字^
	 * @param N
	 * @return
	 */
	public static int bit1counts(int N) {
		int count = 0;
		while(N != 0) {
			int rightOne = N & -N;
			N ^= rightOne;
			count++;
		}

		return count;
	}

	/*
    题目三：一个数组中有一种数出现了奇数次，其他数出现了偶数次，怎么找到并打印这种数？
    题解：
        解法1：哈希表做词频统计
        解法2：只用一个变量遍历1次；
        用一个变量去异或上这个数组所有的数，偶数次的数都会变成0，因为N^N=0;最终留下来的数就是那个奇数次的数。
     */
	public static void printOddTimesNum1(int[] arr) {
		int eor = 0;
		for (int i = 0; i < arr.length; i++) {
			eor ^= arr[i];
		}
		System.out.println(eor);
	}

	/*
	题目四：一个数组中有两个数出现了奇数次，其他数都出现了偶数次，怎么找到这两种数
	题解：
		1.假设这两种出现奇数次的数是a和b，先用一个变量eor去^整个数组，最终eor = a ^ b；因为a != b, 所以eor一定不为0
		2.既然eor一定不为0，那就一定存在某一位上是1！假设从右往左第3位是1;
		3.再来一个变量eor`，初始为0, 再遍历数组，如果第三位上是1的，就让eor` ^ 这个数，因为偶数次的还是全部消掉，
	最终eor`就是a,b中第3位上是1的那个数；
		4.a,b中的另一个数就让eor ^ eor`也能得到。

	 */
	public static void printOddTimesNum2(int[] arr) {
		int eor = 0;
		for (int i = 0; i < arr.length; i++) {
			eor ^= arr[i];
		}
		int rightOne = eor & (-eor); // 提取出最右的1

		int onlyOne = 0; // eor'
		for (int i = 0 ; i < arr.length;i++) {
			if ((arr[i] & rightOne) != 0) {
				onlyOne ^= arr[i];
			}
		}
		System.out.println(onlyOne + " " + (eor ^ onlyOne));
	}

	
	
	public static void main(String[] args) {
		int a = 5;
		int b = 7;

		a = a ^ b;
		b = a ^ b;
		a = a ^ b;

		System.out.println(a);
		System.out.println(b);

		int[] arr1 = { 3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1 };
		printOddTimesNum1(arr1);

		int[] arr2 = { 4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2 };
		printOddTimesNum2(arr2);

	}

}
