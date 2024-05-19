package class33;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

// 需要自己找一下javax.xml.bind的jar，然后导入到项目

/*【时间】
hash函数特点：6
hash碰撞：9
最重要的特征：离散型和均匀性 10
hash结果都%10，那么就在0-9均匀分布：19

hash表：22
hash表的扩容：26
hash表的时间复杂度O（1）：29

布隆过滤器干啥用的：43
失误率是什么：47：布隆过滤器可能无中生有，不可能有中生无！
位图：50
布隆过滤器原理：53
位图的大小应该跟什么因素有关？如何确定位图的大小？59
3个公式：1：03
hash函数的个数跟失误率的关系：1：03
真实失误率：1：07
多个hash函数怎么找：1：14
HDFS中布隆过滤器的应用：1：17

一致性哈希
经典的数据存储结构：1：21
问题：如果加了一台机器，就G了：1：28  ：数据迁移的代价是全量的-> 一致性hash就是解决这个问题，增减机器，数据迁移不是全量
一致性hash：1：29
什么叫顺时针找：1：38
初始化的3台机器如果把环均分，加了1台机器如何把环均分：1：41
虚拟节点技术解决：1：45
虚拟节点技术解决负载均衡、负载管理：1：57
 */

/*
有语雀笔记！！！
 */
public class Hash {

	private MessageDigest hash;

	public Hash(String algorithm) {
		try {
			hash = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String hashCode(String input) {
		return DatatypeConverter.printHexBinary(hash.digest(input.getBytes())).toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println("支持的算法 : ");
		for (String str : Security.getAlgorithms("MessageDigest")) {
			System.out.println(str);
		}
		System.out.println("=======");

		String algorithm = "MD5";
		Hash hash = new Hash(algorithm);

		String input1 = "zuochengyunzuochengyun1";
		String input2 = "zuochengyunzuochengyun2";
		String input3 = "zuochengyunzuochengyun3";
		String input4 = "zuochengyunzuochengyun4";
		String input5 = "zuochengyunzuochengyun5";
		System.out.println(hash.hashCode(input1));
		System.out.println(hash.hashCode(input2));
		System.out.println(hash.hashCode(input3));
		System.out.println(hash.hashCode(input4));
		System.out.println(hash.hashCode(input5));

	}

}
