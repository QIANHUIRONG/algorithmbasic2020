package class26;

// 时间：49
/*
	1、斐波那契数列f(n) = f(n-1) + f(n-2)，如果按照递归式求下去，O(n). 这种严格的递归式，有Log(n)的方法！（非严格的递归式，就是某些情况f(n)是一个公式，某些情况是另一个公式，就没有log(n)的方法）
	2、f(n) = f(n-1) + f(n-2)，减的最多的是2，这是一个二阶递推。（这里需要线性代数的知识，但是并不难。线性代数就是为了解决这种递推式的问题的）
	3、求base矩阵：|f3,f2| = |f2,f1| * |a,b|  , 列出前几项，就可以得到a=1,b=1,c=1,d=0;
						  			  |c,d|
	4、|fn,fn-1| = |f2,f1| * |x| ^ n-2。要想第n项算的快，只要|x|^n算的快就行了
 */
/*	一个数的^n怎么快的快
	1、比如10^75, 如果简单是10累乘75次就是O(n),不够快
	2、t跟自己玩的过程中按位决定乘不乘到ans里面去，ans初始=1。75二进制：1001011， t初始=10^1, 要累乘，ans*=10 = 10；t*t=10^2,要累乘；t*t=10^4,不要累乘...。
	3、t只跟自己玩log(n)次
	4、推广到矩阵：ans初始为单位矩阵，就是对角线都是1
 */
/*
大推广：
	1、Fn=a*f(n-1) + b*f(n-2) + ... + kf(n-i), 都有log(n)的方法
	2、最小是n-几，就是几阶。比如3阶，那么|fn,fn-1,fn-2| = |f3,f2,f1| * |x|^n-3, |x|是base矩阵，是3阶的
 */
/*
母牛生小牛问题：1：30 -> 方法c3
1、f(n) = f(n-1) + f(n-3),f(1) = 1 牛不会死， f(n-1):去年的牛；f(n-3):3年前的牛
2、如果牛第5年死了，就是f(n) = f(n-1) + f(n-3) - f(n-5), 就是5阶问题
 */
public class Code02_FibonacciProblem {

	// 普通递归
	public static int f1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		return f1(n - 1) + f1(n - 2);
	}

	// 普通迭代
	public static int f2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		int res = 1;
		int pre = 1;
		int tmp = 0;
		for (int i = 3; i <= n; i++) {
			tmp = res;
			res = res + pre;
			pre = tmp;
		}
		return res;
	}

	// 矩阵快速幂技巧：O(logN)
	public static int f3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		// 1、base矩阵
		int[][] base = { 
				{ 1, 1 }, 
				{ 1, 0 } 
				};

		// base矩阵的n-2次方
		int[][] res = matrixPower(base, n - 2);
		// |fn,fn-1| = |f2,f1| * |x| ^ n-2
		// |fn,fn-1| = |1,1| * |a,b|
		//                     |c,d|
		// |fn,fn-1| = |a+c,b+d|
		return res[0][0] + res[1][0];
	}

	public static int[][] matrixPower(int[][] m, int p) {
		int[][] res = new int[m.length][m[0].length];
		for (int i = 0; i < res.length; i++) {
			res[i][i] = 1;
		}
		// res = 矩阵中的1
		int[][] t = m;// 矩阵1次方
		for (; p != 0; p >>= 1) {
			if ((p & 1) != 0) { // 最末尾是1，就要累乘。乘完之后，右移
				res = product(res, t);
			}
			// t矩阵每次都跟自己乘。
			t = product(t, t);
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

	public static int s1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		return s1(n - 1) + s1(n - 2);
	}

	public static int s2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		int res = 2;
		int pre = 1;
		int tmp = 0;
		for (int i = 3; i <= n; i++) {
			tmp = res;
			res = res + pre;
			pre = tmp;
		}
		return res;
	}

	public static int s3(int n) {
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

	public static int c1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		return c1(n - 1) + c1(n - 3);
	}

	public static int c2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		int res = 3;
		int pre = 2;
		int prepre = 1;
		int tmp1 = 0;
		int tmp2 = 0;
		for (int i = 4; i <= n; i++) {
			tmp1 = res;
			tmp2 = pre;
			res = res + prepre;
			pre = tmp1;
			prepre = tmp2;
		}
		return res;
	}

	/*
	母牛生小牛问题
	 */
	public static int c3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		int[][] base = { 
				{ 1, 1, 0 }, 
				{ 0, 0, 1 }, 
				{ 1, 0, 0 } };

		int[][] res = matrixPower(base, n - 3);
		// |fn,fn-1，fn-3| = |f3,f2,f1| * |x| ^ n-3
		// |fn,fn-1,fn-3| = |3,2,1| * |a,b,d|
		//                     		  |e,f,g|
		//                     		  |h,i,j|
		return 3 * res[0][0] + 2 * res[1][0] + res[2][0];
	}

	public static void main(String[] args) {
		int n = 19;
		System.out.println(f1(n));
		System.out.println(f2(n));
		System.out.println(f3(n));
		System.out.println("===");

		System.out.println(s1(n));
		System.out.println(s2(n));
		System.out.println(s3(n));
		System.out.println("===");

		System.out.println(c1(n));
		System.out.println(c2(n));
		System.out.println(c3(n));
		System.out.println("===");

	}

}
