package com.companyName.base;

import com.companyName.drivers.DriverManager;
import com.companyName.reports.ReportManager;
import com.companyName.reports.ReportManagerRunner;
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
        initDriver(device);
    }

    @AfterMethod(alwaysRun = true)
    public void AfterMethod(ITestResult result) {
        CommonUtils.logInfo("----------------------------------INSIDE AFTER METHOD----------------------------------");
        ReportManagerRunner.setReportCategories(result.getTestClass().getRealClass().getSimpleName());
        TestTrailUtils.getTestCaseId(result);
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
