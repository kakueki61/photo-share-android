package com.project.photoshare.listener;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.project.photoshare.UploadCompleted;

import org.json.JSONObject;

/**
 * Created by TakuyaKodama on 14/06/19.
 */
public class PostImageSuccessListener implements Response.Listener<JSONObject> {

    private static final String TAG = PostImageSuccessListener.class.getSimpleName();
    private Context mContext;

    public PostImageSuccessListener(Context context) {
        mContext = context;
    }

    @Override
    public void onResponse(JSONObject response) {
        mContext.startActivity(new Intent(mContext, UploadCompleted.class));
    }
}
