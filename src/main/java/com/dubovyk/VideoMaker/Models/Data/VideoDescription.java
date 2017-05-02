package com.dubovyk.VideoMaker.Models.Data;

import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class VideoDescription {
    private String title;
    private String description;
    private String url;
    private List<String> tags;

    public VideoDescription(){
        this(null);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VideoDescription(String title) {
        this(title, null);
    }

    public VideoDescription(String title, String description) {
        this(title, null, description, null);
    }

    public VideoDescription(String title, String url, String description, List<String> tags) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString(){
        return "The video has name: " + title + ", its URL is: " +
                url + ", it has following tags: " + tags.toString() + ", and its description is: " + description;
    }
}
