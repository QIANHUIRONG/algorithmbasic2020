package class28;

/*
题意：求最长回文子串的长度
 */


/*
时间：

	定义(6)：求最长回文子串的长度
	暴力解(11)：O(n^2),以每个点作为扩的中心点，去扩
	偶数的时候不行(14):加#。#串 / 2就是原始串的最长回文长度。
	暴力解复杂度(22)：O(n^2)。Manacher可以做到O(n)

	manacher:
		1.先玩一些概念(24): 回文直径、回文半径、回文半径数组、最右回文边界R(30)，初始R=-1、取得最右回文边界的中心点是啥，C。R和C是联动的！
		2.算法流程[39]:
			1.i没被R罩住：没法优化，暴力扩
			2.i被R罩住：
				1(47).i`扩出来的区域彻底在l,r内,那么i的答案就和i`一样
				2【57】.i`的回文区域跑到l外面了，回文半径就是i到R
				3【1：06】.i`的回文区域左边界和l压线，至少是i到R,还需要继续扩
		3.时间复杂度[1:12]:O(n),  扩失败了 每个点都会失败1次，o(n);扩成功了，就推高R，R从0-N不回退，O(n)
		4.code[1:19]
 */

/*
思维导图：（只记录流程吧）
1.无论的暴力解，还是manacher，都需要补充#字符，否则偶数无法求出答案
	1、#串位置i，对应原串(i-1)/2
	2、#串求出回文半径i，对应原串回文半径i/2; #串求出回文直径i，对应原串回文直径i/2;
	3、#串求出最大回文半径i，i-1对应原串最大回文直径，也就是最长回文子串


一、暴力解法
	每一个字符都往左右两边扩，如果左右两边一样就继续扩，直到扩不动：O(n^2)

二、Manacher
	1.先玩一些概念：
		1.回文半径
		2.回文半径数组：存储每一个字符的回文半径
		3.最右回文边界R
		4.取得最右回文边界的字符的下标
	2.流程
		1.i没被R罩住：没法优化，暴力扩
		2.i被R罩住：首先要找到 i 位置关于C位置(最长半径字符)的对称点 i`, 对于i`，它也有自己的回文半径存放在回文半径数组pArr中，接下来要对这个回文半径的长度进行讨论
			情况一：i`的回文串整个都在L...R内,那么i的答案就和i`一样
			情况二：i`的回文串在L...R外，回文半径就是i到R
			情况三：i`的回文串恰好落在L上,i`的回文区域左边界和l压线，至少是i到R,还需要继续扩
	（具体的证明在语雀）
	3.时复：o(n):扩失败了 每个点都会失败1次，o(n);扩成功了，就推高R，R从0-N不回退，O(n)
 */
public class Code01_Manacher {

	public static int manacher(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		// "12132" -> "#1#2#1#3#2#"
		char[] str = manacherString(s);
		int[] pArr = new int[str.length]; // 回文半径数组，最大值就是我们想要的
		int C = -1;// 最右边界是谁扩出来的
		int R = -1;// 讲述中：R代表最右的扩成功的位置，code中表示的最初的失败位置，也就是最右的扩成功位置的，再下一个位置
		int max = Integer.MIN_VALUE; // 抓最大回文半径的
		for (int i = 0; i < str.length; i++) { // 每个位置去求回文半径
			// 【1：23】
			/*
			i位置扩出来的答案，i位置扩的区域，至少是多大。
            来到i位置去设置回文半径：
                1.i>=R,表示i没被R罩住（R是第一个不能扩的位置），则pArr[i]就先设置成至少的1
                2.i<R,被R罩住了，此时有三种情况，首先i`的位置是2 * C - i
                    2.1 i`的回文半径在L..R内，答案就是pArr[2*C-i]
                    2.2 i`的回文半径在L..R外，答案就是R-i
                    2.3 i`的回文半径在L...R上，答案至少是R-i,然后还要继续验
                    所以三个步骤可以简化成先求出不用验的最小区域，再继续验证；
             */
			pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;

			//【1：27】
			while (i + pArr[i] < str.length && i - pArr[i] > -1) { // 如果i加上一个不用验的区域不越界，i减去一个不用验的区域不越界
				if (str[i + pArr[i]] == str[i - pArr[i]])// 往外扩的意思，再往右的字符如果跟我再往左的字符一样，回文半径就++，否则就break（情况一、二都会直接break）
					pArr[i]++;
				else {
					break;
				}
			}
			/*
             在情况三下，才有可能继续扩，才有可能继续推高R的位置：
             i + pArr[i]这个位置是第一个扩失败的位置！
             例如：
                a b c b a
                0 1 2 3 4
                当前i=2,pArr[2]=3,2+3=5,是第一个扩失败的位置
             如果第一个扩失败的位置要推高了，就更新；
             */
			if (i + pArr[i] > R) {
				R = i + pArr[i];
				C = i;
			}
			max = Math.max(max, pArr[i]);
		}
		// [1：31] #串里面半径-1就是原始串的直径
		return max - 1;
	}

	public static char[] manacherString(String str) {
		char[] charArr = str.toCharArray();
		char[] res = new char[str.length() * 2 + 1];
		int index = 0;
		for (int i = 0; i != res.length; i++) {
			res[i] = (i % 2) == 0 ? '#' : charArr[index++];
		}
		return res;
	}

	// for test
	public static int right(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = manacherString(s);
		int max = 0;
		for (int i = 0; i < str.length; i++) {
			int L = i - 1;
			int R = i + 1;
			while (L >= 0 && R < str.length && str[L] == str[R]) {
				L--;
				R++;
			}
			max = Math.max(max, R - L - 1);
		}
		return max / 2;
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
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			if (manacher(str) != right(str)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
