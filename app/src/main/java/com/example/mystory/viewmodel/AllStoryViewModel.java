package com.example.mystory.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.text.BoringLayout;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mystory.R;
import com.example.mystory.model.StoryDao;
import com.example.mystory.model.StoryDatabase;
import com.example.mystory.model.StoryModel;
import com.example.mystory.view.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AllStoryViewModel extends AndroidViewModel {


    public MutableLiveData<List<StoryModel>> allstories=new MutableLiveData<List<StoryModel>>();
    public MutableLiveData<Boolean> isLoading=new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isError=new MutableLiveData<Boolean>();

    public AllStoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){

        isLoading.setValue(true);

        //we will either fetch the data from firebase or from local database
//        fetchFromDatabase();
        fetchFromFirebase();
    }

    public void fetchFromFirebase(){

        final List<StoryModel> stories=new ArrayList<>();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String image_url=postSnapshot.child("image_url").getValue().toString();
                    String author_name=postSnapshot.child("author_name").getValue().toString();
                    String story_title=postSnapshot.child("story_title").getValue().toString();
                    String story_text=postSnapshot.child("story_text").getValue().toString();
                    String story_firebase_uuid=postSnapshot.getKey();
                    Log.i("info123456",story_firebase_uuid);
                    stories.add(new StoryModel(image_url,author_name,story_title,story_text,story_firebase_uuid));
                }

                Toast.makeText(getApplication(), "Data fetched from Firebase", Toast.LENGTH_SHORT).show();

                //now all the stories have been retrieved from the firebase
                //we should now add them to our database
                new AddStoryToDatabaseAsyncTask().execute(stories);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isLoading.setValue(false);
                isError.setValue(true);
            }
        });
    }

    private  class AddStoryToDatabaseAsyncTask extends AsyncTask<List<StoryModel> ,Void,List<StoryModel> > {

        @Override
        protected List<StoryModel> doInBackground(List<StoryModel>... lists) {
            List<StoryModel> stories=lists[0];
            List<Long> results;
//            now we will add all the stories to the database
            //in turn we will get the uuids for es=ach story
            //we will then create a list with the uuids inserted at the right place
            //return that which will be collected by the post execute function
            StoryDao storyDao=StoryDatabase.getInstance(getApplication()).storyDao();
            storyDao.deleteAll();
            results= storyDao.insertAll(stories.toArray(new StoryModel[0]));
            for(int i=0;i<stories.size();i++)
                stories.get(i).uuid=results.get(i);

            return stories;
        }

        @Override
        protected void onPostExecute(List<StoryModel> storyModels) {
            super.onPostExecute(storyModels);
            Log.i("info123",String.valueOf(storyModels.size()));
            storiesAdded(storyModels);
        }
    }

    private void storiesAdded(List<StoryModel> stories){
        isLoading.setValue(false);
        isError.setValue(false);
        allstories.setValue(stories);
    }

    public void fetchFromDatabase(){
        new ReadStoryFromDatabase().execute();
    }

    private class ReadStoryFromDatabase extends AsyncTask<Void,Void,List<StoryModel>>{

        @Override
        protected List<StoryModel> doInBackground(Void... voids) {
            return StoryDatabase.getInstance(getApplication()).storyDao().getAllStories();
        }

        @Override
        protected void onPostExecute(List<StoryModel> storyModels) {
            super.onPostExecute(storyModels);
            Toast.makeText(getApplication(), "Data fetched from database", Toast.LENGTH_SHORT).show();
            storiesAdded(storyModels);
        }
    }

    //now the data has been added into the database
    //so we are calling from function from async task on post execute

}
