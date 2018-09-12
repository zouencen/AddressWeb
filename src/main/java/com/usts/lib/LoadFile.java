package com.usts.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadFile {
	public static ArrayList<String> loadWord(String wordsPath) {
		ArrayList<String> wordsList = new ArrayList<String>();
		try {
			wordsList = read(wordsPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordsList;
	}

	private static ArrayList<String> read(String filePath) throws IOException {
		ArrayList<String> readList = new ArrayList<String>();
		ReadFile rf = new ReadFile(filePath);
		BufferedReader br = rf.get();
		String strLine = br.readLine();
		// 读取文件一行将其转化成对象
		while (strLine != null) {
			readList.add(strLine);
			strLine = br.readLine();
		}
		rf.close();
		return readList;
	}
}
