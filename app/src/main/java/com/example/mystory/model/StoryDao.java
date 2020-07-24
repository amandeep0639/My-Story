package com.example.mystory.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StoryDao {
    @Insert
    List<Long> insertAll(StoryModel... storyModels);

    @Query("Select * from stories ")
    List<StoryModel> getAllStories();

    @Query("select * from stories where firebase_uuid=:story_id")
    StoryModel getStory(String story_id);

    @Query("select * from stories where author_name=:author_email")
    List<StoryModel> getMyStories(String author_email);

    @Query("delete from stories")
    void deleteAll();

    @Query("delete from stories where uuid=:story_id")
    void delete(long story_id);

}
