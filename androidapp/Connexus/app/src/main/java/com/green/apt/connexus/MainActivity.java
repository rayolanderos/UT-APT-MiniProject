package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.green.apt.connexus.UPLOAD_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void uploadAPictureClick(View view) {
        Intent intent  = new Intent(this, UploadActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        startActivity(intent);
    }
}
