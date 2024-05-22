package class40;

/**
 * 【题意】
 * 给定一个长方形矩阵matrix，实现转圈打印
 *
 * a b c d
 * e f g h
 * i j k L
 *
 * 打印顺序：a b c d h L k j I e f g
 */
/*
【时间】
题意、题解：2：16
 */
/*
【思维导图】
分圈结构
先想在这一个圈里怎么实现打印。结果你会发现下一个圈的第一个正好跟上一个圈的
最后一个接上了。
也就是说你只用解决在一个圈里这样打印就行了。下一个圈再这么打印，它每一个第
一个都会跟前一个最后一个接上。

 */

/**
 * 怎么写代码？——举例子
 * 先假设具体好理解的具体数据量把事串明白了再做抽象化，你硬写抽象化是特别困难的。
 * 硬憋抽象化是要出问题的。你除非能力很强。你把数据量假设一个具体的例子，亲近它，
 * 玩明白之后再做抽象化。一上来做抽象化，是要疯掉的，人脑子不是这样的，人脑子抽象
 * 化很弱的，我们只能理解具象的事物，你跟我都是凡人都一样的，那种特别牛的人抽象化
 * 程度很高，人家不牵扯到这种顾忌
 */
public class Code06_PrintMatrixSpiralOrder {

	public static void spiralOrderPrint(int[][] matrix) {
		int a = 0;
		int b = 0;
		int c = matrix.length - 1;
		int d = matrix[0].length - 1;
		// 因为是长方形，有可能会a和c提前碰到，就是一条横线；或者b和d提前碰到，就是一条竖线
		while (a <= c && b <= d) {
			printEdge(matrix, a++, b++, c--, d--);
		}
	}

	public static void printEdge(int[][] m, int a, int b, int c, int d) {
		if (a == c) {
			// 一条横线的情况
			for (int i = b; i <= d; i++) {
				System.out.print(m[a][i] + " ");
			}
		} else if (b == d) {
			// 一条竖线的情况
			for (int i = a; i <= c; i++) {
				System.out.print(m[i][b] + " ");
			}
		} else {
			// 是矩形的情况
			int x = a;
			int y = b;
			while (y != d) {
				System.out.print(m[a][y] + " ");
				y++;
			}
			while (x != c) {
				System.out.print(m[x][d] + " ");
				x++;
			}
			while (y != b) {
				System.out.print(m[c][y] + " ");
				y--;
			}
			while (x != a) {
				System.out.print(m[x][b] + " ");
				x--;
			}
		}
	}

	public static void main(String[] args) {
		int[][] matrix =
				{
						{ 1, 2, 3, 4 },
						{ 5, 6, 7, 8 },
						{ 9, 10, 11, 12 },
						{ 13, 14, 15, 16 }
				};
		spiralOrderPrint(matrix);

	}

}
