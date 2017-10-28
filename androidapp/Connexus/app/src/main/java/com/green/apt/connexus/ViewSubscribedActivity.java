package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.green.apt.connexus.controllers.ViewSubscribedController;

public class ViewSubscribedActivity extends AppCompatActivity {

    private ViewSubscribedController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subscribed);
        controller = new ViewSubscribedController(this);
        controller.getSubscribedStreams();
    }

    public void goToStreams(View view) {
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

}
