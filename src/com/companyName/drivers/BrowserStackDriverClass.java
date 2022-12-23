package com.companyName.drivers;

import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.GetFrameworkKeys;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStackDriverClass {

    ThreadLocal<RemoteWebDriver> bsDriver;
    String userName;
    String accessKey;
    String device;
    String os_version;
    String os;
    String idleTimeOut;
    String browser,browserVersion,resolution;
    static final String projectName = "CompanyName_E2E_" + System.currentTimeMillis();

    public BrowserStackDriverClass(String deviceName) {
        userName = GetFrameworkKeys.getSecretsValue("browserStackUserName");
        accessKey = GetFrameworkKeys.getSecretsValue("browserStackAccessKey");
        idleTimeOut = GetFrameworkKeys.initBrowserVar("browserStackProperties").get("buildTimeOut").toString();
        device = deviceName;
        os_version = GetFrameworkKeys.initBrowserVar(deviceName).get("Version").toString();
        os = GetFrameworkKeys.initBrowserVar(deviceName).get("OS").toString();
    }

    private synchronized DesiredCapabilities setMobileOptions(String methodName) {
        DesiredCapabilities options = new DesiredCapabilities();
        options.setCapability("device", device);
        options.setCapability("os_version", os_version);
        options.setCapability("project", projectName);
        options.setCapability("build", "Jenkins_BuildNumber_" + System.getenv("BUILD_NUMBER"));
        options.setCapability("browserstack.idleTimeout", idleTimeOut);
        options.setCapability("name", methodName);
        options.setCapability("deviceOrientation", "portrait");
        if (!FrameworkVariables.browser.equalsIgnoreCase("chrome")) {
            options.setCapability("app", FrameworkVariables.appUrl);
        }

        return options;
    }

    private synchronized DesiredCapabilities setDesktopOptions(String methodName) {
        DesiredCapabilities options = new DesiredCapabilities();
        options.setCapability("os", os);
        options.setCapability("os_version", os_version);
        options.setCapability("browser", GetFrameworkKeys.initBrowserVar(device).get("browser").toString());
        options.setCapability("browser_version", GetFrameworkKeys.initBrowserVar(device).get("browser_version").toString());
        options.setCapability("resolution", GetFrameworkKeys.initBrowserVar(device).get("resolution").toString());
        options.setCapability("project", projectName);
        options.setCapability("build", "Jenkins_BuildNumber_" + System.getenv("BUILD_NUMBER"));
        options.setCapability("browserstack.idleTimeout", idleTimeOut);
        options.setCapability("name", methodName);
        options.setCapability("browserstack.selenium_version", "3.141.59");

        return options;
    }


    @SuppressWarnings("unchecked")
    public synchronized ThreadLocal<RemoteWebDriver> getBSDriver(String methodName) {
        bsDriver = new ThreadLocal<>();
        URL url = null;
        try {
            url = new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (os.equalsIgnoreCase("Android")) {
            bsDriver.set(new AndroidDriver<MobileElement>(url, setMobileOptions(methodName)));
        } else if (os.equalsIgnoreCase("IOS")) {
            bsDriver.set(new IOSDriver<MobileElement>(url, setMobileOptions(methodName)));
        } else {
            bsDriver = new SeleniodDriverClass().getSNDriver(device,methodName);
            bsDriver.get().manage().window().maximize();
        }

        return bsDriver;
    }
}
