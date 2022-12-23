package com.companyName.drivers;

import com.companyName.localsetup.LocalServer;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.GetFrameworkKeys;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager{

	private String DeviceId;
	private String DevcieName;
	private String DeviceVersion;
	private String automationName;
	private LocalServer server;

	private static ThreadLocal<RemoteWebDriver> driver;

	public DriverManager(){ }

	/**
	 * Configure server and device details
	 * @param server
	 * @param device
	 */
	public DriverManager(LocalServer server, String device) {
		if(server!=null) {
			this.server = server;
			String[] deviceDetails = null;
			if (device == null)
				deviceDetails = LocalServer.getDevice().split(":");
			else
				deviceDetails = device.split(":");
			this.DeviceId = deviceDetails[0];
			this.DevcieName = deviceDetails[1];
			this.DeviceVersion = deviceDetails[2];
		}
	}

	/**
	 * initiate drive as given in frameworkvariables.properties file
	 */
	public void initializeDriver() {

		DriverTypes driverType = DriverTypes.valueOf(FrameworkVariables.PLATFORM.toUpperCase());
		MobileDriverClass mobileDriverClass = new MobileDriverClass();
		switch (driverType) {

			case ANDROID:
				driver = mobileDriverClass.getAndriodLocalDriver(driverType, DeviceVersion, DevcieName, DeviceId, automationName, server);
				break;

			case IOS:
				driver = mobileDriverClass.getIOSLocalDriver(driverType, DeviceVersion, DevcieName, DeviceId, automationName, server);
				break;

			case DESKTOP:
				WebDriverClass webDriverClass = new WebDriverClass();
				driver = webDriverClass.getDriver();
				break;
		}
	}

	public void initBrowserStackDriver(String methodName){
		if(FrameworkVariables.PLATFORM.equalsIgnoreCase("BrowserStack")) {
			if (FrameworkVariables.appUrl.contains("bs://XXXX")) {
				CommonUtils.logInfo("Getting device OS: "+GetFrameworkKeys.initBrowserVar(System.getenv("AvailableDevices")).get("OS").toString());
				if (GetFrameworkKeys.isAndroid(GetFrameworkKeys.initBrowserVar(System.getenv("AvailableDevices")).get("OS").toString())) {
					FrameworkVariables.APP = System.getProperty("user.dir") + GetFrameworkKeys.getPropValue("appAndroid");
				} else if (GetFrameworkKeys.isIOS(GetFrameworkKeys.initBrowserVar(System.getenv("AvailableDevices")).get("OS").toString())) {
					FrameworkVariables.APP = System.getProperty("user.dir") + GetFrameworkKeys.getPropValue("appIOS");
				}
				FrameworkVariables.appUrl = CommonUtils.uploadAppUniRest(GetFrameworkKeys.getSecretsValue("browserStackUserName"), GetFrameworkKeys.getSecretsValue("browserStackAccessKey"), FrameworkVariables.APP);
			}
			CommonUtils.logInfo("App URL:" + FrameworkVariables.appUrl);
			BrowserStackDriverClass browserStackDriverClass = new BrowserStackDriverClass(System.getenv("AvailableDevices"));
			driver = browserStackDriverClass.getBSDriver(methodName);
		}
	}

	/**
	 * To get driver anywhere in the framework
	 * ex:DriverManager.getDriver().get();
	 * @return ThreadLocal<RemoteWebDriver>
	 */
	@SuppressWarnings("rawtypes")
	public static RemoteWebDriver getDriver() {
		return driver.get();
	}

	/**
	 * To close and quit the driver
	 */
	public void closeDriver() {
			if (driver != null) {
				driver.get().quit();
			}
	}

}
