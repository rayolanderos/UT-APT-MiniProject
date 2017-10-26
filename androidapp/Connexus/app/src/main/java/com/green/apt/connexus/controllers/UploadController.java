package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;
import com.green.apt.connexus.UploadActivity;
import com.green.apt.connexus.core.ImageHelper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * Created by memo on 23/10/17.
 */

public class UploadController extends BaseController{


    public static final String TAKEN_PICTURE_URL = "Picture_Url";
    public static final String DISPLAY_MESSAGE = "com.green.apt.connexus.Library";
    public static final String CAMERA_MESSAGE = "com.green.apt.connexus.OpenCamera";

    static final int USER_PICTURE_TAKEN = 45;
    static final int USER_PICTURE_SELECTED = 48;

    private String photoUrlToUpload = null;
    private Long streamId = null;

    public UploadController(UploadActivity activity, Long streamId) {
        super(activity);
        this.streamId = streamId;
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
            ImageHelper.setImageFromUrl(preview, photoUrlToUpload);
        }
    }

    public void uploadSelectedImage() {
        if (photoUrlToUpload != null) {
            getUploadUrl();


        }
    }

    private void getUploadUrl() {
        String url = getAbsoluteUrl("generate_upload_url");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {

                    String type = getActivity().getContentResolver().getType(Uri.parse(photoUrlToUpload));

                    InputStream stream = ImageHelper.getInputStream(getActivity().getContentResolver(), photoUrlToUpload);
                    new UploadFile(stream, type, streamId).execute(response);
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


    public static class UploadFile extends AsyncTask<String, String, String> {
        String file_name = "";
        InputStream inputStream;
        Long streamId;
        String imageType;

        public UploadFile(InputStream stream, String imageType, Long streamId) {
            inputStream = stream;
            this.streamId = streamId;
            this.imageType = imageType;
        }


        @Override
        protected String doInBackground(String[] params) {
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024;
                URL url = new URL(params[0].replace("https:", "http:"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs &amp; Outputs.
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // Set HTTP method to POST.
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                FileInputStream fileInputStream;
                DataOutputStream outputStream;
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

                outputStream.writeBytes("Content-Disposition: form-data; name=\"stream_id\""+ lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(String.format("%d", streamId));
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadFile\"; filename=\"connexusAppPhoto\"" + lineEnd);
                outputStream.writeBytes("Content-Type: " + imageType + lineEnd);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = inputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = inputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = inputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = inputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = connection.getResponseCode();
                String result = null;
                if (serverResponseCode == 200) {
                    StringBuilder s_buffer = new StringBuilder();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        s_buffer.append(inputLine);
                    }
                    result = s_buffer.toString();
                } else {
                    System.err.println("ServerResponse: " + serverResponseCode);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                if (result != null) {
                    Log.d("result_for upload", result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file_name;
        }

    }



}
