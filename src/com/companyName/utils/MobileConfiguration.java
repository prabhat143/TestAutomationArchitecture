package com.companyName.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MobileConfiguration {

    public static String appiumServerHost;
    public static String appiumServerPort;

    public static String deviceId;
    public static String deviceName;
    public static String devicePlatformVersion;
    public static String androidApp;
    public static String androidAppPackage;
    public static String androidAppActivity;
    public static String androidAutomationName;
    public static String iosApp;
    public static String iosAutomationName;
    public static String iosAppBundleId;
    public static String iosDeviceName;
    public static String iosPlatformVersion;
    public static String iosUDID;

    public static void initMobileVariables(){
        appiumServerHost = getPropValue("appiumServerHost");
        appiumServerPort = getPropValue("appiumServerPort");

        deviceId = getPropValue("deviceId");
        deviceName = getPropValue("deviceName");
        devicePlatformVersion = getPropValue("devicePlatformVersion");
        androidApp = System.getProperty("user.dir")+ File.separator+getPropValue("app");
        androidAppPackage = getPropValue("appPackage");
        androidAppActivity = getPropValue("appActivity");

        iosApp = System.getProperty("user.dir")+ File.separator+getPropValue("iosApp");
        iosAppBundleId = getPropValue("iosAppBundleId");
        iosDeviceName = getPropValue("iosDeviceName");
        iosPlatformVersion = getPropValue("iosPlatformVersion");
        iosUDID = getPropValue("iosUDID");
    }

    private static String getPropValue(String key){
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(System.getProperty("user.dir")+"/config/mobile_config.properties");
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prop.get(key).toString();
    }
}
