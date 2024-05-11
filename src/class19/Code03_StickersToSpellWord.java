package class19;

import java.util.HashMap;

// 本题测试链接：https://leetcode.com/problems/stickers-to-spell-word
/*
时间：1：13
 */
public class Code03_StickersToSpellWord {


    /**
     * 暴力递归，没有任何优化的版本
     *
     * @param stickers
     * @param target
     * @return
     */
    public static int minStickers1(String[] stickers, String target) {
        int ans = process1(stickers, target);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    // 所有贴纸stickers，每一种贴纸都有无穷张
    // target是目标
    // 返回拼出target的最少张数
    public static int process1(String[] stickers, String target) {
        if (target.length() == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (String first : stickers) {
            String rest = minus(target, first);
            // 减完之后，如果都没有变化，就没有必要走后序分支了，因为当前贴纸没有减少我任何字符。如果没有这个if，再去调用process1(stickers, rest)，又回到自己，死循环了
            if (rest.length() != target.length()) {
                min = Math.min(min, process1(stickers, rest));
            }
        }
        // target: b
        // ticker: ac, ka, 那么一开始min=Integer.MAX_VALUE, 循环之后，if (rest.length() != target.length()) 都进不去，都搞不定，这个min还是MAX_VALUE
//		return min + (min == Integer.MAX_VALUE ? 0 : 1);
        return min == Integer.MAX_VALUE ? min : min + 1;
    }

    public static String minus(String s1, String s2) {
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        // 题目规定只有小写字母
        int[] count = new int[26];
        for (char cha : str1) {
            count[cha - 'a']++;
        }
        for (char cha : str2) {
            count[cha - 'a']--;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                for (int j = 0; j < count[i]; j++) {
                    builder.append((char) (i + 'a'));
                }
            }
        }
        return builder.toString();
    }


    /**
     * 暴力递归：记忆化搜索
     * 361ms
     *
     * @param stickers
     * @param target
     * @return
     */
    public static int minStickers4(String[] stickers, String target) {
        HashMap<String, Integer> map = new HashMap<>();
        int ans = process4(stickers, target, map);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    // 所有贴纸stickers，每一种贴纸都有无穷张
    // target是目标
    // 返回拼出target的最少张数
    public static int process4(String[] stickers, String target, HashMap<String, Integer> map) {
        if (map.containsKey(target)) {
            return map.get(target);
        }
        int ans = Integer.MAX_VALUE;
        if (target.length() == 0) {
            ans =  0;
        } else {
            int min = Integer.MAX_VALUE;
            for (String first : stickers) {
                String rest = minus(target, first);
                if (rest.length() != target.length()) {
                    min = Math.min(min, process4(stickers, rest, map));
                }
            }
            ans = min == Integer.MAX_VALUE ? min : min + 1;
        }
        map.put(target, ans);
        return ans;
    }


    /**
     * 暴力递归，有了剪枝+词频表优化
     * 词频表快在哪里？
     * @param stickers
     * @param target
     * @return
     */
    public static int minStickers2(String[] stickers, String target) {
        int N = stickers.length;
        // 关键优化(用词频表替代贴纸数组)
        int[][] counts = new int[N][26];
        for (int i = 0; i < N; i++) {
            char[] str = stickers[i].toCharArray();
            for (char cha : str) {
                counts[i][cha - 'a']++;
            }
        }
        int ans = process2(counts, target);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    // stickers[i] 数组，当初i号贴纸的字符统计 int[][] stickers -> 所有的贴纸
    // 每一种贴纸都有无穷张
    // 返回搞定target的最少张数
    // 最少张数
    public static int process2(int[][] stickers, String t) {
        if (t.length() == 0) {
            return 0;
        }
        // target做出词频统计
        // target  aabbc  2 2 1..
        //                0 1 2..
        char[] target = t.toCharArray();
        int[] tcounts = new int[26];
        for (char cha : target) {
            tcounts[cha - 'a']++;
        }
        int N = stickers.length;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            // 尝试第一张贴纸是谁
            int[] sticker = stickers[i];
            // 最关键的优化(重要的剪枝!这一步也是贪心!)
			/*
			选择所有贴纸中，必须含有我这个字符去跑后序：时间：1：37
			 */
            if (sticker[target[0] - 'a'] > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < tcounts.length; j++) { // 从'a'到'z'每个字符都去减
                    if (tcounts[j] > 0) { // 如果有词频才去减
                        int nums = tcounts[j] - sticker[j];
                        for (int k = 0; k < nums; k++) {
                            builder.append((char) (j + 'a'));
                        }
                    }
                }
                String rest = builder.toString();
                min = Math.min(min, process2(stickers, rest));
            }
        }
        return min == Integer.MAX_VALUE ? min : min + 1;
    }

    /**
     * 记忆化搜索
     * 16ms
     * @param stickers
     * @param target
     * @return
     */
    public static int minStickers3(String[] stickers, String target) {
        int N = stickers.length;
        int[][] counts = new int[N][26];
        for (int i = 0; i < N; i++) {
            char[] str = stickers[i].toCharArray();
            for (char cha : str) {
                counts[i][cha - 'a']++;
            }
        }
        HashMap<String, Integer> dp = new HashMap<>();
        dp.put("", 0);
        int ans = process3(counts, target, dp);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static int process3(int[][] stickers, String t, HashMap<String, Integer> dp) {
        if (dp.containsKey(t)) {
            return dp.get(t);
        }
        char[] target = t.toCharArray();
        int[] tcounts = new int[26];
        for (char cha : target) {
            tcounts[cha - 'a']++;
        }
        int N = stickers.length;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            int[] sticker = stickers[i];
            if (sticker[target[0] - 'a'] > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < 26; j++) {
                    if (tcounts[j] > 0) {
                        int nums = tcounts[j] - sticker[j];
                        for (int k = 0; k < nums; k++) {
                            builder.append((char) (j + 'a'));
                        }
                    }
                }
                String rest = builder.toString();
                min = Math.min(min, process3(stickers, rest, dp));
            }
        }
        int ans = min == Integer.MAX_VALUE ? min : min + 1;
        dp.put(t, ans);
        return ans;
    }


}
