package com.example.mystory.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mystory.R;
import com.example.mystory.util.Author;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpScreen extends Fragment {

    FirebaseAuth mAuth;
    EditText password;
    EditText email;
    Button button;
    View view;
    public SignUpScreen() {}

    public void signUp(final View view){
        if(email.getText().toString().trim().equals("")||password.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Please provide valid email id and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Account has been successfully created", Toast.LENGTH_SHORT).show();
                            add_author();

                        } else {
                            Toast.makeText(getContext(), "Please provide valid email id and password", Toast.LENGTH_SHORT).show();
                        }

                    }


                });

    }

    //after the users authentication account has been created
    //we will have to add them to the author table in the database

    public void add_author(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("author");
        String uuid=mAuth.getCurrentUser().getUid();
        reference=reference.child(uuid);
        Author author=new Author(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getEmail(),"","18","Male");
        reference.setValue(author).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                NavDirections directions=SignUpScreenDirections.actionSignUpScreenToAllStory();
                Navigation.findNavController(view).navigate(directions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                NavDirections directions=SignUpScreenDirections.actionSignUpScreenToAllStory();
                Navigation.findNavController(view).navigate(directions);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          mAuth=FirebaseAuth.getInstance();
          view= inflater.inflate(R.layout.fragment_sign_up_screen, container, false);
          button=view.findViewById(R.id.signUpButton);
          email=view.findViewById(R.id.signupEmail);
          password=view.findViewById(R.id.signUpPassword);
          button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {signUp(view);    }
        });

     return view;
    }
}
