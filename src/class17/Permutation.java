package class17;

/**
 * @author QIANHUIRONG
 * @date 2024-06-05 17:19
 */
import java.util.ArrayList;
import java.util.List;

public class Permutation {

    public static List<String> permutation1(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        boolean[] used = new boolean[str.length];
        StringBuilder path = new StringBuilder();
        f(str, used, path, ans);
        return ans;
    }

    public static void f(char[] str, boolean[] used, StringBuilder path, List<String> ans) {
        if (path.length() == str.length) {
            ans.add(path.toString()); // 收集一个完整的排列
        } else {
            for (int i = 0; i < str.length; i++) { // 遍历所有字符
                if (!used[i]) { // 只选择未使用的字符
                    used[i] = true; // 标记当前字符已使用
                    path.append(str[i]); // 将当前字符加入路径
                    f(str, used, path, ans); // 递归，继续选择下一个字符
//                    path.deleteCharAt(path.length() - 1); // 恢复现场，移除最后一个字符
                    used[i] = false; // 恢复标记，表示当前字符未使用
                }
            }
        }
    }

    public static void main(String[] args) {
        String s = "abc";
        List<String> result = permutation1(s);
        for (String perm : result) {
            System.out.println(perm);
        }
    }
}
