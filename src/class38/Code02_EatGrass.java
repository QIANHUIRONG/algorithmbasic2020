package class38;

/*
题意：
给定一个正整数N,表示有N份青草统一堆放在仓库里有一只牛和一只羊，牛先吃，羊后吃，它俩
轮流吃草不管是牛还是羊，每一轮能吃的草量必须是：
1,，4,16,64..(4的某次方)
谁最先把草吃完，谁获胜假设牛和羊都绝顶聪明，都想赢，都会做出理性的决定根据唯一的参数
N,返回谁会赢
 */

/*
题意：33
流程：35
暴力解：37
数学解法：
 */

/*
【思维导图】
1.暴力解：o(n)
	1.注意先手后手的身份转换，主过程是先手，那么下一个过程变为后手
	2.当前的先手依次去尝试1，4，16...,看能不能赢。如果都不能赢，就是后手赢
2.数学解：o(1) 后先后先先。
 */
public class Code02_EatGrass {

	// 如果n份草，最终先手赢，返回"先手"
	// 如果n份草，最终后手赢，返回"后手"
	public static String whoWin(int n) {
		if (n < 5) {
			return n == 0 || n == 2 ? "后手" : "先手";
		}
		// 进到这个过程里来，当前的先手，先选
		int want = 1; // 从1开始尝试
		while (want <= n) {
			// 后续如果是后手赢了，就是我这个过程里面的先手赢了，返回先手
			if (whoWin(n - want).equals("后手")) {
				return "先手";
			}
			if (want <= (n / 4)) { // 如果n是Integer.MAX_VALUE，want是距离n很近的数，如果没有这个判断，want *= 4就溢出了
				want *= 4; // 如果后续没有赢，我就试试4份草，16份草...看能不能赢
			} else {
				break;
			}
		}
		return "后手";
	}


	/*
	数学解：后先后先先
	 */
	public static String winner2(int n) {
		if (n % 5 == 0 || n % 5 == 2) {
			return "后手";
		} else {
			return "先手";
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i <= 50; i++) {
			System.out.println(i + " : " + whoWin(i));
		}
	}

}
