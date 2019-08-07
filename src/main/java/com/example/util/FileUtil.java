package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FileUtil {

	/**
	 * 读取txt文件里的内容
	 * @param fileUrl	文件路径
	 * @return
	 */
	public static List<String> readTxtFile(String fileUrl) throws Exception{
		List<String> list = new ArrayList<String>();
		File file = new File(fileUrl);
		FileInputStream in = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String lineTxt = null;
		//读取文件中的记录并添加到list里
		while ((lineTxt = br.readLine()) != null) {
			list.add(lineTxt);
		}
		return list;
	}
	
	//往txt里写内容
	public static void writeTxtFile(List<String> connents,String filePath){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
			for(String connent : connents){
				//fos.write((connent + "\r\n").getBytes());
				osw.append(connent + "\r\n");
			}
			if(fos != null){
				osw.flush();
				osw.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//往txt里写内容
	public static void writeTxtFile(Set<String> connents,String filePath){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
			for(String connent : connents){
				fos.write((connent + "\r\n").getBytes());
			}
			if(fos != null){
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
