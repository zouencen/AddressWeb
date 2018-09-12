package com.usts.addressSim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.usts.lib.TupleComparator;
import com.usts.lib.model.Tuple;

public class CosDistance {
	//取并集
	
	public static ArrayList<Tuple> computerAddressList(String nonAddress,ArrayList<String> addressCollection){
		ArrayList<Tuple> resultSim = new ArrayList<>(); 
		for (int i = 0; i < addressCollection.size(); i++) {
			try {
				resultSim.add(new Tuple(addressCollection.get(i),computer(nonAddress, addressCollection.get(i))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(resultSim,new TupleComparator());
		return resultSim;
	}
	
	private static float computer(String nonAddress,String randomAddress) throws IOException{
		float sim = 0;
		Map<String, Map<String, Integer>> nonAddressMap1 = StrchangeToMap(nonAddress);
		Map<String, Map<String, Integer>> randomAddressMap = StrchangeToMap(randomAddress);
		sim = computeSim(nonAddressMap1.get(nonAddress), randomAddressMap.get(randomAddress));
		return sim;
	}
	
	/**将字符串转化为map
	 * @param str
	 * @return
	 * @throws IOException
	 */
	private static Map<String, Map<String, Integer>> StrchangeToMap(String str)
			throws IOException {
		Map<String, Map<String, Integer>> addressMap = new HashMap<String, Map<String, Integer>>();
		String strLine = str;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		int i = 0;
		for (; i < strLine.length() - 1; i++) {
			addToMap(wordMap, strLine.substring(i, i + 2));
		}
		if (strLine.length() > 0) {
			addToMap(wordMap, strLine.substring(i, i + 1));
		}
		addressMap.put(strLine, wordMap);
		return addressMap;
	}
	
	private static void addToMap(Map<String, Integer> map, String str) {
		if (str == null)
			return;
		int digitalFlag = 0;
		if (str.charAt(0) < '9' && str.charAt(0) > '0')
			digitalFlag = 0;
		if (str.length() > 1) {
			if (str.charAt(1) < '9' && str.charAt(1) > '0')
				digitalFlag = 0;
		}
		if (map.containsKey(str)) {
			Integer value = map.get(str);
			if (digitalFlag == 1) {
				value = value + 2;
			} else {
//				value++;
			}
			map.put(str, value);
		} else {
			if (digitalFlag == 1) {
				map.put(str, 2);
			} else {
				map.put(str, 1);
			}
		}
	}
	
	// 计算相似度值
	/**通过两个map计算相似度。
	 * @param map1
	 * @param map2
	 * @return
	 */
	private static float computeSim(Map<String, Integer> map1,
			Map<String, Integer> map2) {
		Map<String, Tuple> vectorMap = new HashMap<String, Tuple>();
		ArrayList<Float> va = new ArrayList<Float>();
		ArrayList<Float> vb = new ArrayList<Float>();

		Set<String> set1 = map1.keySet();
		// System.out.println(set1);
		Iterator<String> iterator1 = set1.iterator();
		while (iterator1.hasNext()) {
			String key1 = iterator1.next();
			if (map2.containsKey(key1)) {
				float value1 = map1.get(key1);
				float value2 = map2.get(key1);
				vectorMap.put(key1, new Tuple(value1, value2));
			} else {
				float value1 = map1.get(key1);
				vectorMap.put(key1, new Tuple(value1, new Float(0)));
			}
		}

		Set<String> set2 = map2.keySet();
		Iterator<String> iterator2 = set2.iterator();
		while (iterator2.hasNext()) {
			String key2 = iterator2.next();
			if (!map1.containsKey(key2)) {
				float value2 = map2.get(key2);
				vectorMap.put(key2, new Tuple(new Float(0), value2));
			}
		}

		Set<String> set = vectorMap.keySet();
		Iterator<String> iteratorVector = set.iterator();
		while (iteratorVector.hasNext()) {
			String key = iteratorVector.next();
			Tuple tuple = vectorMap.get(key);
			va.add((Float) tuple.getFirst());
			vb.add((Float) tuple.getSecond());
		}

		float res = sim(va, vb);
		return res;
	}
	
	private static float sim(ArrayList<Float> va, ArrayList<Float> vb) {
		if (va.size() != vb.size()) {
			return 0;
		}

		int size = va.size();
		float simVal = 0;
		float num = 0;// numerator����
		float den = 1;// denominator��ĸ

		double a = 0, b = 0;
		for (int i = 0; i < size; i++) {
			num += Float.parseFloat(va.get(i).toString())
					* Float.parseFloat(vb.get(i).toString());
		}
		for (int j = 0; j < size; j++) {
			a += Math.pow(Double.parseDouble(va.get(j).toString()), 2);
			b += Math.pow(Double.parseDouble(vb.get(j).toString()), 2);
		}
		double s = Math.sqrt(a) * Math.sqrt(b);
		den = (float) s;

		if (den == 0) {
			den = 1;
		}
		simVal = num / den;
		return simVal;
	}
}
