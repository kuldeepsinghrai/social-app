package com.ksr.socialapp.model;

import java.util.ArrayList;

public class Story {

    private String storyBy;
    private long storyAt;
    private ArrayList<UserStories> userStories;

    public Story() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStories> getUserStories() {
        return userStories;
    }

    public void setUserStories(ArrayList<UserStories> userStories) {
        this.userStories = userStories;
    }
}
