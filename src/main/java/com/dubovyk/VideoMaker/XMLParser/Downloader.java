package com.dubovyk.VideoMaker.XMLParser;

import com.dubovyk.VideoMaker.Models.Data.SingleSlide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class Downloader {
    private  Preferences prefs = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);

    public void getFile(SingleSlide slide){
        try {
            URL url = new URL(slide.getUrl());
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream in = connection.getInputStream();
            String newPath = prefs.get("temp_path", "temp") + getFileName(slide.getUrl());
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
            slide.setFilePath(newPath);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getFileName(String URL){
        Pattern fileName = Pattern.compile("[\\w-]+[.](jpg|png)");
        Matcher matcher = fileName.matcher(URL);
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }
}
