package class32;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
时间：
题意：1：32
先把眼光放到敏感词上，建前缀树：1：35
ac自动机流程：1：38
fail指针本质：1：50
有什么用？怎么通过fail指针加速匹配？1：59
fail指针的code：2：11
	build()方法建立fail指针：2：18
	containWords()查找：2：27
 */
/*
一句话：在前缀树上玩KMP
 */
/*
【思维导图】
题意：有一个小词典有若干敏感词，一篇大文章，收集大文章中包含的所有小词典中的敏感词

一、先关注小词典
先关注小词典中出现的单词，先把他们建成前缀树，加快后来大文章找敏感词。
所谓的AC自动机就是在前缀树的基础上做升级。

二、流程
	1.对前缀树升级，每个节点加fail指针，表示如果匹配失败了节点要去的下一个节点。它可以加速匹配过程
	2.人为规定初始化：
		前缀树的头结点fail指针一定是指向null
		头结点直接节点（它只往下一级的节点）fail指针一律指向头部
		接下来的fail指针怎么设置？先建完前缀树之后再去设置
		建完之后，然后再根据宽度优先的顺序设置每一个节点的fail指针
	3.设定底层的Fail指针
		1.X的父亲节点的fai指针指向->头，头有没有走向b的路？头去到头的fail指针，为null，表示没有走向b的路---> X的fai指针直接指向头
		2.X的父亲节点的fil指针指向->头，头有没有走向c的路？有，走过c的路来到y ---> x的fail指针就指向y

	4.某一个节点X，它父亲到它的路是a，它父亲节点的fail指针指向了节点甲
	如果甲有指向a的路，x直接指过去，如果甲没有走向a的路，再通过fail指针跳到乙
	如果乙有指向的路，x直接指过去，如果乙没有走向a的路，再通过fail	指针跳到丙
	如果丙有指向a的路，x直接指过去，如果丙没有走向a的路，再通过fai指针跳到丁
	即果跳着跳着，跳到空了都没有找到有对应的路，那么x节点指向头节点

	5.ac自动机，关键在于怎么理解fail指针
	fail指针的含义：如果必须以e结尾，哪一个另外的前缀串和我以e结尾的后缀串彻底相等并且长度最大，我就指到哪儿去
	我是abcde，
	bcde,cde,de,e都满足
	此时应该指向bcde

	6.fail指针怎么用在大文章查询敏感词汇中？【1：59】
	大文章是abcdf...， 从0位置的a开始匹配，一直匹配成功，直到f失败了，那么就跳到f.fail继续匹配， f.fail代表的路径是cdf，表示大文章从2位置的c继续匹配，0位置的a和1位置的b不可能匹配出来，
	因为这已经是以f结尾，另一个前缀串和我匹配的最长长度了，尽最大努力的继续匹配。

	7.kmp是单串匹配单串，ac自动机是大文章匹配多串


 */
public class Code04_AC2 {

	// 前缀树的节点
	public static class Node {
		public String end; 		// 如果一个node，end为空，不是结尾，如果end不为空，表示这个点是某个字符串的结尾，end的值就是这个字符串
		public boolean endUse; 		// 只有在上面的end变量不为空的时候，endUse才有意义，表示，这个字符串之前有没有加入过答案
		public Node fail;
		public Node[] nexts;

		public Node() {
			endUse = false;
			end = null;
			fail = null;
			nexts = new Node[26];
		}
	}

	public static class ACAutomation {
		private Node root;

		public ACAutomation() {
			root = new Node();
		}

		// 敏感词汇先建出初始的前缀树，还没有设置fail指针
		public void insert(String s) {
			char[] str = s.toCharArray();
			Node cur = root;
			for (int i = 0; i < str.length; i++) {
				int path = str[i] - 'a';
				if (cur.nexts[path] == null) {
					cur.nexts[path] = new Node();
				}
				cur = cur.nexts[path];
			}
			cur.end = s;
		}

		// 建立ac自动机。也就是在前缀树的基础上设置好fail指针
		public void build() {
			Queue<Node> queue = new LinkedList<>(); // 宽度优先遍历需要的队列
			queue.add(root);
			while (!queue.isEmpty()) {
				// 某个父亲，cur。队列弹出的节点，去设置它的孩子的fail指针，而不是设置自己的fail指针
				Node cur = queue.poll();
				for (int i = 0; i < 26; i++) { // 所有的孩子
					// cur -> 父亲  i号儿子，必须把i号儿子的fail指针设置好！
					if (cur.nexts[i] != null) { // 如果真的有i号儿子
						cur.nexts[i].fail = root; // 孩子的fail指针先设置成root，如果找到了，我再设置成别的。如果都没有找到，就是这个root
						Node cfail = cur.fail; // cfail：父亲的fail指针指向的节点
						// 寻找  cur的子的fail指针
						while (cfail != null) { // 如果为空，就是root了
							if (cfail.nexts[i] != null) { // 如果cfail有指向i的路，孩子的fail指针就可以设置了
								cur.nexts[i].fail = cfail.nexts[i];
								break;
							}
							cfail = cfail.fail; // 如果没有i方向上的路，继续往fail跳
						}
						queue.add(cur.nexts[i]); // 孩子设置好了，加到队列中，继续宽度优先
					}
				}
			}
		}

		// 大文章：content。收集大文章的包含的敏感词汇
		public List<String> containWords(String content) {
			char[] str = content.toCharArray();
			Node cur = root;
			List<String> ans = new ArrayList<>();
			for (int i = 0; i < str.length; i++) {
				int path = str[i] - 'a'; // 当前来到的路
				// 如果当前字符在这条路上没配出来，就随着fail方向走向下条路径。能匹配出来，就直接跳过这个循环，下一行代码 继续去下一个节点
				while (cur.nexts[path] == null && cur != root) {
					cur = cur.fail;
				}
				// 1) 现在来到的路径，是可以继续匹配的，那就继续往下走
				// 2) 现在来到的节点，就是前缀树的根节点
				cur = cur.nexts[path] != null ? cur.nexts[path] : root;
				// 不是root我就绕一圈，收集可能的答案。这里通过fail指针绕过去的，都是可以匹配上的。比如大文章abcde，敏感词abcd、bcd, 那么遍历到大文章d的时候，follow去绕一圈收集到abcd、bcd
				Node follow = cur;// follow变量专门用来转一圈的
				while (follow != root) {
					if (follow.endUse) { // 我之前收集过这个答案，那么第二次来的时候就break了，不用重复收集
						break;
					}
					// 不同的需求，在这一段之间修改
					if (follow.end != null) {
						ans.add(follow.end); // 收集到一个敏感词汇
						follow.endUse = true;
					}
					// 不同的需求，在这一段之间修改
					follow = follow.fail;
				}
			}
			return ans;
		}
	}

	public static void main(String[] args) {
		ACAutomation ac = new ACAutomation();
		ac.insert("dhe");
		ac.insert("he");
		ac.insert("abcdheks");
		// 设置fail指针
		ac.build();

		List<String> contains = ac.containWords("abcdhekskdjfafhasldkflskdjhwqaeruv");
		for (String word : contains) {
			System.out.println(word);
		}
	}

}
