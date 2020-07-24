package com.example.mystory.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "stories")
public class StoryModel {

    @ColumnInfo(name = "image_url")
    public String imageUrl;
    @ColumnInfo(name = "author_name")
    public String author;
    @ColumnInfo(name = "story_title")
    public String title;
    @ColumnInfo(name = "story_text")
    public String text;
    @PrimaryKey(autoGenerate = true)
    public long  uuid;

    public StoryModel(String imageUrl, String author, String title, String text) {
        this.imageUrl = imageUrl;
        this.author = author;
        this.title = title;
        this.text = text;
    }
}
