package com.companyName.testsetup;

import com.companyName.utils.CommonUtils;
import com.companyName.utils.GetFrameworkKeys;
import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Class to create Testng file : Not stable
 */
public class CreateTestngXml {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private synchronized void runTestNGTest(Map<String, String> testngParams) {
		String groupIncludenames[]=null;
		String groupExcludenames[] =null;
		try {
			groupIncludenames = System.getenv("groupInclude").split(",");
			groupExcludenames = System.getenv("groupExclude").split(",");
		}catch(Exception e){
			groupIncludenames = GetFrameworkKeys.getPropValue("GroupsToBeInclude").split(",");
			groupExcludenames = GetFrameworkKeys.getPropValue("GroupsToBeExculde").split(",");
			CommonUtils.logInfo("groups not declared in maven");
		}
		ArrayList packages = new ArrayList();
		packages.add(new XmlPackage("test.java.suit.*"));

		List listnerPackages = new ArrayList();
		listnerPackages.add("com.companyName.listeners.RetryListenerClass");
		listnerPackages.add("com.companyName.listeners.TestListener");
		int parallelCount = testngParams.size();

		// Create an instance on TestNG
		TestNG myTestNG = new TestNG();

		XmlSuite mySuite = new XmlSuite();
		mySuite.setName("Web Automation");
		mySuite.setListeners(listnerPackages);
		mySuite.setParallel(ParallelMode.TESTS);
		mySuite.setThreadCount(parallelCount);


		Iterator<Entry<String, String>> set = testngParams.entrySet().iterator();
		int count = 1;
		while (set.hasNext()) {
			HashMap<String, String> deviceMap = new HashMap<String, String>();
			deviceMap.put("device", set.next().getValue());
			XmlTest map = new XmlTest(mySuite, count);
			map.setName("Mytest" + count);
			map.setPackages(packages);
			map.setParameters(deviceMap);

			if(groupIncludenames!=null && !groupIncludenames[0].equals(""))
			for(String groupName:groupIncludenames){
				map.addIncludedGroup(groupName);
			}
			if(groupExcludenames!=null && !groupExcludenames[0].equals(""))
			for(String groupName:groupExcludenames){
				map.addExcludedGroup(groupName);
			}

			count++;
		}

		// Add the suite to the list of suites.
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		mySuites.add(mySuite);


		// Set the list of Suites to the testNG object you created earlier.
		myTestNG.setXmlSuites(mySuites);
		mySuite.setFileName("testng.xml");

		myTestNG.run();

		// Create physical XML file based on the virtual XML content
		for (XmlSuite suite : mySuites) {
			createXmlFile(suite);
		}
	}

	// This method will create an Xml file based on the XmlSuite data
	private synchronized void createXmlFile(XmlSuite mSuite) {
		FileWriter writer;
		try {
			CommonUtils.logInfo(System.getProperty("user.dir"));
			writer = new FileWriter(
					new File(System.getProperty("user.dir") + File.separator+"target"+ File.separator+"testng.xml"));
			writer.write(mSuite.toXml());
			writer.flush();
			writer.close();
			CommonUtils.logInfo(new File(System.getProperty("user.dir") + File.separator+"target"+ File.separator+"testng.xml")
					.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CreateTestngXml() {
		CreateTestngXml dt = new CreateTestngXml();
		//EmulatorRunner.runEmulators();
		Map<String, String> sets = new HashMap<>();
		dt.runTestNGTest(sets);
	}

	public static void main(String[] args) {
		CreateTestngXml();
	}

}
