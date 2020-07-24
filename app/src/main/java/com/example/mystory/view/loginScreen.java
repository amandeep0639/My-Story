package com.example.mystory.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginScreen extends Fragment {

    FirebaseAuth mAuth;
    TextView signup;
    Button button;
    EditText email;
    EditText password;
    public loginScreen() {
        // Required empty public constructor
    }

    public void signUp(View view){
        NavDirections directions=loginScreenDirections.actionLoginScreenToSignUpScreen();
        Navigation.findNavController(view).navigate(directions);
    }

    public void login_function(final View view){
        Log.i("info2123","trying to login");

        if(email.getText().toString().trim().equals("")||password.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Please provide valid email id and password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            redirect_to_AllStory(view);
                        } else {
                            Toast.makeText(getContext(), "USer Not Found!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void redirect_to_AllStory(final View view){

        NavDirections directions=loginScreenDirections.actionLoginScreenToAllStory();
        Navigation.findNavController(view).navigate(directions);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_login_screen, container, false);
        mAuth=FirebaseAuth.getInstance();
        signup=view.findViewById(R.id.toSignUp);
        button=view.findViewById(R.id.loginButton);
        email=view.findViewById(R.id.loginEmail);
        password=view.findViewById(R.id.loginpassword);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Log.i("info2123","not a user so trying to sign up");signUp(view);    }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {login_function(v);    }
        });









        return view;
    }
}
