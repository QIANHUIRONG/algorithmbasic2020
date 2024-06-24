package class22;

/*
题意：英雄砍死怪兽的概率
给定3个参数，N，M，K
怪兽有N滴血，等着英雄来砍自己
英雄每一次打击，都会让怪兽流失[0-M]的血量
到底流失多少?每一次在[0~M]上等概率的获得一个值
求K次打击之后，英雄把怪兽砍死的概率
 */
/**
 * 时间：4
 * 斜率优化：46
 */
/*
思维导图：
[一个样本做行一个样本做列的对应模型]
1.所有情况数：Math.pow(M + 1, K)
2.收集野怪死掉的情况数，血量只要<=0, 就收集一个点数，不剪枝
3.
    process(int times, int hp, int M) {}：怪兽还剩hp点血，还有times次可以砍，返回砍死的情况数！
    basecase：
        如果没有次数砍了，血量<=0就获得一个死亡数
        提前砍死了，直接返回接下来递归所有的死亡数。
    普遍流程：
        每次在[0-M]等概率流失血量，所以[0-M]展开，所有的情况累加

4.dp
5.斜率优化


 */
public class Code01_KillMonster {

    /**
     * 暴力递归
     *
     * @param N 初始血量
     * @param M 每次伤害在[0,M]上
     * @param K 可以砍K次
     * @return
     */
    public static double right(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K); // 所有情况
        long kill = process(K, N, M); // 死掉的情况
        return (double) ((double) kill / (double) all);
    }

    // 怪兽还剩hp点血
    // 每次的伤害在[0~M]范围上
    // 还有times次可以砍
    // 返回砍死的情况数！
    public static long process(int times, int hp, int M) {
        if (times == 0) {
            return hp <= 0 ? 1 : 0;
        }
        // 提前砍死了，可以直接得出接下来递归所有的死亡数。这里所有的死亡数都需要收集，不能剪枝
        // 不写这个basecase也对，只不过后面动态规划的时候，会有边界问题，然后反过来补充了这个basecase
        if (hp <= 0) {
            return (long) Math.pow(M + 1, times);
        }
        long ways = 0;
        for (int i = 0; i <= M; i++) {
            ways += process(times - 1, hp - i, M);
        }
        return ways;
    }

    /**
     * 动态规划
     *
     * @param N
     * @param M
     * @param K
     * @return
     */
    public static double dp1(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K);
        long[][] dp = new long[K + 1][N + 1];
        // 第0行。其他位置都是0
        dp[0][0] = 1;
        // 第0列
        for (int times = 1; times <= K; times++) {
            dp[times][0] = (long) Math.pow(M + 1, times);
        }
        for (int times = 1; times <= K; times++) {
            for (int hp = 1; hp <= N; hp++) {
                long ways = 0;
                for (int i = 0; i <= M; i++) {
                    if (hp - i > 0) {
                        ways += dp[times - 1][hp - i];
                    } else {  // 递归中是调用dp[times - 1][hp - i]，但是这里的hp - i会越界。dp[times - 1][hp - i]就是(long) Math.pow(M + 1, times - 1)
                        ways += (long) Math.pow(M + 1, times - 1);
                    }
                }
                dp[times][hp] = ways;
            }
        }
        long kill = dp[K][N];
        return (double) ((double) kill / (double) all);
    }

    /**
     * 斜率优化
     *
     * @param N
     * @param M
     * @param K
     * @return
     */
    public static double dp2(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K);
        long[][] dp = new long[K + 1][N + 1];
        // 第0行。其他位置都是0
        dp[0][0] = 1;
        // 第0列
        for (int times = 1; times <= K; times++) {
            dp[times][0] = (long) Math.pow(M + 1, times);
        }
        for (int times = 1; times <= K; times++) {
            for (int hp = 1; hp <= N; hp++) {
                dp[times][hp] = dp[times][hp - 1] + dp[times - 1][hp];
                if (hp - 1 - M > 0) { // 递归中是调用dp[times - 1][hp - i]，但是这里的hp - i会越界。dp[times - 1][hp - i]就是(long) Math.pow(M + 1, times - 1)
                    dp[times][hp] -= dp[times - 1][hp - 1 - M];
                } else {
                    dp[times][hp] -= Math.pow(M + 1, times - 1);
                }
            }
        }
        long kill = dp[K][N];
        return (double) ((double) kill / (double) all);
    }

    public static void main(String[] args) {
        int NMax = 10;
        int MMax = 10;
        int KMax = 10;
        int testTime = 200;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int N = (int) (Math.random() * NMax);
            int M = (int) (Math.random() * MMax);
            int K = (int) (Math.random() * KMax);
            double ans1 = right(N, M, K);
            double ans2 = dp1(N, M, K);
            double ans3 = dp2(N, M, K);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops!");
                break;
            }
        }
        System.out.println("测试结束");
    }

}
