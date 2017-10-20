package com.green.apt.connexus.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * Created by rayolanderos on 10/18/17.
 */

public class ViewAllController extends BaseController {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";
    private static String relativeUrl = "view_all";

    private String getAllStreams(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url =getAbsoluteUrl(relativeUrl);
        String response = "";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        response = "Response is: "+ response.substring(0,500);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //response = "That didn't work!";
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }



}
