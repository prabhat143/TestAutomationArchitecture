package com.companyName.drivers;

import com.companyName.localsetup.LocalServer;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.GetFrameworkKeys;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collections;

public class MobileDriverClass {

	ThreadLocal<RemoteWebDriver> appiumDriver;

	/**
	 *Set DesiredCapabilities
	 * @param platform
	 * @param platformVersion
	 * @param deviceName
	 * @param deviceId
	 * @param automationName
	 * @return DesiredCapabilities
	 */
	private synchronized DesiredCapabilities setAndoidLocalOptions(String platform, String platformVersion, String deviceName, String deviceId, String automationName) {
		DesiredCapabilities options = new DesiredCapabilities();
		options.setCapability(MobileCapabilityType.PLATFORM_NAME, platform);
		options.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		options.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		options.setCapability(MobileCapabilityType.UDID,deviceId);
		options.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

		if(GetFrameworkKeys.getPropValue("browser").equalsIgnoreCase("chrome")) {
			options.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
			ChromeOptions cop = new ChromeOptions();
			cop.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setCapability(ChromeOptions.CAPABILITY, cop);
		}else {
			options.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT , 600);
			options.setCapability(MobileCapabilityType.APP , FrameworkVariables.APP);
			options.setCapability("appPackage", FrameworkVariables.AppPackage);
			options.setCapability("appActivity", FrameworkVariables.AppActivity);
		}

		return options;
	}

	/**
	 *Set AndriodDriver in thread
	 * @param platform
	 * @param platformVersion
	 * @param deviceName
	 * @param deviceId
	 * @param automationName
	 * @param server
	 * @return ThreadLocal<RemoteWebDriver>
	 */
	@SuppressWarnings("unchecked")
	public synchronized ThreadLocal<RemoteWebDriver> getAndriodLocalDriver(DriverTypes platform, String platformVersion, String deviceName,
																		   String deviceId, String automationName, LocalServer server) {
			appiumDriver = new ThreadLocal<>();
			appiumDriver.set(new AndroidDriver<MobileElement>(server.getUrl(), setAndoidLocalOptions(String.valueOf(platform), platformVersion,
					deviceName, deviceId, automationName)));
			return appiumDriver;
	}

	private synchronized DesiredCapabilities setIOSLocalOptions(String platform, String platformVersion, String deviceName, String deviceId, String automationName) {
		DesiredCapabilities options = new DesiredCapabilities();
		options.setCapability(MobileCapabilityType.PLATFORM_NAME, platform);
		options.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		options.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		options.setCapability(MobileCapabilityType.UDID,deviceId);
		options.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

		if(GetFrameworkKeys.getPropValue("browser").equalsIgnoreCase("chrome")) {
			options.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
			ChromeOptions cop = new ChromeOptions();
			cop.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setCapability(ChromeOptions.CAPABILITY, cop);
		}else {
			options.setCapability("app" , FrameworkVariables.APP);
		}
		return options;
	}

	/**
	 *Set AndriodDriver in thread
	 * @param platform
	 * @param platformVersion
	 * @param deviceName
	 * @param deviceId
	 * @param automationName
	 * @param server
	 * @return ThreadLocal<RemoteWebDriver>
	 */
	@SuppressWarnings("unchecked")
	public synchronized ThreadLocal<RemoteWebDriver> getIOSLocalDriver(DriverTypes platform, String platformVersion, String deviceName,
																	   String deviceId, String automationName, LocalServer server) {
		appiumDriver = new ThreadLocal<>();
		appiumDriver.set(new IOSDriver<MobileElement>(server.getUrl(), setIOSLocalOptions(String.valueOf(platform), platformVersion,
				deviceName, deviceId, automationName)));
		return appiumDriver;
	}
}
