package class11;

/**
 * 折纸折痕问题：2：07
 * 请把一段纸条竖着放在桌子上，然后从纸条的下边向上方对折1次，压出折痕后展开。此时 折痕是凹下去的，
 * 即折痕突起的方向指向纸条的背面。如果从纸条的下边向上方连续对折2 次，压出折痕后展开，
 * 此时有三条折痕，从上到下依次是下折痕、下折痕和上折痕。给定一 个输入参
 * 数N，代表纸条都从下边向上方连续对折N次，请从上到下打印所有折痕的方向。
 */
public class Code07_PaperFolding {

	public static void printAllFolds(int N) {
		process(1, N, true);
		System.out.println();
	}

	// 当前你来了一个节点，脑海中想象的！
	// 这个节点在第i层，一共有N层，N固定不变的
	// 这个节点如果是凹的话，down = T
	// 这个节点如果是凸的话，down = F
	// 函数的功能：中序打印以你想象的节点为头的整棵树！
	public static void process(int i, int N, boolean down) {
		if (i > N) {
			return;
		}
		process(i + 1, N, true);
		System.out.print(down ? "凹 " : "凸 ");
		process(i + 1, N, false);
	}

	public static void main(String[] args) {
		int N = 4;
		printAllFolds(N);
	}
}
