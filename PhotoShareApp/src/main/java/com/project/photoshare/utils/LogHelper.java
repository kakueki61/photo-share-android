package com.project.photoshare.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Helper class for logging stuffs. You can see the logs on Logcat.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/02/06 kodama-t
 */
public class LogHelper {

    public static void logHashList(List<Map<String, String>> hashMapList) {
        for(int i = 0; i < hashMapList.size(); i++) {
            Log.i("map", i + ": ");
            Iterator<String> iterator = hashMapList.get(i).keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                Log.i(key, hashMapList.get(i).get(key));
            }
        }
    }

    public static void logJsonString(String tag, JSONObject jsonObject) {
        try {
            Log.d(tag, jsonObject.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
