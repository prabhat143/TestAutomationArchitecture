package com.companyName.base;


import com.companyName.drivers.DriverManager;
import com.companyName.listeners.RetryAnnotationClass;
import com.companyName.listeners.TestListener;
import com.companyName.localsetup.LocalServer;
import com.companyName.reports.ExtentManager;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.TestTrailUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;


@Listeners({TestListener.class, RetryAnnotationClass.class})
public class BaseTest {

	private LocalServer server;
	protected DriverManager driverManger;


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

	public void initConfigFile(){
		FrameworkVariables.createSampleConfig();
		FrameworkVariables.createReadME();
	}

	public void initFrameworkVariables() {
		FrameworkVariables.initFrameVariables();
		TestTrailUtils.setTestTrailUserDetails();
	}

	public void initExtentRep(){
		ExtentManager.getInstance();
	}

	public void initAppiumServer() {
		if(!FrameworkVariables.PLATFORM.equalsIgnoreCase("desktop")&& !FrameworkVariables.PLATFORM.equalsIgnoreCase("BrowserStack")) {
			server = new LocalServer();
			server.appiumStart();
		}
	}

	public void initDriver(String device) {
			setDeviceDetailsExtent();
			driverManger = new DriverManager(server, device);
			driverManger.initializeDriver();
	}

	public void initBrowserStackInstance(String methodName){
		driverManger = new DriverManager();
		driverManger.initBrowserStackDriver(methodName);
	}

	public void setDeviceDetailsExtent(){
		ITestResult result = Reporter.getCurrentTestResult();
		if (result.getTestClass().getXmlTest().getAllParameters().size() != 0)
			ExtentManager.setReportInfo(result.getTestClass().getXmlTest().getAllParameters().get("device").split(":")[1], server.getUrl().toString());
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

}
