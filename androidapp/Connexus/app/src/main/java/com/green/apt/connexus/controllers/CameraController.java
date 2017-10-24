package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by memo on 23/10/17.
 */

public class CameraController extends BaseController {

    static final int REQUEST_IMAGE_CAPTURE = 42;

    CameraActivity activity;

    private String capturedPhotoFilePath;

    public CameraController(CameraActivity activity){
        this.activity = activity;
    }

    public void handleCameraResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            refreshPreviewImage();
        }

    }

    private void refreshPreviewImage() {
        ImageView preview = (ImageView) activity.findViewById(R.id.picturePreview);
        int targetW = preview.getWidth();
        int targetH = preview.getHeight();

        if (capturedPhotoFilePath != null) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(capturedPhotoFilePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(capturedPhotoFilePath, bmOptions);
            preview.setImageBitmap(bitmap);
        }
    }

    public void fireupCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {

            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, "com.green.apt.connexus.fileprovider", photoFile);
                capturedPhotoFilePath = photoFile.getAbsolutePath();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = String.format("JPEG_%s_", timeStamp);
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            return image;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public void goBackToStreams() {

    }

    public void useTakenPicture() {

    }
}
