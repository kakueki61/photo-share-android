package com.project.photoshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Tab Fragment for download page screen.
 *
 * @author <a href="mailto:t.kodama61@gmail.com">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/06/01 kodama-t
 */
public class MyPageFragment extends Fragment {

    private static final String TAG = MyPageFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }
}
