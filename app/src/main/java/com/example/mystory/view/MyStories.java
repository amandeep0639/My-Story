package com.example.mystory.view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystory.R;
import com.example.mystory.model.StoryDatabase;
import com.example.mystory.model.StoryModel;
import com.example.mystory.viewmodel.AllStoryViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MyStories extends Fragment {

    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView error_message;
    SwipeRefreshLayout swipeRefreshLayout;
    private AllStoryViewModel allStoryViewModel;
    private MyStoriesAdapter adapter = new MyStoriesAdapter(new ArrayList<StoryModel>());
    View view;

    public MyStories() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_stories, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.my_stories_recycler_view);
        progressBar = view.findViewById(R.id.my_stories_progress_bar);
        error_message = view.findViewById(R.id.my_stories_error_message);
        swipeRefreshLayout = view.findViewById(R.id.my_stories_swipe_refresh_layout);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allStoryViewModel = ViewModelProviders.of(this).get(AllStoryViewModel.class);
        allStoryViewModel.refresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        observeModel();
    }

    public void storiesAdded(List<StoryModel> stories) {

        if (stories != null && stories instanceof List) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.updateMyStories((ArrayList<StoryModel>) stories);
        }

    }

    private void observeModel() {
        //recyler view
        allStoryViewModel.allstories.observe(getViewLifecycleOwner(), new Observer<List<StoryModel>>() {
            @Override
            public void onChanged(List<StoryModel> storyModels) {
                if (storyModels != null && storyModels instanceof List) {
                    //we cant set this story directly because the story with the view model
                    //is the complete list of story
                    //so we have to apply a database operation here
                    //and then get the list of my stories
                    new ReadMyStoryFromDatabase().execute();
                }
            }
        });
        //error
        allStoryViewModel.isError.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean instanceof Boolean) {
                    if (aBoolean)
                        error_message.setVisibility(View.VISIBLE);
                    else
                        error_message.setVisibility(View.GONE);
                }
            }
        });
        //progress bar
        allStoryViewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean instanceof Boolean) {
                    if (aBoolean) {
                        recyclerView.setVisibility(View.GONE);
                        error_message.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    } else
                        progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private class ReadMyStoryFromDatabase extends AsyncTask<Void, Void, List<StoryModel>> {

        @Override
        protected List<StoryModel> doInBackground(Void... voids) {
            return StoryDatabase.getInstance(getContext()).storyDao().getMyStories(mAuth.getCurrentUser().getEmail());
        }

        @Override
        protected void onPostExecute(List<StoryModel> storyModels) {
            super.onPostExecute(storyModels);
            Toast.makeText(getContext(), "My stories fetched from database", Toast.LENGTH_SHORT).show();
            storiesAdded(storyModels);
        }
    }

}
