package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.ImageAdapter;
import com.green.apt.connexus.R;
import com.green.apt.connexus.ViewSearchActivity;
import com.green.apt.connexus.ViewSingleActivity;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by memo on 25/10/17.
 */

public class ViewSearchController extends BaseController {

    private static String relativeUrl = "search";
    private static String streams = "Search Results";
    private static String searchQuery = "";

    private static List<String> streamCoverUrls = new ArrayList<>();
    private static List<Long> streamIds = new ArrayList<>();
    private static List<String> streamNames = new ArrayList<>();

    private int offset = 0;
    private ViewSearchActivity viewSearchActivity;


    public ViewSearchController(ViewSearchActivity activity) {
        super(activity);
        viewSearchActivity = activity;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int increaseOffset(int delta) {
        offset += delta;
        return offset;
    }

    public void runSearch(String query){
        try{
            searchQuery = URLEncoder.encode(query, "UTF-8");
        }
        catch(Exception e) {
            Log.d("ViewSearchActivity", "Errored encoding: " + query);
            Log.e("ViewSearchActivity", e.getMessage());
        }
        streamCoverUrls.clear();
        streamIds.clear();
        streamNames.clear();

        viewSearchActivity.updateTextFields(query);

        this.getSearchStreams();
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
            }
        }
        catch(Exception e){
            Log.d("ViewSearchActivity", "Errored Streams: " + streams);
            Log.e("ViewSearchActivity", e.getMessage());
        }

    }

    private String getSearchStreams(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // hardcoded user id for now
        String url = getAbsoluteAPIUrl(relativeUrl)+ "?limit=8&s=" + searchQuery + "&offset=" + String.valueOf(offset);
        Log.d("ViewSearchActivity", "Request URL: " + url);
        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        streams = response.substring(0);
                        Log.d("ViewSearchActivity", "Response: " + streams);
                        parseStreams();
                        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), streamCoverUrls));

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Toast.makeText(getActivity(), "" + streamNames.get(position),
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), ViewSingleActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("SINGLE_STREAM_NAME", streamNames.get(position));
                                extras.putString("SINGLE_STREAM_ID", streamIds.get(position).toString());
                                intent.putExtras(extras);
                                getActivity().startActivity(intent);
                            }
                        });
                        viewSearchActivity.updateResultCount(streamCoverUrls.size());
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
}
