package com.companyName.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    Properties prop;

    /**
     * Method will create testdata directory to store all the data supportive file
     * @param filepath
     */
    public PropertiesReader(String filepath) {
        prop=new Properties();
        FileInputStream fis;
        try {
            File testDataPath = new File(System.getProperty("user.dir")+ "/testdata");
            if(testDataPath.exists()) {
                fis = new FileInputStream(System.getProperty("user.dir") + "/testdata/" + filepath);
                prop.load(fis);
            }else{
                testDataPath.mkdir();
                CommonUtils.logInfo("Please add test data file in below created path \n"+testDataPath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  String getPropValue(String key) {
        return prop.getProperty(key);
    }

}
