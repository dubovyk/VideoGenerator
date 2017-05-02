package com.dubovyk.VideoMaker.ImageProcessor;

import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class textFieldSizeProperties {
    private Preferences preferences = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);
    private BufferedImage image;
    private VideoData videoData;

    public textFieldSizeProperties(VideoData videoData){
        this.videoData = videoData;
    }

    private void processHeaderBlock(SingleSlide slide){
        image = new BufferedImage(videoData.getFrameHeight(), videoData.getFrameWidth(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(videoData.getHeaderFont());

        FontMetrics fm = g.getFontMetrics();

        int titleSize = videoData.getHeaderFont().getSize();
        while (fm.stringWidth(slide.getName()) > videoData.getFrameWidth() * 9 / 10){
            videoData.setHeaderFont(videoData.getHeaderFont().deriveFont((float)titleSize));
            titleSize--;
            g.setFont(videoData.getHeaderFont());
            fm = g.getFontMetrics();
        }
        //slide.setCons(newCons);
    }

    private void processTextBlocks(VideoData vData){
        image = new BufferedImage(videoData.getFrameHeight(), videoData.getFrameWidth(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(videoData.getTextFont());

        FontMetrics fm = g.getFontMetrics();
        for(SingleSlide slide: vData.getVideoSlides()) {
            List<String> cons = new ArrayList<String>();
            List<String> pros = new ArrayList<String>();
            int textSize = videoData.getTextFont().getSize();

            for (String con : slide.getCons()) {
                if ((fm.stringWidth(con) > videoData.getFrameWidth() / 2)) {
                    String[] words = con.split(" ");
                    StringBuilder newConBegin = new StringBuilder();
                    StringBuilder newConEnd = new StringBuilder();
                    int i = 0;
                    for (; i < words.length / 2; i++) {
                        newConBegin.append(words[i]).append(" ");
                    }
                    for (; i < words.length; i++) {
                        newConEnd.append(words[i]).append(" ");
                    }
                    cons.add(newConEnd.toString());
                    cons.add(newConBegin.toString());
                }
                else {
                    cons.add(con);
                }
            }

            for (String pro : slide.getPros()) {
                if ((fm.stringWidth(pro) > videoData.getFrameWidth() / 2)) {
                    String[] words = pro.split(" ");
                    StringBuilder newProBegin = new StringBuilder();
                    StringBuilder newProEnd = new StringBuilder();
                    int i = 0;
                    for (; i < words.length / 2; i++) {
                        newProBegin.append(words[i] + " ");
                    }
                    for (; i < words.length; i++) {
                        newProEnd.append(words[i] + " ");
                    }
                    pros.add(newProEnd.toString());
                    pros.add(newProBegin.toString());
                } else {
                    pros.add(pro);
                }
            }

            slide.setCons(cons);
            slide.setPros(pros);

            for (String con : slide.getCons()) {
                while (fm.stringWidth(con) > videoData.getFrameWidth() / 2) {
                    videoData.setTextFont(videoData.getTextFont().deriveFont((float) textSize));
                    textSize--;
                    g.setFont(videoData.getTextFont());
                    fm = g.getFontMetrics();
                }
            }

            textSize = videoData.getTextFont().getSize();
            for (String pro : slide.getPros()) {
                while (fm.stringWidth(pro) > videoData.getFrameWidth() / 2) {
                    videoData.setTextFont(videoData.getTextFont().deriveFont((float) textSize));
                    textSize--;
                    g.setFont(videoData.getTextFont());
                    fm = g.getFontMetrics();
                }
            }
        }
        //slide.setCons(newCons);
    }

    public int getHeaderTextHeight(){
        for(SingleSlide slide: videoData.getVideoSlides()){
            processHeaderBlock(slide);
        }
        image = new BufferedImage(videoData.getFrameHeight(), videoData.getFrameWidth(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(videoData.getHeaderFont());

        FontMetrics fm = g.getFontMetrics();
        return fm.getHeight();
    }

    public int getBodyTextHeight(){
        image = new BufferedImage(videoData.getFrameHeight(), videoData.getFrameWidth(), BufferedImage.TYPE_INT_RGB);
        processTextBlocks(videoData);

        int height = 0;
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(videoData.getTextFont());
        FontMetrics fm = g.getFontMetrics();
        for(SingleSlide slide: videoData.getVideoSlides()){
            int consHeight = 0;
            int prosHeight = 0;
            for(String string: slide.getCons()){
                consHeight += fm.getHeight();
            }
            for(String string: slide.getPros()){
                prosHeight += fm.getHeight();
            }
            height = (consHeight > height) ? consHeight : height;
            height = (prosHeight > height) ? prosHeight : height;
        }
        return height;
    }

}
