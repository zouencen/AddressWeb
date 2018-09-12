package com.usts.addressSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.usts.lib.model.NodeInformation;
import com.usts.lib.model.Tuple;
import com.usts.tools.SingleAddressDispose;

public class AddressAllInfoComputer {

	public static ArrayList<Tuple> computeAddress(String nonAddress,
			HashMap<String, ArrayList<NodeInformation>> searchMap) {
		ArrayList<NodeInformation> matchAddress = new ArrayList<NodeInformation>();
		//匹配结果合并
		unionConllection(nonAddress, searchMap, matchAddress);
		ArrayList<Tuple> simMatchAddress = new ArrayList<Tuple>();
		//余弦相似度计算
		simMatchAddress = CosDistanceAllInfo.computerAddressList(nonAddress,
				matchAddress);
		// 限制集合长度在10以内
		limitCollectionLength(simMatchAddress);
		// 三级计算
		simMatchAddress = ThreeLevelOptimization.resultCheckBymin(nonAddress,
				simMatchAddress);
		return simMatchAddress;
	}

	/**取前10个进入第三级别优化
	 *
	 * @param simMatchAddress 余弦相似计算结果集
	 */
	private static void limitCollectionLength(ArrayList<Tuple> simMatchAddress) {
		if (simMatchAddress.size() > 9) {
			simMatchAddress.subList(9, simMatchAddress.size()).clear();
		}
	}

	/**合并地址匹配的所有集
	 * @param nonAddress
	 * @param searchMap
	 * @param matchAddress
	 */
	private static void unionConllection(String nonAddress,
			HashMap<String, ArrayList<NodeInformation>> searchMap,
			ArrayList<NodeInformation> matchAddress) {
		ArrayList<String> cutResult = SingleAddressDispose
				.addressCutByTwo(nonAddress);
		for (int i = 0; i < cutResult.size(); i++) {
			try {
				ArrayList<NodeInformation> address = searchMap.get(cutResult
						.get(i));
				unionAddress(matchAddress, address);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**合并两个集，去掉重复值
	 * @param matchAddress
	 * @param address
	 */
	private static void unionAddress(ArrayList<NodeInformation> matchAddress,
			ArrayList<NodeInformation> address) {
		if(address!=null){
			for (int i = 0; i < address.size(); i++) {
				if (!matchAddress.contains(address.get(i))) {
					matchAddress.add(address.get(i));
				}
			}
		}
	}
}
