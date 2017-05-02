package com.dubovyk.VideoMaker.Controllers;

import com.dubovyk.VideoMaker.Models.Data.VideoData;
import com.dubovyk.VideoMaker.Models.MainModels.GlobalModel;
import com.dubovyk.VideoMaker.VideoCreater.AudioMerger;
import com.dubovyk.VideoMaker.VideoCreater.HumbleVideoProcessor;
import com.dubovyk.VideoMaker.VideoCreater.Preprocessor;
import com.dubovyk.VideoMaker.VideoCreater.VideoProcessor;
import com.dubovyk.VideoMaker.XMLParser.Parser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */

public class ControllerMain implements Initializable {
    public TextField titleField;
    public TextArea descriptionField;
    public TextField widthField;
    public TextField heightField;
    public ComboBox introSelector;
    public ComboBox outroSelector;
    public ComboBox musicSelector;
    private Preferences preferences = Preferences.userNodeForPackage(GlobalModel.class);
    private GlobalModel globalModel = GlobalModel.getInstance();
    private Preprocessor preprocessor;
    private VideoProcessor videoProcessor;

    public void initialize(URL url, ResourceBundle resourceBundle){
        File temp = new File("data");

        if(!temp.exists()){
            temp.mkdir();
        }
        File downloads = new File(preferences.get("temp_path", null));
        if(!downloads.exists()){
            downloads.mkdir();
        }

        File imports = new File(preferences.get("import_path", null));
        if(!imports.exists()){
            imports.mkdir();
        }

        File music = new File(preferences.get("import_music", null));
        if(!music.exists()){
            music.mkdirs();
        }

        File videos = new File(preferences.get("import_videos", null));
        if(!videos.exists()){
            videos.mkdirs();
        }
        updateVideos();
        updateMusic();
    }

    @FXML
    public void handleOpenFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();

        File f = fileChooser.showOpenDialog(new Stage());
        if (f != null){
            Parser p = new Parser();
            VideoData video = p.parseFile(f.getAbsolutePath());
            GlobalModel.getInstance().setVideoData(video);
            titleField.setText(video.getTitle());
            descriptionField.setText(video.getDescription());
            widthField.setText(String.valueOf(video.getFrameWidth()));
            heightField.setText(String.valueOf(video.getFrameHeight()));
        }
    }

    @FXML
    public void handleProcess(ActionEvent event) {
        if(globalModel.getVideoData() == null){
            handleOpenFile(null);
            if (globalModel.getVideoData() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("No file was chosen");
                return;
            }
        }
        VideoData targetVideo = GlobalModel.getInstance().getVideoData();
        if(introSelector.getValue() != null) {
            targetVideo.setIntroPath(preferences.get("import_videos", "data/import/videos/") + introSelector.getValue());
        }
        if(outroSelector.getValue() != null) {
            targetVideo.setOutroPath(preferences.get("import_videos", "data/import/videos/") + outroSelector.getValue());
        }
        if(musicSelector.getValue() != null) {
            targetVideo.setMusicPath(preferences.get("import_music", "data/import/music/") + musicSelector.getValue());
        }
        Preprocessor preprocessor = new Preprocessor(targetVideo);
        targetVideo.setBodyTextHeight(preprocessor.getBodyTextHeight());
        targetVideo.setHeaderTextHeight(preprocessor.getHeaderTextHeight());
        preprocessor.downloadFiles();
        preprocessor.convertImages();

        HumbleVideoProcessor processor = new HumbleVideoProcessor(targetVideo);
        try {
            processor.start(); //recordScreen("test.mp4", "mp4", null, 5, 25);
            processor.join();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleImportMusic(ActionEvent event){
        List<String> extensions = Arrays.asList("*.mp3", "*.wav");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio files", extensions);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(filter);
        File targetMusic = chooser.showOpenDialog(new Stage());
        if(targetMusic != null){
            String newPath = preferences.get("import_music", "data/music/") + targetMusic.getName();
            try {
                FileUtils.copyFile(targetMusic, new File(newPath));
            } catch (IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File could not be imported");
            }
        }
        updateMusic();
    }

    @FXML
    public void handleImportVideo(ActionEvent event){
        List<String> extensions = Arrays.asList("*.mp4", "*.avi");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Video files", extensions);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(filter);
        File targetVideo = chooser.showOpenDialog(new Stage());
        if(targetVideo != null){
            String newPath = preferences.get("import_videos", "data/videos/") + targetVideo.getName();
            try {
                FileUtils.copyFile(targetVideo, new File(newPath));
                System.out.println(targetVideo.getAbsolutePath());
                System.out.println(newPath);
            } catch (IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File could not be imported");
            }
        }
        updateVideos();
    }

    @FXML
    public void handleExit(ActionEvent event){
        Platform.exit();
    }

    public void updateVideos(){
        File intros[] = new File(preferences.get("import_videos", "data/import/videos/")).listFiles();
        for(File f: intros){
            if(!introSelector.getItems().contains(f.getName())){
                introSelector.getItems().add(f.getName());
            }
            if(!outroSelector.getItems().contains(f.getName())) {
                outroSelector.getItems().add(f.getName());
            }
        }
    }

    public void updateMusic(){
        File music[] = new File(preferences.get("import_music", "data/import/music/")).listFiles();
        for(File f: music){
            if(!musicSelector.getItems().contains(f.getName())) {
                musicSelector.getItems().add(f.getName());
            }
        }
    }
}

