package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;
import com.green.apt.connexus.UploadActivity;
import com.green.apt.connexus.core.ImageHelper;

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
    private String streamId = null;

    public UploadController(UploadActivity activity, String streamId) {
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

    private static final String CONNEXUS_URL = "http://apt-miniproject-greenteam-v2.appspot.com";

    public void uploadSelectedImage() {
        if (photoUrlToUpload != null) {
            String url = String.format("%s/generate_upload_url", CONNEXUS_URL);

            System.out.println(url);


        }
    }



}
