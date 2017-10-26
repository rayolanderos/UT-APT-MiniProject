package com.green.apt.connexus.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.green.apt.connexus.ImageAdapter;
import com.green.apt.connexus.R;
import com.green.apt.connexus.ViewNearbyActivity;
import com.green.apt.connexus.ViewSingleActivity;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rayolanderos on 10/25/17.
 */

public class ViewNearbyController extends BaseController {
    private static String relativeUrl = "nearby";
    private static String photos = "Nearby Results";
    private static String lat = "";
    private static String lon = "";
    private static Double latitude;
    private static Double longitude;

    private GoogleApiClient mGoogleApiClient;

    private Location lastLocation;

    private static List<String> photoUrls = new ArrayList<>();
    private static List<Long> streamIds = new ArrayList<>();

    private int offset = 0;
    private ViewNearbyActivity viewNearbyActivity;


    public ViewNearbyController(ViewNearbyActivity activity, Double latitude, Double longitude) {
        super(activity);
        viewNearbyActivity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int increaseOffset(int delta) {
        offset += delta;
        return offset;
    }

    public void runSearch(){

        // Find lat and lon of user
        lat = "30"; //latitude.toString();
        lon =  "30"; //longitude.toString();

        photoUrls.clear();
        streamIds.clear();

        this.getNearbyPhotos();
    }

    private void parsePhotos(){

        try {
            Object object=null;
            String cover = "";
            Long stream_id;
            String stream_name = "";
            JSONArray jsonArr = new JSONArray(photos);
            for (int i = 0; i < jsonArr.length(); i++)
            {
                cover = jsonArr.getJSONObject(i).getString("url");
                stream_id = Long.parseLong(jsonArr.getJSONObject(i).getString("stream_id"));
                photoUrls.add(cover);
                streamIds.add(stream_id);
            }
        }
        catch(Exception e){
            Log.d("ViewNearbyController", "Errored Streams: " + photos);
            Log.e("ViewNearbyController", e.getMessage());
        }

    }

    private String getNearbyPhotos(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // hardcoded user id for now
        String url =getAbsoluteUrl(relativeUrl)+ "?limit=9&offset=" + String.valueOf(offset)+ "&lat=" + lat + "&lon=" + lon;
        Log.d("ViewNearbyController", "Request URL: " + url);
        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        photos = response.substring(0);
                        Log.d("ViewSearchActivity", "Response: " + photos);
                        parsePhotos();
                        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), photoUrls));

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), ViewSingleActivity.class);
                                Bundle extras = new Bundle();
                                //TO DO: Index photo model to include stream name as well
                                extras.putString("SINGLE_STREAM_NAME", streamIds.get(position).toString());
                                extras.putString("SINGLE_STREAM_ID", streamIds.get(position).toString());
                                intent.putExtras(extras);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewNearbyController","response: "+ "Response is empty or failed.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return response;
    }

}
