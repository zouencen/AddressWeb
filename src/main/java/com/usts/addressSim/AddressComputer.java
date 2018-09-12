package com.usts.addressSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.usts.lib.model.Tuple;
import com.usts.tools.SingleAddressDispose;

public class AddressComputer {
	public static ArrayList<Tuple> computeAddress(String nonAddress,HashMap<String, ArrayList<String>> searchMap){
		ArrayList<String> cutResult = SingleAddressDispose
				.addressCutByTwo(nonAddress);
//		String matchResult = "";
		//将所有能匹配到的地址集取并
		ArrayList<String> matchAddress = new ArrayList<>();
		for (int i = 0; i < cutResult.size(); i++) {
			try {
				ArrayList<String> address = searchMap.get(cutResult.get(i));
				unionAddress(matchAddress, address);
			} catch (Exception e) {

			}
		}
//		System.out.println("匹配的地址集:"+matchAddress.size());
		ArrayList<Tuple> simMatchAddress = new ArrayList<>();
		simMatchAddress = CosDistance.computerAddressList(nonAddress, matchAddress);
		
		//三级计算
		if (simMatchAddress.size()> 9)
		{
			simMatchAddress = new ArrayList<Tuple>(simMatchAddress.subList(0, 9));
		}
		simMatchAddress = ThreeLevelOptimization.resultCheckBymin(nonAddress, simMatchAddress);
		return simMatchAddress;
	}
	
	private static void unionAddress(ArrayList<String> matchAddress,ArrayList<String> address){
//		System.out.println("地址集长度："+matchAddress.size()+"新增地址长度："+address.size());
		for (int i = 0; i < address.size(); i++) {
			if (!matchAddress.contains(address.get(i))) {
				matchAddress.add(address.get(i));
			}
		}
	}
}
