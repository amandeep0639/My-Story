package com.example.mystory.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeScreen extends Fragment {
    FirebaseAuth mAuth;
    public WelcomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      final View view=inflater.inflate(R.layout.fragment_welcome_screen, container, false);
      mAuth=FirebaseAuth.getInstance();
      final FirebaseUser user=mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //do something
                if(user==null){
                    NavDirections directions =WelcomeScreenDirections.actionWelcomeScreenToLoginScreen();
                    Navigation.findNavController(view).navigate(directions);
                }else{
                    NavDirections directions=WelcomeScreenDirections.actionWelcomeScreenToAllStory();
                    Navigation.findNavController(view).navigate(directions);
                }

            }
        }, 2000 );//time in milisecond
       return view;
    }
}
