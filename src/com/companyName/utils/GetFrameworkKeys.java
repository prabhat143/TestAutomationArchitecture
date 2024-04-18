package com.companyName.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is to get values form configyration.properties
 */
public class GetFrameworkKeys {
	
	public static Properties prop=new Properties();
	public static FileInputStream fis;
	
	private static Properties initFV() {
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"/config/frameworkvariables.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private static Properties initSecretsVar() {
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"/config/secrets.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static String getPropValue(String key) {
		return initFV().getProperty(key);
	}

	public static String getSecretsValue(String key) {
		return initSecretsVar().getProperty(key);
	}

	public static boolean isAndroid(String osName){
		return osName.equalsIgnoreCase("Android");
	}

	public static boolean isIOS(String osName){
		return osName.equalsIgnoreCase("ios");
	}

}
