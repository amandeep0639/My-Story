package com.example.mystory.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystory.R;
import com.example.mystory.model.StoryModel;
import com.example.mystory.util.Util;

import java.util.ArrayList;

public class AllStoryAdapter extends RecyclerView.Adapter<AllStoryAdapter.AllStoryHolder> {

    private ArrayList<StoryModel> allstories;
    LinearLayout layout;

    public AllStoryAdapter(ArrayList<StoryModel> allstories) {
        this.allstories = allstories;
    }

    public void updateAllStories(ArrayList<StoryModel> newAllstories) {
        allstories.clear();
        allstories.addAll(newAllstories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllStoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_story, parent, false);
        return new AllStoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllStoryHolder holder, final int position) {

        ImageView all_story_image = holder.itemView.findViewById(R.id.all_story_image);
        TextView all_story_title = holder.itemView.findViewById(R.id.all_story_title);
        TextView all_story_text = holder.itemView.findViewById(R.id.all_story_text);
        TextView all_story_author = holder.itemView.findViewById(R.id.all_story_author);
        String image_url = allstories.get(position).imageUrl;
        layout = holder.itemView.findViewById(R.id.item_layout);
        all_story_title.setText(allstories.get(position).title);
        all_story_text.setText(allstories.get(position).text);
        all_story_author.setText(allstories.get(position).author);


        //here we now need to get the image from this image url and set each image view to the the corresponding image
        Util x = new Util();
        x.setImage(all_story_image, image_url, Util.getProgressDrawable(all_story_image.getContext()));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllStoryDirections.ActionAllStoryToReadStory directions = AllStoryDirections.actionAllStoryToReadStory();
                directions.setStoryUuid(allstories.get(position).firebase_uuid);
                Navigation.findNavController(v).navigate(directions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allstories.size();
    }

    class AllStoryHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public AllStoryHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
