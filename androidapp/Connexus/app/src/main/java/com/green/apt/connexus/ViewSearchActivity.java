package com.green.apt.connexus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ViewSearchActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";
    private static String relativeUrl = "search";
    private static String streams = "Search Results";
    private static String searchQuery = "";
    private static List<String> streamCoverUrls = new ArrayList<>();
    private static List<Long> streamIds = new ArrayList<>();
    private static List<String> streamNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_search);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String query = intent.getStringExtra("SEARCH_QUERY");

        runSearch(query);

    }

    private void updateTextFields(String query){
        // Capture the layout's TextView and set the string as its text
        TextView searchText = (TextView) findViewById(R.id.searchText);
        searchText.setText(query);

        EditText searchField = (EditText) findViewById(R.id.searchField);
        searchField.setText(query);
    }
    private void updateResultCount(){
        TextView searchText = (TextView) findViewById(R.id.resultCt);
        int resultSize = streamCoverUrls.size();
        searchText.setText(String.valueOf(resultSize));
    }

    public void searchAgain(View view){
        this.hideKeyboard();
        EditText searchField = (EditText) findViewById(R.id.searchField);
        String query = searchField.getText().toString();

        runSearch(query);
    }

    private void runSearch(String query){
        try{
            searchQuery = URLEncoder.encode(query, "UTF-8");
        }
        catch(Exception e) {
            Log.d("ViewSearchActivity", "Errored encoding: " + query);
            Log.e("ViewSearchActivity", e.getMessage());
        }
        streamCoverUrls.clear();
        streamNames.clear();
        streamNames.clear();

        this.updateTextFields(query);

        this.getSearchStreams();
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void parseStreams(){

        try {
            Object object=null;
            String cover = "";
            Long stream_id;
            String stream_name = "";
            JSONArray jsonArr = new JSONArray(streams);
            for (int i = 0; i < jsonArr.length(); i++)
            {
                cover = jsonArr.getJSONObject(i).getString("cover_url");
                stream_id = Long.parseLong(jsonArr.getJSONObject(i).getString("id"));
                stream_name = jsonArr.getJSONObject(i).getString("name");
                streamCoverUrls.add(cover);
                streamIds.add(stream_id);
                streamNames.add(stream_name);
//                Log.d("ViewSearchActivity","Parsed: Image #"+ i +"-- Cover: "+ cover + " -- name: " + stream_name + " -- id "+ stream_id);
            }
        }
        catch(Exception e){
            Log.d("ViewSearchActivity", "Errored Streams: " + streams);
            Log.e("ViewSearchActivity", e.getMessage());
        }

    }

    private String getSearchStreams(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // hardcoded user id for now
        String url =getAbsoluteUrl(relativeUrl)+ "?s=" + searchQuery;
        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        streams = response.substring(0);
                        parseStreams();
                        GridView gridview = (GridView) findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getApplicationContext(), streamCoverUrls));

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Toast.makeText(ViewSearchActivity.this, "" + streamNames.get(position),
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewSingleActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("SINGLE_STREAM_NAME", streamNames.get(position));
                                extras.putString("SINGLE_STREAM_ID", streamIds.get(position).toString());
                                intent.putExtras(extras);
                                startActivity(intent);
                            }
                        });
                        updateResultCount();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewSearchActivity","response: "+ "Response is empty or failed.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return response;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
