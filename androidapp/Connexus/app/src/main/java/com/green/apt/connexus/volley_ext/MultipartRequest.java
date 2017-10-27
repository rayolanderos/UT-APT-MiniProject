package com.green.apt.connexus.volley_ext;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.e;

/**
 * Created by memo on 26/10/17.
 */

public class MultipartRequest extends Request<String> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private final InputStream mImageFile;
    private Map<String, String> mParams;
    private String mFileFieldName;
    private String mFilename;
    private String mBodyContentType;
    private String mMimeType;

    public MultipartRequest(String url, final Map<String, String> params, InputStream imageFile, String filename, String mimeType, String fileFieldName, Response.ErrorListener errorListener, Response.Listener<String> listener ){
        super(Method.POST, url, errorListener);

        mListener = listener;
        mImageFile = imageFile;
        mParams = params;
        mFileFieldName = fileFieldName;
        mFilename = filename;
        mMimeType = mimeType;

        buildMultipartEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Connection", "Keep-Alive");
        headers.put("X-Requested-With", "XMLHTTPRequest");
        headers.put("User-Agent", "ConnexusAndroidApp");
        return headers;
    }

    private void buildMultipartEntity(){
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            mBuilder.addTextBody(entry.getKey(), entry.getValue());
        }

        mBuilder.addBinaryBody(mFileFieldName, mImageFile, ContentType.create(mMimeType), mFilename);
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    @Override
    public String getBodyContentType(){
        return mBodyContentType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            HttpEntity entity = mBuilder.build();
            mBodyContentType = entity.getContentType().getValue();
            entity.writeTo(bos);
        } catch (IOException e) {
            e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
