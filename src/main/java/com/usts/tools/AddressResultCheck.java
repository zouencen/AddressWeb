package com.usts.tools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import com.usts.lib.ReadFile;
import com.usts.lib.WriteFile;

public class AddressResultCheck {

	static AddressResultCheck addressResultCheck = new AddressResultCheck();
	String path = "C://Users//zouencen//Documents//日常处理文档//职称论文//addressRun//";
	String expResultFile = "data_1_20000_w2_s1//output1_20000.txt";
	String humanCheckFile = "人工标注地址匹配.txt";

	int selectRange = 1;
	int weblab1 = 1;
	double accuracy;
	double precision;
	double recall;
	double f1;

	Map<String, ArrayList<String> > expResultMap = new HashMap<String, ArrayList<String> >();
	Map<String, String> humanCheckMap = new HashMap<String, String>();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		addressResultCheck.loadingFiles();
		addressResultCheck.checking();
		addressResultCheck.output();
	}

	private void checking() {
		checkFind(expResultMap, humanCheckMap);
	}

	private void output() throws IOException {
		System.out.println("accuracy: "+accuracy);
		System.out.println("precision: "+precision);
		System.out.println("recall: "+recall);
		System.out.println("f1: "+f1);
	}



	private void checkFind(Map<String, ArrayList<String> > expResultMap, Map<String, String> humanCheckMap) {
		int total = 0;
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		System.out.println(expResultMap.size());
		System.out.println(humanCheckMap.size());
		
		for (Iterator<String> it = humanCheckMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			String humanValue = humanCheckMap.get(key);
			ArrayList<String> expValue = expResultMap.get(key);
			total++;
			if (expValue != null)
			{
				if (humanValue.equals(new String("其他")))
				{
					tn++;
					continue;
				}
				int tp_true = 0;
				for (int i = 0; i < selectRange && i < expValue.size(); i++)
				{
					//System.out.println(expValue.size());
					if (humanValue.equals(expValue.get(i)))
					{
						tp_true = 1;
					}

				}
				if (tp_true == 1)
					{
						tp++;
					}
					else
					{
						fp++;
					}
			}
			else if (humanValue.equals(new String("其他")))
			{
				tn++;
				System.out.println(key);
			}
		}
		System.out.println("tp:"+tp);
		System.out.println("tn:"+tn);
		System.out.println("fp:"+fp);
		System.out.println("fn:"+fn);
		System.out.println("total:"+total);
		accuracy = (double)(tp+tn)/(double)(tp+tn+fp+fn);
		precision = (double)tp/(double)(tp+fp);
		recall = (double)total/(double)(16682);
		f1 = 2 * precision * recall / (precision + recall);
	}

	// 构建每个地址的向量
	private void loadingFiles() throws IOException {
		System.out.println("Loading files...");
		read(path + expResultFile, expResultMap);
		readHumanCheck(path + humanCheckFile, humanCheckMap);
		System.out.println("Loading finished.");
		System.out.println("Computing...");
	}
	
	


	private void read(String filePath,
			Map<String, ArrayList<String> > inputMap) throws IOException {
		System.out.println("read...");
		ReadFile rf = new ReadFile(filePath);
		BufferedReader br = rf.get();
		String strLine = br.readLine();
		int count = 0;
		while (strLine != null) {
			count++;
			System.out.println(count);
			String[] strArr = strLine.split("\t");
			//凯翔花园4#401:	玉山镇凯翔花园1幢404室|0.77849895	玉山镇凯翔花园4幢401室|0.77849895	玉山镇凯翔花园4幢404室|0.76088595	玉山镇凯翔花园3幢404室|0.700649	玉山镇凯翔花园1幢401室|0.700649	玉山镇凯翔花园1幢410室|0.700649	玉山镇凯翔花园2幢404室|0.700649	玉山镇凯翔花园4幢402室|0.700649	玉山镇凯翔花园4幢403室|0.700649	玉山镇凯翔花园4幢405室|0.700649	
			int strLen = strArr[0].length();
			String key = strArr[0].substring(0, strLen - 1);
//			String value = "";
			ArrayList<String> valueList = new ArrayList<String>();
			if(strArr.length > 1)
			{
				for(int i=1; i<strArr.length; i++)
				{
					String[] valueArr = strArr[i].split("\\|");
					valueList.add(valueArr[0]);
				}
			}
			inputMap.put(key, valueList);
			//System.out.println(key);
			strLine = br.readLine();
		}
		rf.close();
		System.out.println("read finish...");
	}
	
	private void readHumanCheck(String filePath,
			Map<String, String> inputMap) throws IOException {
		
		String file = "duplicate.txt";
		WriteFile wf= new WriteFile(path + file);
		PrintWriter pw = wf.get();
		String file1 = "conflict.txt";
		WriteFile wf1= new WriteFile(path + file1);
		PrintWriter pw1 = wf1.get();
		
		System.out.println("readHumanCheck...");
		ReadFile rf = new ReadFile(filePath);
		BufferedReader br = rf.get();
		String strLine = br.readLine();
		while (strLine != null) {
			String[] strArr = strLine.split("\t");
			//中华园15#405	玉山镇中华园15幢405室
			String key = strArr[0];
			String value = strArr[1];
			if (inputMap.get(key)==null)
			{
				inputMap.put(key, value);
			}
			else
			{
				if(value.equals(inputMap.get(key)))
				{
					pw.println("key:"+key+","+"value:"+inputMap.get(key));
				}
				else
				{
					pw1.println("ori-key:"+key+","+"value:"+inputMap.get(key));
					pw1.println("now-key:"+key+","+"value:"+value);
					inputMap.put(key, new String("其他"));
				}
			}
			//System.out.println(key);
			strLine = br.readLine();
		}
		rf.close();
		wf.close();
		wf1.close();
		System.out.println("readHumanCheck finish...");
	}
}
