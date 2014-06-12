package com.project.photoshare.api;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <p>Request class to post multipart contents.</p>
 *
 * Created by TakuyaKodama on 14/06/08.
 */
public class MultipartJsonRequest extends JsonRequest<JSONObject> {
    private static final String TAG = MultipartJsonRequest.class.getSimpleName();

    private Map<String, String> mStringParams;
    private Map<String, ?> mBinaryParams;
    private MultipartEntityBuilder mMultipartEntityBuilder = MultipartEntityBuilder.create();

    public MultipartJsonRequest(String url, Map<String, String> stringParams, Map<String, ?> bynaryParams,
                                Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null, listener, errorListener);

        mStringParams = stringParams;
        mBinaryParams = bynaryParams;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        mMultipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mMultipartEntityBuilder.setBoundary("___________________" + Long.toString(System.currentTimeMillis()));
        mMultipartEntityBuilder.setCharset(Consts.UTF_8);

        for (Map.Entry<String, String> entry : mStringParams.entrySet()) {
            mMultipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, ?> entry : mBinaryParams.entrySet()) {
            ContentType imageContentType = ContentType.create("image/jpeg");

            if (entry.getValue() instanceof byte[]) {
                Log.d(TAG, "entry.getValue() => byte[]");
                mMultipartEntityBuilder.addBinaryBody("uploadFiles", (byte[]) entry.getValue(), imageContentType, entry.getKey());
            } else if (entry.getValue() instanceof File) {
                Log.d(TAG, "entry.getValue() => File");
                mMultipartEntityBuilder.addBinaryBody("uploadFiles", (File) entry.getValue(), imageContentType, entry.getKey());
            } else if (entry.getValue() instanceof InputStream) {
                Log.d(TAG, "entry.getValue() => InputStream");
                Log.d(TAG, "key: " + entry.getKey());
                mMultipartEntityBuilder.addBinaryBody("uploadFiles[]", (InputStream) entry.getValue(), imageContentType, entry.getKey());
            }
        }
    }

    @Override
    public String getBodyContentType() {
        return mMultipartEntityBuilder.build().getContentType().getValue();
    }

    public HttpEntity getEntity() {

        return mMultipartEntityBuilder.build();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        byte[] responseData = response.data;
        try {
            String jsonString = new String(responseData, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, new String(responseData));
            return Response.error(new ParseError(e));
        }
    }
}
