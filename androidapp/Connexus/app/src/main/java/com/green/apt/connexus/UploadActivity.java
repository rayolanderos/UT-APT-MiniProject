package com.green.apt.connexus;

import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.green.apt.connexus.controllers.UploadController;

public class UploadActivity extends AppCompatActivity {

    public static final String DISPLAY_MESSAGE = "com.green.apt.connexus.Library";
    public static final String CAMERA_MESSAGE = "com.green.apt.connexus.OpenCamera";
    private UploadController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new UploadController(this);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void chooseFromLibraryClick(View view) {
//        Intent intent  = new Intent(this, DisplayMessageActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }

    public void useCameraClick(View view) {
        Intent intent  = new Intent(this, CameraActivity.class);
        intent.putExtra(CAMERA_MESSAGE, "");
        startActivity(intent);
    }

}
