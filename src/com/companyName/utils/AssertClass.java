package com.companyName.utils;

import com.companyName.drivers.DriverManager;
import com.companyName.reports.ExtentRunner;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;

public class AssertClass {

    public static void info(String logMessage) {
        ExtentRunner.getTest().log(Status.INFO, logMessage);
    }

    public static void assertEquals(RemoteWebDriver driver, String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.equals(expected))
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, actual, expected);
    }

    public static void assertEquals(RemoteWebDriver driver, int actual, int expected, String logMessage, String logErrMessage) {
        if (actual == expected)
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, String.valueOf(actual), String.valueOf(expected));
    }

    public static void assertEquals(RemoteWebDriver driver, double actual, double expected, String logMessage, String logErrMessage) {
        if (actual == expected)
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, String.valueOf(actual), String.valueOf(expected));
    }

    public static void assertTrue(RemoteWebDriver driver, boolean condition, String logMessage, String logErrMessage) {
        if (condition)
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage);
    }

    public static void assertFalse(RemoteWebDriver driver,boolean condition, String logMessage, String logErrMessage) {
        if (!condition)
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage);
    }

    public static void assertEqualsIgnoreCase(RemoteWebDriver driver,String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.equalsIgnoreCase(expected))
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, actual, expected);
    }

    public static void assertContains(RemoteWebDriver driver, String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.contains(expected) || expected.contains(actual))
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, actual, expected);
    }

    public static void assertEquals(RemoteWebDriver driver, Object actual, Object expected, String logMessage, String logErrMessage) {
        if (actual.equals(expected))
            ExtentRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(driver, logErrMessage, actual.toString(), expected.toString());
    }

    public static void assertNotEquals(int actual, int expected, String logErrMessage) {
        if (actual != expected)
            ExtentRunner.getTest().log(Status.FAIL, logErrMessage);

    }

    public static void assertTrue(String logMessage) {
        ExtentRunner.getTest().log(Status.PASS, logMessage);
    }

    public static void assertFail(String logMessage) {
        ExtentRunner.getTest().log(Status.FAIL, logMessage);
        Assert.fail(logMessage);
    }

    public static void assertFail(String logMessage,Throwable throwable) {
        ExtentRunner.getTest().log(Status.FAIL, logMessage);
        ExtentRunner.getTest().log(Status.FAIL, throwable);
        Assert.fail(logMessage);
    }
    public static void assertFalse(RemoteWebDriver driver,String logMessage, Throwable throwable) {
        fail(driver, logMessage);
        ExtentRunner.getTest().log(Status.FAIL, throwable);
    }

    public static void checkStepStatus(String logMessage,String errLogMessage) {
        if (ExtentRunner.getTest().getStatus().getName().equalsIgnoreCase("Fail")) {
            assertFail(errLogMessage);
        }else {
            assertTrue(logMessage);
        }
    }

    public static void assertFalse(String logMessage) {
        ExtentRunner.getTest().log(Status.FAIL, logMessage);
    }


    private static void fail(RemoteWebDriver driver, String logErrMessage) {
        SessionId sessionId;
        try {
            sessionId = DriverManager.getDriver().getSessionId();
        } catch (NullPointerException e) {
            sessionId = null;
        }
        if (sessionId != null) {
            String screenShotName = CommonUtils.captureScreenShotAsBase64(driver);
            if (screenShotName != null) {
                ExtentRunner.getTest().log(Status.FAIL, logErrMessage, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
            } else {
                ExtentRunner.getTest().log(Status.FAIL, logErrMessage);
            }

        } else {
            ExtentRunner.getTest().log(Status.FAIL, logErrMessage);
        }
    }

    private static void fail(RemoteWebDriver driver, String logErrMessage, String actual, String expected) {
        SessionId sessionId;
        try {
            sessionId = DriverManager.getDriver().getSessionId();
        } catch (NullPointerException e) {
            sessionId = null;
        }
        if (sessionId != null) {
            try {
                String screenShotName = CommonUtils.captureScreenShotAsBase64(driver);
                if (screenShotName != null) {
                    ExtentRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
                } else {
                    ExtentRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
                }
            } catch (NullPointerException e) {
                ExtentRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
            }
        } else {
            ExtentRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
        }
    }


}
