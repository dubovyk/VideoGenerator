package com.dubovyk.VideoMaker.VideoCreater;

import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;
import org.jcodec.api.awt.SequenceEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class VideoProcessor extends Thread{
    private Thread t;
    private Preferences prefs = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);
    private VideoData videoData;

    public VideoProcessor(VideoData videoData){
        this.videoData = videoData;
    }

    public void run(){
        SequenceEncoder encoder;
        try {
            encoder = new SequenceEncoder(new File(videoData.getFilePath()));
        } catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        long current = 0;
        for(SingleSlide slide: videoData.getVideoSlides()){
            try {
                BufferedImage image = ImageIO.read(new File(slide.getFilePath()));
                for(int i = 0; i < 250; i++) {
                    current++;
                    System.out.println("Encoding " + current);
                    encoder.encodeImage(image);
                }
            } catch (Exception ex){
                ex.printStackTrace();
                break;
            }
        }
        try {
            encoder.finish();
        } catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        System.out.println(prefs.get("temp_path", "temp") + "output.mp4");
    }

    public void start(){
        if(t == null){
            t = new Thread(this, "VideoMaker");
            t.start();
        }
    }

    public void generateVideo(){

    }
}
