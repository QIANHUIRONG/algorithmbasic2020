package class02;


/*
 [题意]
认识异或运算
*/
/*
[时间]

 */
// 时复：
/*
[思维导图]
认识异或运算：
	1.异或运算：无进位相加
	异或运算的性质：
		1.0^N = N; N^N = 0;
		2.异或运算满足交换律和结合律
		其实这两个性质用无进位相加去理解就行。

	2.如何不用额外变量交换两个数
	a = a ^ b;
	b = a ^ b;
	a = a ^ b;
	解释一下：
	a = a ^ b;
	b = a ^ b ^ b = a;
	a = a ^ b ^ a = b;就完成了交换；
	但是如果a和b相等，那就会全部刷成0；所以知道就好，没用。比如在数组arr中，i和j位置交换，如果i==j，那么就刷成0了。
 */
public class Code01_Swap {
	
	public static void main(String[] args) {

		
		
		
		
		
		int a = 16;
		int b = 603;
		
		System.out.println(a);
		System.out.println(b);
		
		
		a = a ^ b;
		b = a ^ b;
		a = a ^ b;
		
		
		System.out.println(a);
		System.out.println(b);
		
		
		
		
		int[] arr = {3,1,100};
		
		int i = 0;
		int j = 0;
		
		arr[i] = arr[i] ^ arr[j];
		arr[j] = arr[i] ^ arr[j];
		arr[i] = arr[i] ^ arr[j];
		
		System.out.println(arr[i] + " , " + arr[j]);
		
		
		
		
		
		
		
		
		
		System.out.println(arr[0]);
		System.out.println(arr[2]);
		
		swap(arr, 0, 0);
		
		System.out.println(arr[0]);
		System.out.println(arr[2]);
		
		
		
	}
	
	
	public static void swap (int[] arr, int i, int j) {
		// arr[0] = arr[0] ^ arr[0];
		arr[i]  = arr[i] ^ arr[j];
		arr[j]  = arr[i] ^ arr[j];
		arr[i]  = arr[i] ^ arr[j];
	}
	
	

}
