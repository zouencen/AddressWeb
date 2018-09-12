package com.usts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.usts.addressSim.AddressAllInfoComputer;
import com.usts.addressSim.AddressAllInfoHashMapByWordRate;
import com.usts.addressSim.AddressComputer;
import com.usts.addressSim.AddressHashMapByWordRate;
import com.usts.addressSim.AddressSimCompute;
import com.usts.lib.LoadProperties;
import com.usts.lib.model.NodeInformation;
import com.usts.lib.model.Tuple;

public class ServiceUtil {
	/**
	 * 1:老版本2:词频哈希版本3:词频哈希版本，2加载的地址库文件只包含地址名， 3加载的地址库文件包含全地址信息
	 * 
	 */
	private static int VERSION = 3;// 默认为2
	private static AddressSimCompute addressSimCompute = new AddressSimCompute();
	private static HashMap<String, ArrayList<String>> singleAddressSearchMap = new HashMap<>();
	private static HashMap<String, ArrayList<NodeInformation>> detailAddressSearchMap = new HashMap<>();

	public ServiceUtil() {
		loadFile();
	}

	public ArrayList<String> getRight(String str) {
		ArrayList<Tuple> tupleResult = null;
		try {
			switch (VERSION) {
			case 1:
				tupleResult = addressSimCompute.computing(str);
				break;
			case 2:
				tupleResult = AddressComputer.computeAddress(str,
						singleAddressSearchMap);
				break;
			case 3:
				tupleResult = AddressAllInfoComputer.computeAddress(str,
						detailAddressSearchMap);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 相似度限制
		tupleResult = limitSim(tupleResult);
		ArrayList<String> resultStr = new ArrayList<>();
		tupleConvertIntoStr(tupleResult, resultStr);
		resultStr = getNoMoreTen(resultStr);//结果约束到十个以内
		return resultStr;
	}

	/**
	 * 文件加载函数
	 * 
	 */
	private static void loadFile() {
		String wordRate = LoadProperties.getValues("wordRate");
		String addressFile = LoadProperties.getValues("addressFile");
		switch (VERSION) {
		case 1:
			addressSimCompute.loadingFiles();// 最先版本
		case 2:
			singleAddressSearchMap = AddressHashMapByWordRate.SearchMap(
					wordRate, addressFile);// 单一地址信息版本
			break;
		case 3:
			detailAddressSearchMap = AddressAllInfoHashMapByWordRate.SearchMap(
					wordRate, addressFile);// 全地址信息版本
		default:
			break;
		}
	}

	/**
	 * 相似度限制函数
	 * 
	 * @param result
	 *            含有地址集的ArrayList
	 * @return 返回相似度高于阈值的地址集
	 */
	private ArrayList<Tuple> limitSim(ArrayList<Tuple> result) {
		if (result!=null&&result.isEmpty()) {
			return result;
		} else {
			float simDefault = Float
					.parseFloat(LoadProperties.getValues("sim"));
			ArrayList<Tuple> nResult = new ArrayList<>();
			for (Tuple aResult : result) {
				float simResult = (float) aResult.getSecond();
				if (simResult > simDefault) {
					nResult.add(aResult);
				}
			}
			return nResult;
		}
	}

	/**将Tuple格式的ArrayList转化为String格式的ArrayList
	 * @param result 
	 * 			Tuple格式的ArrayList
	 * @param resultArrayList
	 * 			String格式的ArrayList
	 */
	private void tupleConvertIntoStr(ArrayList<Tuple> result,
			ArrayList<String> resultArrayList) {
		if (result == null) {
			result = new ArrayList<>();
		}
		for (int i = 0; i < result.size(); i++) {
			resultArrayList.add(result.get(i).toString());
		}
	}
	
	//获取地址中不超过十个的最优匹配
		private ArrayList<String> getNoMoreTen(ArrayList<String> arrayList){
			ArrayList<String> arrayList2 = new ArrayList<>();
			if (arrayList==null) {
				return null;
			}
			if(arrayList.size()>=10)
				for (int i = 0; i < 10; i++)
					arrayList2.add(arrayList.get(i));
			else{
				arrayList2 = arrayList;
			}
			return arrayList2;
		}
}
