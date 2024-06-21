package class26;

/*
题意：
斐波那契数列f(n) = f(n-1) + f(n-2)，如果按照递归式求下去，O(n). 这种严格的递归式，有Log(n)的方法！
 */
/*
 时间：49
 一个数的n次方怎么算的足够快:1:03
 */

// 思维导图 语雀有详细笔记！！！
/*
	1、斐波那契数列f(n) = f(n-1) + f(n-2)，如果按照递归式求下去，O(n). 这种严格的递归式，有Log(n)的方法！（非严格的递归式，就是某些情况f(n)是一个公式，某些情况是另一个公式，就没有log(n)的方法）
	2、f(n) = f(n-1) + f(n-2)，减的最多的是2，这是一个二阶递推。（这里需要线性代数的知识，但是并不难。线性代数就是为了解决这种递推式的问题的）
	3、求base矩阵：|f3,f2| = |f2,f1| * |a,b|  , 列出前几项，就可以得到a=1,b=1,c=1,d=0;
						  			  |c,d|
	4、|fn,fn-1| = |f2,f1| * |x| ^ n-2。要想第n项算的快，只要|x|^n算的快就行了
 */

/*	一个数的^n怎么快的快
	1、比如10^75, 如果简单是10累乘75次就是O(n),不够快
	2、t跟自己玩的过程中按位决定乘不乘到ans里面去，ans初始=1。75二进制：1001011，10^75 = 1 * 10^1 * 10^2 * 10^8 ^ 10^64, 根据二进制上是否是1，决定t要不要累乘进去， t初始=10^1, 要累乘，ans*=10 = 10；t*t=10^2,要累乘；t*t=10^4,不要累乘...。
	3、t每次都跟自己累乘，要累乘到n，t最多玩log(2,n), 时间复杂度顶多就是log(n)

	4、抽象化：x^y
	t 初始是x^1; 每次跟自己累乘，变成x^2,x^4,x^8...
	根据y的二进制形式是否是1去累乘，1(单位1，如果是矩阵，就是单位矩阵) * t

	5、如果是矩阵：
	|x|^y
	t初始是|x|^1, 每次跟自己累乘，变成|x|^2,|x|^4,|x|^8...
	根据y的二进制形式是否是1去累乘，|1| * t; (单位矩阵就是对角线都是1，所以需要先生成和|x|一样大小的单位矩阵)


	4、推广到矩阵：ans初始为单位矩阵，就是对角线都是1
 */

/*
矩阵的乘法怎么算？

两个矩阵要能做乘法，那么必须是这种规则：
第一个矩阵：m * n;（m行n列）
第二个矩阵：n * p; (n行p列)
第一个矩阵的列数必须等于第二个矩阵的行数
他们相乘的结果是产生一个 m * p 规格的矩阵

A = 1，2，3
	4，5，6

B = 7，8
	9，10
	11，12

矩阵A是2*3的；矩阵B是3*2的，那么将产生一个2*2的矩阵
A的第一行与B的第一列对应元素相乘并求和：
C(1,1)=(1*7)+(2*9)+(3*11)=7+18+33=58
然后，A的第一行与B的第二列对应元素相乘并求和：
C(1,2)=(1*8)+(2*10)+(3*12)=8+20+36=64
接着，A的第二行与B的第一列对应元素相乘并求和：
C(2,1)=(4*7)+(5*9)+(6*11)=28+45+66=139
最后，A的第二行与B的第二列对应元素相乘并求和：
C(2,2)=(4*8)+(5*10)+(6*12)=32+50+72=154

所以矩阵A*B = 58, 64
			 139,154
 */

/*
大推广：
	1、Fn=a*f(n-1) + b*f(n-2) + ... + kf(n-i), 都有log(n)的方法
	2、最小是n-几，就是几阶。比如3阶，那么|fn,fn-1,fn-2| = |f3,f2,f1| * |x|^n-3, |x|是base矩阵，是3阶的
 */





/*
题意：母牛生小牛，N年后牛的数量
第一年农场有1只成熟的母牛A，往后的每年：
1）每一只成熟的母牛都会生一只母牛
2）每一只新出生的母牛都在出生的第三年成熟
3）每一只母牛永远不会死
返回N年后牛的数量


时间：1:30
对应本页面方法c3


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
		int[][] base =
				{
				{ 1, 1 }, 
				{ 1, 0 } 
				};

		// base矩阵的n-2次方
		int[][] res = matrixPower(base, n - 2);
		// |fn,fn-1| = |f2,f1| * |x| ^ n-2
		// |fn,fn-1| = |1,1| * |a,b|
		//                     |c,d|
		// |fn,fn-1| = |a+c,b+d|
		// fn = a + c
		return res[0][0] + res[1][0];
	}


	public static int[][] matrixPower(int[][] m, int n) {
		// 先生成m矩阵一样大小的单位矩阵
		int[][] res = new int[m.length][m[0].length]; // 结果是第一个矩阵的行*第二个矩阵的列
		for (int i = 0; i < res.length; i++) {
			res[i][i] = 1; // 单位矩阵
		}
		while (n != 0) { // n已经看成二进制的形式
			if ((n & 1) != 0) { // 最末尾是1，就要累乘。乘完之后，右移
				res = product(res, m);
			}
			// 每次都跟自己乘
			m = product(m, m);
			n >>= 1;
		}
		return res;
	}

	// 两个矩阵乘完之后的结果返回
	public static int[][] product(int[][] a, int[][] b) {
		int n = a.length;
		int m = b[0].length;
		int[][] ans = new int[n][m]; // a的行 * b的列
		for(int i = 0 ; i < n; i++) {
			for(int j = 0 ; j < m;j++) { // 这2个循环是循环ans去填
				for (int k = 0; k < a[0].length; k++) { // 每个位置，去累乘。k一共需要a的列数个，不理解可以看下上面的例子。
					ans[i][j] += a[i][k] * b[k][j];
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
