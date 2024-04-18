package com.companyName.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CSVUtils {

    /**
     * Read csv file
     * @param Path
     * @return List<String[]>
     */
    public List<String[]> readCSVFile(String Path){

        List<String[]> csvBody = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(Path).getAbsolutePath()));
            CSVReader csv = new CSVReader(reader);
            csvBody = csv.readAll();
            csv.close();
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return csvBody;
    }

    /**
     * Get CSV Header column number
     * @param csvBody
     * @param name
     * @return int
     */
    public int getHeaderColnumber(List<String[]> csvBody,String name){
        int count =-1;
      for(String header : csvBody.get(0)){
          count++;
          if(header.equalsIgnoreCase(name))
              break;
      }
        return count;
    }

    /**
     * To write data to csv
     * @param csvBody
     * @param filePath
     * @param row
     * @param col
     * @param data
     */
    public void writeDataCSV(List<String[]> csvBody,String filePath,int row,int col,String data){
        try {
        csvBody.get(row)[col] = data;
        CSVWriter writer = new CSVWriter(new FileWriter(filePath));
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write row data to cvs file
     * @param filePath
     * @param mapData
     */
    public void writeRowDataCSV(String filePath,HashMap<String,String> mapData){
        String[] data = new String[mapData.size()];
        List<String[]> csvBody = readCSVFile(filePath);
        int row=0;
        for(String header : readCSVFile(filePath).get(0)){
            data[row] = mapData.get(header);
            row++;
        }
        csvBody.add(csvBody.size(),data);
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get row count from csv file
     * @param csvBody
     * @param name
     * @return
     */
    public int getRowCount(List<String[]> csvBody,String name){
        int row=0;
        for(String[] arg : csvBody) {
            if (Arrays.stream(arg).anyMatch(str -> str.contains(name)))
                break;
            ++row;
        }
        return row;
    }

    /**
     * get cell data from csv file
     * @param filePath
     * @param colName
     * @param rowName
     * @return
     */
    public String getCellData(String filePath,String colName, String rowName){
        List<String[]> csvBody = readCSVFile(filePath);
        int column = getHeaderColnumber(csvBody,colName);
        int row = getRowCount(csvBody,rowName);
        return csvBody.get(row)[column];
    }

    /**
     * Create webEkyc and edign response log Template in csv file
     */
    public static void creatWebKycPdfResponseLogTemplate() {
        String webEkycReponseLog = System.getProperty("user.dir")+"/target/responseLogs";
        File testDirectory = new File(webEkycReponseLog);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                CommonUtils.logInfo("Directory: " + webEkycReponseLog + " is created!");

            } else {
                CommonUtils.logInfo("Failed to create directory: " + webEkycReponseLog);
            }
        } else {
            CommonUtils.logInfo("Directory already exists: " + webEkycReponseLog);
        }

        File csvFile =null;
        CSVWriter csv = null;
        csvFile = new File(webEkycReponseLog+"/webEkycPdfResponseLog.csv");
        if(!csvFile.exists()) {
            try {
                csv = new CSVWriter(new FileWriter(csvFile));
                csv.writeNext(new String[]{"doe", "referenceID", "JWTtoken", "url"});
                csv.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get first row with any column data from csv file
     * @param filePath
     * @param colName
     * @return
     */
    public String getCellFirstData(String filePath,String colName){
        List<String[]> csvBody = readCSVFile(filePath);
        int column = getHeaderColnumber(csvBody,colName);

        return csvBody.get(1)[column];
    }

    /**
     * To get csv data as a hashmap
     * key -> header
     * value -> cell data
     * @param csv
     * @param scenario
     * @return HashMap<String,String>
     */
    public HashMap<String,String> getJsonBody(List<String[]> csv, String scenario){
        int rowCount = getRowCount(csv,scenario);
        HashMap<String,String> mapTable = new HashMap<>();
        List<String> header = Arrays.asList(csv.get(0));
        header.iterator().forEachRemaining(e->mapTable.put(e,csv.get(rowCount)[header.indexOf(e)]));

        return mapTable;
    }

}
