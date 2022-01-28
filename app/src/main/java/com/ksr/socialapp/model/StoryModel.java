package com.ksr.socialapp.model;

public class StoryModel {
    int story;
    String name;

    public StoryModel(int story, String name) {
        this.story = story;
        this.name = name;
    }

    public int getStory() {
        return story;
    }

    public void setStory(int story) {
        this.story = story;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
