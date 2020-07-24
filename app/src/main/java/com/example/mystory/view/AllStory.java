package com.example.mystory.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mystory.R;
import com.example.mystory.model.StoryModel;
import com.example.mystory.viewmodel.AllStoryViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllStory extends Fragment {
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView error_message;
    SwipeRefreshLayout swipeRefreshLayout;
    private AllStoryViewModel allStoryViewModel;
    private AllStoryAdapter adapter = new AllStoryAdapter(new ArrayList<StoryModel>());
    View view;

    public AllStory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_story, container, false);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        error_message = view.findViewById(R.id.error_message);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

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

    private void observeModel() {
        //recyler view
        allStoryViewModel.allstories.observe(getViewLifecycleOwner(), new Observer<List<StoryModel>>() {
            @Override
            public void onChanged(List<StoryModel> storyModels) {
                if (storyModels != null && storyModels instanceof List) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.updateAllStories((ArrayList<StoryModel>) storyModels);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.all_story_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            mAuth.signOut();
            NavDirections directions = AllStoryDirections.actionAllStoryToLoginScreen();
            Navigation.findNavController(view).navigate(directions);
        } else if (item.getItemId() == R.id.new_story) {
            NavDirections directions = AllStoryDirections.actionAllStoryToNewStory();
            Navigation.findNavController(view).navigate(directions);
        } else if (item.getItemId() == R.id.user_details) {
            NavDirections directions = AllStoryDirections.actionAllStoryToMyDetails();
            Navigation.findNavController(view).navigate(directions);
        }
        return super.onOptionsItemSelected(item);
    }
}
