package com.companyName.utils;

import java.io.*;

public class ExtractFiles {

    /**
     * Used to extract file from jar
     * @param resourceName
     */
    static public void ExportResource(String resourceName) {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder = null;
        try {
            stream = ExtractFiles.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                CommonUtils.logInfo("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(ExtractFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                stream.close();
                resStreamOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
