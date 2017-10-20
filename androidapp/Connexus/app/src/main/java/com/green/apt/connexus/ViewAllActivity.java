package com.green.apt.connexus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.List;

import static android.R.attr.data;

public class ViewAllActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";
    private static String relativeUrl = "view_all";
    private static String streams = "All Streams";
    private static List<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        this.getAllStreams();
    }

    private void parseStreams(){

        try {
            Object object=null;
            String cover = "";
            JSONArray jsonArr = new JSONArray(streams);

            for (int i = 0; i < jsonArr.length(); i++)
            {

                //JSONObject jsonObj = jsonArr.getJSONObject(i); // json.toString()
                Log.d("ViewAllActivity", "1. It: "+i+ " json: "+jsonArr.toString());

                cover = jsonArr.getJSONObject(i).getString("cover_url");
                Log.d("ViewAllActivity","2. Image #"+ i +"-- URL: "+ cover);

                imageUrls.add(cover);
                Log.d("ViewAllActivity","3. Image #"+ i +"-- URL: "+ cover);
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
        Log.d("ViewAllActivity","URL: "+ url);
        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("ViewAllActivity","response: "+ response.substring(0));
                        streams = response.substring(0);
                        parseStreams();
                        GridView gridview = (GridView) findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getApplicationContext(), imageUrls));

                        gridview.setOnItemClickListener(new OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Toast.makeText(ViewAllActivity.this, "" + position,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewAllActivity","response: "+ "that didn't work");
                streams = "That didn't work!";
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
