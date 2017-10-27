package com.green.apt.connexus.core;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.green.apt.connexus.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by memo on 24/10/17.
 */

public class ImageHelper {

    public static void setImageFromUrl(ImageView view, String imageUrl) {
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        InputStream inputStream = getInputStream(view.getContext().getContentResolver(), imageUrl);

        if (inputStream == null) {
            System.err.printf("Impossible to find %s", imageUrl);
        }

        BitmapFactory.decodeStream(inputStream, null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        // Need to refresh the stream
        inputStream = getInputStream(view.getContext().getContentResolver(), imageUrl);

        if (inputStream == null) {
            System.err.printf("Impossible to open %s", imageUrl);
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions);
        view.setImageBitmap(bitmap);
    }

    public static InputStream getInputStream(ContentResolver resolver, String url) {
        try {
            return resolver.openInputStream(Uri.parse(url));
        } catch (IOException ioe) {
            return null;
        }
    }
}
