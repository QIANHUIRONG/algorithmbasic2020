package class17;

import java.util.ArrayList;
import java.util.List;

/*
题目：打印字符串的全排列
permutation 英/ˌpɜːmjuˈteɪʃn/ 排列
 */
/*
时间：
方法一：
流程：1:14
code：1：16
恢复现场：1：24

方法二：好一点的递归：1：27
 */
/*
思维导图
思路一：
1.全排列：说白了就是所有的字符都得要，只不过顺序可以不一样
2.a b c d, 0位置是4个字符选1个，1位置是剩下的3个字符选一个... 如果我们所有的分支都摊开，收集所有的结果，这个问题就解了

思路二：


 */
public class Code04_PrintAllPermutations {

    /**
     * 一个比较差的实现。（可变参数差）。不过比较符合自然智慧
     * 下面有比较好的实现
     */
    public static List<String> permutation1(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        ArrayList<Character> rest = new ArrayList<Character>();
        for (char cha : str) {
            rest.add(cha);
        }
        String path = "";
        f(rest, path, ans);
        return ans;
    }

    public static void f(ArrayList<Character> rest, String path, List<String> ans) {
        if (rest.isEmpty()) {
            ans.add(path); // 已经没办法做决定了，收集答案，这个答案就是你之前做过的决定
        } else {
            int N = rest.size();
            for (int i = 0; i < N; i++) { // 当前集合中，每一个字符都可以做当前字符
                char cur = rest.get(i); // 当前字符
                rest.remove(i); // 当前字符用了，就从集合中移除
                f(rest, path + cur, ans); // 我当前的决定要加到path中
                // 恢复现场
                // 解释一下：比如a b c, 第一个字符选了a，那么接下来就b c去跑递归收集答案；收集完之后，第二个字符选了b，那么接下来应该是 a c去跑递归收集答案。如果这里不恢复现场，接下来就只剩c去跑递归了。
                // 我的递归需要我数据的原始时刻
                rest.add(i, cur);
            }
        }
    }


    public static List<String> permutation4(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        ArrayList<Character> rest = new ArrayList<Character>();
        for (char cha : str) {
            rest.add(cha);
        }
        String path = "";
        boolean[] used = new boolean[str.length];
        f(rest, path, ans);
        return ans;
    }
    /**
     上面的方法，涉及到了遍历list删除元素，这个是有危险的；
     找gpt改了一版，用一张表标记i号元素用过没有
     本质上，i位置的元素用过了，去跑深度递归的时候，i位置的元素移除的本质就是不让再使用，那我们用个表标记就行了 --> 还有问题。。还没看
     */
    public static void f2(ArrayList<Character> rest, String path, List<String> ans, boolean[] used) {
        if (rest.isEmpty()) {
            ans.add(path); // 已经没办法做决定了，收集答案，这个答案就是你之前做过的决定
        } else {
            int N = rest.size();
            for (int i = 0; i < N; i++) { // 当前集合中，每一个字符都可以做当前字符
                if (!used[i]) {
                    used[i] = true;
                    f(rest, path + rest.get(i), ans); // 我当前的决定要加到path中
                    used[i] = false;
                }
            }
        }
    }

    /**
     * 一个比较好的递归。
     * 用交换模拟了全排列
     * 如果你的算法需要原始时刻，不要忘了恢复现场！！！
     *
     * @param s
     * @return
     */
    public static List<String> permutation2(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        g1(str, 0, ans);
        return ans;
    }

    public static void g1(char[] str, int index, List<String> ans) {
        if (index == str.length) {
            ans.add(String.valueOf(str));
        } else {
            for (int i = index; i < str.length; i++) {
                swap(str, index, i);
                g1(str, index + 1, ans);
                swap(str, index, i);
            }
        }
    }

    /**
     * 去重的版本
     *
     * @param s
     * @return
     */
    public static List<String> permutation3(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        g2(str, 0, ans);
        return ans;
    }

    public static void g2(char[] str, int index, List<String> ans) {
        if (index == str.length) {
            ans.add(String.valueOf(str));
        } else {
            /**
             * 如果收集完所有的结果用set去重，也行，但是这样是递归跑了所有的分支，最后过滤。
             * 而剪枝是，如果发现这个递归不用走，我提前杀死，快多了！
             */
            boolean[] visited = new boolean[256];
            for (int i = index; i < str.length; i++) {
                if (!visited[str[i]]) { // i位置的字符如果是试过的，不走。交换完了，如果不是重复的，才会跑递归。比如aca，0位置的a和2位置的a交换后，发现是一样的，就不跑了
                    visited[str[i]] = true;
                    swap(str, index, i);
                    g2(str, index + 1, ans);
                    swap(str, index, i);
                }
            }
        }
    }

    public static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }

    public static void main(String[] args) {
        String s = "abc";
        List<String> ans1 = permutation1(s);
        for (String str : ans1) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans2 = permutation2(s);
        for (String str : ans2) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans3 = permutation3(s);
        for (String str : ans3) {
            System.out.println(str);
        }

        List<String> ans4 = permutation4(s);

        for (String str : ans4) {
            System.out.println(str);
        }

    }

}
