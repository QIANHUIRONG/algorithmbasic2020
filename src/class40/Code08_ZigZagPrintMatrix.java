package class40;

/*
【题目】
zigzag打印矩阵
 */
/*
题意、题解：2：24

 */

/*
【思维导图】
1.
你要是把眼光盯在局部位置怎么变上你被玩死了。我们刚才说的分圈结构其实是一种拔出局部想法之外的一种，让你刻意的去找宏观感的一种东西。
2.所以给你带来的想法就是你怎么样跳出局部一旦遇到这种类似矩阵调整的问题和打印的问题，就设计宏观过程。
有一个A点，它一开始来自于左上角，有1个B点，它也来自于左上角，我规定了A先往右，走到不能再右了再往下。我规定B先往下走，
走到不能再下了，再往右。A,B运动过程中它俩会压中一条直直的斜线，每次斜线的方向是相反的，搞出来了。你只要写一个简单函数给你
一条直直的斜线，要么往上方打印，要么往下方打印，接下来AB一起走路，这事解决了。
 */
public class Code08_ZigZagPrintMatrix {

    public static void printMatrixZigZag(int[][] matrix) {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int N = matrix.length;
        int M = matrix[0].length;
        boolean fromUp = false;
        while (a < N) { // a最终会到达第N-1行，此时，b是M-1, C是N-1，D是M-1, 也就是齐聚右下角
            printLevel(matrix, a, b, c, d, fromUp);
            // (a,b)初始在(0,0), 先往右走，走到尽头再往下走。
            // (c,d)初始在(0,0), 先往下走，再往右走
            a = b == M - 1 ? a + 1 : a; // 一开始b先往右走，先更新a，下一行再更新b
            b = b == M - 1 ? b : b + 1;
            d = c == N - 1 ? d + 1 : d;
            c = c == N - 1 ? c : c + 1;
            fromUp = !fromUp;
        }
        System.out.println();
    }

    public static void printLevel(int[][] m, int a, int b, int c, int d, boolean f) {
        if (f) {
            while (a <= c && b >= d) {
                System.out.print(m[a++][b--] + " ");
            }
        } else {
            while (c >= a && d <= b) {
                System.out.print(m[c--][d++] + " ");
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        printMatrixZigZag(matrix);

    }

}
