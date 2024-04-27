package class15;

// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/friend-circles/
// 可以直接通过
/*
题意：
n个人，给你一个 n x n 的矩阵 isFriends ，其中 isFriends[i][j] = 1 表示第 i 个人和第 j 个人认识，
而 isFriends[i][j] = 0 表示二者不认识
返回矩阵中朋友圈数量
 */

/*
题解：使用并查集数据结构，认识就合并，最终返回有几个集合。
 */
public class Code01_FriendCircles {

    public static int findCircleNum(int[][] m) {
        int N = m.length;
        // 1、每一个人都当作一个元素，初始化并查集
        UnionFind unionFind = new UnionFind(N);
        // 2、遍历所有人，如果认识就合并
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) { // 两个for只遍历右上半部区
                if (m[i][j] == 1) { // i和j互相认识
                    unionFind.union(i, j);
                }
            }
        }
        // 3、返回集合个数
        return unionFind.setCount();
    }

    public static class UnionFind {
        public static int[] father;// father[i] = k, 表示i的父节点是k。
        public static int[] size; // size[i]=k, 如果i是代表节点，那么i所在集合大小是k；如果不是代表节点，无意义。
        public static int setCount; // 一共有多少个集合

        // 初始化并查集
        public UnionFind(int n) {
            father = new int[n];
            size = new int[n];
            setCount = n; // 初始化时，每一个元素都是一个独立的集合
            for (int i = 0; i < n; i++) {
                father[i] = i; // 初始化时，i的父节点就是i
                size[i] = 1; // 初始化时，都是代表节点，size都是1
            }
        }

        // x所在的集合，和y所在的集合，合并成一个集合
        public static void union(int x, int y) {
            // 1、找到各自的代表节点
            int fx = find(x);
            int fy = find(y);
            if (fx != fy) {
                // 2、小挂大优化。这里也是一个大优化，可以使得合并完链长度短一点
                if (size[fx] >= size[fy]) { // y小，y挂x
                    father[fy] = fx; // y的父亲变成a，表示y挂a
                    size[fx] += size[fy]; // 累加y的size到x上
                } else {
                    father[fx] = fy;
                    size[fy] += size[fx];
                }
                setCount--; // 产生一次集合的合并，setCount--
            }
        }

        // 返回并查集有几个集合
        public int setCount() {
            return setCount;
        }

        // 从i开始寻找集合代表点
		/*
		这里会做链的扁平化优化
		从某个节点一直往上找到代表节点x，记录沿途经过的节点，最后把沿途节点的父亲节点都设置为x
		这里是一个大优化，单次看起来可能慢，但是痛就痛1次，调用频繁之后，均摊下来O（1）
		 */
        public static int find(int i) {
            int[] help = new int[father.length];// 充当原本栈的结构
            int j = 0; // 这个变量用来表示沿途的长度，不然不知道help[]数组什么时候遍历完
            // i不等于自己的父亲，就一直往上。什么时候i和自己的父亲是相等，就是i到了代表节点的时候
            while (father[i] != i) {
                help[j++] = i;
                i = father[i]; // 我来到我的父
            }
            j--;
            while (j >= 0) {
                father[help[j--]] = i;
            }
            return i;
        }
    }

}
