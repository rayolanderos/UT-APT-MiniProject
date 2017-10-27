package com.green.apt.connexus.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.green.apt.connexus.ViewAllActivity;
import com.green.apt.connexus.ViewSingleActivity;

import org.json.JSONArray;


/**
 * Created by rayolanderos on 10/18/17.
 */

public class ViewAllController extends BaseController {

    private static String streams = "All Streams";
    private static String relativeUrl = "view_all";
    private static List<String> streamCoverUrls = new ArrayList<>();
    private static List<Long> streamIds = new ArrayList<>();
    private static List<String> streamNames = new ArrayList<>();

    public ViewAllController(ViewAllActivity activity) {
        super(activity);
    }

    public String getAllStreams(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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

}
