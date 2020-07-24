package com.example.mystory.view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mystory.R;
import com.example.mystory.model.StoryDatabase;
import com.example.mystory.model.StoryModel;
import com.example.mystory.util.Firebase_our_story;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update_delete_my_story extends Fragment {
    View view;
    String myStoryUuid;
    EditText myStoryTitle;
    EditText myStoryText;
    StoryModel currentStory;
    public update_delete_my_story() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_update_delete_my_story, container, false);
        setHasOptionsMenu(true);
        myStoryUuid = update_delete_my_storyArgs.fromBundle(getArguments()).getMyStoryUuid();
        Log.i("info1234",myStoryUuid);
        myStoryTitle = view.findViewById(R.id.edit_story_title);
        myStoryText = view.findViewById(R.id.edit_story_text);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetStory().execute();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.update_story_page_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_story) {
            deleteStory();
        } else if (item.getItemId() == R.id.update_story) {
            updateStory();
        }
        return super.onOptionsItemSelected(item);
    }

    void deleteStory() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference().child("story").child(myStoryUuid);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "The story has been deleted!!", Toast.LENGTH_SHORT).show();
                NavDirections directions=update_delete_my_storyDirections.actionUpdateDeleteMyStoryToMyStories();
                Navigation.findNavController(view).navigate(directions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "The story could not be deleted!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void updateStory() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference().child("story").child(myStoryUuid);

        String author_name=currentStory.author;
        String image_url=currentStory.imageUrl;
        Firebase_our_story updated_story=new Firebase_our_story(author_name,image_url,myStoryTitle.getText().toString(),myStoryText.getText().toString());

        databaseReference.setValue(updated_story).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "The story has been updated!!", Toast.LENGTH_SHORT).show();
                NavDirections directions=update_delete_my_storyDirections.actionUpdateDeleteMyStoryToMyStories();
                Navigation.findNavController(view).navigate(directions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "The story could not be updated!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inflate_views(StoryModel storyModel) {
        myStoryText.setText(storyModel.text);
        myStoryTitle.setText(storyModel.title);
    }

    private class GetStory extends AsyncTask<Void, Void, StoryModel> {

        @Override
        protected StoryModel doInBackground(Void... voids) {
            StoryModel x = StoryDatabase.getInstance(getContext()).storyDao().getStory(myStoryUuid);
            currentStory=x;
            return x;
        }

        @Override
        protected void onPostExecute(StoryModel storyModel) {
            super.onPostExecute(storyModel);
            //we will then inflate the views
            inflate_views(storyModel);
        }
    }


}
