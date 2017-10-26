package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.json.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.controllers.ViewAllController;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private ViewAllController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new ViewAllController(this);
        setContentView(R.layout.activity_view_all);
        controller.getAllStreams();
    }

    public void goToSubscribed(View view) {
        Intent intent = new Intent(this, ViewSubscribedActivity.class);
        startActivity(intent);
    }

    public void goToNearby(View view) {
        Intent intent = new Intent(this, ViewNearbyActivity.class);
        startActivity(intent);
    }

    public void searchStream(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String searchQuery = editText.getText().toString();
        Intent intent = new Intent(this, ViewSearchActivity.class);
        intent.putExtra("SEARCH_QUERY", searchQuery);
        startActivity(intent);
    }

}
