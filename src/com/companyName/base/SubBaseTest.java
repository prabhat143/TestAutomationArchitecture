package com.companyName.base;

import com.companyName.drivers.DriverManager;
import com.companyName.reports.ExtentRunner;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.TestTrailUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class SubBaseTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        CommonUtils.logInfo("----------------------------------INSIDE BEFORE CLASS----------------------------------");
        initAppiumServer();
    }

    @Parameters({"device"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(@Optional String device,ITestResult result) {
        CommonUtils.logInfo("----------------------------------INSIDE BEFORE METHOD----------------------------------");
        if(!FrameworkVariables.PLATFORM.equalsIgnoreCase("BrowserStack")) {
            initDriver(device);
        }else{
            initBrowserStackInstance(result.getMethod().getMethodName());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void AfterMethod(ITestResult result) {
        CommonUtils.logInfo("----------------------------------INSIDE AFTER METHOD----------------------------------");
        ExtentRunner.setReportCategories(result.getTestClass().getRealClass().getSimpleName());
        CommonUtils.setBrowserStackStatus(DriverManager.getDriver(),result.isSuccess());
        TestTrailUtils.getTestCaseId(result,CommonUtils.getAppVideoRest(DriverManager.getDriver()));
        driverManger.closeDriver();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        CommonUtils.logInfo("----------------------------------INSIDE AFTER CLASS----------------------------------");
        if(FrameworkVariables.PLATFORM.equalsIgnoreCase("Android")) {
            stopAppium();
        }
    }

}
