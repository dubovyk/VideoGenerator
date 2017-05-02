package com.dubovyk.VideoMaker.ImageProcessor;

import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;
import com.dubovyk.VideoMaker.Models.MainModels.GlobalModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class ImageProcessor {
    private ImageCropper cropper;
    private VideoData videoData;

    public ImageProcessor(){
        cropper = new ImageCropper();
    }

    /**
     * This method takes a single slide object and
     * modifies the image in the temp directory:
     * - adds pros and cons
     * - adds title
     *
     * @param slide SingleSlide instance to be updated.
     */
    public void processImage(SingleSlide slide, VideoData videoData){
        try {
            this.videoData = videoData;
            BufferedImage image = ImageIO.read(new File(slide.getFilePath()));
            image = cropper.getCroppedImage(image, .1);
            image = extendSize(image);
            Graphics2D imageGraphics = (Graphics2D) image.getGraphics();


            drawTitle(slide.getName(), image, imageGraphics);
            drawCons(slide.getCons(), image, imageGraphics);
            drawPros(slide.getPros(), image, imageGraphics);
            imageGraphics.dispose();

            ImageIO.write(image, "jpg", new File(slide.getFilePath()));
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void drawTitle(String s, BufferedImage image, Graphics g) {
        g.setColor(videoData.getHeaderColor());
        g.setFont(videoData.getHeaderFont());
        FontMetrics fm = g.getFontMetrics();
        int x = (image.getWidth() - fm.stringWidth(s)) / 2;
        int y = fm.getAscent() + (image.getHeight() - (fm.getAscent() + fm.getDescent())) / 30;
        g.drawString(s, x, y);
    }

    private int calculateHorizontalOffset(FontMetrics fm, Collection<String> strings){
        int max = 0;
        for(String str: strings){
            if(fm.stringWidth(str) > max){
                max = fm.stringWidth(str);
            }
        }
        return max;
    }

    private void drawCons(List<String> cons, BufferedImage image, Graphics g){
        g.setColor(videoData.getConsColor());
        g.setFont(videoData.getTextFont());

        FontMetrics fm = g.getFontMetrics();
        int x = image.getWidth() - calculateHorizontalOffset(fm, cons) - 50;
        int y = image.getHeight();
        drawMultipleLine(cons, g, x, y);
    }

    private void drawPros(List<String> pros, BufferedImage image, Graphics g){
        g.setColor(videoData.getProsColor());
        g.setFont(videoData.getTextFont());
        int x = 50;
        int y = image.getHeight();
        drawMultipleLine(pros, g, x, y);
    }

    private void drawMultipleLine(List<String> lines, Graphics g, int startXOffset, int startYOffset){
        for(String string : lines){
            g.drawString(string, startXOffset, startYOffset -= g.getFontMetrics().getHeight());
        }
    }

    private BufferedImage extendSize(BufferedImage image){
        int frameWidth = GlobalModel.getInstance().getVideoData().getFrameWidth();
        int frameHeight = GlobalModel.getInstance().getVideoData().getFrameHeight();
        BufferedImage newImage = new BufferedImage(frameWidth, frameHeight, image.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setColor(null);
        graphics.fillRect(0, 0, frameWidth, frameHeight);
        int newHeight = (frameHeight - videoData.getBodyTextHeight() - videoData.getHeaderTextHeight() - 100);
        int newWidth = getWidthToHeight(image, newHeight);
        graphics.drawImage(image, (frameWidth-newWidth)/2, (frameHeight-newHeight + 100 - videoData.getBodyTextHeight())/2, newWidth, newHeight, null, null);
        graphics.dispose();
        return newImage;
    }

    private int getWidthToHeight(BufferedImage image, int newHeight){
        return newHeight * image.getWidth() / image.getHeight();
    }
}
