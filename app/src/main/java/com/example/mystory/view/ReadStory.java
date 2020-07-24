package com.example.mystory.view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystory.R;
import com.example.mystory.model.StoryDatabase;
import com.example.mystory.model.StoryModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadStory extends Fragment {

    View view;
    String story_uuid;
    public ReadStory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_read_story, container, false);


//

        story_uuid=ReadStoryArgs.fromBundle(getArguments()).getStoryUuid();
        Log.i("info123","id is "+ String.valueOf(story_uuid));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetStory().execute();


    }

    private class GetStory extends AsyncTask<Void,Void,StoryModel >{

        @Override
        protected StoryModel doInBackground(Void... voids) {
            StoryModel x =StoryDatabase.getInstance(getContext()).storyDao().getStory(story_uuid);
            return x;
        }

        @Override
        protected void onPostExecute(StoryModel storyModel) {
            super.onPostExecute(storyModel);
            //we will then inflate the views
        }
    }

}
