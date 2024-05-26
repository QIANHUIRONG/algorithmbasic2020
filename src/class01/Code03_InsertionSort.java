package class01;

import java.util.Arrays;


/*
 [题意]

*/
/*
[时间]
流程：45
code:
随机函数：
 */
/*
[思维导图]
一、插入排序
     * 时间复杂度：O（N^2) 有稳定性，相等的时候不往左边插入就行
     * 每遍历一趟，保证0-i有序；
     * 0-0有序，0-1有序，0-i有序，0...N-1有序；
     * 每一次遍历，当前数都往左边看，比较当前数和左边的数，如果左边的数大，就把当前数往左插入。直到左边数不比当前数大。
     （类似抓扑克牌，手里已经抓了一些牌了，又来了一张牌，从右往左一直看到当前排该去的位置）
     * 感觉和冒泡排序像，其实是天差地别的。冒泡排序即使是0，1，2，3，4这种优良的数据，还是得跑O(N^2)次
     * 但是这个数据状况，插入排序的时候第二个for循环arr[j] > arr[j + 1]都进不去，第二个for循环其实就是O（1）的，整个过程就看成O（N)的
     * <p>
     * 所以工程上在数据量非常小时，小到影响运行时间的不是时间复杂度，而是常熟时间，比如<60，就会使用插入排序。
     在bfprt算法中，5个数据一组，每组排有序取中位数时，我们也喜欢用插入排序。

二、随机函数
    Math.random() -> [0,1) 所有的小数，等概率返回一个
    Math.random() * N -> [0,N) 所有小数，等概率返回一个
    (int)(Math.random() * N) -> [0,N-1] 所有的整数，等概率返回一个
    (int)Math.randon() * (b - a + 1) -> [0, b-a] 所有的整数，等概率返回一个
    a + (int)(Math.randon() * (b - a + 1)) -> [a,b]所有的整数，等概率返回一个

 */

public class Code03_InsertionSort {

    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 一开始0-0有序
        // 接着搞定0-1有序
        // 接着搞定0-2有序
        // ...
        for (int i = 0; i < arr.length; i++) { // 0-i做到有序
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }

    // i和j是一个位置的话，会出错
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // for test
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        // Math.random() -> [0,1) 所有的小数，等概率返回一个
        // Math.random() * N -> [0,N) 所有小数，等概率返回一个
        // (int)(Math.random() * N) -> [0,N-1] 所有的整数，等概率返回一个
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())]; // 长度随机
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
        int maxSize = 100; // 随机数组的长度0～100
        int maxValue = 100;// 值：-100～100
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            insertionSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                // 打印arr1
                // 打印arr2
                succeed = false;
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + " ");
                }
                System.out.println();
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        insertionSort(arr);
        printArray(arr);
    }

}
