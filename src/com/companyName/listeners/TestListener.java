package com.companyName.listeners;

import com.companyName.drivers.DriverManager;
import com.companyName.reports.ReportManager;
import com.companyName.reports.ReportManagerRunner;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.TestTrailUtils;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * Configure testng listner and guid testng what to do on start,fail,success
 */
public class TestListener  implements ITestListener {

	public static final String TEST_CASE = "Test Case ";
	String testName;

	public void onTestStart(ITestResult result) {
		CommonUtils.logInfo("--------------------------- Start " + result.getMethod().getDescription() + "---------------------------");
		testName = String.valueOf(Arrays.stream(result.getParameters()).findFirst());
		testName = testName.replace("Optional","").replace(".empty","");

		if(testName.isEmpty()) {
			ReportManagerRunner.startTest(result.getMethod().getDescription());
			ReportManagerRunner.getTest().log(Status.INFO,
					TEST_CASE + result.getMethod().getDescription());
		}else{
			ReportManagerRunner.startTest( result.getMethod().getDescription() + " "+testName);
			ReportManagerRunner.getTest().log(Status.INFO, TEST_CASE + testName + " "+ result.getMethod().getDescription());
		}

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ReportManagerRunner.getTest().log(Status.PASS, result.getMethod().getDescription()
				+ " execution is completed");
		if(ReportManagerRunner.getExtentTestStatus().equalsIgnoreCase("fail")){
			result.setStatus(ITestResult.FAILURE);
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String screenShotName = CommonUtils.captureScreenShotAsBase64();
		ReportManagerRunner.getTest().log(Status.FAIL, (TEST_CASE + result.getMethod().getDescription()
				+ " Executed and " + "TEST FAILED"));
		if(screenShotName!=null) {
			ReportManagerRunner.getTest().log(Status.FAIL, result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
		}else{
			ReportManagerRunner.getTest().log(Status.FAIL, result.getThrowable());
		}

	}

	public void onTestSkipped(ITestResult result) {
		String screenShotName = CommonUtils.captureScreenShotAsBase64();
		ReportManagerRunner.getTest().log(Status.SKIP, "TEST SKIPPED");
		if(screenShotName!=null) {
			ReportManagerRunner.getTest().log(Status.SKIP, result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
		}else{
			ReportManagerRunner.getTest().log(Status.SKIP, result.getThrowable());
		}

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext iTestContext) {
		CommonUtils.logInfo(("==== Test Suite " + iTestContext.getName() + " Started ======"));
	}

	public void onFinish(ITestContext iTestContext) {
		CommonUtils.logInfo(("==== Test Suite " + iTestContext.getName() + " ending ======"));
		ReportManagerRunner.endTest();
		ReportManager.logoIntegration();
		TestTrailUtils.addAttachmentToRun();
	}

	public void runningTest(ITestResult result) {
		testName = String.valueOf(Arrays.stream(result.getParameters()).findFirst());
		testName = testName.replaceAll("Optional","").replaceAll(".empty","");

		if(testName.isEmpty()) {
			ReportManagerRunner.startTest(result.getMethod().getDescription());
			ReportManagerRunner.getTest().log(Status.INFO,
					"Test Case " + result.getMethod().getDescription());
		}else{
			ReportManagerRunner.startTest( result.getMethod().getDescription() + " "+testName);
			ReportManagerRunner.getTest().log(Status.INFO, "Test Case "  + testName + " "+ result.getMethod().getDescription());
		}
	}

}