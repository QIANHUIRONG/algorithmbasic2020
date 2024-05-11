package class21;

import java.util.ArrayList;
import java.util.List;

/**
 * 本道题，就是体系学习班23节，code03
 * 只不过体系学习班求的是方法数，这里求的是收集每一种具体的方法
 * 像这种我需要收集答案的，我是要入参中带上这个List<String> ans ? 还是return这个ans？
 * 我们可以参考体系学习班17的Code03,Code04
 * /

 /*
 * 我问一下深入的问题：
 * 1、把resut和ans放在入参中；2、不增加入参，直接返回List<List<Integer>>
 * 两种方式各有什么特点？哪种好？什么情况下应该选择哪种？
 * <p>
 * ChatGPT
 * 这两种方式各有其特点，具体选择取决于实际情况和个人偏好。
 * <p>
 * 将result和ans放在入参中：-> 代码简单点
 * 特点：将结果列表作为参数传递，可以在递归过程中持续地对结果进行添加，这样可以方便地实现对结果的累加。
 * 优点：代码相对简单，适用于递归过程中需要对结果进行累加的情况，同时可以避免不必要的创建和合并列表操作，节省内存空间。
 * 缺点：需要将结果列表作为参数传递，增加了函数的参数数量，可能会使得函数调用过程变得复杂。
 * <p>
 * 不增加入参，直接返回List<List<Integer>>：
 * 特点：递归函数直接返回当前层的结果，不需要额外的参数传递，简化了函数的参数列表。
 * 优点：代码相对简洁清晰，不需要考虑参数传递的复杂性，适用于简单的递归场景。
 * 缺点：每次递归都需要创建新的结果列表，然后合并结果，可能会增加内存开销，特别是在递归深度较大的情况下。
 * 选择哪种方式取决于具体情况：
 * <p>
 * 如果递归过程中需要对结果进行持续累加，且参数传递不会导致过于复杂的函数调用过程，可以选择将结果列表作为参数传递。
 * 如果递归过程比较简单，不需要对结果进行持续累加，并且希望代码简洁清晰，可以选择直接返回结果列表。
 * 总的来说，两种方式各有利弊，具体选择应根据实际情况和个人偏好进行考量。
 *
 */

/*
我再问一个深入的问题：
题目1是返回方法数，我可以轻易的改成动态规划，也就是dp[][]的形式；
题目2要求返回组成的具体情况，我要改成动态规划怎么好像很难搞？
这两种方式有什么本质的区别？

ChatGPT
本质上，两种问题的区别在于其求解方式和结果的表现形式。

求方法数的问题：
这类问题通常是一个简单的计数问题，即求满足条件的解的个数。例如，在题目1中，需要求组成目标金额的方法数。
动态规划非常适合解决这类问题，因为可以利用动态规划的状态转移方程，将问题划分为若干个子问题，并通过计算子问题的解来求解原问题的解。
结果的表现形式一般是一个整数，表示满足条件的解的个数。在动态规划的过程中，可以用一个二维数组 dp[][] 来记录计算过程，其中 dp[i][j] 表示在某种状态下满足条件的解的个数。
求具体组合情况的问题：
这类问题不仅需要知道满足条件的解的个数，还需要知道每个解的具体情况。例如，在题目2中，需要返回组成目标金额的货币组合情况。
动态规划也可以用来解决这类问题，但是难度相对较大，因为需要在动态规划的过程中记录下每个解的具体情况，而不仅仅是计数。
结果的表现形式一般是一个列表的列表，其中每个列表表示一种满足条件的解的具体情况。在动态规划的过程中，需要设计合适的数据结构来记录每个解的具体情况，同时要考虑如何进行状态转移和记录每个状态下的解的情况。
综上所述，本质上两种问题的区别在于对问题的求解方式和结果表现形式的要求不同。求方法数的问题更加简单，可以直接利用动态规划的思想求解；而求具体组合情况的问题相对复杂，需要在动态规划的过程中设计合适的数据结构来记录每个解的具体情况。
 */

    /*
    好，我知道了。接下来我要问一下递归的基本概念

递归中都有哪些常见名词？比如回溯
ChatGPT
在递归中，除了回溯，还有一些常见的名词，它们描述了递归中的不同方面和特性。以下是一些常见的递归相关名词：

递归（Recursion）：在计算机科学中，递归是指一个函数在其定义中调用自身的过程。递归通常用于解决可以被分解为相似但规模较小的子问题的问题。
基本情况（Base Case）：递归函数中的基本情况是指一个简单的、不再需要递归调用的情况。基本情况用于结束递归过程，否则递归会无限循环。
递归调用（Recursive Call）：递归函数在其定义中调用自身的过程称为递归调用。通过递归调用，函数可以处理更小规模的子问题。
递归深度（Recursion Depth）：递归深度是指递归过程中递归调用的层数。递归深度的增加会消耗更多的内存和系统资源。
回溯（Backtracking）：回溯是一种在解决问题时尝试所有可能的情况，并通过逐步回退来寻找问题的解的方法。在回溯过程中，如果某一路径不满足条件，就会回退到之前的状态，尝试其他路径。
分治法（Divide and Conquer）：分治法是一种递归算法的设计方法，将原问题分解成若干个规模较小的子问题，然后递归地求解子问题，最后将子问题的解合并得到原问题的解。
记忆化（Memoization）：记忆化是一种优化技术，用于在递归过程中存储已经计算过的结果，以避免重复计算。记忆化可以显著提高递归算法的效率。
这些名词描述了递归过程中的不同方面和特性，理解它们有助于更好地理解和设计递归算法。

回溯用代码举例子说明
ChatGPT
好的，让我们通过一个经典的回溯问题来说明：组合求和。

问题描述：给定一个无重复元素的正整数数组 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合。candidates 中的数字可以无限制重复被选取。

例如：

输入：candidates = [2,3,6,7], target = 7
输出：[[2,2,3],[7]]

下面是用 Java 编写的回溯算法来解决这个问题：



public class CombinationSum {

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length == 0) {
            return result;
        }
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] candidates, int target, int start, List<Integer> combination, List<List<Integer>> result) {
        if (target == 0) { // 如果目标数为0，说明找到了一组满足条件的组合，将其加入结果列表
            result.add(new ArrayList<>(combination));
            return;
        }
        if (target < 0) { // 如果目标数小于0，说明当前组合不满足条件，直接返回
            return;
        }
        // 从start索引开始尝试所有可能的组合
        for (int i = start; i < candidates.length; i++) {
            combination.add(candidates[i]); // 将当前数字添加到组合中
            backtrack(candidates, target - candidates[i], i, combination, result); // 递归尝试下一个数字，注意传递的start索引仍为i，表示可以重复使用当前数字
            combination.remove(combination.size() - 1); // 回溯，将当前数字从组合中移除
        }
    }

    public static void main(String[] args) {
        int[] candidates = {2, 3, 6, 7};
        int target = 7;
        List<List<Integer>> result = combinationSum(candidates, target);
        System.out.println("组合求和结果：");
        for (List<Integer> combination : result) {
            System.out.println(combination);
        }
    }
}
这段代码中，我们使用了回溯算法来解决组合求和问题。在 backtrack 方法中，我们首先判断如果目标数 target 为0，说明找到了一组满足条件的组合，
将其加入结果列表；然后判断如果 target 小于0，说明当前组合不满足条件，直接返回。接着，我们从 start 索引开始尝试所有可能的组合，
对于每个数字 candidates[i]，将其添加到组合中，然后递归调用 backtrack 方法尝试下一个数字，直到达到目标数或者不满足条件为止。最后，
在递归调用结束后，我们需要进行回溯，将当前数字从组合中移除，以便尝试其他选择。
     */
public class Problem_0039_CombinationSum {


    /**
     * 加在入参中
     *
     * @param arr
     * @param aim
     * @return
     */
    public static List<List<Integer>> combinationSum(int[] arr, int aim) {
        List<List<Integer>> ans = new ArrayList<>();
        if (arr == null || arr.length == 0 || aim < 0) {
            return ans;
        }
        List<Integer> path = new ArrayList<>();
        process(arr, 0, aim, path, ans);
        return ans;
    }

    public static void process(int[] arr, int index, int rest, List<Integer> path, List<List<Integer>> ans) {
        if (index == arr.length) {
            if (rest == 0) {
                /*
                如果将ans作为入参传递，并在递归过程中直接向ans中添加path，会导致结果列表中的所有元素都指向同一个对象path，
                最终得到的结果列表中的所有子列表都是相同的。这是因为在 Java 中，当将一个对象添加到列表中时，实际上添加的是该对象的引用，
                而不是对象本身的拷贝。因此，最终结果列表中的所有子列表都指向同一个对象，它们在不同的添加操作中可能会被修改，从而导致结果不正确。
                要解决这个问题，可以在添加到结果列表之前，先对path进行一次拷贝，确保每次添加的是path的一个副本而不是引用。
                 */
                // ans.add(path);
                ans.add(new ArrayList<>(path));
            }
            return;
        }
        for (int zhang = 0; arr[index] * zhang <= rest; zhang++) {
            for (int i = 0; i < zhang; i++) {
                path.add(arr[index]);
            }
            process(arr, index + 1, rest - arr[index] * zhang, path, ans);
            for (int i = 0; i < zhang; i++) {
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * 直接返回答案
     *
     * @param arr
     * @param aim
     * @return
     */
    public static List<List<Integer>> combinationSum2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return new ArrayList<>();
        }
        return process(arr, 0, aim);
    }

    public static List<List<Integer>> process(int[] arr, int index, int rest) {
        List<List<Integer>> result = new ArrayList<>();
        if (index == arr.length) {
            if (rest == 0) {
                List<Integer> emptyList = new ArrayList<>();
                result.add(emptyList);
            }
            return result;
        }
        for (int zhang = 0; arr[index] * zhang <= rest; zhang++) {
            List<List<Integer>> subResult = process(arr, index + 1, rest - arr[index] * zhang);
            for (List<Integer> subList : subResult) {
                List<Integer> combination = new ArrayList<>();
                combination.addAll(subList);
                for (int i = 0; i < zhang; i++) {
                    combination.add(arr[index]);
                }
                result.add(combination);
            }
        }
        return result;
    }



    public static void main(String[] args) {
        int[] arr = {2, 3, 6, 7};
        int aim = 7;
        List<List<Integer>> result = combinationSum(arr, aim);
        System.out.println("组成目标金额的货币组合：");
        for (List<Integer> combination : result) {
            System.out.println(combination);
        }
    }

}
