package class14;

import java.util.Arrays;
import java.util.Comparator;


/*
题意：
会议室能容纳的最多宣讲场次
 */

/*
时间：
 */

/*
题解：
方法一：纯暴力
process(Program[] programs, int done, int timeLine) ：目前来到timeLine的时间点，已经安排了done多的会议，剩下的会议programs可以自由安排，返回能安排的最多会议数量

方法二：贪心
按照会议结束时间早，先安排

 */
public class Code01_BestArrange {

    public static class Program {
        public int start;
        public int end;

        public Program(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    /*
    方法一：纯暴力
     */
    // 暴力！所有情况都尝试！
    public static int bestArrange1(Program[] programs) {
        if (programs == null || programs.length == 0) {
            return 0;
        }
        return process(programs, 0, 0);
    }

    // 还剩下的会议都放在programs里
    // done之前已经安排了多少会议的数量
    // timeLine目前来到的时间点是什么

    // 目前来到timeLine的时间点，已经安排了done多的会议，剩下的会议programs可以自由安排
    // 返回整体能安排的最多会议数量
    public static int process(Program[] programs, int done, int timeLine) {
        if (programs.length == 0) { // 如果没有剩余的会议可以安排，返回已安排的会议数量
            return done;
        }

        // 初始化最大会议安排数为 Integer.MIN_VALUE
        int max = Integer.MIN_VALUE;

        // 遍历剩余的每一个会议
        for (int i = 0; i < programs.length; i++) {
            // 如果当前会议的开始时间不早于当前时间线
            if (programs[i].start >= timeLine) {
                // 创建一个新的数组，排除当前会议，递归处理剩余会议
                Program[] next = copyButExcept(programs, i);
                // 更新最大安排会议数
                max = Math.max(max, process(next, done + 1, programs[i].end));
            }
        }

        // 如果 max 仍为 Integer.MIN_VALUE，说明没有找到符合条件的会议，返回 done
        // 否则，返回最大安排会议数。此时max一定是大于done的，因为至少选了一个会议
        return max == Integer.MIN_VALUE ? done : max;
    }

    public static Program[] copyButExcept(Program[] programs, int i) {
        Program[] ans = new Program[programs.length - 1];
        int index = 0;
        for (int k = 0; k < programs.length; k++) {
            if (k != i) {
                ans[index++] = programs[k];
            }
        }
        return ans;
    }

    /*
    方法二：贪心
    贪心策略：按照会议结束时间早，先安排
     */
    // 会议的开始时间和结束时间，都是数值，不会 < 0
    public static int bestArrange2(Program[] programs) {
        Arrays.sort(programs, new ProgramComparator());
        int timeLine = 0;
        int done = 0;
        // 依次遍历每一个会议，结束时间早的会议先遍历
        for (int i = 0; i < programs.length; i++) {
            // 当前会议的开始时间必须在timeLine之后
            if (programs[i].start >= timeLine) {
                done++;
                timeLine = programs[i].end;
            }
        }
        return done;
    }

    public static class ProgramComparator implements Comparator<Program> {

        // 按照结束时间由小到大排序
        @Override
        public int compare(Program o1, Program o2) {
            return o1.end - o2.end;
        }

    }

    // for test
    public static Program[] generatePrograms(int programSize, int timeMax) {
        Program[] ans = new Program[(int) (Math.random() * (programSize + 1))];
        for (int i = 0; i < ans.length; i++) {
            int r1 = (int) (Math.random() * (timeMax + 1));
            int r2 = (int) (Math.random() * (timeMax + 1));
            if (r1 == r2) {
                ans[i] = new Program(r1, r1 + 1);
            } else {
                ans[i] = new Program(Math.min(r1, r2), Math.max(r1, r2));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int programSize = 12;
        int timeMax = 20;
        int timeTimes = 1000000;
        for (int i = 0; i < timeTimes; i++) {
            Program[] programs = generatePrograms(programSize, timeMax);
            if (bestArrange1(programs) != bestArrange2(programs)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }

}
