package com.usts.lib;

import java.io.IOException;
import java.util.Properties;

/**加载配置文件
 * @author 	PiZhou
 *
 */
public class LoadProperties {
	
	/**获取值
	 * @param key
	 * 		 	参数
	 * @return String
	 * 			值
	 */
	public static String getValues(String key){
		Properties prop = new Properties();
		try {
			prop.load(LoadProperties.class
					.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String values = prop.getProperty(key);
		return values;
	}
//	 public static void main(String[] args) {
//	 System.out.println(getValues("sim"));;
//	 }
}
