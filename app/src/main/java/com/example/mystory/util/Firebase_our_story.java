package com.example.mystory.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Firebase_our_story {

    public String author_name;
    public String image_url;
    public String story_title;
    public String story_text;

    public Firebase_our_story(){}

    public Firebase_our_story(String author_name, String image_url, String story_title, String story_text) {
        this.author_name = author_name;
        this.image_url = image_url;
        this.story_title = story_title;
        this.story_text = story_text;
    }
}
