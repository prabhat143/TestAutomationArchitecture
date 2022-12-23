package com.companyName.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScrollUtils {

    /**
     * To scroll top of the page
     */
    public static void scrollByPageTop(RemoteWebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    /**
     * To scroll on to the element
     * @param element
     */
    public static void scrollByVisibleElement(RemoteWebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public static void scrollUpFromBottomEleToTopElement(RemoteWebDriver driver,MobileElement topElement, MobileElement bottomElement,int timeout)  {

        PointOption bottom = new PointOption();
        bottom.withCoordinates(bottomElement.getLocation().getX(), bottomElement.getLocation().getY());

        PointOption top = new PointOption();
        top.withCoordinates(topElement.getLocation().getX(), topElement.getLocation().getY());

        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(bottom).moveTo(top).release().perform();
        CommonUtils.threadWait(timeout);

    }

    public static void scrollUpFromTopEleToBottomElement(RemoteWebDriver driver,MobileElement topElement, MobileElement bottomElement,int timeout)  {

        PointOption bottom = new PointOption();
        bottom.withCoordinates(bottomElement.getLocation().getX(), bottomElement.getLocation().getY());

        PointOption top = new PointOption();
        top.withCoordinates(topElement.getLocation().getX(), topElement.getLocation().getY());

        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(top).moveTo(bottom).release().perform();
        CommonUtils.threadWait(timeout);
    }

    public static void scrollUpFromTopEleToBottomElement(RemoteWebDriver driver,PointOption bottom, PointOption top,int timeout)  {
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(top).moveTo(bottom).release().perform();
        CommonUtils.threadWait(timeout);
    }

    public static void scrollUpFromBottomEleToTopElement(RemoteWebDriver driver,PointOption bottom, PointOption top,int timeout)  {
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(bottom).moveTo(top).release().perform();
        CommonUtils.threadWait(timeout);

    }

    public static void swipeHalfScreen(RemoteWebDriver driver){
        Dimension size = driver.manage().window().getSize();
        int startY = (int)(size.height*0.98);//0.967490
        int endY = (int)(size.height*0.5);//0.92490
        int x = (int)(size.width*0.02);//0.327777
        PointOption bottom = new PointOption();
        bottom.withCoordinates(x, startY);//2113

        PointOption top = new PointOption();
        top.withCoordinates(x,endY);//354 2020
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(top).moveTo(bottom).release().perform();
    }

    public static void swipeUp(RemoteWebDriver driver,MobileElement ele){
        Dimension size = ele.getSize();
        int startY = (int)(size.height*0.85);//0.967490
        int endY = (int)(size.height*0.70);//0.92490
        int x = (int)(size.height*0.33);//0.327777
        PointOption bottom = new PointOption();
        bottom.withCoordinates(x, startY);//2113

        PointOption top = new PointOption();
        top.withCoordinates(x,endY);//354 2020
        TouchAction action = new TouchAction((AppiumDriver)driver);
        action.longPress(bottom).moveTo(top).release().perform();
    }

    public static void swipeDown(RemoteWebDriver driver,MobileElement ele){
        Dimension size = ele.getSize();
        int startY = (int)(size.height*0.85);//0.967490
        int endY = (int)(size.height*0.70);//0.92490
        int x = (int)(size.height*0.33);//0.327777
        PointOption top = new PointOption();
        top.withCoordinates(x, startY);//2113

        PointOption bottom = new PointOption();
        bottom.withCoordinates(x,endY);//354 2020

        TouchAction action = new TouchAction((AppiumDriver)driver);
        action.longPress(bottom).moveTo(top).release().perform();
    }

    public static void scrollForward(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void flingForward(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).flingForward()"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void scrollBackward(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollBackward()"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void flingBackward(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).flingBackward()"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void scrollToBeginning(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(10)"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void flingToBeginning(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).flingToBeginning(10)"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void scrollToEnd(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(10)"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void flingToEnd(RemoteWebDriver driver){
        try {
            ((AppiumDriver) driver).findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).flingToEnd(10)"));
        } catch (InvalidSelectorException e) {
            e.printStackTrace();
        }
    }

    public static void scrollOnText(RemoteWebDriver driver,String visibleText){
        try {
            ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+visibleText+"\").instance(0))");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void swipeHalfScreenBottomToTop(RemoteWebDriver driver){
        Dimension size = driver.manage().window().getSize();
        int startY = (int)(size.height*0.98);//0.967490
        int endY = (int)(size.height*0.5);//0.92490
        int x = (int)(size.width*0.02);//0.327777
        PointOption bottom = new PointOption();
        bottom.withCoordinates(x, startY);//2113

        PointOption top = new PointOption();
        top.withCoordinates(x,endY);//354 2020
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.longPress(bottom).moveTo(top).release().perform();
    }

}
