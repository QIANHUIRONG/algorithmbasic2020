package class03;



/*
 [题意]
 用递归求数组中的最大值
*/

/*
[时间]
Master公式：1：52
 */

// 时复：
// 空复：

/*
[思维导图]
1.递归函数要实现的逻辑定义好之后，要有黑盒思维，就拿起来用
2.如果要扣每一层的细节，画递归调用逻辑图

3.写递归：
	1.basecase,递归终止条件
	2.处理当前层
	3.进入下一级递归
	4.现场恢复（非必须）

4.Master公式.--> 子问题的规模都是一样的才能用，子问题规模不一样不能用māster公式（面试不需要掌握）
形如
T(N)=a*T(N/b)+O(N^d)(其中的a、b、d都是常数)的递归函数，可以直接通过Master公式来确定时间复杂度
如果log(b,a)<d,复杂度为O(n^d)
如果log(b,a)>d,复杂度为O(n^log(b,a))
如果log(b,a)==d,复杂度为O(n^d * logn)

本题目：
T(n) = 2*T(n/2) + o(1), a = 2, b = 2, d = 0
log(b,a) = 1 > d, 所以复杂度： o(N^1) , 所以其实这个递归和遍历一遍是一样的，都是o(n)

5.master公式要求每个部分的复杂度一致才可以求解，复杂度不一致无法使用，比如：
T(N)=T(1/3N)+T(2/3N)+O(N)不可以使用公式求解
·子问题的规模必须是一样的，这一类递归能用master公式来估计时间复杂度
·子问题规模不一致的递归你只能用数学新进的方法去求，对于面试，不需要掌握

 */
public class Code08_GetMax {

	// 求arr中的最大值
	public static int getMax(int[] arr) {
		return process(arr, 0, arr.length - 1);
	}

	// arr[L..R]范围上求最大值  L ... R   N
	public static int process(int[] arr, int L, int R) {
		// arr[L..R]范围上只有一个数，直接返回，base case
		if (L == R) { 
			return arr[L];
		}
		// L...R 不只一个数
		// mid = (L + R) / 2
		int mid = L + ((R - L) >> 1); // 中点   	1
		int leftMax = process(arr, L, mid);
		int rightMax = process(arr, mid + 1, R);
		return Math.max(leftMax, rightMax);
	}

}
