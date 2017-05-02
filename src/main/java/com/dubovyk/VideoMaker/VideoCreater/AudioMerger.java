package com.dubovyk.VideoMaker.VideoCreater;

import com.dubovyk.VideoMaker.Models.Data.VideoData;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class AudioMerger {
    private VideoData videoData;

    public AudioMerger(VideoData videoData){
        this.videoData = videoData;
    }

    public void addAudioAllLength(){
        String audioSrc = videoData.getMusicPath();
        String videoSrc = videoData.getFilePath();
        String videoDst = videoSrc.replace(".mp4", "final.mp4");
        try {
            FFmpeg fFmpeg = new FFmpeg("ffmpeg.exe");
            FFprobe fFprobe = new FFprobe("ffprobe.exe");
            FFmpegBuilder builder = new FFmpegBuilder().
                    setInput(audioSrc).
                    addInput(videoSrc).
                    overrideOutputFiles(true).
                    addOutput(videoDst).
                    addExtraArgs("-shortest").
                    done();
            FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
            executor.createJob(builder).run();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
