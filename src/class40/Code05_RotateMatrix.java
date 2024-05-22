package class40;

/**
 * 【题意】
 * 给定一个正方形矩阵matrix，原地调整成顺时针90度转动的样子
 */
/*
【时间】
题意，题解：1：59

 */
	// 空间复杂度 o(1)
/*
【思维导图】
1.
(0,0)(4,4)分别是左上角点跟右下角点两个点代表这一圈。这一圈里的东西转90度还停留在这一圈里，
既不可能转到里面去，也不可能转到外边来。接下来我让(0,0)这个点往右下方移动来到(1,1)位置，(4,4)
这个点往左上方移动来到(3,3)位置，这两个点就可以规定这个圈。这个圈里面所有的东西转90度，
只在这个圈里玩，既不可能转到里面去，也不可能坐到外边来。继续这样，一旦错过去了，说明调整结束了。
如果我能把每一个圈里的东西转好，这事解决了。

2.一圈里面的怎么办？
	分组调整。N*N的正方形就分为N-1组


 */
public class Code05_RotateMatrix {

	public static void rotate(int[][] matrix) {
		// 两个点：(a,b), (c,d)
		int a = 0;
		int b = 0;
		int c = matrix.length - 1;
		int d = matrix[0].length - 1;
		while (a <= c) { // 错过去了，也就调整好了，就跳出循环
			// 一圈中怎么调整
			rotateEdge(matrix, a++, b++, c--, d--);
		}
	}


	public static void rotateEdge(int[][] m, int a, int b, int c, int d) {
		int t = 0;
		for (int i = 0; i < d - b; i++) { // 一组中怎么调整
			t = m[a][b + i];
			m[a][b + i] = m[c - i][b];
			m[c - i][b] = m[c][d - i];
			m[c][d - i] = m[a + i][d];
			m[a + i][d] = t;
		}
	}



	public static void main(String[] args) {
		int[][] matrix = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
		printMatrix(matrix);
		rotate(matrix);
		System.out.println("=========");
		printMatrix(matrix);

	}

	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

}
