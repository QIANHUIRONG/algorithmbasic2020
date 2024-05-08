package class17;

import java.util.ArrayList;
import java.util.List;

/*
题目：打印字符串的全排列 1:14
permutation 英/ˌpɜːmjuˈteɪʃn/ 排列

 */
public class Code04_PrintAllPermutations {

    /**
     * 一个比较差的实现。（可变参数差）。不过比较符合自然智慧
     * 下面有比较好的实现
     *
     * @param s
     * @return
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
            ans.add(path);
        } else {
            int N = rest.size();
            for (int i = 0; i < N; i++) {
                char cur = rest.get(i);
                rest.remove(i);
                f(rest, path + cur, ans);
                rest.add(i, cur);
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
        String s = "acc";
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

    }

}
