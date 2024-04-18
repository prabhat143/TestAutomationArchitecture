package com.companyName.utils;

import com.companyName.drivers.DriverManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.companyName.reports.ReportManagerRunner;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;

public class AssertClass {

    public static void info(String logMessage) {
        ReportManagerRunner.getTest().log(Status.INFO, logMessage);
    }

    public static void assertEquals( String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.equals(expected))
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, actual, expected);
    }

    public static void assertEquals(int actual, int expected, String logMessage, String logErrMessage) {
        if (actual == expected)
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, String.valueOf(actual), String.valueOf(expected));
    }

    public static void assertEquals(double actual, double expected, String logMessage, String logErrMessage) {
        if (actual == expected)
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, String.valueOf(actual), String.valueOf(expected));
    }

    public static void assertTrue(boolean condition, String logMessage, String logErrMessage) {
        if (condition)
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage);
    }

    public static void assertFalse(boolean condition, String logMessage, String logErrMessage) {
        if (!condition)
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage);
    }

    public static void assertEqualsIgnoreCase(String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.equalsIgnoreCase(expected))
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, actual, expected);
    }

    public static void assertContains(String actual, String expected, String logMessage, String logErrMessage) {
        if (actual.contains(expected) || expected.contains(actual))
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, actual, expected);
    }

    public static void assertEquals(Object actual, Object expected, String logMessage, String logErrMessage) {
        if (actual.equals(expected))
            ReportManagerRunner.getTest().log(Status.PASS, logMessage);
        else
            fail(logErrMessage, actual.toString(), expected.toString());
    }

    public static void assertNotEquals(int actual, int expected, String logErrMessage) {
        if (actual != expected)
            ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage);

    }

    public static void assertTrue(String logMessage) {
        ReportManagerRunner.getTest().log(Status.PASS, logMessage);
    }

    public static void assertFail(String logMessage) {
        ReportManagerRunner.getTest().log(Status.FAIL, logMessage);
        Assert.fail(logMessage);
    }

    public static void assertFail(String logMessage,Throwable throwable) {
        ReportManagerRunner.getTest().log(Status.FAIL, logMessage);
        ReportManagerRunner.getTest().log(Status.FAIL, throwable);
        Assert.fail(logMessage);
    }
    public static void assertFalse(String logMessage, Throwable throwable) {
        fail(logMessage);
        ReportManagerRunner.getTest().log(Status.FAIL, throwable);
    }

    public static void checkStepStatus(String logMessage,String errLogMessage) {
        if (ReportManagerRunner.getTest().getStatus().getName().equalsIgnoreCase("Fail")) {
            assertFail(errLogMessage);
        }else {
            assertTrue(logMessage);
        }
    }

    public static void assertFalse(String logMessage) {
        ReportManagerRunner.getTest().log(Status.FAIL, logMessage);
    }


    private static void fail(String logErrMessage) {
        SessionId sessionId;
        try {
            sessionId = DriverManager.getDriver().getSessionId();
        } catch (NullPointerException e) {
            sessionId = null;
        }
        if (sessionId != null) {
            String screenShotName = CommonUtils.captureScreenShotAsBase64();
            if (screenShotName != null) {
                ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
            } else {
                ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage);
            }

        } else {
            ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage);
        }
    }

    private static void fail(String logErrMessage, String actual, String expected) {
        SessionId sessionId;
        try {
            sessionId = DriverManager.getDriver().getSessionId();
        } catch (NullPointerException e) {
            sessionId = null;
        }
        if (sessionId != null) {
            try {
                String screenShotName = CommonUtils.captureScreenShotAsBase64();
                if (screenShotName != null) {
                    ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
                } else {
                    ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
                }
            } catch (NullPointerException e) {
                ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
            }
        } else {
            ReportManagerRunner.getTest().log(Status.FAIL, logErrMessage + " | " + "Actual: " + actual + " Expected: " + expected);
        }
    }


}
