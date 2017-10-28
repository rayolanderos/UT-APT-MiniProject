package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.green.apt.connexus.controllers.UploadController;
import com.green.apt.connexus.controllers.ViewSingleController;

public class ViewSingleActivity extends AppCompatActivity {


    private ViewSingleController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single);

        controller = new ViewSingleController(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String streamName = extras.getString("SINGLE_STREAM_NAME");
        Long streamId = Long.parseLong(extras.getString("SINGLE_STREAM_ID"));
        controller.setStreamId(streamId);
        controller.setStreamName(streamName);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(streamName);


    }

    @Override
    protected void onResume() {
        super.onResume();

        controller.getStreamPhotos();
    }

    public void goToStreams(View view) {
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    public void uploadAnImageClick(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("SINGLE_STREAM_ID", controller.getStreamId());
        intent.putExtra("SINGLE_STREAM_NAME", controller.getStreamName());
        startActivity(intent);
    }


}
