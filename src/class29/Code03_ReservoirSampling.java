package class29;

/*
题意：蓄水池算法
一个只有n个容量的袋子
一个源源不断吐球的流
故到每一个球进袋子的概率均等
 */

/*
时间：1：21
题意【1：21】
流程【1：26】：
	1-10号求直接进
	后面的球，先用10/i(n/i)的概率决定要不要进袋子，n是容量，i是第几个的索引
	如果要进了，袋子中1-10随机取一个丢了
流程为什么对【1：29】
做实验，code【1：39】
应用【1：53】：游戏抽奖

 */

/*
思维导图：
	1.假设袋子容量是10，1-10号求直接进入袋子
	2.10号球之后，以10/i的概率决定要不要进入袋子，如果要进入袋子，袋子中原有的10个随机取一个丢了
	3.抽象化：用n/i的概率决定要不要进袋子，n是容量，i是第几个的索引
	4.怎么用代码表示n/i的概率，随机求一个1-i的值，如果<=n,就是命中了n/i

	5.为什么对？
	比如当前来到1749号球，需要保证每个球进入袋子的概率都是10/1749；
	对于1749号球来说，它进入袋子概率就是10/1749；
	对于其他球，比如24号球，它在袋子中的概率：10/24（24号球的时候需要进入袋子） * 24/25（下一行解释） * 26/25 ... 1748/1749, 分子分母约掉之后，也是10/1749；
	这里的 24/25是什么？
	25号球来的时候，24号球想要留在袋子中的概率，就是 1-24号球出去的概率；
	24号球出去的概率：10/25(25号球进来) * 1/10(从袋子中出去的刚好是24号球) = 1/25；那么24剩下的概率就是24/25；

	6.应用：游戏抽奖
	1月1号，所有登录的用户，抽奖100个人；
	如果你用常规的想法，需要收集所有登录的用户，然后1月2号0点的时候，对所有的用户进行抽奖处理；如果用户贼多，这么搞很重；
	利用蓄水池算法：1月1号登录的用户，如果是第一个登录，就顺序给一个编号i，然后以100/i决定要不要进入奖池，在1月2号0点的时候，你直接可以公布100个名单，代码很轻。
 */
public class Code03_ReservoirSampling {

	public static class RandomBox {
		private int[] bag; // 袋子
		private int N; // 袋子的容量
		private int count; // 现在有几个球进袋子了

		public RandomBox(int capacity) {
			bag = new int[capacity];
			N = capacity;
			count = 0;
		}

		/**
		 * 球来了，要等概率进入袋子
		 * @param num
		 */
		public void add(int num) {
			count++;
			if (count <= N) { // 还没到容量直接进
				bag[count - 1] = num;
			} else { // 到容量了，先用N/i的概率决定要不要进袋子
				if (rand(count) <= N) { // n/i的概率
					bag[rand(N) - 1] = num;
				}
			}
		}

		// 返回此时袋子中的球,为了安全，只给了一个副本
		public int[] choices() {
			int[] ans = new int[N];
			for (int i = 0; i < N; i++) {
				ans[i] = bag[i];
			}
			return ans;
		}

		// [1,max]随机返回1个
		private int rand(int max) {
			return 1 + (int) (Math.random() * max);
		}

	}

	// 请等概率返回1~i中的一个数字
	public static int random(int i) {
		return (int) (Math.random() * i) + 1;
	}

	public static void main(String[] args) {
		System.out.println("hello");
		int test = 10000;
		int ballNum = 17;
		int[] count = new int[ballNum + 1];
		for (int i = 0; i < test; i++) {
			int[] bag = new int[10];
			int bagi = 0;
			for (int num = 1; num <= ballNum; num++) {
				if (num <= 10) {
					bag[bagi++] = num;
				} else { // num > 10
					if (random(num) <= 10) { // 一定要把num球入袋子
						bagi = (int) (Math.random() * 10);
						bag[bagi] = num;
					}
				}

			}
			for (int num : bag) {
				count[num]++;
			}
		}
		for (int i = 0; i <= ballNum; i++) {
			System.out.println(count[i]);
		}

		System.out.println("hello");
		int all = 100;
		int choose = 10;
		int testTimes = 50000;
		int[] counts = new int[all + 1];
		for (int i = 0; i < testTimes; i++) {
			RandomBox box = new RandomBox(choose);
			for (int num = 1; num <= all; num++) {
				box.add(num);
			}
			int[] ans = box.choices();
			for (int j = 0; j < ans.length; j++) {
				counts[ans[j]]++;
			}
		}

		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " times : " + counts[i]);
		}

	}
}
