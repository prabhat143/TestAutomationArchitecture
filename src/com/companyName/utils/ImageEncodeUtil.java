package com.companyName.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImageEncodeUtil {

    public static String imageEncodetoBase64(File inputImage) {
        String base64String;
        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(inputImage.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        base64String = Base64.getEncoder().encodeToString(fileContent);
        return base64String ;
    }
    public static File imageDecodeFromBase64(String encodedSting) {
        byte[] decodedBytes = Base64
                .getDecoder()
                .decode(encodedSting);
        File outputFile = new File( File.pathSeparator + "temp.jpeg");
        try {
            FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }
}
