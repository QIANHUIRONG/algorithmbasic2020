package class32;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
题意：实现AC自动机
 */
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
// 时复：O(n + m)，其中n是大文章的长度（匹配阶段），m是所有敏感词的长度（因为需要建立前缀树）
/*
【思维导图】

一、AC自动机解决什么问题：
	1.有一个小词典有若干敏感词String[] str，一篇大文章String s，收集大文章中包含的所有小词典中的敏感词

二、AC自动机的思想：前缀树+kmp
	kmp是单串匹配单串，ac自动机是大文章匹配多串

二、流程
	1.先建立前缀树，ac自动机是对前缀树升级，每个节点加了fail指针，表示如果匹配失败了节点要去的下一个节点。它可以加速匹配过程。类似KMP中的next[]信息数组！
	2.人为规定初始化：
		前缀树的头结点fail指针一定是指向null
		头结点直接节点（它只往下一级的节点）fail指针一律指向头部
	3.接下来的fail指针怎么设置：根据宽度优先的顺序设置每一个节点的fail指针
		1.X的父亲节点到自己是什么字符的路：假设b, x的fail指针指向->Y节点，Y有没有走向b的路？没有
		Y的fail指针指向Z，Z有没有走向b的路，没有
		Z的fail指针指向K，K有没有走向b的路，有！那么X节点的fail指针就去指向K节点顺着b的路走向的节点，比如是J节点

		2.如果走着走着走到头节点了，而且头节点也没有走向b的路，头节点也要去走向fail指针指向的节点，fail指针指向null，那么x节点的fail指针直接指向头

	4.某一个节点X，它父亲到它的路是a，它父亲节点的fail指针指向了节点甲
	如果甲有指向a的路，x直接指过去，如果甲没有走向a的路，再通过fail指针跳到乙
	如果乙有指向的路，x直接指过去，如果乙没有走向a的路，再通过fail	指针跳到丙
	如果丙有指向a的路，x直接指过去，如果丙没有走向a的路，再通过fai指针跳到丁
	即果跳着跳着，跳到空了都没有找到有对应的路，那么x节点指向头节点

	5.fail指针的本质
	fail指针的含义：如果必须以e结尾，哪一个敏感词的前缀串和我以e结尾的后缀串相等并且长度最大，我就指到哪儿去
	我是abcde，
	bcde,cde,de,e都满足
	此时应该指向bcde

	6.fail指针怎么用在大文章查询敏感词汇中？【1：59】
	大文章是abcdf...， 从0位置的a开始匹配，一直匹配成功，直到f失败了，那么就跳到f.fail继续匹配， f.fail代表的路径是cdf，表示大文章从2位置的c继续匹配，0位置的a和1位置的b不可能匹配出来，
	因为这已经是以f结尾，另一个前缀串和我匹配的最长长度了，尽最大努力的继续匹配。


 */

/*
来自GPT
1.AC自动机（Aho-Corasick自动机）是一种用于多模式字符串匹配的算法，它是在给定的输入文本中同时寻找多个模式串的位置。AC自动机结合了前缀树（Trie）和KMP（Knuth-Morris-Pratt）算法的思想
Aho-Corasick 是人名。Aho-Corasick自动机（Aho-Corasick Algorithm）是由Alfred V. Aho和Margaret J. Corasick于1975年在他们的论文《Efficient string matching: an aid to bibliographic search》中提出的一种多模式字符串匹配算法。

2.应用
	文本编辑器中的关键字高亮
	生物信息学中的DNA序列匹配

4.如果是暴力解法，大文章的每个位置都需要去匹配每一个敏感词，假设大文章n，k个敏感词，敏感词平均长度m，那么时复就是o(k * n * m) n中的每一个位置都需要尝试匹配m的长度，共k次

 */
public class Code04_AC2 {

	// 前缀树的节点
	public static class Node {
		public String end; 		// 如果一个node，end为空，不是结尾，如果end不为空，表示这个点是某个字符串的结尾，end的值就是这个字符串
		public boolean endUse; 		// 只有在上面的end变量不为空的时候，endUse才有意义，表示，这个字符串之前有没有成功匹配过敏感词，如果匹配过了，以前也一定收集到答案中了，就不重复收集答案了
		public Node fail; // ac自动机的fail指针！
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
			cur.end = s; // 如果来到了结尾，end属性就是当前字符串
		}

		// 建立ac自动机。也就是在前缀树的基础上设置好fail指针
		public void build() {
			Queue<Node> queue = new LinkedList<>(); // 宽度优先遍历需要的队列
			queue.add(root);
			while (!queue.isEmpty()) {
				// parent是某个父亲。由父亲去设置它的孩子的fail指针，而不是设置自己的fail指针
				Node parent = queue.poll();
				for (int i = 0; i < 26; i++) { // 所有的孩子
					// 父亲要把i号儿子的fail指针设置好！
					if (parent.nexts[i] != null) { // 如果真的有i号儿子
						parent.nexts[i].fail = root; // 孩子的fail指针先设置成root，如果找到了，我再设置成别的。如果都没有找到，就是这个root
						Node parentFail = parent.fail; // cfail：父亲的fail指针指向的节点
						while (parentFail != null) { // 如果为空，就是root了,这个while直接结束了,i号孩子的fail指针就是上面设置好的指向root头节点
							if (parentFail.nexts[i] != null) { // 如果有指向i的路，孩子的fail指针就可以设置了
								parent.nexts[i].fail = parentFail.nexts[i];
								break;
							}
							parentFail = parentFail.fail; // 如果没有i方向上的路，继续往fail跳
						}
						queue.add(parent.nexts[i]); // 孩子设置好了，加到队列中，继续宽度优先
					}
				}
			}
		}

		// 大文章：content。收集大文章的包含的敏感词汇
		public List<String> containWords(String content) {
			char[] str = content.toCharArray();
			Node cur = root; // 从根节点开始匹配
			List<String> ans = new ArrayList<>(); // 收集答案
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
				while (follow != root) { // 来到一个节点就转一圈，收集是end的节点到ans中
					if (follow.endUse) { // 我之前收集过这个答案，那么第二次来的时候就break了，不用重复收集
						break;
					}
					// 不同的需求，在这一段之间修改
					if (follow.end != null) {
						ans.add(follow.end); // 收集到一个敏感词汇，因为end属性存的就是字符串
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
