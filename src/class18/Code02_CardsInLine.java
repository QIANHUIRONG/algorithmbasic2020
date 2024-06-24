package class18;


/*
题意：
	玩家A和玩家B依次拿走每张纸牌，
	规定玩家A先拿，玩家B后拿，
	但是每个玩家每次只能拿走最左或最右的纸牌，
	玩家A和玩家B都绝顶聪明。请返回最后获胜者的分数
 */
/*
时间：1：31
 */
/*
思维导图
1.范围上的尝试模型
2.首先先手不是必赢，举个例子：1，100，1， 先手无论怎么拿，后手都会拿走100
3.
	f1(int[] arr, int L, int R) {}，arr[L..R]你去先手获得的最好分数返回
	basecase：L==R时，就是return arr[L]
	普遍位置：拿L, 然后去arr[L+1,R]后手；或者拿R，去arr[L,R-1]后手，求max
4.
	g1(int[] arr, int L, int R) {}  arr[L..R]你去后手获得的最好分数返回
	basecase：L==R时，你是后手，先手一定会拿走，return 0
	普遍位置：对手拿走了L位置，你去arr[L+1,R]先手；对手拿走了R位置，你去arr[L,R-1]先手，求min
5.第四步求min，解释一下：
	 因为你是后手姿态，对手是先手姿态，又是绝顶聪明的，对手会拿走先手姿态下的最大，而你只能拿到最小值
	 比如[100, 3], 你是后手，那么先手一定会拿走100， 你只剩一个[3]去继续先手。
	 先手一定赢吗？不一定，比如[1,100,1]，无论先手拿什么，后手一定拿100

 */

public class Code02_CardsInLine {

	// 根据规则，返回获胜者的分数
	/**
	 * 暴力递归版本
	 * @param arr
	 * @return
	 */
	public static int win1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int first = f1(arr, 0, arr.length - 1); // 先手
		int second = g1(arr, 0, arr.length - 1); // 后手
		return Math.max(first, second); // 返回先手、后手谁赢
	}

	// arr[L..R]，先手获得的最好分数返回
	public static int f1(int[] arr, int L, int R) {
		if (L == R) {
			return arr[L];
		}
		int p1 = arr[L] + g1(arr, L + 1, R);
		int p2 = arr[R] + g1(arr, L, R - 1);
		return Math.max(p1, p2);
	}

	// // arr[L..R]，后手获得的最好分数返回
	public static int g1(int[] arr, int L, int R) {
		if (L == R) {
			return 0;
		}
		int p1 = f1(arr, L + 1, R); // 对手拿走了L位置的数
		int p2 = f1(arr, L, R - 1); // 对手拿走了R位置的数
		// 因为你是后手姿态，对手是先手姿态，又是绝顶聪明的，对手会拿走先手姿态下的最大，而你只能拿到最小值
		// 比如[100, 3], 你是后手，那么先手一定会拿走100， 你只剩一个[3]去继续先手。
		// 先手一定赢吗？不一定，比如[1,100,1]，无论先手拿什么，后手一定拿100
		return Math.min(p1, p2);
	}

	/**
	 * 记忆化搜索
	 * @param arr
	 * @return
	 */
	public static int win2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] fmap = new int[N][N];
		int[][] gmap = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				fmap[i][j] = -1;
				gmap[i][j] = -1;
			}
		}
		int first = f2(arr, 0, arr.length - 1, fmap, gmap);
		int second = g2(arr, 0, arr.length - 1, fmap, gmap);
		return Math.max(first, second);
	}

	// arr[L..R]，先手获得的最好分数返回
	public static int f2(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
		if (fmap[L][R] != -1) {
			return fmap[L][R];
		}
		int ans = 0;
		if (L == R) {
			ans = arr[L];
		} else {
			int p1 = arr[L] + g2(arr, L + 1, R, fmap, gmap);
			int p2 = arr[R] + g2(arr, L, R - 1, fmap, gmap);
			ans = Math.max(p1, p2);
		}
		fmap[L][R] = ans;
		return ans;
	}

	// // arr[L..R]，后手获得的最好分数返回
	public static int g2(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
		if (gmap[L][R] != -1) {
			return gmap[L][R];
		}
		int ans = 0;
		if (L != R) {
			int p1 = f2(arr, L + 1, R, fmap, gmap); // 对手拿走了L位置的数
			int p2 = f2(arr, L, R - 1, fmap, gmap); // 对手拿走了R位置的数
			ans = Math.min(p1, p2);
		}
		gmap[L][R] = ans;
		return ans;
	}

	/**
	 * 动态规划
	 * @param arr
	 * @return
	 */
	public static int win3(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] fmap = new int[N][N];
		int[][] gmap = new int[N][N];
		for (int i = 0; i < N; i++) {
			fmap[i][i] = arr[i];
		}
		// 老师课上的填法：填对角线
//		for (int startCol = 1; startCol < N; startCol++) {
//			int L = 0;
//			int R = startCol;
//			while (R < N) {
//				fmap[L][R] = Math.max(arr[L] + gmap[L + 1][R], arr[R] + gmap[L][R - 1]);
//				gmap[L][R] = Math.min(fmap[L + 1][R], fmap[L][R - 1]);
//				L++;
//				R++;
//			}
//		}
		// 自己想的填法，会好写一点，从底往上，从左往右
		for (int l = N - 2; l >= 0; l--) {
			for (int r = l + 1; r < N ; r++) {
				int p1 = arr[l] + gmap[l + 1][r];
				int p2 = arr[r] + gmap[l][r - 1];
				fmap[l][r] = Math.max(p1, p2);
				int p3 = fmap[l + 1][r];
				int p4 = fmap[l][r - 1];
				gmap[l][r] = Math.min(p3, p4);
			}
		}
		return Math.max(fmap[0][N - 1], gmap[0][N - 1]);
	}

	public static void main(String[] args) {
		int[] arr = { 5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7 };
		System.out.println(win1(arr));
		System.out.println(win2(arr));
		System.out.println(win3(arr));

	}

}
