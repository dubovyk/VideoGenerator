package com.dubovyk.VideoMaker.Models.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class VideoData {
    private Preferences prefs = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);
    private List<SingleSlide> videoSlides;
    private String title;
    private String description;
    private String url;
    private List<String> tags;
    private int bodyTextHeight;
    private int headerTextHeight;
    private int frameWidth, frameHeight;
    private Font headerFont, textFont;
    private Color headerColor, prosColor, consColor;
    private String introPath, outroPath, musicPath;
    public VideoData(){}

    public VideoData(String title, String description, String url, Collection<String> tags, Collection<SingleSlide> slides){
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = new ArrayList<String>();
        this.tags.addAll(tags);
        this.videoSlides = new ArrayList<SingleSlide>();
        this.videoSlides.addAll(slides);
    }

    public VideoData(VideoDescription description, List<SingleSlide> videoSlides) {
        this.videoSlides = videoSlides;
        this.url = description.getUrl();
        this.title = description.getTitle();
        this.tags = description.getTags();
        this.description = description.getDescription();
    }

    public VideoData(List<SingleSlide> videoSlides) {
        this.videoSlides = videoSlides;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBodyTextHeight() {
        return bodyTextHeight;
    }

    public void setBodyTextHeight(int bodyTextHeight) {
        this.bodyTextHeight = bodyTextHeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<SingleSlide> getVideoSlides() {
        return videoSlides;
    }

    public void setVideoSlides(List<SingleSlide> videoSlides) {
        this.videoSlides = videoSlides;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getIntroPath() {
        return introPath;
    }

    public void setIntroPath(String introPath) {
        this.introPath = introPath;
    }

    public String getOutroPath() {
        return outroPath;
    }

    public void setOutroPath(String outroPath) {
        this.outroPath = outroPath;
    }

    public int getHeaderTextHeight() {
        return headerTextHeight;
    }

    public void setHeaderTextHeight(int headerTextHeight) {
        this.headerTextHeight = headerTextHeight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public Font getHeaderFont() {
        return headerFont;
    }

    public void setHeaderFont(Font headerFont) {
        this.headerFont = headerFont;
    }

    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public Color getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(Color headerColor) {
        this.headerColor = headerColor;
    }

    public Color getProsColor() {
        return prosColor;
    }

    public void setProsColor(Color prosColor) {
        this.prosColor = prosColor;
    }

    public Color getConsColor() {
        return consColor;
    }

    public void setConsColor(Color consColor) {
        this.consColor = consColor;
    }

    public String getFilePath(){
        return prefs.get("temp_path", "temp") + this.getTitle() + ".mp4";
    }

    public String getSlidesDesc(){
        StringBuilder sb = new StringBuilder();
        for(SingleSlide slide: videoSlides){
            sb.append(slide.getName() + "  " + slide.getUrl() + " " + slide.getPros());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        return "The video has name: " + title + ", its URL is: " +
                url + ", it has following tags: " + tags.toString() + ", its description is: " + description +
                "and slides are:\n" + getSlidesDesc();
    }
}
