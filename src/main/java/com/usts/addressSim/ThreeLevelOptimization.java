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
import com.usts.lib.model.Tuple;

/**三级优化算法
 * @author EnCheng
 *
 */
public class ThreeLevelOptimization {
	
	//dumXXX是归一化因子，用于调整各计算结果参数对总值的影响程度
	static float dumSum = (float) 2000;
	static float dumSim = (float) 40;
	static float dumNum = (float) 3;
	
	/**文本流，对做完一级二级的文本追加三级运算
	 * @param inputPath
	 * @param outPath
	 * @throws IOException
	 */
	private void readResultFile(String inputPath, String outPath)
			throws IOException {
		ReadFile rf = new ReadFile(inputPath);
		BufferedReader br = rf.get();


		String strLine = br.readLine();
		while (strLine != null) {
			if (strLine.trim().length() > 0) {
				try {
					String[] strAddress = strLine.trim().split(":");
//					String strResult = "";
					if (strAddress.length > 1) {// 存在匹配的标准地址
						String nonAddress = strAddress[0];
						String addressList[] = strAddress[1].trim().split("\t");
						ArrayList<Tuple> result = new ArrayList<>();
						for (int i = 0; i < addressList.length; i++) {
							String address[] = addressList[i].trim().split(
									"\\|");
							if (address.length > 1) {
								result.add(new Tuple(address[0], address[1]));
							}
						}
						result = resultCheckBymin(nonAddress, result);
					}
					else
					{
						//WriteAddress.WriteAddress(outPath, strAddress[0] + ":\t");
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Exception:" + strLine);
				}
			}
			strLine = br.readLine();
		}
		rf.close();
	}

	/**三级计算
	 * @param nonAddress 
	 * 			非标地址
	 * @param result
	 * 			匹配的结果集
	 * @return
	 */
	public static ArrayList<Tuple> resultCheckBymin(String nonAddress,
			ArrayList<Tuple> result) {
		if (nonAddress == null || result == null) {
			return null;
		}
		for (int i = 0; i < result.size(); i++) {
			String qdz = result.get(i).getFirst().toString();
			float simnew = effectWordDistance(nonAddress, qdz);
			//simnew = 0;
			float second = (float) result.get(i).getSecond();
			result.get(i).setSecond((Float) (simnew+second));
		}
//		result.sort(new TupleComparator());
		Collections.sort(result, new TupleComparator());
		return result;
	}
	
	/**
	 * 尾部匹配，缩减相似匹配，数字相似匹配
	 * 
	 * @param innormWord
	 * @param normWord
	 * @return
	 */
	private static float effectWordDistance(String innormWord, String normWord) {
		//去除括号
		innormWord = removeBrace(innormWord);
		normWord = removeBrace(normWord);
		int len1 = innormWord.length();
		int len2 = normWord.length();
		
		//缩减标准地址字数，只保留标准地址里在非标地址里出现的字
		String normTmpWord = "";
		for (int i = len2 - 1; i >=0 ; i--)
		{
			String tmp1 = "";
			char tmp = normWord.charAt(i);
			tmp1 = tmp1 + tmp;
			
			if(innormWord.contains(tmp1))
			{
				normTmpWord = tmp1 + normTmpWord;
			}
		}
		
		//尾部匹配
		ArrayList<String> innormList = new ArrayList<String>();
		for (int i = 0; i < innormWord.length(); i++)
		{
			innormList.add(new String("")+innormWord.charAt(i));
		}
		char[] innormArr = innormWord.toCharArray();
		char[] normArr = normTmpWord.toCharArray();
		
		int innormIndex = len1 -1;
		int normIndex = normTmpWord.length() - 1;
		int sum = 0;
		for (int i = normIndex; i >=0; i--)
		{
			for(int j = innormIndex; j>=0; j--)
			{
				if(normArr[i] == innormArr[j])
				{
					innormArr[j] = '\0';
					sum += 20 - i;
					sum += 15 - j;
				}
			}
		}
		
		//缩减后标准地址和非标地址做相似，WinLen=2，Step=1;WinLen=3，Step=1
		Map<String, Integer> normWordMap = new HashMap<String, Integer>();
		{
				int i = 0;
		        for (; i < normTmpWord.length()-1; i++)
		        {
		            addToMap(normWordMap,normTmpWord.substring(i,i + 2));
		        }
		        if (normTmpWord.length() > 0)
		        {
		            addToMap(normWordMap,normTmpWord.substring(i,i + 1));
		        }

		}
		Map<String, Integer> innormWordMap = new HashMap<String, Integer>();
		{
				int i = 0;
		        for (; i < innormWord.length()-1; i++)
		        {
		            addToMap(innormWordMap,innormWord.substring(i,i + 2));
		        }
		        if (innormWord.length() > 0)
		        {
		            addToMap(innormWordMap,innormWord.substring(i,i + 1));
		        }

		}
		float sim2 = computeSim(normWordMap,innormWordMap);
		
		
		//分别提取非标和标准地址所有的数字，然后做相似，WinLen=2，Step=1; WinLen=3，Step=1
		String normNum = getNumbers(normWord);
		String innormNum = getNumbers(innormWord);
				
		
		Map<String, Integer> normNumMap = new HashMap<String, Integer>();
		{
				int i = 0;
		        for (; i < normNum.length()-1; i++)
		        {
		            addToMap(normNumMap,normNum.substring(i,i + 2));
		        }
		        if (normNum.length() > 0)
		        {
		            addToMap(normNumMap,normNum.substring(i,i + 1));
		        }
				i = 0;
		        for (; i < normNum.length()-2; i++)
		        {
		            addToMap(normNumMap,normNum.substring(i,i + 3));
		        }

		}
		Map<String, Integer> innormNumMap = new HashMap<String, Integer>();
		{
				int i = 0;
		        for (; i < innormNum.length()-1; i++)
		        {
		            addToMap(innormNumMap,innormNum.substring(i,i + 2));
		        }
		        if (innormNum.length() > 0)
		        {
		            addToMap(innormNumMap,innormNum.substring(i,i + 1));
		        }
				i = 0;
		        for (; i < innormNum.length()-2; i++)
		        {
		            addToMap(innormNumMap,innormNum.substring(i,i + 3));
		        }

		}
		float simNum = computeSim(normNumMap,innormNumMap);
		
		
		//dumXXX是归一化因子，用于调整各计算结果参数对总值的影响程度
		float res = 0;
	    res = res + sum / dumSum;
		res = res + sim2 / dumSim;
		res = res + simNum / dumNum;
		return res;
	}
	
	static String getNumbers(String str)
	{

		String res = "";
		int numFlag = 0;
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i)>='0' &&str.charAt(i)<='9')
			{
				res +=str.charAt(i);
				numFlag = 1;
			}
			else
			{
				if(numFlag == 1)
				{
					res +="*";
					numFlag =0;
				}
			}
		}
		return res;
	}
	
	static String removeBrace(String str)
	{
		if (!(str.contains("(")||str.contains("（")))
			return str;
		int left = str.indexOf("(");
		int right = str.indexOf(")");
		int leftBig = str.indexOf("（");
	    int rightBig = str.indexOf("）");
	    String resLeft = "";
	    String resRight = "";
	    String resLeftBig = "";
	    String resRightBig = "";
	    if (left >0)
	    resLeft = str.substring(0, left);
	    if (right  >0)
	    resRight = str.substring(right+1,str.length());
	    if (leftBig >0)
	    resLeftBig = str.substring(0, leftBig);
	    if (rightBig >0)
	    resRightBig = str.substring(rightBig+1,str.length());
	    return resLeft + resRight + resLeftBig + resRightBig;
	}
	
	public static void main(String[] args) {
		try {
			new ThreeLevelOptimization().readResultFile(args[0], args[1]);
		} catch (IOException e) {
			System.out
					.println("java -jar AddressResultCheck5.jar args[0] args[1]");
			System.out.println("args[0]:inputFilePath");
			System.out.println("args[1]:outputFilePath");
			e.printStackTrace();
		}
	}
	
	//计算相似度值
		private static float computeSim(Map<String, Integer> map1, Map<String, Integer> map2)
		{
			Map<String, Tuple> vectorMap = new HashMap<String, Tuple>();
			ArrayList<Float> va = new ArrayList<Float>();
			ArrayList<Float> vb = new ArrayList<Float>();
			
			Set<String> set1 = map1.keySet();
			Iterator<String> iterator1 =set1.iterator();
			while (iterator1.hasNext())
			{
				String key1 = iterator1.next();
				if(map2 == null)
				{
					System.out.println("map2 empty!");
					return 0;
				}
				if (map2.containsKey(key1))
				{
					float value1 = map1.get(key1);
					float value2 = map2.get(key1);
					vectorMap.put(key1, new Tuple(value1, value2));
				}
				else
				{
					float value1 = map1.get(key1);
					vectorMap.put(key1, new Tuple(value1, new Float(0)));
				}
			}
			
			Set<String> set2 = map2.keySet();
			Iterator<String> iterator2 =set2.iterator();
			while (iterator2.hasNext())
			{
				String key2 = iterator2.next();
				if (!map1.containsKey(key2))
				{
					float value2 = map2.get(key2);
					vectorMap.put(key2, new Tuple(new Float(0), value2));
				}
			}
			
			Set<String> set = vectorMap.keySet();
			Iterator<String> iteratorVector =set.iterator();
			while (iteratorVector.hasNext())
			{
				String key = iteratorVector.next();
				Tuple tuple = vectorMap.get(key);
				va.add((Float) tuple.getFirst());
				vb.add((Float) tuple.getSecond());
			}
			
			float res = sim(va,vb);
			return res;
		}

		private static float sim(ArrayList<Float> va, ArrayList<Float> vb)
		{
			// �������ά�Ȳ���ȣ����ܼ��㣬�����˳�
			if (va.size() != vb.size())
			{
				return 0;
			}
			
			int size = va.size();
			float simVal = 0;
			
			float num = 0;// numerator����
			float den = 1;// denominator��ĸ
			
			double a=0,b=0;
			for (int i = 0 ; i <size ; i ++)
			{
				num  += Float.parseFloat(va.get(i).toString()) *  Float.parseFloat(vb.get(i).toString());
			}
			for (int j = 0 ; j <size ; j ++)
			{
				a += Math.pow(Double.parseDouble(va.get(j).toString()), 2);
				b += Math.pow(Double.parseDouble(vb.get(j).toString()), 2);
			}
			double s = Math.sqrt(a) * Math.sqrt(b);
			den = (float)s;
			
			if (den == 0)
			{
				den = 1;
			}
			simVal = num / den;
			return simVal;
		}
		
		private static void addToMap(Map<String,Integer> map, String str)
		{
			if (map.containsKey(str))
			{
				Integer value = map.get(str);
				value++;
				map.put(str, value);
			}
			else
			{
				map.put(str, 1);
			}
		}	
	}