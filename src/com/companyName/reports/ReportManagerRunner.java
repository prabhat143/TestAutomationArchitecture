package com.companyName.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ReportManagerRunner {
    static ThreadLocal<Map<Integer, ExtentTest>> reportTestMap = new ThreadLocal<>();
    static ThreadLocal<ExtentReports> extentReports = ThreadLocal.withInitial(()->ReportManager.reports);
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    /**
     * to generate extentreport
     */
    public static synchronized void endTest() {
        extentReports.get().flush();
    }

    /**
     * To start extentreport
     * @param testName
     * @return ExtentTest
     */
    public static synchronized ExtentTest startTest(String testName) {
        test.set(extentReports.get().createTest(testName));
        Map<Integer, ExtentTest> map = new HashMap<>();
        map.put((int) (Thread.currentThread().getId()), test.get());
        reportTestMap.set(map);
        return test.get();
    }

    /**
     * To get extentreport
     * @return ExtentTest
     */
    public static synchronized ExtentTest getTest() {
        return reportTestMap.get().get((int) (Thread.currentThread().getId()));
    }

    /**
     * To set Report categories
     * @param suitName
     */
    public static synchronized void setReportCategories(String suitName){
        getTest().assignCategory(suitName);
    }

    public static synchronized String getExtentTestStatus(){
        return test.get().getStatus().getName();
    }
}
