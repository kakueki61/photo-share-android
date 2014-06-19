package com.project.photoshare.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.photoshare.utils.Constants;
import com.project.photoshare.utils.LogHelper;
import com.project.photoshare.utils.volley.VolleyHelper;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TakuyaKodama on 14/06/08.
 */
public class ApiRequestService {
    private static final String TAG = ApiRequestService.class.getSimpleName();

    private Context mContext;

    public ApiRequestService(Context context) {
        mContext = context;
    }

    public void postImages(String password, List<Uri> uriList, boolean isAdd,
                           Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Constants.BASE_API_URL + "image/upload";
        Map<String, String> stringParams = new HashMap<String, String>();
        Map<String, InputStream> binaryParams = new HashMap<String, InputStream>();

        stringParams.put("password", password);
        for (int i = 0; i < uriList.size(); i++) {
            Uri uri = uriList.get(i);
            try {
                InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
                binaryParams.put(uri.toString(), inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        MultipartJsonRequest multipartJsonRequest
                = new MultipartJsonRequest(url, stringParams, binaryParams, listener, errorListener);

        VolleyHelper.getRequestQueue(mContext).add(multipartJsonRequest);
    }
}
