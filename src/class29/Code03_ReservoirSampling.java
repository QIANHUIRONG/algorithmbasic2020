package class29;

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
public class Code03_ReservoirSampling {

	public static class RandomBox {
		private int[] bag; // 袋子
		private int N; // 袋子的容量
		private int count; // 现在有几个球近袋子了

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
				if (rand(count) <= N) { // n/i，就是
					bag[rand(N) - 1] = num;
				}
			}
		}

		// 返回此时袋子中的球
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
