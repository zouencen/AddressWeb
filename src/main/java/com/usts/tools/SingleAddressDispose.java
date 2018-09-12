package com.usts.tools;

import java.util.ArrayList;

/**地址信息处理
 * @author PiZhou
 *
 */
public class SingleAddressDispose {
	/**将地址按步长为1窗长为2做切分
	 * @param address
	 * 			地址字符串
	 * @return ArrayList<String>
	 * 			长度为2的词组成的ArrayList
	 */
	public static ArrayList<String> addressCutByTwo(String address){
		ArrayList<String> cutResult = new ArrayList<String>();
		for (int i = 0; i < address.length()-1; i++) {
			cutResult.add(address.substring(i,i+2));
		}
		return cutResult;
	}

}
