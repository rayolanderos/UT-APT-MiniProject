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

    private UploadController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        String streamId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        controller = new UploadController(this, streamId);


        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.handleActivityResult(requestCode, resultCode, data);
    }

    public void chooseFromLibraryClick(View view) {
        controller.selectFromLibrary();
    }

    public void useCameraClick(View view) {
        controller.launchCameraActivity();
    }

    public void uploadBtnClick(View view) {
        controller.uploadSelectedImage();
    }

}
