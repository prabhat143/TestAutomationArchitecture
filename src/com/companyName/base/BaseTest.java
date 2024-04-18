package com.companyName.base;


import com.companyName.drivers.DriverManager;
import com.companyName.listeners.RetryAnnotationClass;
import com.companyName.listeners.TestListener;
import com.companyName.localsetup.LocalServer;
import com.companyName.reports.ReportManager;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.TestTrailUtils;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;


@Listeners({TestListener.class, RetryAnnotationClass.class})
public class BaseTest implements ITest {

	private LocalServer server;
	protected DriverManager driverManger;
	private ThreadLocal<String> testName = new ThreadLocal<>();


	/**
	 * initiate ConstantVariables, ExtentReport, Configuration.properties file(if not present)
	 */
	@BeforeSuite(alwaysRun = true)
	public void beforeSuit() {
		CommonUtils.logInfo("----------------------------------INSIDE BEFORE SUITE----------------------------------");
		initConfigFile();
		initExtentRep();
		initFrameworkVariables();
	}


	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext ctx) {
		CommonUtils.logInfo("----------------------------------INSIDE BEFORE TEST----------------------------------");
		TestTrailUtils.createTestRun(ctx.getCurrentXmlTest().getName());
	}

	@BeforeMethod(alwaysRun = true)
	protected void beforeMethod(Method method, ITestContext context, Object[] testData){
		if (testData.length > 0) {
			testName.set(testData[0].toString());
			context.setAttribute("testName", testName.get());
		} else
			context.setAttribute("testName", method.getName());
	}

	public void initConfigFile(){
		FrameworkVariables.createSampleConfig();
		FrameworkVariables.createReadME();
	}

	public void initFrameworkVariables() {
		FrameworkVariables.initFrameVariables();
		TestTrailUtils.setTestTrailUserDetails();
	}

	public void initExtentRep(){
		ReportManager.getInstance();
	}

	public void initAppiumServer() {
		if(!FrameworkVariables.PLATFORM.equalsIgnoreCase("desktop")&& !FrameworkVariables.PLATFORM.equalsIgnoreCase("BrowserStack")) {
			server = new LocalServer();
			server.appiumStart();
		}
	}

	public void initDriver(String device) {
			driverManger = new DriverManager(server, device);
			driverManger.initializeDriver();
	}

	protected void stopAppium(){
		if(server!=null)
			server.appiumStop();
	}

	private void killAppiumServer(){
		if(server!=null)
			server.killAllAppiumServers();
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() {
		CommonUtils.logInfo("----------------------------------INSIDE AFTER TEST----------------------------------");
		TestTrailUtils.addTestCases();
		TestTrailUtils.setTestRailCaseStatus();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuit() {
		CommonUtils.logInfo("----------------------------------INSIDE AFTER SUIT----------------------------------");
		if(!FrameworkVariables.PLATFORM.equalsIgnoreCase("desktop")) {
			killAppiumServer();
		}
	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
