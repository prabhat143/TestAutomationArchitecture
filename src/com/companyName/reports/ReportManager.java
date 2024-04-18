package com.companyName.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.companyName.utils.CommonUtils;
import com.companyName.utils.FrameworkVariables;
import com.companyName.utils.GetFrameworkKeys;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportManager {
    public static ExtentReports reports;
    public static String reportFileName = GetFrameworkKeys.getPropValue("ReportName");
    public static String reportFilepath = System.getProperty("user.dir") + File.separator + "GeneratedReport";
    public static String reportLocation = reportFilepath + File.separator + reportFileName + ".html";


    public static synchronized void getInstance() {
        if (reports == null)
            createInstance();
    }

    /**
     * Create extentreport with reportname plus ebvironemnt given in configuration file
     * @return ExtentReports
     */
    public static synchronized ExtentReports createInstance() {
        String filePath = getReportPath(reportFilepath);

        ExtentSparkReporter extentHtmlReporter = new ExtentSparkReporter (filePath);
        extentHtmlReporter.viewConfigurer().viewOrder().as(new ViewName[]{ViewName.DASHBOARD, ViewName.CATEGORY, ViewName.TEST, ViewName.EXCEPTION});
        extentHtmlReporter.config().setTimelineEnabled(false);
        extentHtmlReporter.config().setTheme(Theme.DARK);
        extentHtmlReporter.config().setJs("for (let e of document.querySelectorAll (\".details-col\")) { e.innerHTML ='Steps' };");
        extentHtmlReporter.config().setCss(".header .vheader .nav-logo>a .logo {min-height: 52px;min-width: 52px;}");



       /* if(FrameworkVariables.env!=null)
            extentHtmlReporter.config().setReportName(GetFrameworkKeys.getPropValue("ReportName")+" - "+ FrameworkVariables.env.toUpperCase()+" Environment");*/

        reports = new ExtentReports();
        reports.attachReporter(extentHtmlReporter);

        if (System.getProperty("os.name").contains("Win")) {
            reports.setSystemInfo("OS", "Windows 10");
        } else {
            reports.setSystemInfo("OS", "Mac OSX");
        }

        return reports;

    }

    /**
     * To create report directory
     * @param path
     * @return String
     */
    private synchronized static String getReportPath(String path) {

        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdirs()) {
                CommonUtils.logInfo("Directory: " + path + " is created!");
                return reportLocation;
            } else {
                CommonUtils.logInfo("Failed to create directory: " + path);
                return System.getProperty("user.dir");
            }
        } else {
            CommonUtils.logInfo("Directory already exists: " + path);
        }
        return reportLocation;
    }

    public static synchronized void setReportInfo(String key, String value ) {
        reports.setSystemInfo(key, value);
    }

    /**
     * Report date format
     * @return String
     */
    public static String dateFormat() {
        Date date1=null;
        String strDate=null;
        try {
            date1=new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy hh:mm:ss");
            strDate = formatter.format(date1).replaceAll(":", "-").replace(" ", "_");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strDate;

    }

    public static synchronized void logoIntegration(){
        File file = new File(reportLocation);
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element els = doc.head();
        els.html(els.html().replaceAll("https://cdn.jsdelivr.net/gh/extent-framework/extent-github-cdn@b00a2d0486596e73dd7326beacf352c639623a0e/commons/img/logo.png","image url"));

        Element body = doc.body();
        body.html(body.html().replaceAll("https://cdn.jsdelivr.net/gh/extent-framework/extent-github-cdn@b00a2d0486596e73dd7326beacf352c639623a0e/commons/img/logo.png","image url"));

        BufferedWriter htmlWriter = null;
        try {
            htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportLocation), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            htmlWriter.write(doc.html());
            htmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
