package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by memo on 23/10/17.
 */

public class CameraController extends BaseController {

    static final int REQUEST_IMAGE_CAPTURE = 42;

    CameraActivity activity;

    public CameraController(CameraActivity activity){
        this.activity = activity;
    }

    public void handleCameraResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            setPreviewImage((Bitmap) extras.get("data"));
        }

    }

    private void setPreviewImage(Bitmap bitmap) {
        ImageView preview = (ImageView) activity.findViewById(R.id.picturePreview);
        preview.setImageBitmap(bitmap);
    }

    public void fireupCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void goBackToStreams() {

    }

    public void useTakenPicture() {

    }
}
