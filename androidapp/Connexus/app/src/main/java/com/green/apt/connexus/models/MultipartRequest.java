package com.green.apt.connexus.models;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;


/**
 * Created by memo on 25/10/17.
 */

public class MultipartRequest extends Request<String> {


    public MultipartRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(String response) {

    }
}
