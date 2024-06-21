package class28;

/*
题意：字符串变为回文需要添加的最少字符
给定一个字符串, 让它整体变回文字符串, 只能在后面添加字符, 最短加多少个
 */
/*
时间：
1.题意：[1:37]
流程；必须包含最后1个字符的情况下，最长回文子串多长
 */

/*
思维导图：
1.其实就是要求包含最后1个字符的情况下的回文半径，比如abcac,那么再把ab反转补充到后面就行了，abcacbaf
 */

public class Code02_AddShortestEnd {

	public static String shortestEnd(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		char[] str = manacherString(s);
		int[] pArr = new int[str.length];
		int C = -1;
		int R = -1;
		int maxContainsEnd = -1; // 包含最后1个字符的情况下的回文半径
		for (int i = 0; i != str.length; i++) {
			pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;
			while (i + pArr[i] < str.length && i - pArr[i] > -1) {
				if (str[i + pArr[i]] == str[i - pArr[i]])
					pArr[i]++;
				else {
					break;
				}
			}
			if (i + pArr[i] > R) {
				R = i + pArr[i];
				C = i;
			}
			if (R == str.length) {
				maxContainsEnd = pArr[i];
				break;
			}
		}
//		char[] res = new char[s.length() - maxContainsEnd + 1];
//		for (int i = 0; i < res.length; i++) {
//			res[res.length - 1 - i] = str[i * 2 + 1];
//		}
//		return String.valueOf(res);
		
		// abc123321， 那么maxContainsEnd=7， originalAns=6，接下来就是把abc反转一下返回。
		int originalAns = maxContainsEnd - 1; // 对应原始串的回文直径
		char[] chs = s.toCharArray();
		
		int end = s.length() - 1 - originalAns;
		char[] ans = new char[end + 1];
		int start = 0;
		while (start != ans.length) {
			ans[start] = chs[end];
			start++;
			end--;
		}
		return String.valueOf(ans);
	}

	public static char[] manacherString(String str) {
		char[] charArr = str.toCharArray();
		char[] res = new char[str.length() * 2 + 1];
		int index = 0;
		for (int i = 0; i != res.length; i++) {
			res[i] = (i & 1) == 0 ? '#' : charArr[index++];
		}
		return res;
	}

	public static void main(String[] args) {
		String str1 = "abc123321";
		System.out.println(shortestEnd(str1));
	}

}
