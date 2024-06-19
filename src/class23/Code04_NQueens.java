package class23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
题意：N皇后问题
按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。
n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。
每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
输入：n = 4
输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
解释：如上图所示|，4 皇后问题存在两个不同的解法。
// 测试链接：https://leetcode.cn/problems/n-queens/description/
 */
/*
时间：1：55
 */
// 时间复杂度:每一行都有N种决策，复杂度：O(N^N)
/*
思维导图：
题外话：超级计算机会用n皇后问题来测试性能
1.大流程：流程严格限制死，每一行只放一个皇后；记录每一行皇后摆放的位置到record[]数组，放皇后的时候只需要考虑不共列，不共斜线
2.轨迹信息数组 record[i]=j，表示i行皇后放在j列
3.
	process1(int i, int[] record, int n) {}:当前来到i行，一共是0~N-1行,在i行上放皇后，返回i.... 后续有多少合法的方法数；
	basecase：i来到n行，说明之前放置的皇后互相不打架，收集到了一种合理的方法，返回1
	普遍流程：来到第i行，只放一个皇后，每一个列都尝试，累加所有的方法数；

4.怎么判断是否打架：
	之前的record[]记录中，第a行皇后在b列；当前尝试在第x行皇后放在y列
	共列：b == y
	共斜线：|行-行| == |列-列|，就是|a - x| == |b - y|
 */
public class Code04_NQueens {


	/**
	 * 课堂上的N皇后
	 * @param n
	 * @return
	 */
	public static int num1(int n) {
		if (n < 1) {
			return 0;
		}
		// 轨迹信息数组 record[i]=j，表示i行皇后放在j列
		int[] record = new int[n];
		return process1(0, record, n);
	}

	// 当前来到i行，一共是0~N-1行
	// 在i行上放皇后，所有列都尝试
	// 必须要保证跟之前所有的皇后不打架
	// int[] record record[x] = y 之前的第x行的皇后，放在了y列上
	// 返回：不关心i以上发生了什么，i.... 后续有多少合法的方法数
	public static int process1(int i, int[] record, int n) {
		if (i == n) {
			return 1;
		}
		int res = 0;
		// i行的皇后，放哪一列呢？j列，
		for (int j = 0; j < n; j++) {
			if (isValid(record, i, j)) {
				record[i] = j;
				res += process1(i + 1, record, n);
			}
		}
		return res;
	}


	/**
	 * 用位运算优化的N皇后
	 * @param n
	 * @return
	 */
	// 请不要超过32皇后问题
	public static int num2(int n) {
		if (n < 1 || n > 32) {
			return 0;
		}
		// 如果你是13皇后问题，limit 最右13个1，其他都是0
		int limit = n == 32 ? -1 : (1 << n) - 1;
		return process2(limit, 0, 0, 0);
	}

	// 7皇后问题
	// limit : 0....0 1 1 1 1 1 1 1
	// 之前皇后的列影响：colLim
	// 之前皇后的左下对角线影响：leftDiaLim
	// 之前皇后的右下对角线影响：rightDiaLim
	public static int process2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
		if (colLim == limit) {
			return 1;
		}
		// pos中所有是1的位置，是你可以去尝试皇后的位置
		int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
		int mostRightOne = 0;
		int res = 0;
		while (pos != 0) {
			mostRightOne = pos & (~pos + 1);
			pos = pos - mostRightOne;
			res += process2(limit, colLim | mostRightOne, (leftDiaLim | mostRightOne) << 1,
					(rightDiaLim | mostRightOne) >>> 1);
		}
		return res;
	}


	/*
	力扣N皇后问题。需要生成皇后的位置，而不是方法数
	 */
	public static List<List<String>> solveNQueens(int n) {
		int[] record = new int[n];
		List<List<String>> ans = new ArrayList<>();
		process2(0 ,record, n, ans);
		return ans;
	}

	// 当前来到i行，一共是0~N-1行
	// 在i行上放皇后，所有列都尝试
	// 必须要保证跟之前所有的皇后不打架
	// int[] record record[x] = y 之前的第x行的皇后，放在了y列上
	// 如果此次尝试正确，也就是能填到第N行，收集皇后的填写位置，放在ans里面去
	public static void process2(int i, int[] record, int n, List<List<String>> ans) {
		if (i == n) {
			// 如果i来到了n行，说明此次尝试有效，此次尝试皇后的方法都放在了record[]数组中，此时我就可以生成一个放法了
			List<String> anAns = generateAns(record, n);
			ans.add(anAns);
			return;
		}
		for (int j = 0; j < n; j++) {
			if (isValid(record, i, j)) {
				record[i] = j;
				process2(i + 1, record, n, ans);
			}
		}
	}

	public static List<String> generateAns(int[] record, int n) {
		List<String> board = new ArrayList<String>();
		for (int i = 0; i < n; i++) {
			char[] row = new char[n];
			Arrays.fill(row, '.');
			row[record[i]] = 'Q';
			board.add(new String(row));
		}
		return board;
	}

	public static boolean isValid(int[] record, int i, int j) {
		// 0..i-1
		for (int k = 0; k < i; k++) {
			if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
				return false;
			}
		}
		return true;
	}


	public static void main(String[] args) {
		int n = 4;

		long start = System.currentTimeMillis();
		System.out.println(num2(n));
		long end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		System.out.println(num1(n));
		end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

		List<List<String>> lists = solveNQueens(n);
		System.out.println(lists);
	}
}
