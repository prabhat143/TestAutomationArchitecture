package com.companyName.listeners;

import com.companyName.drivers.DriverManager;
import com.companyName.reports.ExtentManager;
import com.companyName.reports.ExtentRunner;
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

	String testName;

	public void onTestStart(ITestResult result) {
		CommonUtils.logInfo("--------------------------- Start " + result.getMethod().getDescription() + "---------------------------");
		try {
			String deviceDetails[] = result.getTestClass().getXmlTest().getAllParameters().get("device").split(":");
			if (deviceDetails.length != 0) {
				ExtentRunner.startTest(result.getMethod().getDescription() + " :Started on device name - " + deviceDetails[1]);
				ExtentRunner.getTest().log(Status.INFO,
						"Test Case " + result.getMethod().getDescription() + " with device details " + "<br> deviceID: " + deviceDetails[0] + "<br> deviceVersion: " + deviceDetails[2]);
			} else {
				runningTest(result);
			}
		}catch (Exception e){
			runningTest(result);
		}
	}

	public void onTestSuccess(ITestResult result) {
		ExtentRunner.setExtentTestngStatus(result);
		ExtentRunner.getTest().log(Status.INFO, result.getMethod().getDescription()
				+ " execution is completed");
	}

	public void onTestFailure(ITestResult result) {
		String screenShotName = CommonUtils.captureScreenShotAsBase64(DriverManager.getDriver());
		ExtentRunner.getTest().log(Status.FAIL, ("Test Case " + result.getMethod().getDescription()
				+ " Executed and " + "TEST FAILED"));
		if(screenShotName!=null) {
			ExtentRunner.getTest().log(Status.FAIL, result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
		}else{
			ExtentRunner.getTest().log(Status.FAIL, result.getThrowable());
		}


	}

	public void onTestSkipped(ITestResult result) {
		String screenShotName = CommonUtils.captureScreenShotAsBase64(DriverManager.getDriver());
		ExtentRunner.getTest().log(Status.SKIP, "TEST SKIPPED");
		if(screenShotName!=null) {
			ExtentRunner.getTest().log(Status.SKIP, result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotName).build());
		}else{
			ExtentRunner.getTest().log(Status.SKIP, result.getThrowable());
		}

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext iTestContext) {
		CommonUtils.logInfo(("==== Test Suite " + iTestContext.getName() + " Started ======"));
	}

	public void onFinish(ITestContext iTestContext) {
		CommonUtils.logInfo(("==== Test Suite " + iTestContext.getName() + " ending ======"));
		ExtentRunner.endTest();
		ExtentManager.logoIntegration();
		TestTrailUtils.addAttachmentToRun();
	}

	public void runningTest(ITestResult result) {
		testName = String.valueOf(Arrays.stream(result.getParameters()).findFirst());
		testName = testName.replaceAll("Optional","").replaceAll(".empty","");

		if(testName.isEmpty()) {
			ExtentRunner.startTest(result.getMethod().getDescription());
			ExtentRunner.getTest().log(Status.INFO,
					"Test Case " + result.getMethod().getDescription());
		}else{
			ExtentRunner.startTest( result.getMethod().getDescription() + " "+testName);
			ExtentRunner.getTest().log(Status.INFO, "Test Case "  + testName + " "+ result.getMethod().getDescription());
		}
	}

}