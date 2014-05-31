package com.project.photoshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Tab Fragment for download page screen.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/04/06 kodama-t
 */
public class DownloadPageFragment extends Fragment {

    private static final String TAG = DownloadPageFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_fragment, container, false);

        EditText editText = (EditText) view.findViewById(R.id.edit_text);

        view.findViewById(R.id.button_fetch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button_fetch !!!!!!!");
            }
        });

        return view;
    }
}
