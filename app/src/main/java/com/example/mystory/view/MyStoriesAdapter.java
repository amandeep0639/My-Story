package com.example.mystory.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystory.R;
import com.example.mystory.model.StoryModel;
import com.example.mystory.util.Util;

import java.util.ArrayList;

public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.MyStoriesHolder> {
    private ArrayList<StoryModel> allstories;
    LinearLayout layout;

    public MyStoriesAdapter(ArrayList<StoryModel> allstories) {
        this.allstories = allstories;

    }

    public void updateMyStories(ArrayList<StoryModel> newMystories) {
        allstories.clear();
        allstories.addAll(newMystories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyStoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_story, parent, false);
        return new MyStoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyStoriesHolder holder, final int position) {
        ImageView all_story_image = holder.itemView.findViewById(R.id.my_story_image);
        TextView all_story_title = holder.itemView.findViewById(R.id.my_story_title);
        TextView all_story_text = holder.itemView.findViewById(R.id.my_story_text);
        TextView all_story_author = holder.itemView.findViewById(R.id.my_story_author);

        String image_url = allstories.get(position).imageUrl;
        layout = holder.itemView.findViewById(R.id.item_my_story_layout);
        all_story_title.setText(allstories.get(position).title);
        all_story_text.setText(allstories.get(position).text);
        all_story_author.setText(allstories.get(position).author);


        //here we now need to get the image from this image url and set each image view to the the corresponding image
        Util x = new Util();
        x.setImage(all_story_image, image_url, Util.getProgressDrawable(all_story_image.getContext()));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 MyStoriesDirections.ActionMyStoriesToUpdateDeleteMyStory directions=MyStoriesDirections.actionMyStoriesToUpdateDeleteMyStory();
                 directions.setMyStoryUuid(allstories.get(position).firebase_uuid);
                 Navigation.findNavController(v).navigate(directions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allstories.size();
    }


    class MyStoriesHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public MyStoriesHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
