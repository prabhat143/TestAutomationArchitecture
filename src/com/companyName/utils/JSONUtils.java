package com.companyName.utils;


import com.github.wnameless.json.flattener.JsonFlattener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONUtils {

    /**
     * @param pathOfFileWithJsonContent
     * @return A flattened Map of the nested json
     */
    public static HashMap<String, Object> getJSONAsFlattenedMap(String pathOfFileWithJsonContent, String templateName) {
        JSONParser parser = new JSONParser();
        Map<String, Object> flattenedJsonMap = null;
        try {
            // Get the json file content
            Object obj = parser.parse(new FileReader(pathOfFileWithJsonContent));
            JSONObject jsonObject = (JSONObject) obj;
            //Flatten the json content as a Map
            flattenedJsonMap = JsonFlattener.flattenAsMap(jsonObject.get(templateName).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // convert the Map to HashMap and return
        return new HashMap<String, Object>(flattenedJsonMap);
    }
}

