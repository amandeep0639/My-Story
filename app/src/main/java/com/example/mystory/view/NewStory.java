package com.example.mystory.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mystory.R;
import com.example.mystory.util.Firebase_our_story;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewStory extends Fragment {
    ImageView imageView;
    Button choose_button;
    Button submit_button;
    View view;
    Bitmap bitmap_image;
    String uuid;
    public NewStory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.fragment_new_story, container, false);
        imageView=view.findViewById(R.id.add_story_image_view);
        choose_button=view.findViewById(R.id.add_story_choose_button);
        submit_button=view.findViewById(R.id.add_story_submit_button);
        bitmap_image=((BitmapDrawable)imageView.getDrawable()).getBitmap();

        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_the_story();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void chooseImage(){
        ((MainActivity)getActivity()).ask_permission_for_add_story();
    }

    public void onPermissionResult(boolean permissionValue){
//        set the image now after starting the intent to fetch the image
          if(permissionValue==true)
              getImage();
    }

    public void getImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==-1&&data!=null){

            Uri image_data=data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),image_data);
                bitmap_image =bitmap;
                imageView.setImageBitmap(bitmap);

//                Bitmap x=((BitmapDrawable)imageView.getDrawable()).getBitmap();

                }catch (Exception e){
                e.printStackTrace();
            }
       }

    }

    public void submit_the_story(){
        //first we submit the image to firebase storage
        //on successfull submission we take the image link
        //then we create the story and then we submit the story to the firebase database
        //then we refresh the view model
//        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        uuid = UUID.randomUUID().toString();
        Bitmap bitmap= bitmap_image;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] image_out=baos.toByteArray();

        final FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference mountainsRef=storage.getReference("image4.jpg");
        final UploadTask uploadTask = mountainsRef.putBytes(image_out);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getActivity().getApplicationContext(), "Could not upload image", Toast.LENGTH_SHORT).show();
                Log.i("info123",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 //now we need to get the download url of our image
                //then we create the story and then we submit the story to the firebase database
                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //then we create the story and then we submit the story to the firebase database
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference=database.getReference().child("story").child(uuid);
                        String story_text=getResources().getString(R.string.demo_story_text);
                        Firebase_our_story new_story=new  Firebase_our_story(FirebaseAuth.getInstance().getCurrentUser().getEmail(),uri.toString(),"night light",story_text);
                        databaseReference.setValue(new_story).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "New Story Added", Toast.LENGTH_SHORT).show();
                                NavDirections navDirections=NewStoryDirections.actionNewStoryToAllStory();
                                Navigation.findNavController(view).navigate(navDirections);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Could not add the new story", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Could not upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
