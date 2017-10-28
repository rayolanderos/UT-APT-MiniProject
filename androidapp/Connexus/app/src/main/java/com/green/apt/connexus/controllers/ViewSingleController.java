package com.green.apt.connexus.controllers;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.ImageAdapter;
import com.green.apt.connexus.R;
import com.green.apt.connexus.ViewSingleActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memo on 25/10/17.
 */

public class ViewSingleController extends BaseController {

    private static String relativeUrl = "view";
    private String stream = "";
    private String streamName = "Single Stream";
    private Long streamId = null;
    private List<String> streamPhotos = new ArrayList<>();

    public ViewSingleController(ViewSingleActivity activity) {
        super(activity);
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public Long getStreamId() {return this.streamId; }

    public void setStreamName(String name) {
        this.streamName = name;
    }

    public String getStreamName() {
        return streamName;
    }

    private void parseStream(){
        try {
            Object object=null;
            String image = "";
            JSONObject json = new JSONObject(stream);
            JSONArray jsonArr = json.getJSONArray("photos");
            Log.d("ViewAllActivity", "Stream: " + stream);

            streamPhotos.clear();
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

    public String getStreamPhotos(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = getAbsoluteAPIUrl(relativeUrl) + "?id=" + streamId + "&offset=0&limit=300";

        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        stream = response.substring(0);
                        parseStream();
                        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), streamPhotos));

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
}
