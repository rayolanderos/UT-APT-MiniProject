package com.green.apt.connexus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.green.apt.connexus.controllers.CameraController;

public class CameraActivity extends AppCompatActivity {

    CameraController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new CameraController(this);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.handleCameraResult(requestCode, resultCode, data);
    }


    public void takePictureBtnClick(View view){
        controller.fireupCamera();
    }

    public void streamsBtnClick(View view) {
        controller.goBackToStreams();
    }

    public void usePhotoBtnClick(View view) {
        controller.useTakenPicture();
    }
}
