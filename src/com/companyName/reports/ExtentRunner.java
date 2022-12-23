package com.companyName.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

public class ExtentRunner {
    static Map<Integer, ExtentTest> reportTestMap = new HashMap<Integer, ExtentTest>();
    static ExtentReports extentReports = ExtentManager.reports;

    /**
     * to generate extentreport
     */
    public static synchronized void endTest() {
        extentReports.flush();
    }

    /**
     * To start extentreport
     * @param testName
     * @return ExtentTest
     */
    public static synchronized ExtentTest startTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        reportTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }

    /**
     * To get extentreport
     * @return ExtentTest
     */
    public static synchronized ExtentTest getTest() {
        return reportTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    /**
     * To set Report categories
     * @param suitName
     */
    public static void setReportCategories(String suitName){
        getTest().assignCategory(suitName);
    }

    public static void setExtentTestngStatus(ITestResult result) {
        if (getTest().getStatus().getName().equalsIgnoreCase("Fail")) {
            result.setStatus(2);
        }else {
            result.setStatus(1);
        }
    }
}
