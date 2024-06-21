package class26;

/*
题意：由0和1两种字符构成的达标字符串
给定一个数N，想象只由0和1两种字符，组成的所有长度为N的字符串
如果某个字符串,任何0字符的左边都有1紧挨着,认为这个字符串达标
返回有多少达标的字符串
 */
// 1：49
// 思维导图:
/*
方法一：
n=1: 0，1：1个达标
n=2: 00, 01, 10, 2个达标
n=3: 000, 001, 010, 011, 101, 100, 111，3个达标
1、肉眼观察得到1，2，3，5，8...，就是初始项是1，2的斐波那契数列

 */
/*
方法二：通过尝试，发现是斐波那契数列
1、int  f(int i), 前1个格子一定是1，还剩i个格子没有填，填完之后，达标的数有多少？
2、如果初始n=9，那么主函数调用f(8), 因为0位置一定填1
3、普遍位置i填1，那就去f（i-1); i填0，那么i+1格子一定填1，否则就00就不达标了，然后就可以调用f(i-2). 所以f(n) = f(n-1) + f(n-2)
 */

/*
加题：贴瓷砖问题
题意：贴瓷砖问题你有无限的1 x 2的砖块，要铺满2 x N的区域，不同的铺法有多少种?
1、地面大小一定是2 * N，fn表示干干净净的n*2的区域有几种方法？
2、第一种选择，第一个瓷砖竖着摆，fn=f(n-1); 第二种选择，第一个瓷砖横着摆，那么必须有第二个也横着摆，后序就是fn = f(n-2)； 所以fn=fn-1 + fn-2
 */

/*
精华：如果写递归发现是严格的递推式，请不要错过o(logn)的优化！
 */
public class Code03_ZeroLeftOneStringNumber {

	public static int getNum1(int n) {
		if (n < 1) {
			return 0;
		}
		return process(1, n);
	}

	public static int process(int i, int n) {
		if (i == n - 1) {
			return 2;
		}
		if (i == n) {
			return 1;
		}
		return process(i + 1, n) + process(i + 2, n);
	}

	public static int getNum2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int pre = 1;
		int cur = 1;
		int tmp = 0;
		for (int i = 2; i < n + 1; i++) {
			tmp = cur;
			cur += pre;
			pre = tmp;
		}
		return cur;
	}

	public static int getNum3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		int[][] base = { { 1, 1 }, { 1, 0 } };
		int[][] res = matrixPower(base, n - 2);
		return 2 * res[0][0] + res[1][0];
	}
	
	
	
	
	
	
	public static int fi(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		int[][] base = { { 1, 1 }, 
				         { 1, 0 } };
		int[][] res = matrixPower(base, n - 2);
		return res[0][0] + res[1][0];
	}

	
	
	
	public static int[][] matrixPower(int[][] m, int p) {
		int[][] res = new int[m.length][m[0].length];
		for (int i = 0; i < res.length; i++) {
			res[i][i] = 1;
		}
		int[][] tmp = m;
		for (; p != 0; p >>= 1) {
			if ((p & 1) != 0) {
				res = product(res, tmp);
			}
			tmp = product(tmp, tmp);
		}
		return res;
	}

	// 两个矩阵乘完之后的结果返回
	public static int[][] product(int[][] a, int[][] b) {
		int n = a.length;
		int m = b[0].length;
		int k = a[0].length; // a的列数同时也是b的行数
		int[][] ans = new int[n][m];
		for(int i = 0 ; i < n; i++) {
			for(int j = 0 ; j < m;j++) {
				for(int c = 0; c < k; c++) {
					ans[i][j] += a[i][c] * b[c][j];
				}
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		for (int i = 0; i != 20; i++) {
			System.out.println(getNum1(i));
			System.out.println(getNum2(i));
			System.out.println(getNum3(i));
			System.out.println("===================");
		}

	}
}
