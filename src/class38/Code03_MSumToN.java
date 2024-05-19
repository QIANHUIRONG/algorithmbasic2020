package class38;

/*
题意：
定义一种数：可以表示成若干（数量>1）连续正数和的数
比如：
5=2+3,5就是这样的数
12=3+4+5,12就是这样的数
1不是这样的数，因为要求数量大于1个、连续正数和
2=1+1,2也不是，因为等号右边不是连续正数
给定一个参数N,返回是不是可以表示成若干连续正数和的数
 */

/*
题意：54
暴力解：55
code：56
打表找规律：1：04
打表找规律题目总结：1：11
 */

/*
【思维导图】
暴力解：
	1.尝试1开始，1+2能不能凑出num，不行；尝试1+2+3能不能凑出num；... 直到1+2+3+...+num
	2.如果1开始都不行，继续从2开始

数学规律解：
	1.打表找到规律：2的某次方就是false。其余是true
	2.如何判断一个数是不是2的某次方：如果一个数的二进制只有1个1，就是2的某次方；否则就不是
	3.怎么判断一个数二进制只有1个1：提取最右侧的1，如果和自己一样大，就是只有1个1

打表找规律总结：
	1.对数器找规律
		1)某个面试题，输入参数类型简单，并且只有一个实际参数
		2)要求的返回值类型也简单，并且只有一个
		3)用暴力方法，把输入参数对应的返回值，打印出来看看，进而优化code
 */
public class Code03_MSumToN {

	public static boolean isMSum1(int num) {
		for (int i = 1; i <= num; i++) {
			// 尝试1开始，1+2能不能凑出num，不行；尝试1+2+3能不能凑出num；
			// 如果1开始都不行，继续从2开始
			int sum = i;
			for (int j = i + 1; j <= num; j++) {
				if (sum + j > num) { // 要试出num，如果超过了就break，返回false
					break;
				}
				if (sum + j == num) {
					return true;
				}
				sum += j;
			}
		}
		return false;
	}

	// 2的某次方就是false。其余是true
	// 如何判断一个数是不是2的某次方：如果一个数的二进制只有1个1，就是2的某次方；否则就不是
	// 怎么判断一个数二进制只有1个1：提取最右侧的1，如果和自己一样大，就是只有1个1
	public static boolean isMSum2(int num) {
//		return num == (num & (~num + 1));
		return num == (num & (-num));
	}

	public static void main(String[] args) {
		for (int num = 1; num < 200; num++) {
			System.out.println(num + " : " + isMSum1(num));
		}
		System.out.println("test begin");
		for (int num = 1; num < 5000; num++) {
			if (isMSum1(num) != isMSum2(num)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test end");

	}
}
