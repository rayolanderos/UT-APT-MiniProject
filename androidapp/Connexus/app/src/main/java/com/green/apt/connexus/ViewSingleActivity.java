package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSingleActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";
    private static String relativeUrl = "view";
    private static String stream = "";
    private static String streamName = "Single Stream";
    private static Long streamId = null;

    private static List<String> streamPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        streamName = extras.getString("SINGLE_STREAM_NAME");
        streamId = Long.parseLong(extras.getString("SINGLE_STREAM_ID"));

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(streamName);

        this.getStreamPhotos();
    }

    public void backToStreams(View view) {
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    private void parseStream(){

        try {
            Object object=null;
            String image = "";
            JSONObject json = new JSONObject(stream);
            JSONArray jsonArr = json.getJSONArray("photos");
            Log.d("ViewAllActivity", "Stream: " + stream);

            for (int i = 0; i < jsonArr.length(); i++)
            {
                image = jsonArr.getString(i);
                streamPhotos.add(image);
                Log.d("ViewAllActivity", "Photo: " + image);
            }

        }
        catch(Exception e){
            Log.d("ViewAllActivity", "Errored Stream: " + stream);
            Log.e("ViewAllActivity", e.getMessage());
        }

    }

    private String getStreamPhotos(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =getAbsoluteUrl(relativeUrl) + "?id=" + streamId + "&offset=0&limit=300";

        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        stream = response.substring(0);
                        parseStream();
                        GridView gridview = (GridView) findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getApplicationContext(), streamPhotos));

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewAllActivity","response: "+ "Response is empty or failed.");
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
