package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;
import com.green.apt.connexus.UploadActivity;
import com.green.apt.connexus.ViewAllActivity;
import com.green.apt.connexus.core.ImageHelper;
import com.green.apt.connexus.volley_ext.MultipartRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.*;

/**
 * Created by memo on 23/10/17.
 */

public class UploadController extends BaseController{
    private UploadActivity activity;

    public static final String TAKEN_PICTURE_URL = "Picture_Url";
    public static final String DISPLAY_MESSAGE = "com.green.apt.connexus.Library";
    public static final String CAMERA_MESSAGE = "com.green.apt.connexus.OpenCamera";

    static final int USER_PICTURE_TAKEN = 45;
    static final int USER_PICTURE_SELECTED = 48;

    private String photoUrlToUpload = null;
    private Long streamId = null;

    public UploadController(UploadActivity activity, Long streamId) {
        super(activity);
        this.activity = activity;
        this.streamId = streamId;
        activity.enableUploadButton(false);
    }

    public void launchCameraActivity() {
        Intent intent  = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CAMERA_MESSAGE, "");
        getActivity().startActivityForResult(intent, USER_PICTURE_TAKEN);
    }

    public void selectFromLibrary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, USER_PICTURE_SELECTED);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView preview = (ImageView) getActivity().findViewById(R.id.uploadPreview);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case USER_PICTURE_TAKEN:
                    String photoUrl = data.getStringExtra(TAKEN_PICTURE_URL);
                    photoUrlToUpload = photoUrl;
                    break;

                case USER_PICTURE_SELECTED:
                    Uri uri = data.getData();
                    photoUrlToUpload = uri.toString();
                    break;

            }
            activity.enableUploadButton(true);
            ImageHelper.setImageFromUrl(preview, photoUrlToUpload);
        }

        if (resultCode == RESULT_CANCELED) {
            Intent i = new Intent(getActivity(), ViewAllActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(i);
        }
    }

    public void uploadSelectedImage() {
        if (photoUrlToUpload != null) {
            String url = getAbsoluteUrl("generate_upload_url");
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            String mimeType = getActivity().getContentResolver().getType(Uri.parse(photoUrlToUpload));

                            InputStream stream = ImageHelper.getInputStream(getActivity().getContentResolver(), photoUrlToUpload);
                            String uploadUrl = response;
                            uploadFile(stream, mimeType, streamId, uploadUrl, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                }
                            });

                        }
                    }
                    , new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.add(request);
        }
    }

    private void uploadFile(InputStream media, String mimeType, Long streamId, String uploadUrl, final Response.Listener<String> completion) {

        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + getExtension(mimeType);
        Map<String,String> params = new HashMap<>();
        params.put("stream_id", streamId.toString());


        final String reqUrl = uploadUrl;

        MultipartRequest imageUploadReq = new MultipartRequest(reqUrl, params, media, fileName, mimeType, "file",
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Multipart Request Url: ", reqUrl);
                        Log.d("Multipart ERROR", "error => " + error.toString());
                        completion.onResponse(error.toString());
                    }
                },
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MediaSent Response", response);
                        completion.onResponse(response);
                    }
                }
        );

        imageUploadReq.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(imageUploadReq);

    }

    private String getExtension(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return ".jpg";

            case "image/gif":
                return ".gif";
        }

        return ".bmp";
    }

}
