package class04;


/*
 [题意]

在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和。求数组小和。
例子： [1,3,4,2,5]
1左边比1小的数：没有
3左边比3小的数：1
4左边比4小的数：1、3
2左边比2小的数：1
5左边比5小的数：1、3、4、 2
所以数组的小和为1+1+3+1+1+3+4+2=16
*/

/*
[时间]

 */

// 时复：O(n*logn)
// 空复：

/*
[思维导图]
1.暴力法：每个数找一下自己左边，o(n^2)
2.最优解：利用归并排序。左边产生的小和+右边产生的小和+整体merge过程中产生的小和
	1.做转化的标准：一个数左边有多少个数比他小 == 右边有多少个数比当前数大
	你原来求的是每一个数左边有多少数处比他小，然后把它们都累加起来下一个数左边有多少数处比他小，然后都累加起来…
	我假设来到i的时候，arr[i]=a,我右边有多少个数处比a大，假设x个，我直接产生x个a.
	每个数都这么求，这两种方案得到答案是一样的

3.归并排序，merge过程中，左右两边都是有序的。一个数以左组姿态去求答案，右边有多少个数比我大是可以直接通过下标换算的！
	1.每一次Merge的时候产生小和，如果发现左组数字小于右组，产生小和，左组拷贝的时候看目前右组有多少个数处比左组当前数字大（不需要遍历，下标换算直接出来）
	。同一组内部不产生小和，只有跨组才产生小和
	2.相等的时候先拷贝右组，不产生小和因为如果先拷贝左组就不知道有多少个数处比右组大

4.一个平凡数x的心路历程：
	数组中的任何一个数X整件事情是这样的，我先遭遇一个最近的右组，求出来几个比x大，你们都变成有序之后
我再遭遇一个最近的右组，求出来几个比x大，你们再合成有序之后，我再去遭遇下一个更大的右组，求出来右几个比x大，每一个数都如此计算。
 */

public class Code02_SmallSum {

    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    // arr[L..R]既要排好序，也要求小和返回
    // 所有merge时，产生的小和，累加
    // 左 排序   merge
    // 右 排序  merge
    // merge
    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        // l < r
        int mid = (l + r) / 2;
        return
                process(arr, l, mid)
                        +
                        process(arr, mid + 1, r)
                        +
                        merge(arr, l, mid, r);
    }

    public static int merge(int[] arr, int L, int m, int r) {
        int[] help = new int[r - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = m + 1;
        int res = 0;
        while (p1 <= m && p2 <= r) {
            res += arr[p1] < arr[p2] ? (r - p2 + 1) * arr[p1] : 0;
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
        return res;
    }

    // for test
    public static int comparator(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                res += arr[j] < arr[i] ? arr[j] : 0;
            }
        }
        return res;
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // for test
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // for test
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            if (smallSum(arr1) != comparator(arr2)) {
                succeed = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }

}
