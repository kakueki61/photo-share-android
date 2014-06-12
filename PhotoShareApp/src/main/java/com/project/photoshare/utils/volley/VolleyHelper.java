package com.project.photoshare.utils.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.photoshare.api.InputStreamRequest;
import com.project.photoshare.api.MultipartHurlStack;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/02/06 kodama-t
 */
public class VolleyHelper {

    private static RequestQueue mRequestQueue;
    private static RequestQueue mMultipartRequestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context, new MultipartHurlStack());
        }
        return mRequestQueue;
    }

    public static RequestQueue getMultipartRequestQueue(Context context) {
        if (mMultipartRequestQueue == null) {
            mMultipartRequestQueue = Volley.newRequestQueue(context, new HttpStack() {
                @Override
                public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
                    return null;
                }
            });
        }
        return mMultipartRequestQueue;
    }

    public static void sendInputStreamRequest(Context context, String url) {
        sendInputStreamRequest(context, url, null, null);
    }

    public static void sendInputStreamRequest(Context context, String url,
                                              Response.Listener<InputStream> responseListener, Response.ErrorListener errorListener) {
        InputStreamRequest inputStreamRequest = new InputStreamRequest(url, responseListener, errorListener);

        getRequestQueue(context).add(inputStreamRequest);
    }

    public static void postJsonRequest(Context context, String url,
            Response.Listener<JSONObject> resoponseListener, Response.ErrorListener errorListener) {

        getRequestQueue(context).add(
                new JsonObjectRequest(Request.Method.POST, url, null, resoponseListener, errorListener)
        );
    }
}
