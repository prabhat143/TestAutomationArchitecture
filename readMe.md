#Common companyName Automation FrameWork
This framework contains approach for Mobile,Webservice and RestApi testing

###How to use Common companyName Automation framework
Run below maven goal on the terminal, This will build a jar in target folder. JAR consists of all the dependency and package used in this project.
mvn clean install -DskipTests=true

###How to use JAR Created By Common companyName Automation framework
Create a lib directory in the respective framework and add the generated jar into the lib folder.

To use jar as maven dependency, please add the below tags in maven

    <dependency>
	  <groupId>com.companyName</groupId>
	  <artifactId>automation-companyName-common</artifactId>
	  <version>1.0</version>
	  <scope>system</scope>
	  <systemPath>${basedir}/lib/automation-companyName-common-1.0.jar</systemPath>
	</dependency>
	<!-- https://mvnrepository.com/artifact/org.testng/testng -->
	<dependency>
	   <groupId>org.testng</groupId>
	   <artifactId>testng</artifactId>
	   <version>7.3.0</version>
	</dependency>

Note: Testng is already present in jar still we need to add in framework to run project with maven other wise maven will ask to configure maven dependency for TESTNG

Need to add below profile in framework to extract config files and ExtentReport config file. Add "-PConfig-File" with maven goals to unpack
example: mvn clean install -DskipTests=true -PConfig-File

`<profiles>
<profile>
<id>Config-File</id>
<build>
<plugins>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-dependency-plugin</artifactId>
<version>3.1.2</version>
<executions>
<execution>
<id>unpack</id>
<phase>package</phase>
<goals>
<goal>unpack-dependencies</goal>
</goals>
<configuration>
<outputDirectory>${basedir}/</outputDirectory>
<includes>config/**,resources/extentReport/**</includes>
</configuration>
</execution>
</executions>
</plugin>
</plugins>
</build>
</profile>
</profiles>`

Note: Profile should be runs once to unpack required file. If jar is not unpacked then framework will extract the file while running the framework.

###How to work with Common Automation framework
Remember framework will guide and provide all the details' step by step. FrameWork will not be stable until all the required file been extracted from JAR.
Configuration.properties file is main file from where framework will be controlled, configuration file will be provided by framework.


**Note: Please dont' make any changes in project without discussing with Authors.

###Below are usage and description of the class/files used in project

**frameworkvariables.properties** :- This file contains the constant variables which work around the framework. The configuration file will be updated as per the generic requirements.

**resources** :- resources directory consists on drivers for chrome firefox on mac/linux/windows. Its also consists on extentReport  config file template.

**com.companyName.localsetup.LocalServer.java** :- This class consists of non-static methods to start, stop and kill the appium server. To use this class need created object for this class saying "AppiumServer appium = new AppiumServer();".

**com.companyName.base.BaseTest** :- This is the base class where framework starts execution. Base class configure appium/webdriver, Constant variables, ExtentReport as per the configuration file.

**com.companyName.localsetup.DeviceINFO** :- This class consists of static methods which are used to get device/emulator details which are connect to system.

**com.companyName.deviceinfo.EmulatorRunner** :- This class consists of static methods used to start and kill emulator.

**com.companyName.drivers.MobileDriverClass** :- This class consists of non-static methods to set desired capabilities and AndriodDriver.

**com.companyName.drivers.BrowserStackDriverClass** :- This class consists of non-static methods to set desired capabilities and BrowserStack driver.

**com.companyName.drivers.DriverManager** :- This class consists of two constructor paramter and non parameterised constructor. Parameter constructor configure Appiumserver device details. And initiate driver as per the configuration file.

**com.companyName.enums.DriverTypes** :- This an enum class record types of drivers present in the framework.

**com.companyName.drivers.WebDriverClass** :- This class consists of method to configure brower like chrome,firefox and safari depends on OS.

**com.companyName.listeners.RetryTestClass** :- This class used to retry test cases on method level.

**com.companyName.listeners.TestListener** :- This class implements ITestListener of testng

**com.companyName.listeners.TestUtil** :- This class to take screenshot on failure of steps/Case

**com.companyName.reports.EmailManager** :- This class to send email to respective id in configuration.properties with zipped reports.

**com.companyName.reports.ExtentManager** :- This class to create extent report.

**com.companyName.reports.ExtentRunner** :- This class to to start/end extentReport,set categories and  get extentReport functionality.

**com.companyName.testsetup.CreateTestngXml** :- This class to generated own testng xml file, but not stable.

**com.companyName.utils.FrameworkVariables** :- This class initialise variables given in configuration.properties file.

**com.companyName.utils.CSVUtils** :- This class is to read/write/get.. etc from csv file.

**com.companyName.utils.AssertClass** :- This class consist of static methods for custom assertion which integrated with extent report.

**com.companyName.utils.ExtractFiles** :- This class used to extract file from jar to project directory.

**com.companyName.utils.CommonUtils** :-This class consist of common methods required for Appium/WebDriver projects. Method description are given in class.

**com.companyName.utils.GetFrameworkKeys** :- This class used to get values from configuration.properties;

**com.companyName.utils.PropertiesReader** :- This class used to read and get values from properties files. This class method will create testdata directory to store supportive data required for respective project.

**com.companyName.utils.ScrollUtils** :- This class used to scroll the brow page top or on the element.

**com.companyName.utils.WaitUtils** :- This class used to wait for elements as per the conditions.

###How to enable System features from maven

add below tags in maven goal as per the requirement
-DmavenCheck=true -> Enables maven features like sending report through framework.
-DjenkinsCheck=true -> Enable jenkin feature like headless browser.

if below variable are not given in maven then framework will fetch from configuration.properties file

-Denv=test/sandbox -> To set on which environment need to run.
-Dlanguage=id/en -> To set language to framework.
-Dplatform=Android/desktop -> To select webdriver or android
-Dbrowser=chrome/firefox -> To set select browser type

Example: mvn clean install -DjenkinsCheck=true  -Denv=test/sandbox -Dlanguage=en -Dplatform=Android/desktop -Dbrowser=chrome/firefox test 

-Dsurefire.suiteXmlFiles=testngFiles/testng.xml



To deploy MAVEN PROJECT
mvn clean install deploy:deploy-file -DgroupId=com.companyName -DartifactId=automation-companyName-common -Dversion=1.1 -DgeneratePom=true -Dpackaging=jar -DrepositoryId=automation-companyName-common -Durl=http://46.4.229.140:8081/repository/MavenHostedRelease/ -Dfile=target/automation-companyName-common-1.1.jar

