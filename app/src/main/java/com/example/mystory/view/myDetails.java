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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystory.R;
import com.example.mystory.util.Author;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myDetails extends Fragment {
    View view;
    TextView author_email;
    EditText author_name;
    EditText author_mobile;
    EditText author_age;
    EditText author_gender;
    Button author_my_stories;
    Button author_update;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    public myDetails() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_my_details, container, false);
        author_email=view.findViewById(R.id.author_email);author_email.setVisibility(View.GONE);
        author_name=view.findViewById(R.id.author_name);author_name.setVisibility(View.GONE);
        author_mobile=view.findViewById(R.id.author_mobile);author_mobile.setVisibility(View.GONE);
        author_age=view.findViewById(R.id.author_age);author_age.setVisibility(View.GONE);
        author_gender=view.findViewById(R.id.author_gender);author_gender.setVisibility(View.GONE);
        author_my_stories=view.findViewById(R.id.author_my_stories);
        author_update=view.findViewById(R.id.author_update);author_update.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("author").child(mAuth.getCurrentUser().getUid());
        setViews();

        author_my_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewStories();
            }
        });
        author_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });





       // Inflate the layout for this fragment
        return view;
    }

    public void goToNewStories(){
        NavDirections directions=myDetailsDirections.actionMyDetailsToMyStories();
        Navigation.findNavController(view).navigate(directions);
    }

    public void updateDetails(){

        Author author=new Author(author_name.getText().toString(),author_email.getText().toString(),author_mobile.getText().toString(),author_age.getText().toString(),author_gender.getText().toString());
        databaseReference.setValue(author).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Data updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Sorry the data could not be updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setViews(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                author_email.setText(snapshot.child("email").getValue().toString());author_email.setVisibility(View.VISIBLE);
                author_name.setText(snapshot.child("author_name").getValue().toString());author_name.setVisibility(View.VISIBLE);
                author_mobile.setText(snapshot.child("phone").getValue().toString());author_mobile.setVisibility(View.VISIBLE);
                author_age.setText(snapshot.child("age").getValue().toString());author_age.setVisibility(View.VISIBLE);
                author_gender.setText(snapshot.child("gender").getValue().toString());author_gender.setVisibility(View.VISIBLE);
                author_update.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Could not Fetch the user Data!!", Toast.LENGTH_SHORT).show();
                author_update.setVisibility(View.GONE);
            }
        });
    }
}
