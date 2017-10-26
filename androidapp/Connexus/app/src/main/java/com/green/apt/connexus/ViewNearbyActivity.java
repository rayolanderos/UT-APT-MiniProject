package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.green.apt.connexus.R;
import com.green.apt.connexus.controllers.ViewNearbyController;
import com.green.apt.connexus.controllers.ViewSearchController;

public class ViewNearbyActivity extends AppCompatActivity {

    private ViewNearbyController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby);

        controller = new ViewNearbyController(this);
        controller.runSearch();
    }

    public void viewNext(View view){

        controller.increaseOffset(8);
        controller.runSearch();
    }

    public void goToStreams(View view) {
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }
}
