package com.project.photoshare.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by TakuyaKodama on 14/06/19.
 */
public class PostImageErrorListener implements Response.ErrorListener {

    private static final String TAG = PostImageErrorListener.class.getSimpleName();

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
