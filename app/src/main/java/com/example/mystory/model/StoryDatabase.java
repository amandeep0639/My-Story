package com.example.mystory.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {StoryModel.class},version = 1)
public abstract class StoryDatabase extends RoomDatabase {

    private static StoryDatabase instance;

    public static StoryDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(
                    context.getApplicationContext(),StoryDatabase.class,"story_database"
            ).build();
        }

        return instance;
    }

    public abstract StoryDao storyDao();

}
