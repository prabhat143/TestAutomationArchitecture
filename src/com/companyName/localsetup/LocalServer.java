package com.companyName.localsetup;

import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.BufferedReader;
import java.net.URL;

import static com.companyName.utils.CommonUtils.fetchCMD;

public class LocalServer {

	private AppiumServiceBuilder builder;
	private AppiumDriverLocalService service;

	/**
	 * Constructor to set appiumdriver service
	 */
	public LocalServer() {
		CommonUtils.logInfo("INSIDE APPIUM SERVER");
		builder = new AppiumServiceBuilder();
		builder.withIPAddress(FrameworkVariables.APPIUMSERVER);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
		builder.withArgument(GeneralServerFlag.RELAXED_SECURITY);
		builder.usingAnyFreePort();

		service = AppiumDriverLocalService.buildService(builder);
		CommonUtils.logInfo(service.getUrl().toString());
	}

	/**
	 *Start the appium server
	 */
	public void appiumStart() {
		service.start();
	}

	/**
	 *Stop the appium server
	 */
	public void appiumStop() {
		service.stop();
	}

	/**
	 *To get appium url
	 * @return URL
	 */
	public URL getUrl() {
		return service.getUrl();
	}

	/**
	 *To kill appium server
	 */
	public void killAllAppiumServers() {
		String s = "";
		String str="";
		try {
			int count = 0;
			BufferedReader reader = fetchCMD("taskkill /F /IM node.exe");
			s= reader.readLine();
			while ((s = reader.readLine()) != null) {
				if (s.equalsIgnoreCase("ERROR: The process \"node.exe\" not found.") | s.equals("") | s.isEmpty())
					System.out.println("No Appium server is/are Running");
				str=str+s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(str==null|str.equals(""))
			CommonUtils.logInfo("No Appium server is/are Running");
		else
			CommonUtils.logInfo(str);
	}


	/**
	 * Get the connected device details in map
	 * example: device - deviceName:ProductName:Version
	 * @return string
	 */
	public static String getDevice() {
		String device = "";
		try {
			String s = null;

			if(FrameworkVariables.PLATFORM.equalsIgnoreCase("Android")) {
				BufferedReader reader = fetchCMD("adb devices");
				while ((s = reader.readLine()) != null) {
					if (s.contains("devices") | s.equals("") | s.isEmpty())
						continue;
					String[] str = s.split("\t");
					device = str[0] + ":" + fetchCMD("adb -s " + str[0].trim() + " shell getprop ro.product.model").readLine()
							+ ":"
							+ fetchCMD("adb -s " + str[0].trim() + " shell getprop ro.build.version.release").readLine();
				}
			}else if(FrameworkVariables.PLATFORM.equalsIgnoreCase("IOS")) {
				BufferedReader conectedDevices = fetchCMD("ios-deploy -c | grep 'USB'");
				String line = conectedDevices.readLine();
				if(line != null){
					CommonUtils.logInfo(line);
					String [] dd = line.split(" ");
					device = dd[2]+":"+line.substring(line.indexOf("(")+1,line.lastIndexOf(")")).split(",")[1]+":"+line.substring(line.indexOf("(")+1,line.lastIndexOf(")")).split(",")[4];
				}else {
					BufferedReader getAllDevices = fetchCMD("xcrun simctl list | grep 'Booted'| grep 'Phone:'");
					line = getAllDevices.readLine();
					if(line != null){
						CommonUtils.logInfo(line);
						String udid = line.substring(line.indexOf("(")).split(" ")[0].replaceAll("[()]","");
						BufferedReader filterSimulator = fetchCMD("xcrun xctrace list devices | grep '"+udid+"'");
						line = filterSimulator.readLine();
						String[] dd = line.replace("Simulator",":").split(":");
						device = udid+":"+dd[0].trim()+":"+dd[1].replace(udid,"").replaceAll("[()]","").trim();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return device;
	}
}


