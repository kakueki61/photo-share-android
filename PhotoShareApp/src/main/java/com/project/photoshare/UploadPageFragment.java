package com.project.photoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laevatein.Laevatein;
import com.laevatein.MimeType;

import java.util.List;

/**
 * Tab Fragment for uploading images screen.
 *
 * @author <a href="mailto:t.kodama61@gmail.com">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/04/06 kodama-t
 */
public class UploadPageFragment extends Fragment {

    private static final String TAG = UploadPageFragment.class.getSimpleName();
    public static final String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_fragment, container, false);

        view.findViewById(R.id.button_select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Laevatein.from(getActivity()).choose(MimeType.of(MimeType.JPEG)).forResult(MainActivity.REQUEST_IMAGE_SELECTOR);
            }
        });

        return view;
    }

    public UploadPageFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != getActivity().RESULT_OK) {
            return;
        }

        if(requestCode == MainActivity.REQUEST_IMAGE_SELECTOR) {
            List<Uri> selected = Laevatein.obtainResult(data);
            for(int i = 0; i < selected.size(); i++) {
                Log.i(TAG, i + ": " + selected.get(i));
            }
        }
    }
}
