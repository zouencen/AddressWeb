package com.usts.lib;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class WriteFile {
	File file;
	FileWriter fw;
	PrintWriter pw;
	public WriteFile(String file_path) throws IOException{
		file = new File(file_path);
		if(!file.exists()){
			file.createNewFile();
		}
		fw = new FileWriter(file);
		pw = new PrintWriter(fw);
	}
	public PrintWriter get(){
		return pw;
	}
	public void close(){
		pw.close();
	}
}
