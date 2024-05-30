package class08;


/*
 [题意]
    前缀树
*/

/*
[时间]

 */

// 时复：
// 空复：

/*
[思维导图]

一、ppt
1）单个字符串中，字符从前到后的加到一棵多叉树上
2）字符放在路上，节点上有专属的数据项（常见的是pass和end值）
3）所有样本都这样添加，如果没有路就新建，如有路就复用
4）沿途节点的pass值增加1，每个字符串结束时来到的节点end值增加1

二、前缀数和哈希表的比较
1.哈希表不支持查询以某个字符串做前缀的个数
2.哈希表不支持查询某个字符串加入过几次

三、复杂度
    1.建立前缀树：o(m)  任何一个字符串，把每一个字符串挂到树上，这不就是依次你有一个字符，我就跳一下，你还有个字符我就跳下一下，你再有个字符我就再往下蹦一下，
然后你字符都完了，我也蹦到底了，结束了。你既然来新的字符串，你出来个字符我就跳一下，你再出个字符，我再跳一下，你再出个字符，我再跳一下，
你把所有的字符串都加到这个前缀数里去，代价也是 o（m)  m是啥？所有的字符数
    2.查询某一个字符串或者查询某个前缀：O（k) : 查询一个字符串出现几次， b 个O(K)。 k 是什么？它的长度。查前缀也是。
    3.所以你看在性能方面，前缀数不仅跟这个哈希表一样，而且还能实现哈希表所不能的查询。
    （哈希表，时间复杂度o(1), 但是如果样本量大到无法忽略，比如100w个字符串，添加到哈希表，这时候还认为是O(1)有点牵强，这时候认为是O(M)的。
    查询一个字符串，如果要查询的key字符串很长，可以认为是O(k)的，k是key的长度，因为要看完字符串才能算hashcode）


四、AC自动机: 前缀树+KMP
 */

import java.util.HashMap;

// 该程序完全正确
// 课堂上讲解的前缀树
public class Code02_TrieTree {

    public static class Node1 {
        public int pass;
        public int end;
        public Node1[] nexts;

        // char tmp = 'b'  (tmp - 'a')
        public Node1() {
            pass = 0;
            end = 0;
            // 0    a
            // 1    b
            // 2    c
            // ..   ..
            // 25   z
            // nexts[i] == null   i方向的路不存在
            // nexts[i] != null   i方向的路存在
            nexts = new Node1[26]; // 如果前缀树只有小写字符，就建立26个节点就行了
        }
    }


    // 前缀树1：该前缀树的路用数组实现
    public static class Trie1 {
        private Node1 root;

        public Trie1() {
            root = new Node1();
        }

        // 遍历字符，有路直接走，没路建出来
        // 沿途p++, 最后一个节点e++
        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] chs = word.toCharArray();
            Node1 node = root;
            node.pass++;
            int path = 0;
            for (int i = 0; i < chs.length; i++) { // 从左往右遍历字符
                path = chs[i] - 'a'; // 由字符，对应成走向哪条路
                if (node.nexts[path] == null) {
                    node.nexts[path] = new Node1();
                }
                node = node.nexts[path];
                node.pass++;
            }
            node.end++;
        }

        // word这个单词之前加入过几次
        public int search(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node1 cur = root;
            int path = 0;
            for (int i = 0; i < chs.length; i++) {
                path = chs[i] - 'a';
                if (cur.nexts[path] == null) {
                    return 0;
                }
                cur = cur.nexts[path];
            }
            return cur.end;
        }


        // 所有加入的字符串中，有几个是以pre这个字符串作为前缀的
        public int prefixNumber(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node1 cur = root;
            int path = 0;
            for (int i = 0; i < chs.length; i++) {
                path = chs[i] - 'a';
                if (cur.nexts[path] == null) {
                    return 0;
                }
                cur = cur.nexts[path];
            }
            return cur.pass;
        }

        // 沿途节点p--, 最后一个节点e--
        public void delete(String word) {
            if (search(word) != 0) { // delete 的时候我先 search 一下他是否存在我再删
                char[] chs = word.toCharArray();
                Node1 cur = root;
                cur.pass--;
                int path = 0;
                for (int i = 0; i < chs.length; i++) {
                    path = chs[i] - 'a';
                     // 防止内存泄漏：如果我发现。我下级的那个节点它减完之后。 pass 是 0 了，直接把我这个底层塞null  return
                    if (--cur.nexts[path].pass == 0) {
                        cur.nexts[path] = null;
                        return;
                    }
                    cur = cur.nexts[path];
                }
                cur.end--;
            }
        }



    }



    // 前缀树2：该前缀树的路用哈希表实现
    public static class Node2 {
        public int pass;
        public int end;
        public HashMap<Integer, Node2> nexts; // 如果前缀树的节点很多，可以用哈希表代替数组

        public Node2() {
            pass = 0;
            end = 0;
            nexts = new HashMap<>();
        }
    }

    public static class Trie2 {
        private Node2 root;

        public Trie2() {
            root = new Node2();
        }

        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] chs = word.toCharArray();
            Node2 cur = root;
            cur.pass++;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!cur.nexts.containsKey(index)) {
                    cur.nexts.put(index, new Node2());
                }
                cur = cur.nexts.get(index);
                cur.pass++;
            }
            cur.end++;
        }

        // word这个单词之前加入过几次
        public int search(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node2 cur = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!cur.nexts.containsKey(index)) {
                    return 0;
                }
                cur = cur.nexts.get(index);
            }
            return cur.end;
        }

        // 所有加入的字符串中，有几个是以pre这个字符串作为前缀的
        public int prefixNumber(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node2 cur = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!cur.nexts.containsKey(index)) {
                    return 0;
                }
                cur = cur.nexts.get(index);
            }
            return cur.pass;
        }

        public void delete(String word) {
            if (search(word) != 0) {
                char[] chs = word.toCharArray();
                Node2 cur = root;
                cur.pass--;
                int index = 0;
                for (int i = 0; i < chs.length; i++) {
                    index = (int) chs[i];
                    if (--cur.nexts.get(index).pass == 0) {
                        cur.nexts.remove(index);
                        return;
                    }
                    cur = cur.nexts.get(index);
                }
                cur.end--;
            }
        }


    }

    public static class Right {

        private HashMap<String, Integer> box;

        public Right() {
            box = new HashMap<>();
        }

        public void insert(String word) {
            if (!box.containsKey(word)) {
                box.put(word, 1);
            } else {
                box.put(word, box.get(word) + 1);
            }
        }

        public void delete(String word) {
            if (box.containsKey(word)) {
                if (box.get(word) == 1) {
                    box.remove(word);
                } else {
                    box.put(word, box.get(word) - 1);
                }
            }
        }

        public int search(String word) {
            if (!box.containsKey(word)) {
                return 0;
            } else {
                return box.get(word);
            }
        }

        public int prefixNumber(String pre) {
            int count = 0;
            for (String cur : box.keySet()) {
                if (cur.startsWith(pre)) {
                    count += box.get(cur);
                }
            }
            return count;
        }
    }

    // for test
    public static String generateRandomString(int strLen) {
        char[] ans = new char[(int) (Math.random() * strLen) + 1];
        for (int i = 0; i < ans.length; i++) {
            int value = (int) (Math.random() * 6);
            ans[i] = (char) (97 + value);
        }
        return String.valueOf(ans);
    }

    // for test
    public static String[] generateRandomStringArray(int arrLen, int strLen) {
        String[] ans = new String[(int) (Math.random() * arrLen) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = generateRandomString(strLen);
        }
        return ans;
    }

    public static void main(String[] args) {
        int arrLen = 100;
        int strLen = 20;
        int testTimes = 10000;
        for (int i = 0; i < testTimes; i++) {
            String[] arr = generateRandomStringArray(arrLen, strLen);
            Trie1 trie1 = new Trie1();
            Trie2 trie2 = new Trie2();
            Right right = new Right();
            for (int j = 0; j < arr.length; j++) {
                double decide = Math.random();
                if (decide < 0.25) {
                    trie1.insert(arr[j]);
                    trie2.insert(arr[j]);
                    right.insert(arr[j]);
                } else if (decide < 0.5) {
                    trie1.delete(arr[j]);
                    trie2.delete(arr[j]);
                    right.delete(arr[j]);
                } else if (decide < 0.75) {
                    int ans1 = trie1.search(arr[j]);
                    int ans2 = trie2.search(arr[j]);
                    int ans3 = right.search(arr[j]);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println("Oops!");
                    }
                } else {
                    int ans1 = trie1.prefixNumber(arr[j]);
                    int ans2 = trie2.prefixNumber(arr[j]);
                    int ans3 = right.prefixNumber(arr[j]);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println("Oops!");
                    }
                }
            }
        }
        System.out.println("finish!");

    }

}
