package com.usts.addressSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.usts.lib.LoadFile;
import com.usts.tools.SingleAddressDispose;

public class AddressHashMapByWordRate {
	/**
	 * 词频hash
	 */
	// private static HashMap<String, Double> wordRate = new HashMap<>();

	/**
	 * 加载词频文件，生成以词为关键字的HashMap 去掉数字,去掉高频,每个含有的关键字的list中都要被含有，即就是一个地址会被多次添加
	 * 
	 * @param path
	 *            词频文件路径
	 * @return HashMap<String,ArrayList<String>>
	 */
	private static HashMap<String, ArrayList<String>> loadWordRateFileToHashMap(
			String path) {
		HashMap<String, ArrayList<String>> loadResult = new HashMap<>();
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
							|| (s1[0].charAt(s1[0].length() - 1) >= '0'
							&& s1[0].charAt(s1[0].length() - 1) <= '9')) {
						continue;
					} else {
						if (Integer.parseInt(s1[1]) < 45000) {// 去掉高频词
							loadResult.put(s1[0], new ArrayList<String>());
							// wordRate.put(s1[0], Double.parseDouble(s1[1]));
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
	 * @return ArrayList<String>
	 */
	private static ArrayList<String> loadNonAddressFile(String path) {
		ArrayList<String> nonAddress = new ArrayList<>();
		nonAddress = LoadFile.loadWord(path);
		return nonAddress;
	}

	/**
	 * 将加载完成的地址计算放入HASHMAP中
	 * 
	 * @param nonAddress
	 * @param wordRate
	 */
	private static HashMap<String, ArrayList<String>> putNonAddressIntoHashMap(
			ArrayList<String> nonAddress,
			HashMap<String, ArrayList<String>> resultHashMap) {
		for (int i = 0; i < nonAddress.size(); i++) {
			String address = nonAddress.get(i);
			ArrayList<String> cutResult = SingleAddressDispose
					.addressCutByTwo(address);
			// 每一个关键字都需要添加这个地址；
			for (int j = 0; j < cutResult.size(); j++) {
				try {
					resultHashMap.get(cutResult.get(j)).add("昆山市" + address);
				} catch (NullPointerException npe) {
					// 加不进去可能是高频词
					// WriteAddress.WriteAddress("data/wordRateFile/nullAddressKey.txt",cutResult.get(j));
				}
			}
		}
		return resultHashMap;
	}

	/**
	 * 传入词频文件和标准地址文件，返回以词库中词为关键字的hashmap
	 * 
	 * @param wordRateFilePath
	 * @param nonAddressFilePath
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> SearchMap(
			String wordRateFilePath, String nonAddressFilePath) {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		// 生成以词频库中词为关键字的HASHMAP
		result = loadWordRateFileToHashMap(wordRateFilePath);
		ArrayList<String> nonAddress = loadNonAddressFile(nonAddressFilePath);
		result = putNonAddressIntoHashMap(nonAddress, result);
		return result;
	}
}
