package com.companyName.utils;

import com.companyName.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static Logger logger;

    /**
     * Logger
     *
     * @param str
     */
    public static void logInfo(String str) {
        Class<?> className = null;
        try {
            className = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger = LoggerFactory.getILoggerFactory().getLogger(className + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        logger.info(str);
    }

    /**
     * To verify element is present or not
     *
     * @param by
     * @return boolean
     */
    public static boolean isElementPresent(RemoteWebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * To get Text from element
     *
     * @param web
     * @return String
     */
    public static String getText(WebElement web) {
        return web.getText();
    }

    /**
     * Click element by loactor
     *
     * @param tragetLocator
     */
    public static void clickByLocator(RemoteWebDriver driver, By tragetLocator) {
        getElement(driver, tragetLocator).click();
    }

    /*
    click element By Webelement
     */
    public static void clickByWebElement(WebElement targetElement) {
        targetElement.click();
    }

    /**
     * Fill using locator
     *
     * @param targetlocator
     * @param text
     */
    public static void fillByLocator(RemoteWebDriver driver, By targetlocator, String text) {
        getElement(driver, targetlocator).sendKeys(text);
    }

    /**
     * fill using webelement
     *
     * @param targetElement
     * @param text
     */
    public static void clearAndFillByWebElement(RemoteWebDriver driver, WebElement targetElement, String text) {
        clearInput(targetElement);
        targetElement.sendKeys(text);
    }

    /**
     * Clean the input field
     *
     * @param targetElement
     */
    public static void clearInput(WebElement targetElement) {
        String value = targetElement.getAttribute("value");
        for (int i = 0; i < value.length(); i++)
            targetElement.sendKeys(Keys.BACK_SPACE);
    }

    /**
     * Get Element using xpath
     *
     * @param locator
     * @return WebElement
     */
    public static WebElement getElementByXpath(RemoteWebDriver driver, String locator) {
        return driver.findElement(getLocator(locator));
    }

    /**
     * @param locator
     * @return By
     */
    public static By getLocator(String locator) {
        return By.xpath(locator);
    }


    /**
     * Element is clickable or not
     *
     * @param web
     * @return boolean
     */
    public static boolean isClickable(RemoteWebDriver driver, WebElement web) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(web));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * String contains special character
     *
     * @param str
     * @return boolean
     */
    public static boolean isSpecialCharPresent(String str) {
        Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher my_match = my_pattern.matcher(str);
        return my_match.find();
    }


    /**
     * To swtich context in mobile
     *
     * @param context
     */
    public static void switchContext(RemoteWebDriver driver, String context) {
        ((MobileDriver) driver).context(context);
    }

    /**
     * Open url on respective browser
     *
     * @param url
     */
    public static void openUrl(RemoteWebDriver driver, String url) {
        driver.get(url);
    }

    /**
     * Open new Window on browser
     */
    public static void openNewWindow(RemoteWebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        switchWindow(driver);
    }

    /**
     * Mobile driver: Reset App
     */
    public static void resetApp(RemoteWebDriver driver) {
        ((MobileDriver) driver).resetApp();
    }

    /**
     * wait on page loading
     */
    public static void waitPageLoad(RemoteWebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    /**
     * TO hide key boarding
     */
    public static void hideKeyBoard(RemoteWebDriver driver) {
        ((AppiumDriver) driver).hideKeyboard();
    }

    /**
     * @param ele
     * @param attribute
     * @return String
     */
    public static String getAttributeValue(WebElement ele, String attribute) {
        return ele.getAttribute(attribute);
    }

    /**
     * To convert String to json
     *
     * @param jsonString
     * @return JSONObject
     */
    public static JSONObject createJsonObject(String jsonString) {
        return new JSONObject(jsonString);
    }

    /**
     * To get url from WebEkyc/Esign response
     *
     * @param json
     * @return String
     */
    public static String getUrl(JSONObject json) {
        return json.getJSONObject("data").get("url").toString();
    }

    /**
     * Click element using javascript
     *
     * @param ele
     */
    public static void clickByJavaScript(RemoteWebDriver driver, WebElement ele) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", ele);
    }

    /**
     * Browser refresh
     */
    public static void browserRefresh(RemoteWebDriver driver) {
        driver.navigate().refresh();
    }

    /**
     * Navigation back in browser
     */
    public static void browerNavitageBack(RemoteWebDriver driver) {
        driver.navigate().back();
    }

    /**
     * hard wait using thread
     *
     * @param timeout
     */
    public static void threadWait(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get text from mobileElement
     *
     * @param mobileElement
     * @return String
     */
    public static String getText(MobileElement mobileElement) {
        return mobileElement.getText().trim();
    }

    /**
     * Click mobile element
     *
     * @param targetElement
     */
    public static void clickByMobileElement(MobileElement targetElement) {
        targetElement.click();
    }

    /**
     * Clear and fill the input field on Mobile
     *
     * @param targetElement
     * @param text
     */
    public static void clearAndFillByMobileElement(MobileElement targetElement, String text) {
        targetElement.clear();
        targetElement.sendKeys(text);
    }

    /**
     * Get mobile element using locator
     *
     * @param targetLocator
     * @return MobileElement
     */
    public static MobileElement getMobElement(RemoteWebDriver driver, By targetLocator) {
        return (MobileElement) ((MobileDriver) driver).findElement(targetLocator);
    }

    /**
     * Get web element using locator
     *
     * @param targetLocator
     * @return
     */
    public static WebElement getElement(RemoteWebDriver driver, By targetLocator) {
        return driver.findElement(targetLocator);
    }

    /**
     * Get list of mobile element using locator
     *
     * @param targetLocator
     * @return
     */
    public static List<MobileElement> geMobElements(RemoteWebDriver driver, By targetLocator) {
        return ((MobileDriver) driver).findElements(targetLocator);
    }

    /**
     * Get list on web element using locator
     *
     * @param targetLocator
     * @return
     */
    public static List<WebElement> getElements(RemoteWebDriver driver, By targetLocator) {
        return driver.findElements(targetLocator);
    }

    /**
     * Wait until element is clickable
     *
     * @param web
     * @return boolean
     */
    public static boolean isClickable(RemoteWebDriver driver, MobileElement web) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(web));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Switch window in browser
     */
    public static void switchWindow(RemoteWebDriver driver) {
        Set<String> windows = driver.getWindowHandles();
        String mainWindow = driver.getWindowHandle();
        for (String window : windows) {
            if (window.equals(mainWindow))
                continue;
            driver.switchTo().window(window);
            break;
        }
    }

    /**
     * Navigation back in mobile browser
     */
    public static void naviagteBack(RemoteWebDriver driver) {
        ((MobileDriver) driver).navigate().back();
    }

    /**
     * On/Off wifi in ANdroid mobile
     */
    public static void toggleWifi(RemoteWebDriver driver) {
        ((AndroidDriver) driver).toggleWifi();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * To get field value in mobile
     *
     * @param ele
     * @return
     */
    public static String getFieldValue(MobileElement ele) {
        return ele.getAttribute("text");
    }

    /**
     * Close the respective App
     */
    public static void closeApp(RemoteWebDriver driver) {
        ((MobileDriver) driver).closeApp();
    }

    /**
     * Launch the respective app
     */
    public static void launchApp(RemoteWebDriver driver) {
        ((MobileDriver) driver).launchApp();
    }

    /**
     * Is wifi connection is enable or not
     *
     * @return boolean
     */
    public static boolean isConnectionEnable(RemoteWebDriver driver) {
        return ((AndroidDriver) driver).getConnection().isWiFiEnabled();
    }

    /**
     * Element is present or not on the screen that means element is dispayed or not
     *
     * @param ele
     * @return static
     */
    public static boolean elementPresent(MobileElement ele) {
        try {
            if (ele.isDisplayed() && ele != null)
                return true;
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }


    public static String extractLocator(WebElement ele) {
        String eleString = ele.toString();

        if (eleString.contains("css")) {
            return eleString.substring(eleString.indexOf("css selector:") + "css selector:".length(), eleString.lastIndexOf("]") + 1);
        } else if (eleString.contains("xpath")) {
            return eleString.substring(eleString.indexOf("xpath:") + "xpath:".length(), eleString.lastIndexOf("]") + 1);
        }

        return null;
    }

    public static void sendNumericKeyBoard(RemoteWebDriver driver, String text) {
        char[] ch = text.toCharArray();
        for (char c : ch)
            ((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
    }

    public static String appVersion() {
        String version = null;
        if (FrameworkVariables.PLATFORM.equalsIgnoreCase("Android")) {
            try {
                String s = fetchCMD("aapt d badging " + FrameworkVariables.APP + " | grep 'pack'").readLine();
                version = s.substring(s.indexOf("versionName='") + "versionName='".length(), s.indexOf("' compileSdkVersion"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return version.trim();
    }

    public static void switchToActiveApp(RemoteWebDriver driver, String packageName) {
        ((AndroidDriver<MobileElement>) driver).activateApp(packageName);
    }

    public static void pressEnterKey(RemoteWebDriver driver) {
        ((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
    }

    public static void sendAlphabetKeyBoard(RemoteWebDriver driver, String text) {
        char[] ch = text.toUpperCase().toCharArray();
        for (char c : ch)
            ((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.valueOf(Character.toString(c))));

    }

    public static String extractLocator(MobileElement ele) {
        String eleString = ele.toString();

        if (eleString.contains("css")) {
            return eleString.substring(eleString.indexOf("css selector:") + "css selector:".length(), eleString.lastIndexOf("]") + 1);
        } else if (eleString.contains("xpath")) {
            return eleString.substring(eleString.indexOf("xpath:") + "xpath:".length(), eleString.lastIndexOf("]") + 1);
        }

        return null;
    }

    public static void getKeyBoard(RemoteWebDriver driver) {
        ((AppiumDriver<MobileElement>) driver).getKeyboard();
    }

    public static boolean isKeyBoardOpen(RemoteWebDriver driver) {
        return ((AndroidDriver<MobileElement>) driver).isKeyboardShown();
    }

    public static String captureScreenShotAsBase64() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * To run cmd query on Terminal
     *
     * @param cmdStatement
     * @return BufferedReader
     */
    public static BufferedReader fetchCMD(String cmdStatement) {
        Runtime rt = Runtime.getRuntime();
        BufferedReader reader = null;
        Process p = null;
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                p = rt.exec(cmdStatement);
            } else {
                p = rt.exec(new String[]{"bash", "-l", "-c", cmdStatement});
            }
            p.waitFor();
            InputStream is = p.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;

    }


    /**
     * @param ele      The webElement which needs input
     * @param testData The test case's testData HashMap Object reference
     * @param key      The key whose value has to be fetched
     */
    public static void fillTestDataInWebElement(WebElement ele, HashMap<String, Object> testData, String key) {
        if (testData.containsKey(key)) {
            ele.sendKeys(testData.get(key).toString());
        }
    }

    /**
     * This method replaces the 'REPLACE' word in the xpath value passed,
     * finds a webElement with the xpath and returns it
     *
     * @param xpath
     * @param dynamicValue
     * @return
     */
    public static WebElement getDynamicWebElementElement(RemoteWebDriver driver, String xpath, String dynamicValue) {
        return getElement(driver, getLocator(xpath.replace("REPLACE", dynamicValue)));
    }

    /**
     * This method switches to iFrame
     *
     * @param idNameIndex The id or Name or index of the iFrame
     * @return true if switch is successful else throw an exception
     */
    public static Boolean switchToFrame(RemoteWebDriver driver, String idNameIndex) {
        try {
            driver.switchTo().frame(idNameIndex);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * This method waits for the element to be enabled/clickable and then performs a click operation
     *
     * @param element          The webElement to be clicked
     * @param timeOutInSeconds Max time in seconds to wait for the element to be enabled/clickable
     */
    public static void waitForElementToEnableAndClick(WebElement element, int timeOutInSeconds) {
        WaitUtils.waitForElementToEnable(element, timeOutInSeconds);
        CommonUtils.clickByWebElement(element);
    }

    /**
     * This methods return the data in the System Clipboard memory
     *
     * @return The content in the system clipboard
     */
    @SneakyThrows
    public static String getSystemClipboardContent() {
        String clipboardContent = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        return clipboardContent;
    }

    public static boolean isIOSPlatform(){
        return DriverManager.getDriver().getCapabilities().getCapability("platformName").toString().equalsIgnoreCase("ios");
    }

}
