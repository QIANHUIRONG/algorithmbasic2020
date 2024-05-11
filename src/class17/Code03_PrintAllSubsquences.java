package class17;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
 如果递归是需要收集List<String> ans的，是要在入参中带上，还是加在返回值上呢？么可以在入参中带上。我们先模仿，先不讨论哪种好
 如果递归需要之前递归做过的决定，比如这里的path，那么也在入参中带上。 -> 但是这样子好像无法处理成动态规划？

 如果需要收集List<String> ans的，递归函数的返回参数是void，那么就直接去跑递归，递归会帮你收集答案到ans中
 如果递归函数是有返回参数的情况，那么可能需要下游的递归返回给我结果，我才能处理当前层


 */
public class Code03_PrintAllSubsquences {

	 /*
	 打印一个字符串的全部子序列：1小时00分
	  */
	 public static List<String> subs(String s) {
		 char[] str = s.toCharArray();
		 String path = "";
		 List<String> ans = new ArrayList<>();
		 process1(str, 0, ans, path);
		 return ans;
	 }

	// str 固定参数
	// 来到了str[index]字符，index是位置
	// str[0..index-1]已经走过了！之前的决定，都在path上
	// 之前的决定已经不能改变了，就是path
	// str[index....]还能决定，之前已经确定，而后面还能自由选择的话，
	// 把所有生成的子序列，放入到ans里去
	public static void process1(char[] str, int index, List<String> ans, String path) {
		if (index == str.length) {
			ans.add(path);
			return;
		}
		// 没有要index位置的字符
		process1(str, index + 1, ans, path);
		// 要了index位置的字符
		process1(str, index + 1, ans, path + String.valueOf(str[index]));
	}

	/**
	 * 打印一个字符串的全部子序列，要求不要出现重复字面值的子序列 1：12
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
