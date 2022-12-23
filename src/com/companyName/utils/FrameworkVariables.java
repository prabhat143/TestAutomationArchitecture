package com.companyName.utils;

import org.testng.Assert;

import java.io.File;

public class FrameworkVariables {

	public final static String APPIUMSERVER = "127.0.0.1";

	public static String jenkinsCheck;
	public static String APP;
	public static String AppPackage;
	public static String AppActivity;
	public static String PLATFORM;
	public static String appUrl;
	public static String AUTOMATIONNAME;
	public static String browser;

	/**
	 * Initialisation of constant variable given in frameworkvariables.properties file
	 */
	public static void initFrameVariables() {
		AUTOMATIONNAME = GetFrameworkKeys.getPropValue("automationName");
		PLATFORM = System.getenv("platform");
		jenkinsCheck = System.getProperty("jenkinsCheck");
		appUrl = System.getenv("appUrl");

		browser = GetFrameworkKeys.getPropValue("browser");

		if(appUrl == null){
			appUrl = "bs://XXXXXXXX";
		}

		AppPackage = GetFrameworkKeys.getPropValue("appPackage");
		AppActivity =  GetFrameworkKeys.getPropValue("appActivity");

		if(PLATFORM==null){
			PLATFORM = GetFrameworkKeys.getPropValue("platform");
		}
		if (PLATFORM.equalsIgnoreCase("Android")) {
			APP = System.getProperty("user.dir") + GetFrameworkKeys.getPropValue("appAndroid");
		} else if (PLATFORM.equalsIgnoreCase("IOS")) {
			APP = System.getProperty("user.dir") + GetFrameworkKeys.getPropValue("appIOS");
		}

	}

	public static void createSampleConfig(){
		String path = System.getProperty("user.dir")+"/config";
		File config = new File(path);
		if(!config.exists())
			config.mkdir();
		path = path+"/frameworkvariables.properties";
		if(!new File(path).exists()) {
			ExtractFiles.ExportResource("/config/frameworkvariables.properties");
			CommonUtils.logInfo("Configuration file is been created in below location: \n"+path);
			Assert.fail("Please updated the configuration file as per the project requirement and rerun the framework");
		}

		path = System.getProperty("user.dir")+"/config/browserstack.json";
		if(!new File(path).exists()) {
			ExtractFiles.ExportResource("/config/browserstack.json");
			CommonUtils.logInfo("browserstack file is been created in below location: \n"+path);
			Assert.fail("Please updated the browserstack file as per the project requirement and rerun the framework");
		}

		path = System.getProperty("user.dir")+"/config/secrets.properties";
		if(!new File(path).exists()) {
			ExtractFiles.ExportResource("/config/secrets.properties");
			CommonUtils.logInfo("Secret key file is been created in below location: \n"+path);
			Assert.fail("Please encrypt the file before pushing the codes to repo");
		}
	}

	public static void createReadME(){
		String path = System.getProperty("user.dir");
		File config = new File(path);
		if(!config.exists())
			config.mkdir();
		path = path+"/readMe.md";
		if(!new File(path).exists()) {
			ExtractFiles.ExportResource("/readMe.md");
			CommonUtils.logInfo("readMe file is been created in below location: \n"+path);
			System.exit(0);
		}
	}

}
