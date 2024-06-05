package class13;

import java.util.*;

/*
贪心算法
	定义：
	1)最自然智慧的算法
	2)用一种局部最功利的标准，总是做出在当前看来是最好的选择。如果得到了最优解，说明贪心策略有效；否则贪心策略无效；
	3)难点在于证明局部最功利的标准可以得到全局最优解-> 不用管证明，很多证明是数学领域的知识，写一个暴力解，用对数器证明。
	4)对于贪心算法的学习主要以增加阅历和经验为主
	5)笔试有可能考贪心；面试几乎不可能，因为区分度太差。
	6)贪心一般就是用排序、堆等结构去贪心。
 */
/*
题意：给定一个由字符串组成的数组strs，必须把所有的字符串拼接起来，返回所有可能的拼接结果中，字典序最小的结果

*/


/*
时间：
贪心算法介绍：1：11
题意：1：21
code：1：50
暴力code：1：51
 */

/*
思维导图
方法一：暴力法：穷举拼接所有可能的字符串，

方法二：贪心
	1.两个字符串，放到字典中，谁先放在前面，谁的字典序就最小
	2.两个字符串a、b。(a+b) <= (b+a), a在前；否则b在前；
	3.证明：不用管证明。用暴力法当对数期

 */
public class Code05_LowestLexicography {

	/**
	 * 方法一：暴力方法：穷举拼接所有可能的字符串拼接结果。课堂上讲解的暴力方法
	 */
	public static String lowestString1(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		TreeSet<String> ans = process(strs); // TreeSet<String>就是根据字符串的字段序来排的。还有String.compareTo方法就是在比较字典序
		return ans.first(); // 最终返回first，就是字典序最小的
	}

	// strs中所有字符串全排列，返回所有可能的结果
	public static TreeSet<String> process(String[] strs) {
		TreeSet<String> ans = new TreeSet<>();
		if (strs.length == 0) {
			ans.add("");
			return ans;
		}
		for (int i = 0; i < strs.length; i++) {
			String first = strs[i];
			// 移除当前字符
			String[] nextStr = removeIndexString(strs, i);
			// 移除我之后，跑后序结果
			TreeSet<String> nextAns = process(nextStr);
			// 拼接移除我之后的后序结果。
			for (String cur : nextAns) {
				ans.add(first + cur);
			}
		}
		return ans;
	}


	/*
	一个更经典的暴力方法
	本质就是收集字符串数组的全排列，然后再一个个比较
	 */
	public static String lowestString3(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}

		List<String> rest = new ArrayList<>();
		for (String str : strs) {
			rest.add(str);
		}

		List<String> ans = new ArrayList<>();
		String path = "";

		process2(rest, path, ans);


		String lowest = ans.get(0);
		for (int i = 1; i < ans.size(); i++) {
			if (ans.get(i).compareTo(lowest) < 0) {
				lowest = ans.get(i);
			}
		}
		return lowest;
	}

	public static void process2(List<String> rest, String path, List<String> ans) {
		if (rest.isEmpty()) {
			ans.add(path);
		} else {
			for (int i = 0 ; i < rest.size(); i++) {
				String cur = rest.get(i);
				rest.remove(i);
				process2(rest, path + cur, ans);
				rest.add(i, cur);
			}
		}
	}



	// {"abc", "cks", "bct"}
	// 0 1 2
	// removeIndexString(arr , 1) -> {"abc", "bct"}
	public static String[] removeIndexString(String[] arr, int index) {
		int N = arr.length;
		String[] ans = new String[N - 1];
		int ansIndex = 0;
		for (int i = 0; i < N; i++) {
			if (i != index) {
				ans[ansIndex++] = arr[i];
			}
		}
		return ans;
	}


	/**
	 * 方法二：贪心
	 */
	public static String lowestString2(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		Arrays.sort(strs, new MyComparator());
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			res.append(strs[i]);
		}
		return res.toString();
	}

	public static class MyComparator implements Comparator<String> {
		// 两个字符串a、b。(a+b) <= (b+a), a拼接上b的askii码 <= b拼接上a的askii码, a在前；否则b在前；
		/*
		字符串的compareTo()方法用于比较两个字符串的字典顺序。
		如果字符串a按字典顺序应该排在字符串b的前面，则返回负数。
		如果字符串a按字典顺序应该排在字符串b的后面，则返回正数。
		如果字符串a和字符串b相等，则返回0。
		 */
		@Override
		public int compare(String a, String b) {
			return (a + b).compareTo(b + a); // 就是在比较askii码
		}
	}

	// for test
	public static String generateRandomString(int strLen) {
		char[] ans = new char[(int) (Math.random() * strLen) + 1];
		for (int i = 0; i < ans.length; i++) {
			int value = (int) (Math.random() * 5);
			ans[i] = (Math.random() <= 0.5) ? (char) (65 + value) : (char) (97 + value);
		}
		return String.valueOf(ans);
	}

	// for test
	public static String[] generateRandomStringArray(int arrLen, int strLen) {
		String[] ans = new String[(int) (Math.random() * arrLen) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = generateRandomString(strLen);
		}
		return ans;
	}

	// for test
	public static String[] copyStringArray(String[] arr) {
		String[] ans = new String[arr.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = String.valueOf(arr[i]);
		}
		return ans;
	}

	public static void main(String[] args) {
//
//		String[] strs = new String[3];
//		strs[0] = "a";
//		strs[1] = "b";
//		strs[2] = "c";
//
//		String[] arr2 = copyStringArray(strs);
//		System.out.println(lowestString1(strs));
//		System.out.println(lowestString3(strs));


		int arrLen = 6;
		int strLen = 5;
		int testTimes = 10000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String[] arr1 = generateRandomStringArray(arrLen, strLen);
			String[] arr2 = copyStringArray(arr1);
			String[] arr3 = copyStringArray(arr1);
			String[] arr4 = copyStringArray(arr1);
			if (!lowestString1(arr1).equals(lowestString2(arr2)) || !lowestString1(arr1).equals(lowestString3(arr3))) {
				for (String str : arr1) {
					System.out.print(str + ",");
				}
				System.out.println();
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");

		// treeset测试
		System.out.println("====treeSet测试====");
		TreeSet<String> treeSet = new TreeSet<>();
		treeSet.add("bbb");
		treeSet.add("aaa");
		System.out.println(treeSet.first());
	}

}
