package class38;


/*
【题意】
小虎去买苹果，商店只提供两种类型的塑料袋，每种类型都有任意数量。
1)能装下6个苹果的袋子
2)能装下8个苹果的袋子
小虎可以自由使用两种袋子来装苹果，但是小虎有强迫症，他要求自己使用的袋子数量必须最少，
且使用的每个袋子必须装满。
给定一个正整数N,返回至少使用多少袋子。如果N无法让使用的每个袋子必须装满，返回-1
 */

/*
时间
题意：16
暴力解：20
code:22
打表找规律：26
找完规律后的code：29
 */

/*
【思维导图】
1.感觉跟数学规律有关，先写一个暴力解，看看有没有规律
2.暴力解：o(n)
	1.8号袋子从最多开始尝试，比如10个，然后剩余的分配给6号袋子，这样能保证总体袋子数量最少
	2.如果10个8号袋子搞不定，就试试9个8号袋子，剩余的用6号袋子
	3.最终都搞不定，返回-1
3.观察出来的数学解: o(1)
	1.1-18没有规律，硬写
	2.后面每8个一组，组内奇数是-1，偶数有答案
 */
public class Code01_AppleMinBags {

	/*
	暴力解
	 */
	public static int minBags(int apple) {
		if (apple < 0) {
			return -1;
		}
		int bag8 = apple / 8;
		int rest = apple - bag8 * 8;
		while(bag8 >= 0) {
			// rest 个
			if(rest % 6 ==0) {
				return bag8 + (rest / 6);
			} else {
				bag8--;
				rest += 8;
			}
		}
		return -1;
	}

	/*
	观察出来的数学规律解
	 */
	public static int minBagAwesome(int apple) {
		if ((apple % 2) != 0) { // 如果是奇数，返回-1
			return -1;
		}

		if (apple < 18) {
			return apple == 0 ? 0 : (apple == 6 || apple == 8) ? 1
					: (apple == 12 || apple == 14 || apple == 16) ? 2 : -1;
		}
		return (apple - 18) / 8 + 3;
	}

	public static void main(String[] args) {
		for(int apple = 1; apple < 200;apple++) {
			System.out.println(apple + " : "+ minBags(apple));
		}

	}

}
