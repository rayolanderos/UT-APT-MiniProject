package com.green.apt.connexus.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.green.apt.connexus.CameraActivity;
import com.green.apt.connexus.R;
import com.green.apt.connexus.ViewAllActivity;
import com.green.apt.connexus.core.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by memo on 23/10/17.
 */

public class CameraController extends BaseController {

    static final int REQUEST_IMAGE_CAPTURE = 42;

    private String capturedPhotoFilePath;

    public CameraController(CameraActivity activity){
        super(activity);
    }

    public void handleCameraResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            refreshPreviewImage();

        }

    }

    private void refreshPreviewImage() {
        ImageView preview = (ImageView) getActivity().findViewById(R.id.picturePreview);
        if (capturedPhotoFilePath != null) {
            ImageHelper.setImageFromUrl(preview, capturedPhotoFilePath);
        }
    }

    public void fireupCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.green.apt.connexus.fileprovider", photoFile);
                capturedPhotoFilePath = photoURI.toString();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = String.format("JPEG_%s_", timeStamp);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        Intent i = new Intent(getActivity(), ViewAllActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(i);
    }

    public void useTakenPicture() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(UploadController.TAKEN_PICTURE_URL, capturedPhotoFilePath);
        getActivity().setResult(RESULT_OK, resultIntent);
        getActivity().finish();
    }

}
