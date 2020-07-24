package com.example.mystory.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.mystory.R;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment=getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    public void ask_permission_for_add_story(){
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String []{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                 requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }else{
                notifyNewStory(true);
            }
        }
        notifyNewStory(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                notifyNewStory(true);
            }else{
                notifyNewStory(false);
            }
        }
    }

    public void notifyNewStory(Boolean permissionValue){
            //some way to send back this value of x back to the New Story

        Fragment activeFragment=fragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if(activeFragment instanceof NewStory){
            ((NewStory)activeFragment).onPermissionResult(permissionValue);
        }

    }
}
