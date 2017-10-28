package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.green.apt.connexus.controllers.ViewSearchController;

public class ViewSearchActivity extends AppCompatActivity {

    private ViewSearchController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_search);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String query = intent.getStringExtra("SEARCH_QUERY");

        controller = new ViewSearchController(this);


        controller.runSearch(query);

    }

    public void updateTextFields(String query){
        // Capture the layout's TextView and set the string as its text
        TextView searchText = (TextView) findViewById(R.id.searchText);
        searchText.setText(query);

        EditText searchField = (EditText) findViewById(R.id.searchField);
        searchField.setText(query);
    }

    public void updateResultCount(int resultSize){
        TextView searchText = (TextView) findViewById(R.id.resultCt);
        searchText.setText(String.valueOf(resultSize));
    }

    public void searchAgain(View view){
        controller.hideKeyboard();
        EditText searchField = (EditText) findViewById(R.id.searchField);
        String query = searchField.getText().toString();

        controller.setOffset(0);
        controller.runSearch(query);
    }

    public void viewNext(View view){
        TextView searchText = (TextView) findViewById(R.id.searchText);
        String query = searchText.getText().toString();
        controller.increaseOffset(8);
        controller.runSearch(query);
    }


}
