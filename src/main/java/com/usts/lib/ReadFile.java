package com.usts.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//ReadFile 2017-2-5 GBK UTF-8 load -1.1
public class ReadFile {
	File file;
	FileReader fr;
	BufferedReader br;
	public ReadFile(String file_path) throws IOException{
		file = new File(file_path);
		if(!file.exists()){
			System.out.println("\""+file_path+"\" does not exsit!");
			return;
		}
		
		InputStream in= new java.io.FileInputStream(file);  
		byte[] b = new byte[3];  
		in.read(b);  
		in.close();  
		InputStreamReader isr;
		if (b[0] == -17 && b[1] == -69 && b[2] == -65)
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
		else if (b[0] == -26 && b[1] == -104 && b[2] == -122)  
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
		else  
			isr = new InputStreamReader(new FileInputStream(file),"GBK");
		br = new BufferedReader(isr);
	}
	public BufferedReader get(){
		return br;
	}
	public void close() throws IOException{
		br.close();
	}
	
}
