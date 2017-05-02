package com.dubovyk.VideoMaker.VideoCreater;

import com.dubovyk.VideoMaker.ImageProcessor.textFieldSizeProperties;
import com.dubovyk.VideoMaker.ImageProcessor.ImageProcessor;
import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;
import com.dubovyk.VideoMaker.XMLParser.Downloader;

import java.awt.*;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class Preprocessor {
    private ImageProcessor imageProcessor;
    private VideoData videoData;
    private textFieldSizeProperties textProperties;

    public Preprocessor(){
        this(null);
    }

    public Preprocessor(VideoData videoData){
        this.videoData = videoData;
        imageProcessor = new ImageProcessor();
        textProperties = new textFieldSizeProperties(videoData);
        Font f = new Font(videoData.getTextFont().getFontName(), videoData.getTextFont().getStyle(), videoData.getTextFont().getSize());
    }

    public void setVideoData(VideoData videoData){
        this.videoData = videoData;
    }

    public void convertImages(){
        for(SingleSlide slide: videoData.getVideoSlides()){
            imageProcessor.processImage(slide, videoData);
        }
    }

    public int getHeaderTextHeight(){
        return textProperties.getHeaderTextHeight();
    }

    public int getBodyTextHeight(){
        return textProperties.getBodyTextHeight();
    }

    public void downloadFiles(){
        Downloader downloader = new Downloader();
        for(SingleSlide slide: videoData.getVideoSlides()){
            downloader.getFile(slide);
        }
    }
}
