package com.companyName.utils;

import com.companyName.reports.ReportManager;
import com.companyName.testrail.APIClient;
import com.companyName.testrail.APIException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestTrailUtils {
    private static APIClient client=null;
    private static String runId=null;
    private static final String PROJECT_ID = "1";
    public static HashMap<String, String[]> caseIdStatusMap = new HashMap<>();
    private static final String suiteID = "1";
    static String testRailRunName = null;
    private static String mileStoneID = "";

    public static void setTestTrailUserDetails() {
        try {
            String TestTrailCheck = System.getenv("TestTrailCheck");
            if(TestTrailCheck==null){
                TestTrailCheck = GetFrameworkKeys.getPropValue("TestTrailCheck");
            }
            if (TestTrailCheck.equalsIgnoreCase("yes")) {
                client = new APIClient("https://hostName.testrail.io/");
                client.setUser(GetFrameworkKeys.getSecretsValue("TestTrailUser"));
                client.setPassword(GetFrameworkKeys.getSecretsValue("TestTrailPassword"));
            }
        }catch (Exception e){
            CommonUtils.logInfo("TestTrail is not set in framework variables");
        }
    }

    public static void createTestRun(String testName) {
        testRailRunName = System.getenv("TestRailRunName");
        mileStoneID = System.getenv("MileStoneID");
        if(client!=null) {
            if (testRailRunName == null) {
                Map data = new HashMap();
                data.put("suite_id",suiteID);
                data.put("milestone_id",mileStoneID);
                data.put("include_all", false);
                data.put("name", testName + "-" + "release" + "-" + (new SimpleDateFormat("YYYY-MM-dd").format(new Date())) +"_"+ System.currentTimeMillis());
                JSONObject c = null;
                try {
                    c = (JSONObject) client.sendPost("add_run/" + PROJECT_ID, data);
                    runId = c.get("id").toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                runId = getExistingRunID(testRailRunName);
            }
        }
    }

    public static void setTestRailCaseStatus() {
        if(runId!=null) {
            Map data = new HashMap();
            caseIdStatusMap.entrySet().iterator().forEachRemaining(map -> {
                switch (map.getValue()[0]) {
                    case "1":
                        data.put("status_id", 1);
                        data.put("comment", map.getValue()[1]+"\n BSUrl:"+map.getValue()[2]);
                        break;

                    case "2":
                        data.put("status_id", 5);
                        data.put("comment", map.getValue()[1]+"\n BSUrl:"+map.getValue()[2]);
                        break;
                }

                try {
                    client.sendPost("add_result_for_case/" + runId + "/" + map.getKey(), data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void addTestCases() {
        if(runId!=null) {
            Map data = new HashMap();
            ArrayList<Object> caseIds = new ArrayList<>();
            caseIdStatusMap.entrySet().iterator().forEachRemaining(e -> {
                caseIds.add(e.getKey());
            });

            data.put("include_all", false);
            data.put("case_ids", caseIds);
            try {
                client.sendPost("update_run/" + runId, data);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getTestCaseId(ITestResult result) {
        try {
            if (runId != null) {
                String caseId = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName();
                if (caseId != null) {
                    caseId = caseId.replaceAll("[cC]", "");
                    if (caseId.length() != 0 && result.isSuccess()) {
                        CommonUtils.logInfo(caseId + "  " + result.getStatus());
                        caseIdStatusMap.put(caseId, new String[]{String.valueOf(result.getStatus()), "Test case working as expected"});
                    } else if (caseId.length() != 0 && !result.isSuccess()){
                        CommonUtils.logInfo(caseId + "  " + result.getStatus() + " " + result.getThrowable().fillInStackTrace());
                        caseIdStatusMap.put(caseId, new String[]{String.valueOf(result.getStatus()), "Test Case Failed with " + result.getThrowable().fillInStackTrace()});
                    }
                }
            }
        }catch (Exception e){
            AssertClass.info("Issue with while testrail flow");
        }
    }

    public static void addAttachmentToRun(){
        try {
            if(runId!=null) {
                client.sendPost("add_attachment_to_run/" + runId, ReportManager.reportLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    private static String getExistingRunID(String testRailRunName){
        JSONArray c = null;
        try {
            c = (JSONArray) client.sendGet("get_runs/" + PROJECT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
        if(c.toJSONString().contains(testRailRunName)){

            for(Object e : c){
                org.json.JSONObject json = new org.json.JSONObject(e.toString());
                if(json.get("name").toString().equalsIgnoreCase(testRailRunName)) {
                    return  json.get("id").toString();
                }
            }
        }

        return null;
    }

}
