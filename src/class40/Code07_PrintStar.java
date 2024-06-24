package class40;

/**
 * 【题意】
 * 转圈打印N边星号正方形
 */
/*
【时间】
题意：2：19
题解：2：23
 */
/*
【思维导图】
分圈结构
一圈是两层。最外层的圈你想办法打搞定。左上角向右下方跳两个格来
右下角往左上方跳两个格，接下来又是两层算一个圈

 */
public class Code07_PrintStar {

    public static void printStar(int N) {

        // 初始化
        char[][] m = new char[N][N];
        // 转圈填*
        int a = 0;
        int b = 0;
        int c = N - 1;
        int d = N - 1;
        while (a <= c) {
            set2(m, a, b, c, d);
            // 一圈是2层
            a += 2;
            b += 2;
            c -= 2;
            d -= 2;
        }

        // 打印
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void set2(char[][] m, int a, int b, int c, int d) {
        for (int y = b; y < d; y++) {
            m[a][y] = '*';
        }
        for (int x = a; x < c; x++) {
            m[x][d] = '*';
        }
        for (int y = d; y > b; y--) {
            m[c][y] = '*';
        }
        for (int x = c - 1; x >= a + 2; x--) {
            m[x][b + 1] = '*';
        }
    }

    public static void set(char[][] m, int a, int c) {
        for (int col = a; col <= c; col++) {
            m[a][col] = '*';
        }
        for (int row = a + 1; row <= c; row++) {
            m[row][c] = '*';
        }
        for (int col = c - 1; col > a; col--) {
            m[c][col] = '*';
        }
        for (int row = c - 1; row > a + 1; row--) {
            m[row][a + 1] = '*';
        }
    }



    public static void main(String[] args) {
        printStar(10);
        //数组是引用类型，它的元素相当于类的成员变量，因此数组一经分配空间，其中的每个元素也被按照成员变量同样的方式被隐式
        //对于基本数据类型而言，默认初始化值各有不同
        //对于引用数据类型而言，默认初始化值为null(注意与0不同！)
        //char:默认值0，或者'\u0000'表现为空
        System.out.println("================");
        char[] chars = new char[5];
        for (char aChar : chars) {
            System.out.println(aChar == 0);
        }
    }

}
