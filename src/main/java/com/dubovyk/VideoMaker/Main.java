package com.dubovyk.VideoMaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.ColorChooserUI;
import java.awt.*;
import java.io.IOException;
import java.util.prefs.Preferences;


/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class Main extends Application{
    @Override
    public void start(Stage primaryStage){
        Preferences preferences = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);
        preferences.put("temp_path", "data/downloads/");
        preferences.put("temp_video", "data/videos/");
        preferences.put("temp_images", "data/images/");
        preferences.put("import_path", "data/import/");
        preferences.put("import_music", "data/import/music/");
        preferences.put("import_videos", "data/import/videos/");
        preferences.put("target_height", "1080");
        preferences.put("target_width", "1920");
        preferences.putInt("header_fontsize", 62);
        preferences.putInt("body_fontsize", 40);
        preferences.put("header_font", "Arial Black");
        preferences.put("body_font", "Arial Black");
        preferences.put("header_color", "0x000000");
        preferences.put("pros_color", "0x00FA00");
        preferences.put("cons_color", "0xFA0000");
        preferences.putInt("frame_width", 1920);
        preferences.putInt("frame_height", 1080);
        preferences.putInt("imageWidth", 1000);
        preferences.putInt("imageHeight", 563);

        String title = "Videos auto-generator";
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/main.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}
