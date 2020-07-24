package com.example.mystory.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class update_delete_my_story extends Fragment {

    public update_delete_my_story() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_delete_my_story, container, false);
    }
}
