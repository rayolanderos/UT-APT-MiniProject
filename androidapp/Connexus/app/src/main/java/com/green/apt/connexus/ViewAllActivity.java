package com.green.apt.connexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";
    private static String relativeUrl = "view_all";
    private static String streams = "All Streams";
    private static List<String> streamCoverUrls = new ArrayList<>();
    private static List<Long> streamIds = new ArrayList<>();
    private static List<String> streamNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        this.getAllStreams();
    }

    public void goToSubscribed(View view) {
        Intent intent = new Intent(this, ViewSubscribedActivity.class);
        startActivity(intent);
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
                stream_id = jsonArr.getJSONObject(i).getLong("id");
                stream_name = jsonArr.getJSONObject(i).getString("name");
                streamCoverUrls.add(cover);
                streamIds.add(stream_id);
                streamNames.add(stream_name);
                Log.d("ViewAllActivity","Parsed: Image #"+ i +"-- Cover: "+ cover + " -- name: " + stream_name + " -- id "+ stream_id);
            }

        }
        catch(Exception e){
            Log.d("ViewAllActivity", "Errored Streams: " + streams);
            Log.e("ViewAllActivity", e.getMessage());
        }

    }

    private String getAllStreams(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =getAbsoluteUrl(relativeUrl);

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

                        gridview.setOnItemClickListener(new OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Toast.makeText(ViewAllActivity.this, "" + streamNames.get(position),
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewSingleActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("SINGLE_STREAM_NAME", streamNames.get(position));
                                extras.putString("SINGLE_STREAM_ID", streamIds.get(position).toString());
                                intent.putExtras(extras);
                                startActivity(intent);
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
