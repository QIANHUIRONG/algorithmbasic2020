package class22;

/*
题意：
给定一个正整数n,求裂开的方法数
裂开的时候，数字不能比前面的小。
给定一个正数1，裂开的方法有一种，(1) 给定一个正数2，裂开的方法有两种，(1和1)、(2) 给定一个正数3，裂开的方法有三种，(1、1、1)、(1、2)、(3)
给定一个正数4，裂开的方法有五种，(1、1、1、1)、(1、1、2)、(1、3)、(2、2)、 (4)
给定一个正数n，求裂开的方法数。
 */
/*
时间：2：03
 */
/*
思维导图
   1.
    process(int pre, int rest) {}：上一个拆出来的数是pre，还剩rest需要去拆，返回拆解的方法数
    basecase：
        如果rest等于0了，不需要拆了，返回1种方法（求方法数的递归一般都这样），叫做之前做过的决定；
        如果拆的过程pre>rest, 直接返回0种方法，告诉上游这种拆法无效
    普遍流程：从pre开始到rest中的每个数字都可以拆，累加上所有的方法数

    2.dp
    3.斜率优化
 */
public class Code03_SplitNumber {

    // n为正数
    public static int ways(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return process(1, n);
    }

    // 上一个拆出来的数是pre
    // 还剩rest需要去拆
    // 返回拆解的方法数
    public static int process(int pre, int rest) {
        if (rest == 0) {
            return 1;
        }
        if (pre > rest) {
            return 0;
        }
        int ways = 0;
        for (int first = pre; first <= rest; first++) {
            ways += process(first, rest - first);
        }
        return ways;
    }

    /*
    dp：
     */
    public static int dp1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
        }
        for (int pre = n; pre >= 1; pre--) {
            for (int rest = pre; rest <= n; rest++) {
                int ways = 0;
                for (int first = pre; first <= rest; first++) {
                    ways += dp[first][rest - first];
                }
                dp[pre][rest] = ways;
            }
        }
        return dp[1][n];
    }

    /*
    斜率优化
     */
    public static int dp2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
        }
        for (int pre = n; pre >= 1; pre--) {
            for (int rest = pre; rest <= n; rest++) {
                dp[pre][rest] = dp[pre][rest - pre]; // rest是从pre开始的，也就是说rest>=pre,rest - pre 这里一定不会越界
                if (pre + 1 <= n) {
                    dp[pre][rest] += dp[pre + 1][rest];
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        int test = 39;
        System.out.println(ways(test));
        System.out.println(dp1(test));
        System.out.println(dp2(test));
    }

}
