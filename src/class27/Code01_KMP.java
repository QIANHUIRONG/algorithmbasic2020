package class27;

/*
题意：strStr(s1, s2):s1中从哪个位置开始的子串能够匹配出s2,如果能匹配，返回第一个匹配的下标；否则返回-1
 */
/*
 kmp的本质是什么？
 ——暴力法不好的原因就是，我之前的匹配的长度，如果最终匹配失败了，丝毫不能加速我下一次的匹配
 kmp就可以加速，①s1 i之后的长度不用去匹配，一定匹配不上；②s2可以直接跳到j位置去匹配。
 */

/*
时间：
	前提（9）
		1、kmp解决的问题
		2、暴力解。o(n*m)
		3、kmp: o(n)

kmp:
	1、信息数组next[]（18）
	2、匹配失败后努力：31
		实质1：从j位置继续尝试去匹配
		实质2：不用验也知道i之后无法匹配
	举例子（41）
	证明 i之后的都无法匹配（57）
	往右推这件事情其实就是下标转换（1：08）
	code（1：11）
	时间复杂度（1：24）
	kmp:
		1、信息数组next[]（18）
		2、匹配失败后努力：
			实质1：从j位置继续尝试去匹配
			实质2：不用验也知道i之后无法匹配
		举例子（41）
		证明 i之后的都无法匹配（57）
		往右推这件事情其实就是下标转换（1：08）
		code（1：11）
		时间复杂度（1：24）

	next（1：34）
		求next的过程（1：34）
		证明：（1：40）
		例子：1：44
		code（1:50）
		时间复杂：（2：00）
 */

/*
思维导图

 kmp的本质是什么？
 ——暴力法不好的原因就是，我之前的匹配的长度，如果最终匹配失败了，丝毫不能加速我下一次的匹配
 kmp就可以加速，①s1 i之后的长度不用去匹配，一定匹配不上；②s2可以直接跳到j位置去匹配。


一、暴力解，s1中的每个字符都去尝试能不能匹配出完整的s2，如果当前位置匹配失败了，就去下一个位置尝试。时间复杂度：O(n*m), n是s1的长度，m是s2的长度；
比如s1=aaaaaaaaab,s2=aaab, 那么最差情况下，s1中的每个字符都要去匹配出s2，而且都是到b这个字符才匹配失败的

二、kmp算法。时间复杂度能做到o（n)
	一、信息数组
		1.信息数组next[]:每个位置它之前的字符串中前缀和后缀的最长匹配长度。和自己没有半毛钱关系，并且前缀和后缀长度都不能取到整体。（例子看语雀）
		2.这个next数组是针对s2求的，它可以让字符串匹配过程加速。
		3.规定死：next[0] = -1;next[1] = 0;

	二、匹配过程
		1.s1从i开始和s2匹配，一路都匹配上了，直到s1的x位置和s2的y位置没匹配上，y位置的信息拿出来，s1尝试继续从j位置去匹配s2。
		s1:i...j...x
		2.j位置是，s2的信息中，前缀和后缀串的最长匹配长度中，后缀串的开头，对应回s1字符串的位置。
		3.s1继续尝试从j位置去匹配s2，并且j...x这块不用验证，直接从x这个字符串继续验证。
		4.从i开始到j也不用尝试能不能匹配出s2

	三、求next[]数组
		1.当前来到i位置，之前的信息已经求过了，找到i-1位置的信息,拿出i-1最长匹配前缀串的下一个字符，假设这个位置是cn
			1.如果cn位置的字符和i-1位置的字符一样，就可以直接得出i位置的信息是next[i-1] + 1
			2.如果不一样，那就拿出cn位置的信息，cn=next[cn],继续去这么玩
			3.如果next[cn]=-1了，那么next[i]=0
 */

public class Code01_KMP {

	/*
    x, y, next
    能匹配否？能努力否？
     */
	// O(s1.length);
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
		int cn = next[1]; // cn有2个含义:1.i-1位置的字符最长前缀串是多长；2.哪个位置的字符和i-1位置的字符作比较，就是i-1位置最长匹配长度的下一个位置，这个下标刚好就是next[i-1]，初始值i为2，i-1为1，next[1] = 0,cn只能是0
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

	// for test
	public static String getRandomString(int possibilities, int size) {
		char[] ans = new char[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
		}
		return String.valueOf(ans);
	}

	public static void main(String[] args) {
		int possibilities = 5;
		int strSize = 20;
		int matchSize = 5;
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			String match = getRandomString(possibilities, matchSize);
			if (getIndexOf(str, match) != str.indexOf(match)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
