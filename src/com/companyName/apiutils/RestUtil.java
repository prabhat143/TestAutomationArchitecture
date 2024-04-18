package com.companyName.apiutils;

import com.aventstack.extentreports.Status;
import com.companyName.reports.ReportManagerRunner;
import com.companyName.utils.CommonUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class RestUtil {

    /**
     * Get default headers
     * @return Map<String, String>
     * @throws IOException
     */
    public static Map<String, String> getHeaders() throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        headers.put("accept-language", "en-US,en;q=0.9");
        headers.put("accept", "*/*");

        return headers;
    }

    /**
     * Set JWT token in header
     * @param JWT
     * @return Map<String, String>
     * @throws IOException
     */
    public static Map<String, String> getHeaders(String JWT) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        headers.put("accept-language", "en-US,en;q=0.9");
        headers.put("accept", "*/*");
        headers.put("Authorization", "Bearer "+ JWT);

        return headers;
    }

    /**
     *
     * @param token
     * @throws IOException
     */
    public static void updateAuthToken(String token) throws IOException {
        FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + File.separator + "config" + File.separator + "header.properties");
        Properties prop = new Properties();
        prop.load(inputStream);
        inputStream.close();

        FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + File.separator + "config" + File.separator + "header.properties");
        prop.setProperty("authToken",token);
        prop.store(outputStream,"Updated the Auth Token");

    }

    /**
     * Post the request using endpoint and body
     * @param endPoint
     * @param body
     * @return Response
     */
    public static Response postRequest(String endPoint, Object body){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(RestUtil.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all();
        request.body(body);

        return request.post();
    }

    /**
     *
     * @param fileName
     * @return String
     * @throws IOException
     */
    public static String generateBodyFromJSON(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + File.separator + "Resources" +
                File.separator + "JSONRequests" + File.separator+ fileName + ".json")));
    }

    /**
     *
     * @param fileName
     * @throws FileNotFoundException
     */
    public static void emptyHeaderFile(String fileName) throws FileNotFoundException {
        new PrintWriter(System.getProperty("user.dir") + File.separator + "config" + File.separator + fileName + ".properties").close();
    }

    /**
     *
     * @param request
     * @param path
     * @return Response
     */
    public static Response getPostResponse(RequestSpecification request, String path) {
        printRequest(request);
        Response response = request.post(path);
        CommonUtils.logInfo("RESPONSE: " + response.getBody().asString());
        ReportManagerRunner.getTest().log(Status.INFO,"PATH: " +path);
        ReportManagerRunner.getTest().log(Status.INFO, "RESPONSE: " + response.getBody().asString());
        return response;

    }

    /**
     *
     * @param request
     * @param path
     * @return Response
     */
    public static Response getCallResponse( RequestSpecification request,String path) {
        printRequest(request);
        Response response = request.get(path);
        CommonUtils.logInfo(response.getBody().asString());
        ReportManagerRunner.getTest().log(Status.INFO,"PATH: " +path);
        ReportManagerRunner.getTest().log(Status.INFO,"RESPONSE: " + response.getBody().asString());

        return response;
    }

    /**
     *
     * @param request
     */
    public static void printRequest(RequestSpecification request){
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        ReportManagerRunner.getTest().log(Status.INFO, "URI is :"+queryable.getURI());
        ReportManagerRunner.getTest().log(Status.INFO, "Headers are :"+queryable.getHeaders());
        ReportManagerRunner.getTest().log(Status.INFO, "Request Body is :"+queryable.getBody());
    }

    /**
     *
     * @param client_Id
     * @param client_Secret
     * @return JWT token
     */
    public static String postOAuth(String client_Id, String client_Secret,String tokenURL){
        Response response =
                RestAssured.given().auth().preemptive().basic(client_Id, client_Secret)
                        .formParam("grant_type", "client_credentials")
                        .when()
                        .post(tokenURL);

        return response.getBody().asString();
    }

    public static Response postRequestWithJwt(String endPoint, Object body,String JWT){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(RestUtil.getHeaders(JWT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all();
        request.body(body);

        return request.post();
    }

    public static Response getRequestWithJwt(String endPoint, String JWT){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(RestUtil.getHeaders(JWT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all();

        return request.get();
    }

    public static Response postRequestFormUrl(String endPoint, Map body){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(RestUtil.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all();
        request.contentType(ContentType.URLENC)
                .formParams(body);
        return request.post();
    }

    public static Response putRequestFormParams(String endPoint, Map body,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .formParams(body);
        return request.put();
    }

    public static Response deleteRequest(String endPoint,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all();

        return request.delete();
    }
    public static Response deleteRequest(String endPoint, Map body,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .body(body);

        return request.delete();
    }

    public static Response getRequestWithJwt(String endPoint, String JWT,Map<String,Object> params){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(RestUtil.getHeaders(JWT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .params(params);

        return request.get();
    }

    public static Response deleteRequestWithParams(String endPoint, Map body,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .params(body);

        return request.delete();
    }

    public static Response postRequestFormParams(String endPoint, Map body,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .formParams(body);
        return request.post();
    }

    public static Response getWithParams(String endPoint, Map params,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .params(params);
        return request.get();
    }

    public static Response putRequestJson(String endPoint, Map body,Map<String,String> header){
        RequestSpecification request = RestAssured.given();
        request.baseUri(endPoint);
        try {
            request.headers(Objects.isNull(header)? getHeaders() : header.isEmpty()?getHeaders():header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.log().all()
                .body(body);
        return request.put();
    }
}