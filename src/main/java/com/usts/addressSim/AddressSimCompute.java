package com.usts.addressSim;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.usts.lib.ReadFile;
import com.usts.lib.TupleComparator;
import com.usts.lib.WriteFile;
import com.usts.lib.model.Address;
import com.usts.lib.model.NodeInformation;
import com.usts.lib.model.Tuple;
import com.usts.tools.ChangToNode;
public class AddressSimCompute {
	// 相似度最低值和限定的输出个数
	float barSimValue = (float) 0.30;
	int outPutLimit = 10;
	int start = 0;
	int end = 0;

	// 全局变量
	// 0位置存了标准地址的所有向量
	ArrayList<Map<String, Address>> addressMapList = new ArrayList<Map<String, Address>>();
	// 本计算类的静态存储
	static AddressSimCompute addressSimCompute = new AddressSimCompute();
	Map<String, Integer> filterMap = new HashMap<String, Integer>();
	// 文件地址
//	String dirPath = new String("D:\\AddressFile\\" );
	String dirPath = new String("F://ustsAddress//AddressFile//");
//	String dirPath = new String("data//AddressSim//");
//	String dirPath = new String("/home/usts/pz/AddressTreeModel/data/AddressSim/");
//	String dirPath = new String("/home/netmarch/addressWeb/addressFile/");
	String standName = "";
//	String waterName = "";

	// 输出结果缓存
	Map<String, ArrayList<Tuple>> resultMatrix = new HashMap<String, ArrayList<Tuple>>();
	// 快速搜索map，用于给出一个自来水地址，迅速搜索到有关键词的大多数标准地址
	Map<String, ArrayList<String>> searchMap = new HashMap<String, ArrayList<String>>();
	// 文件输出类
	WriteFile wf = null;

	// 构建每个地址的向量
	public void loadingFiles(){
		try{
		System.out.println("Loading files...");
		String[] county = {"玉山","周市","花桥","周庄","千灯","陆家","巴城","淀山","锦溪","张浦","玉山镇","周市镇","花桥镇","周庄镇","千灯镇","陆家","巴城镇","淀山镇","锦溪镇","张浦镇","昆山市","绿地国际家园"};
		for (int i = 0; i < county.length; i++)
		{
			filterMap.put(county[i],1);
		}
		ReadFile rf = new ReadFile(dirPath+"config.txt");
		BufferedReader br = rf.get();
		String strLine = br.readLine();
		String[] strArr = strLine.split(",");
		start = Integer.parseInt(strArr[0]);
		end = Integer.parseInt(strArr[1]);
		strLine = br.readLine();
		strArr = strLine.split(",");
		standName = strArr[0];
//		waterName = strArr[1];
//		System.out.println(waterName + " line:" + start + " to " + end);
		System.out.println(standName);
		Map<String, Address> addressMap = new HashMap<String, Address>();
		addressSimCompute.read(dirPath + standName, addressMap);
//		System.out.println(addressMap);
		// 构建标准地址快速搜索map
		addressSimCompute.readForSearch(dirPath + standName);
//		System.out.println("searchMap:"+addressSimCompute.searchMap);
		addressMapList.add(addressMap);

		addressMap = new HashMap<String, Address>();
//		addressSimCompute.readArrange(dirPath + waterName, addressMap, start,
//				end);
		addressMapList.add(addressMap);

		System.out.println("Loading finished.");
		System.out.println("Computing...");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private Map<String, Map<String, Integer>> StrchangeToMap(String str) throws IOException {
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

	public ArrayList<Tuple> computing(String str) throws IOException {
//		System.out.println("str:"+str);
		for (Iterator<String> iteratorI = addressMapList.get(0).keySet()
				.iterator(); iteratorI.hasNext();) {
			String itStrI = iteratorI.next();
			ArrayList<Tuple> resultLineList = new ArrayList<Tuple>();
			//叠历str获取相近的searchList,但是会返回第一个存在的地区
			String subSearchStr = "";
			String itStr = null;//用来存储关键字
			String removeStr = null;
			boolean falg = true;
			ArrayList<String> searchList = null;
			subSearchStr = str;
			//去除镇级别词进入非标地址的影响
			if (str.length()>=6) {
				for (int j = 0; j < str.length(); j = j + 6) {
					removeStr = subSearchStr.substring(0, 6);
					if (filterMap.containsKey(removeStr)) {
						subSearchStr = subSearchStr.substring(6);
					}
				}
				int length = subSearchStr.length();
				for (int j = 0; j < length; j = j + 3) {
					removeStr = subSearchStr.substring(0, 3);
					if (filterMap.containsKey(removeStr)) {
						subSearchStr = subSearchStr.substring(3);
					}
				}
				length = subSearchStr.length();
				for (int j = 0; j < length; j = j + 2) {
					removeStr = subSearchStr.substring(0, 2);
					if (filterMap.containsKey(removeStr)) {
						subSearchStr = subSearchStr.substring(2);
					}
				}
			}
			String braceLeft;
			String braceRight;
			if (subSearchStr.contains("(")||subSearchStr.contains("（"))
			{
				int indexLeft = subSearchStr.indexOf("(");
				int indexRight = -1;
				if (indexLeft != -1)
				{
					indexRight = subSearchStr.indexOf(")");
				}
				else
				{
					indexLeft = subSearchStr.indexOf("（");
					indexRight = subSearchStr.indexOf("）");
				}

				braceLeft = subSearchStr.substring(0,indexLeft);
				braceRight = subSearchStr.substring(indexRight+1);
				str = braceLeft+braceRight;
				subSearchStr = braceLeft+braceRight;
			}

			for (int i = 0; searchList == null || falg == true; i++) {
				String keyStr = null;
				if (subSearchStr.length()>= 2)
				{
					keyStr = subSearchStr.substring(i,i+2);
				}
				itStr = subSearchStr;
				searchList = addressSimCompute.searchMap.get(keyStr);
				if (searchList!=null) {
					falg = false;
				}
				if (i == subSearchStr.length()-2) {
					break;
				}
			}
			//searchList = checkSearchListSub(searchList, itStr, subSearchStr);
//			System.out.println("searchList:"+searchList);
			// 计算搜索到的所有标准地址与目标地址的相似度
			if (searchList != null && searchList.size() > 0) {
				for (int j = 0; j < searchList.size(); j++) {
					String itStrJ = searchList.get(j);
					NodeInformation node = ChangToNode.changeToNodeInformations(itStrJ);
//					System.out.println("itStrJ:"+itStrJ);
//					NodeInformation nodeInformation = ChangToNode.changeToNodeInformations(itStrJ.trim());
//					System.out.println(addressMapList
//							.get(0).get(node_information.toString()));
//					System.out.println("node_information:"+node_information.toString());
//					System.out.println("itStrJ:"+node.getQdz());
					float res = addressSimCompute.computeSim(addressMapList
							.get(0).get(node.getQdz()).getAddressMap(), 
							StrchangeToMap(str).get(str));
					if (res > barSimValue) {
						resultLineList.add(new Tuple(itStrJ, res));
					}
				}
				Collections.sort(resultLineList, new TupleComparator());
				//test
				if(resultLineList.size()>0)
					System.out.println("simValue:"+resultLineList.get(0).getSecond());
				//test end
				resultMatrix.put(itStr, resultLineList);
				//相似度太低的时候会被置空
//				System.out.println("resultLineList:"+resultLineList);
				return resultLineList;
			} else {
				resultMatrix.put(itStrI, resultLineList);
			}
		}
		return null;
	}

	// 构建快速搜索的map，用于主要词组搜素，主要是以镇后面的两个字为key，带有这两个字的所有地址组成ArrayList作为Value
	private void readForSearch(String filePath) throws IOException {
		ReadFile rf = new ReadFile(filePath);
		BufferedReader br = rf.get();

		String strLine = br.readLine();
		while (strLine != null) {
			int i = 0;
			int length = 0;
			String[] splitStrArr = strLine.split("镇");
			if (splitStrArr.length >= 2) {
				length = splitStrArr[1].length();
				if (splitStrArr[0].length() >= 2)
					addToSearchMap(searchMap,
							splitStrArr[0].substring(i, i + 2), strLine);
			}
			if (length >= 2)
				addToSearchMap(searchMap, splitStrArr[1].substring(i, i + 2),
						strLine);
			if (length >= 3)
				addToSearchMap(searchMap, splitStrArr[1].substring(i, i + 3),
						strLine);
			i = 2;
			if (length >= 5)
				addToSearchMap(searchMap, splitStrArr[1].substring(i, i + 2),
						strLine);
			strLine = br.readLine();
		}
		rf.close();
	}

	// 读取文件数据，并构建向量map
	private void read(String filePath,
			Map<String, Address> addressMap) throws IOException {
		ReadFile rf = new ReadFile(filePath);
		BufferedReader br = rf.get();

		String strLine = br.readLine();
//		Logs.writeSystemLog("文本文件第一行"+strLine);
		//读取文件一行将其转化成对象
		while (strLine != null) {
			NodeInformation nodeInformation = ChangToNode.changeToNodeInformations(strLine);
			strLine = nodeInformation.getQdz();
			Address address = new Address();
			Map<String, Integer> map = new HashMap<>();
			int i = 0;
			for (; i < strLine.length() - 1; i++) {
				addToMap(map, strLine.substring(i, i + 2));
			}
			if (strLine.length() > 0) {
				addToMap(map, strLine.substring(i, i + 1));
			}
			address.setAddressMap(map);
			address.setStandardAddress(nodeInformation.toString());
			addressMap.put(nodeInformation.getQdz(), address);
			strLine = br.readLine();
		}
		rf.close();
	}

	// 构建向量map
	private void addToMap(Map<String, Integer> map, String str) {
	    if(str == null)
	    	return;
	    int digitalFlag = 0;
	    if (str.charAt(0)<'9'&&str.charAt(0)>'0')
	    	digitalFlag = 0;
		if(str.length() > 1) {
			if (str.charAt(1) < '9' && str.charAt(1) > '0')
				digitalFlag = 0;
		}
		if (map.containsKey(str)) {
			Integer value = map.get(str);
			if(digitalFlag==1)
			{
				value = value + 2;
			}
			else {
				value++;
			}
			map.put(str, value);
		} else {
			if(digitalFlag==1) {
				map.put(str, 2);
			}
			else
			{
					map.put(str, 1);
			}
		}
	}

	// 构建搜索map
	private void addToSearchMap(Map<String, ArrayList<String>> map, String str,
			String text) {
		if (map.containsKey(str)) {
			ArrayList<String> value = map.get(str);
			value.add(text);
		} else {
			ArrayList<String> value = new ArrayList<String>();
			value.add(text);
			map.put(str, value);
		}
	}

	// 计算相似度值
	private float computeSim(Map<String, Integer> map1,
			Map<String, Integer> map2) {
		Map<String, Tuple> vectorMap = new HashMap<String, Tuple>();
		ArrayList<Float> va = new ArrayList<Float>();
		ArrayList<Float> vb = new ArrayList<Float>();

		Set<String> set1 = map1.keySet();
//		System.out.println(set1);
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

	float sim(ArrayList<Float> va, ArrayList<Float> vb) {
		// �������ά�Ȳ���ȣ����ܼ��㣬�����˳�
		if (va.size() != vb.size()) {
			return 0;
		}

		int size = va.size();
		float simVal = 0;

		// sim(va,vb) = (va * vb) / (|va| * |vb|)
		// ���� = va.get(0)*vb.get(0) + va.get(1)*vb.get(1) +...+ va.get(size -
		// 1)*vb.get(size - 1)
		// ��ĸ = va��ģ * vb��ģ = sqrt((va.get(0))^2 + (va.get(1))^2 + ... +
		// va.get(size - 1)^2) * sqrt((vb.get(0))^2 + (vb.get(1))^2 + ... +
		// vb.get(size - 1)^2)
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
	class importantInner{
		
	}
}
