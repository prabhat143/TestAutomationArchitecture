package com.companyName.apiutils;

import com.companyName.utils.AssertClass;
import io.restassured.response.Response;

public class RestValidationUtil {

    /**
     *
     * @param response
     * @param statusCode
     */
    public static void validateResponseCode(Response response, int statusCode)  {
        AssertClass.assertEquals(response.getStatusCode(), statusCode,"Status code is as expected", "The status code does not match");
    }

    /**
     *
     * @param response
     * @param key
     */
    public static void checkIfDataExists(Response response, String key) {
        AssertClass.assertTrue(response.getBody().asString().contains(key), "The "+key+" is present in the response","The "+key+" is not present in the response");
    }

    /**
     *
     * @param response
     */
    public static void validateSuccessResponseCode(Response response)  {
        AssertClass.assertEquals(response.getStatusCode(), 200,"Success status code is expected","Success status code is not as expected"+response.getStatusCode());
    }

    /**
     *
     * @param response
     * @param key
     * @param data
     */
    public static void validateData(Response response, String key, Object data) {
        AssertClass.assertEquals(response.jsonPath().get(key).toString(), data.toString(), "Value "+key+" is same as expected","Value "+key+" is different than expected");
    }

    /**
     *
     * @param actual
     * @param expected
     */
    public static void validateData(Object actual, Object expected){
        AssertClass.assertEquals(actual, expected, actual+" is as expected",actual+" is different than expected");
    }

    /**
     * @param response
     * @param key
     * @param data
     */
    public static void validateDataContains(Response response, String key, Object data){
        AssertClass.assertContains(response.jsonPath().get(key).toString(), data.toString(), "Value "+key+" is same as expected","Value "+key+" is different than expected");
    }

}
