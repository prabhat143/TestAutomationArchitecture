package com.companyName.utils;

import com.companyName.drivers.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;

public class ImagePushDevice {

    static String androidPath = "/storage/emulated/0/Android/data/%s/files/images/image.jpeg";
    static String iOSPath = "@%s/Documents/image.jpeg";
    static String imageFilePath = "src/test/resources/images/%s/%s.jpeg";

    public static void pushImageToDevice(String imageName){
        try {
            if(!CommonUtils.isIOSPlatform()) {
                ((AndroidDriver<?>) DriverManager.getDriver()).pushFile(String.format(androidPath, MobileConfiguration.androidAppPackage), new File(String.format(imageFilePath, "android",imageName)));
            } else {
                ((IOSDriver<?>) DriverManager.getDriver()).pushFile(String.format(iOSPath, MobileConfiguration.iosAppBundleId), new File(String.format(imageFilePath, "ios",imageName)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
