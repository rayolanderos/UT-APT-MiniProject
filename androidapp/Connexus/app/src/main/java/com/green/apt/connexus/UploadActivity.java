package com.green.apt.connexus;

import android.animation.StateListAnimator;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.green.apt.connexus.controllers.UploadController;

public class UploadActivity extends AppCompatActivity {

    private UploadController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Long streamId = intent.getLongExtra("SINGLE_STREAM_ID", 0);
        String streamName = intent.getStringExtra("SINGLE_STREAM_NAME");

        setContentView(R.layout.activity_upload);

        controller = new UploadController(this, streamId);
        displayStreamName(streamName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayStreamName(String streamName) {
        TextView titleView = (TextView) findViewById(R.id.streamTitle);
        titleView.setText(streamName);
    }

    public void enableUploadButton(boolean enabled) {
        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setEnabled(enabled);
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
