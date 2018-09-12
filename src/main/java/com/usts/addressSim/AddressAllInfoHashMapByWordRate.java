package com.usts.addressSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.usts.lib.LoadFile;
import com.usts.lib.model.NodeInformation;
import com.usts.tools.ChangToNode;
import com.usts.tools.SingleAddressDispose;

/**
 * 词频哈希匹配
 * 
 * @author PiZhou 根据地址词库建立建立步长为1窗长为2的词频文件
 *         将词频文件中的词作为快速搜索表的关键字，建立HashMap<String,ArrayList<Object>>
 *         读取地址库文件，将地址信息插入相关的关键字指引的list中
 */
public class AddressAllInfoHashMapByWordRate {

	/**
	 * 词频hash
	 */
	// private static HashMap<String, Double> wordRate = new HashMap<>();

	/**
	 * 加载词频文件，生成以词为关键字的HashMap 
	 * 去掉数字,去掉高频,每个含有的关键字的list中都要被含有，
	 * 即就是一个地址会被多次添加
	 * 
	 * @param path
	 *            词频文件路径
	 * @return HashMap<String,ArrayList<String>>
	 * 			只有词频词的hashMap
	 */
	private static HashMap<String, ArrayList<NodeInformation>> loadWordRateFileToHashMap(
			String path) {
		HashMap<String, ArrayList<NodeInformation>> loadResult = new HashMap<String, ArrayList<NodeInformation>>();
		// 加载词频文件
		ArrayList<String> loadFile = LoadFile.loadWord(path);
		System.out.println("显示词库10行：" + loadFile.get(10));
		for (int i = 0; i < loadFile.size(); i++) {
			String s = loadFile.get(i);
			if (s != null && s.length() > 0) {
				String s1[] = s.split(",");
				if (s1[0].length() > 0) {
					if ((s1[0].charAt(0) >= '0' && s1[0].charAt(0) <= '9')
							|| s1[0].charAt(0) < 0x80
							|| (s1[0].charAt(s1[0].length() - 1) >= '0' && s1[0]
									.charAt(s1[0].length() - 1) <= '9')) {
						continue;
					} else {
						if (Integer.parseInt(s1[1]) < 45000) {// 去掉高频词
							loadResult.put(s1[0],
									new ArrayList<NodeInformation>());
						}
					}
				}
			}
		}
		return loadResult;
	}

	/**
	 * 加载标准地址库文件
	 * 
	 * @param path 
	 * 			 标准地址文件路径
	 * @return ArrayList<String>
	 * 			标准地址信息转化成NodeInformation存储到List
	 */
	private static ArrayList<NodeInformation> loadAddressFile(String path) {
		ArrayList<String> nonAddress = new ArrayList<String>();
		nonAddress = LoadFile.loadWord(path);
		System.out.println(nonAddress.get(0));
		ArrayList<NodeInformation> result = new ArrayList<NodeInformation>();
		for (int i = 0; i < nonAddress.size(); i++) {
			result.add(ChangToNode.changeToNodeInformations(nonAddress.get(i)));
		}
		return result;
	}

	
	/**将标准地址信息放入到HashMap中(设置HashMap的Value)
	 * @param nonAddress
	 *          标准地址信息<ArrayList>
	 * @param resultHashMap
	 * 			关键字设置为词频词的HashMap
	 * @return HashMap 
	 * 			快速搜索表HashMap<Key,Value>
	 */
	private static HashMap<String, ArrayList<NodeInformation>> putAddressIntoHashMap(
			ArrayList<NodeInformation> nonAddress,
			HashMap<String, ArrayList<NodeInformation>> resultHashMap) {
		for (int i = 0; i < nonAddress.size(); i++) {
			NodeInformation address = nonAddress.get(i);
			ArrayList<String> cutResult = SingleAddressDispose
					.addressCutByTwo(address.getQdz().toString());
			// 每一个关键字都需要添加这个地址；
			for (int j = 0; j < cutResult.size(); j++) {
				try {
					resultHashMap.get(cutResult.get(j)).add(address);
				} catch (NullPointerException npe) {
					// 加不进去可能是高频词
					// WriteAddress.WriteAddress("data/wordRateFile/nullAddressKey.txt",cutResult.get(j));
				}
			}
		}
		return resultHashMap;
	}

	/**
	 * 传入词频文件和标准地址文件，返回以词库中词为关键字的hashMap
	 * 
	 * @param wordRateFilePath
	 * @param nonAddressFilePath
	 * @return
	 */
	public static HashMap<String, ArrayList<NodeInformation>> SearchMap(
			String wordRateFilePath, String nonAddressFilePath) {
		HashMap<String, ArrayList<NodeInformation>> result = new HashMap<String, ArrayList<NodeInformation>>();
		// 生成以词频库中词为关键字的HASHMAP
		result = loadWordRateFileToHashMap(wordRateFilePath);
		ArrayList<NodeInformation> nonAddress = loadAddressFile(nonAddressFilePath);
		result = putAddressIntoHashMap(nonAddress, result);
		return result;
	}
}
