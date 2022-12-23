package com.companyName.drivers;

import com.companyName.utils.GetFrameworkKeys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SeleniodDriverClass {
    ThreadLocal<RemoteWebDriver> snDriver;

    public DesiredCapabilities setDesktopOptions(String device,String methodName){
        Map<String,Object> map = new HashMap<>();
        map.put("enableVNC", true);
        map.put("enableVideo", false);
        map.put("name",methodName);
        map.put("sessionTimeout",GetFrameworkKeys.initBrowserVar(device).get("sessionTimeout"));
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", GetFrameworkKeys.initBrowserVar(device).get("browser").toString().toLowerCase());
        capabilities.setCapability("browserVersion", GetFrameworkKeys.initBrowserVar(device).get("browser_version").toString());
        capabilities.setCapability("selenoid:options", map);

        return capabilities;
    }

    @SuppressWarnings("unchecked")
    public synchronized ThreadLocal<RemoteWebDriver> getSNDriver(String device,String methodName) {
        snDriver = new ThreadLocal<>();
        URL url = null;
        try {
            url = new URL(GetFrameworkKeys.getSecretsValue("selenoidName")+"/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        snDriver.set(new RemoteWebDriver(url,setDesktopOptions(device,methodName)));
        return snDriver;
    }
}
