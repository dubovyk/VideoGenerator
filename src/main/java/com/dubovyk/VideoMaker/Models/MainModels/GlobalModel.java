package com.dubovyk.VideoMaker.Models.MainModels;

import com.dubovyk.VideoMaker.Models.Data.VideoData;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class GlobalModel {
    private static GlobalModel instance;
    private VideoData videoData;

    private String currentFile;

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    private GlobalModel(){}

    public static GlobalModel getInstance(){
        if (instance == null){
            instance = new GlobalModel();
        }
        return instance;
    }

    public VideoData getVideoData() {
        return videoData;
    }

    public void setVideoData(VideoData videoData) {
        this.videoData = videoData;
    }
}
