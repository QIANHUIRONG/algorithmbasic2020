package class27;


/*
题意：判断str1和str2是否是旋转字符串
 */
/*
题意【1：51】
流程【1：54】：s1自己拼接自己，会包含所有的旋转串；再用kmp

 */
public class Code03_IsRotation {

	public static boolean isRotation(String a, String b) {
		if (a == null || b == null || a.length() != b.length()) {
			return false;
		}
		String b2 = b + b;
		return getIndexOf(b2, a) != -1;
	}

	// KMP Algorithm
	public static int getIndexOf(String s1, String s2) { // 时间复杂度O(s1.length())
		if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
			return -1;
		}
		char[] str1 = s1.toCharArray();// 先转成字符数组
		char[] str2 = s2.toCharArray();
		int x = 0;// x,y是指str1，str2中正在对比的位置
		int y = 0;
		// O(M) m <= n
		int[] next = getNextArray(str2);// 获取信息数组
		// O(N)
		while (x < str1.length && y < str2.length) {
			if (str1[x] == str2[y]) {// 如果匹配上了，两个指针都往右移动
				x++;
				y++;
			} else if (next[y] != -1) { // 如果来到某一个位置没匹配上，就往前跳，努努力
				y = next[y];
			} else { // 无法努力，x下标只好加下去。x位置跟我str2 0位置都匹配不上，x你往下走吧
				x++;
			}
		}
		return y == str2.length ? x - str2.length : -1; // 如果跳出循环是因为y到达边界了，那么表示匹配成功了，匹配成功返回str1数组中从哪一个位置开始能能够找齐s2；如果是因为x到达边界了，表示匹配失败；
	}


	/*
    i,cn
    能匹配否？能努力否？
     */
	// O(str2.length)
	public static int[] getNextArray(char[] str2) {// 时间复杂度：O(str2.length())
		if (str2.length == 1) {
			return new int[]{-1};
		}
		int[] next = new int[str2.length]; // 存放信息的数组
		next[0] = -1;// 0位置规定就是-1
		next[1] = 0;// 1位置就是0
		int i = 2; // // 目前在哪个位置上求next数组的信息。永远都是i-1位置的信息跟cn位置的信息比较
		int cn = next[1]; // cn:哪个位置的字符和i-1位置的字符作比较，cn其实是i-1位置最长匹配长度的下一个位置，这个下标刚好就是next[i-1]，初始值i为2，i-1为1，next[1] = 0,cn只能是0
		while (i < next.length) { // 依次求str2每个位置的信息
			if (str2[i - 1] == str2[cn]) { // // 情况一：匹配成功
				next[i++] = ++cn; // 骚操作，匹配成功，i位置的信息就是cn+1,i位置往下移动；接下來，下一个cn就是next[i], 就是cn+1, 所以next[i++] == ++cn;就包含了next[i] = cn + 1; i++; cn++;3句话
			} else if (next[cn] != -1) {
				cn = next[cn];// 匹配不成功，cn>0表示还能往前跳，还能努努力
			} else {
				next[i++] = 0;// cn努力不动了
			}
		}
		return next;
	}


	public static void main(String[] args) {
		String str1 = "yunzuocheng";
		String str2 = "zuochengyun";
		System.out.println(isRotation(str1, str2));

	}

}
