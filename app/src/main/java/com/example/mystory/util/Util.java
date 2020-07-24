package com.example.mystory.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystory.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Util {
    public static void setImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        Log.i("info123",url);
        //we will take this url and using the glide library we get the image and set it into the image view
        RequestOptions requestOptions=new RequestOptions().placeholder(progressDrawable).error(R.mipmap.ic_launcher_round);
        Glide.with(imageView.getContext()).setDefaultRequestOptions(requestOptions).load(url).into(imageView);
    }
    public static CircularProgressDrawable getProgressDrawable(Context context){
        CircularProgressDrawable cpd=new CircularProgressDrawable(context);
        cpd.setStrokeWidth(10f);
        cpd.setCenterRadius(50f);
        cpd.start();
        return cpd;
    }

}
