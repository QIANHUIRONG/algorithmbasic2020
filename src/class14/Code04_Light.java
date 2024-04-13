package class14;

import java.util.HashSet;

/*
面试中，题目描述不清楚怎么办：Argue
很多题目扭转做笔试的时候，包括面试出题的时候是不是模棱两可，含含糊糊对吧，就这种情况
你不觉得我课上讲东西给你一说就懂了，明面试官跟你说得含湖糊的，或者你笔试怎么猜半天都看猜不出来是啥？
在搞你，故意这么搞得，他为啥故意这么搞？看你有没有declare问题的能力，就你能把问题登清
面试官有时候故意告诉你含含湖湖的东西，他指望着你把他问清楚，我们再继续讨论.结果你以为那就是就全部了，
然后你你在那想，那你能想清楚吗？你有东西没澄清，你想清楚什么.面试官就觉得，这回白面了，
本来想让你发问把问题问清楚，你含湖地方把它搞清楚，结果你在哪儿闷头想.笔试，的时候他故意说得很绕。你一定要理清主线，之后再做整个题
 */
// 点亮str中所有需要点亮的位置至少需要几盏灯
// 时间：1：14
/*
给定一个字符串str，只由‘X’和‘.’两种字符构成。
‘X’表示墙，不能放灯，也不需要点亮
‘.’表示居民点，可以放灯，需要点亮
如果灯放在i位置，可以让i-1，i和i+1三个位置被点亮
返回如果点亮str中所有需要点亮的位置，至少需要几盏灯
 */
public class Code04_Light {

	/**
	 * 方法一：暴力递归

	 * @param road
	 * @return
	 */
	public static int minLight1(String road) {
		if (road == null || road.length() == 0) {
			return 0;
		}
		return process(road.toCharArray(), 0, new HashSet<>());
	}

	// str[index....]位置，自由选择放灯还是不放灯
	// str[0..index-1]位置呢？已经做完决定了，那些放了灯的位置，存在lights里
	// 要求选出能照亮所有.的方案，并且在这些有效的方案中，返回最少需要几个灯
	public static int process(char[] str, int index, HashSet<Integer> lights) {
		if (index == str.length) { // 结束的时候
			for (int i = 0; i < str.length; i++) {
				if (str[i] != 'X') { // 当前位置是点的话
					if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
						return Integer.MAX_VALUE;
					}
				}
			}
			return lights.size();
		} else { // str还没结束
			// i X .
			int no = process(str, index + 1, lights);
			int yes = Integer.MAX_VALUE;
			if (str[index] == '.') {
				lights.add(index);
				yes = process(str, index + 1, lights);
				lights.remove(index);
			}
			return Math.min(no, yes);
		}
	}

	/*
	方法二：贪心策略
		i位置是x，不用放灯，直接去i+1位置；
		i位置是.
			如果i+1位置是x，i位置一定要放灯，灯++，i=i+2
			如果i+1位置是.
				如果i+2位置是x，i位置或者i+1位置要放1个灯，灯++，i=i+3
				如果i+2位置是. 灯放在i+1, 直接照亮i,i+1,i+2,灯++，i=i+3;
	顺序遍历一遍：O（N）
	// minLight4是更顺思路的写法，minLight4优化一下就变成minLight2了。
	 */
	public static int minLight2(String road) {
		char[] str = road.toCharArray();
		int i = 0;
		int light = 0;
		while (i < str.length) {
			if (str[i] == 'X') {
				i++;
			} else {
				light++;
				if (i + 1 == str.length) {
					break;
				} else { // 有i位置 i+ 1 X .
					if (str[i + 1] == 'X') {
						i = i + 2;
					} else {
						i = i + 3;
					}
				}
			}
		}
		return light;
	}

	public static int minLight4(String road) {
		char[] str = road.toCharArray();
		int i = 0;
		int light = 0;
		while (i < str.length) {
			// i位置是x，不用放灯，直接去i+1位置；
			if (str[i] == 'X') {
				i++;
			} else { // i位置是.
				if (i + 1 == str.length) { // 只剩1个i位置，无论如何一定要放灯
					light++;
					break;
				}
				if (i + 2 == str.length) { // 只剩2个位置，无论如何一定要放灯
					light++;
					break;
				}
				if (str[i+1] == 'X') { // 如果i+1位置是x，i位置一定要放灯，灯++，i=i+2
					light++;
					i+=2;
				} else { // 如果i+1位置是.
					if (str[i+2] == 'X') { // 如果i+2位置是x，i位置或者i+1位置要放1个灯，灯++，i=i+3
						light++;
						i+=3;
					} else { // 如果i+2位置是. 灯放在i+1, 直接照亮i,i+1,i+2,灯++，i=i+3;
						light++;
						i+=3;
					}
				}
			}
		}
		return light;
	}

	// 更简洁的解法
	// 两个X之间，数一下.的数量，然后除以3，向上取整
	// 把灯数累加
	public static int minLight3(String road) {
		char[] str = road.toCharArray();
		int cur = 0;
		int light = 0;
		for (char c : str) {
			if (c == 'X') {
				light += (cur + 2) / 3;
				cur = 0;
			} else {
				cur++;
			}
		}
		light += (cur + 2) / 3;
		return light;
	}

	// for test
	public static String randomString(int len) {
		char[] res = new char[(int) (Math.random() * len) + 1];
		for (int i = 0; i < res.length; i++) {
			res[i] = Math.random() < 0.5 ? 'X' : '.';
		}
		return String.valueOf(res);
	}

	public static void main(String[] args) {
		int len = 20;
		int testTime = 100000;
		for (int i = 0; i < testTime; i++) {
			String test = randomString(len);
			int ans1 = minLight1(test);
			int ans2 = minLight2(test);
			int ans3 = minLight3(test);
			int ans4 = minLight4(test);
			if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
				System.out.println("oops!");
			}
		}
		System.out.println("finish!");
	}
}
