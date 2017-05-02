package com.dubovyk.VideoMaker.XMLParser;

import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class Parser {
    Preferences preferences = Preferences.userNodeForPackage(com.dubovyk.VideoMaker.Models.MainModels.GlobalModel.class);
    public VideoData parseFile(String filename){
        VideoData parsedVideo = new VideoData();
        List<SingleSlide> images = new ArrayList<SingleSlide>();
        List<String> tagList = new ArrayList<String>();

        try {
            DocumentBuilder dbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dbuilder.parse(filename);
            parsedVideo.setTitle(document.getElementsByTagName("title").item(0).getTextContent());
            parsedVideo.setDescription(document.getElementsByTagName("description").item(0).getTextContent());
            parsedVideo.setUrl(document.getElementsByTagName("postUrl").item(0).getTextContent());

            NodeList tags = document.getElementsByTagName("tags");
            NodeList tagNodeList = ((Element) tags.item(0)).getElementsByTagName("tag");

            for(int currentTag = 0; currentTag < tagNodeList.getLength(); currentTag++){
                tagList.add(tagNodeList.item(currentTag).getTextContent());
            }

            parsedVideo.setTags(tagList);

            NodeList slides = document.getElementsByTagName("slide");

            for(int i = 0; i < slides.getLength(); i++){
                String id, link, name;
                List<String> advantages = new ArrayList<String>();
                List<String> disadvantages = new ArrayList<String>();

                Element slide = (Element) slides.item(i);

                id = slide.getElementsByTagName("id").item(0).getTextContent();
                link = slide.getElementsByTagName("link").item(0).getTextContent();
                name = slide.getElementsByTagName("name").item(0).getTextContent();

                Element advs = (Element) slide.getElementsByTagName("advantages").item(0);
                NodeList advantagesList = advs.getElementsByTagName("item");
                for(int k = 0; k < advantagesList.getLength(); k++){
                    advantages.add(advantagesList.item(k).getTextContent());
                }

                Element disadv = (Element) slide.getElementsByTagName("disadvantages").item(0);
                NodeList disadvantagesList = disadv.getElementsByTagName("item");
                for(int k = 0; k < disadvantagesList.getLength(); k++){
                    disadvantages.add(disadvantagesList.item(k).getTextContent());
                }
                SingleSlide currentSlide = new SingleSlide(id, name, link, advantages, disadvantages);
                images.add(currentSlide);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        parsedVideo.setVideoSlides(images);
        parsedVideo.setFrameWidth(preferences.getInt("frame_width", 0));
        parsedVideo.setFrameHeight(preferences.getInt("frame_height", 0));
        parsedVideo.setTextFont(new Font(preferences.get("body_font", "Arial Black"), Font.PLAIN, preferences.getInt("body_fontsize", 22)));
        parsedVideo.setHeaderFont(new Font(preferences.get("header_font", "Arial Black"), Font.BOLD, preferences.getInt("header_fontsize", 22)));
        parsedVideo.setHeaderColor(Color.decode(preferences.get("header_color", "0x000000")));
        parsedVideo.setConsColor(Color.decode(preferences.get("cons_color", "0xFA0000")));
        parsedVideo.setProsColor(Color.decode(preferences.get("pros_color", "0x00FA00")));
        System.out.println(parsedVideo);
        return parsedVideo;
    }
}
