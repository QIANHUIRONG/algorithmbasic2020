package class17;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
题意：
打印一个字符串的全部子序列

 */
/*
时间
打印一个字符串的全部子序列：1小时00分
要求不要出现重复字面值的子序列 1：12
 */

/*
思维导图
一、子串和子序列和全排列
	1.子串：子串必须是连续的。如果不用递归，用迭代，两个for循环就可以枚举所有的子串：
		for (int i = 0; i < str.length(); i++) { // 外层循环：确定子串的起始位置
            for (int j = i + 1; j <= str.length(); j++) { // 内层循环：确定子串的结束位置
                String substring = str.substring(i, j); // 提取子串
                System.out.println(substring); // 打印子串
            }
        }
    2.子序列：在原始字符串中从左往右依次拿字符，可以不连续，但是要求你相对次序不能乱。每个位置的字符要跟不要都走一遍
    3.全排列：全排列是指将字符串中的所有字符进行排列，生成所有可能的顺序。0位置从n个的字符选1个，1位置从剩下的n-1个字符选一个...

二、打印一个字符串的全部子序列
	1.process1(int index, String path, char[] str, List<String> ans)，来到了str[index]字符，index是位置，之前的决定，都在path上，
	之前已经确定，而后面还能自由选择的话，把所有生成的子序列，放入到ans里去
	2.basecase：index来到字符串结尾，不能做决定了，就把之前做的决定path加到ans里
	3.普遍过程：要了index位置的字符，不要index位置的字符


三、打印一个字符串的全部子序列，要求不要出现重复字面值的子序列
	1.process1(int index, String path, char[] str, List<String> ans) 将List替换成set去收集答案：process1(int index, String path, char[] str, HashSet<String> ans)

 */
/*
四、
 如果递归是需要收集List<String> ans的，是要在入参中带上，还是加在返回值上呢？可以在入参中带上。我们先模仿，先不讨论哪种好
 如果递归需要之前递归做过的决定，比如这里的path，那么也在入参中带上。 -> 但是这样子好像无法处理成动态规划？

 如果需要收集List<String> ans的，递归函数的返回参数是void，那么就直接去跑递归，递归会帮你收集答案到ans中
 如果递归函数是有返回参数的情况，那么可能需要下游的递归返回给我结果，我才能处理当前层



来自GPT:
在递归算法中，如何处理结果收集有两种主要方法：通过参数传递结果容器，或者通过返回值返回结果容器。每种方法有其特点和适用场景。让我们详细讨论这两种方法。
方法一：通过参数传递结果容器
    这种方法是在递归函数的参数中传递一个结果容器（如 List<String>）。结果容器在整个递归过程中共享，最终包含所有结果。
特点
    效率高：共享一个结果容器，避免了在每次递归调用时创建新的容器，节省内存和时间。
    简洁的返回逻辑：递归函数不需要返回结果，逻辑更清晰。
    灵活性高：适用于需要在递归过程中随时更新和访问结果的场景。
示例代码：就是本页面的代码

方法二：通过返回值返回结果容器
    这种方法是在每次递归调用时返回一个包含结果的容器，最终在初始调用处合并结果。
特点
    清晰的递归结构：每个递归调用返回一个结果容器，逻辑上更加自包含和独立。
    容易理解：适合需要合并子问题结果的场景，特别是在树形结构或图形结构的递归处理中。
    性能较低：每次递归调用都会创建新的容器，可能导致较高的内存和时间消耗。

示例代码：
    public static List<String> subs(String s) {
        char[] str = s.toCharArray();
        return process2(0, "", str);
    }

    public static List<String> process2(int index, String path, char[] str) {
        if (index == str.length) {
            List<String> baseResult = new ArrayList<>();
            baseResult.add(path);
            return baseResult;
        }
        // 没有要index位置的字符
        List<String> resultWithoutCurrent = process2(index + 1, path, str);
        // 要了index位置的字符
        List<String> resultWithCurrent = process2(index + 1, path + str[index], str);

        resultWithoutCurrent.addAll(resultWithCurrent); // 合并结果
        return resultWithoutCurrent;
    }

总结：一个是递归入参复杂点，处理结果简单点；一个是合并结果复杂点，递归逻辑清晰点；都行，实在要建议，就选1吧
 */

public class Code03_PrintAllSubsquences {

    /*
    打印一个字符串的全部子序列：1小时00分
     */
    public static List<String> subs(String s) {
        char[] str = s.toCharArray();
        String path = "";
        List<String> ans = new ArrayList<>();
        process1(0, path, str, ans);
        return ans;
    }

    // str 固定参数
    // 来到了str[index]字符，index是位置
    // str[0..index-1]已经走过了！之前的决定，都在path上
    // 之前的决定已经不能改变了，就是path
    // str[index....]还能决定，之前已经确定，而后面还能自由选择的话，
    // 把所有生成的子序列，放入到ans里去
    public static void process1(int index, String path, char[] str, List<String> ans) {
        if (index == str.length) {
            ans.add(path);
            return;
        }
        // 没有要index位置的字符
        process1(index + 1, path, str,  ans);
        // 要了index位置的字符
        process1(index + 1, path + String.valueOf(str[index]), str, ans);
    }

    /**
     * 打印一个字符串的全部子序列，要求不要出现重复字面值的子序列 1：12
     *
     * @param s
     * @return
     */
    public static List<String> subsNoRepeat(String s) {
        char[] str = s.toCharArray();
        String path = "";
        HashSet<String> set = new HashSet<>();
        process2(str, 0, set, path);
        List<String> ans = new ArrayList<>();
        for (String cur : set) {
            ans.add(cur);
        }
        return ans;
    }

    public static void process2(char[] str, int index, HashSet<String> set, String path) {
        if (index == str.length) {
            set.add(path);
            return;
        }
        String no = path;
        process2(str, index + 1, set, no);
        String yes = path + String.valueOf(str[index]);
        process2(str, index + 1, set, yes);
    }

    public static void main(String[] args) {
        String test = "acccc";
        List<String> ans1 = subs(test);
        List<String> ans2 = subsNoRepeat(test);

        for (String str : ans1) {
            System.out.println(str);
        }
        System.out.println("=================");
        for (String str : ans2) {
            System.out.println(str);
        }
        System.out.println("=================");

    }

}
