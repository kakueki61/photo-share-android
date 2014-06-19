package com.project.photoshare;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.photoshare.R;
import com.project.photoshare.api.ApiRequestService;
import com.project.photoshare.listener.PostImageErrorListener;
import com.project.photoshare.listener.PostImageSuccessListener;
import com.project.photoshare.utils.LogHelper;

import org.json.JSONObject;

import java.util.List;

public class SelectIsAddLater extends ActionBarActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG_INTENT_URI = "uri";
    public static final String TAG_INTENT_PASSWORD = "password";

    private static final String TAG = SelectIsAddLater.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_is_add_later);

        Intent intent = getIntent();
        final List<Uri> uris = (List<Uri>) intent.getSerializableExtra(TAG_INTENT_URI);
        final String password = intent.getStringExtra(TAG_INTENT_PASSWORD);

        for (int i = 0; i < uris.size(); i++) {
            Log.d(TAG, i + ": " + uris.get(i));
        }

        final ApiRequestService requestService = new ApiRequestService(getApplicationContext());

        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestService.postImages(password, uris, true,
                        SelectIsAddLater.this, SelectIsAddLater.this);
            }
        });

        findViewById(R.id.button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestService.postImages(password, uris, false,
                        SelectIsAddLater.this, SelectIsAddLater.this);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_is_add_later, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(JSONObject response) {
        LogHelper.logJsonString(TAG, response);
        startActivity(new Intent(getApplicationContext(), UploadCompleted.class));
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
