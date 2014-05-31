package com.project.photoshare;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="mailto:t.kodama61@gmail.com">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/04/03 kodama-t
 */
public class MainActivity extends FragmentActivity {

    public static final int REQUEST_IMAGE_SELECTOR = 1001;

    private static final String TAG = MainActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab uploadTab = actionBar.newTab()
                .setText(R.string.tab_label_upload)
                .setTabListener(new MainTabListener<UploadPageFragment>(this, "upload", UploadPageFragment.class));
        ActionBar.Tab dowloadTab = actionBar.newTab()
                .setText(R.string.tab_label_download)
                .setTabListener(new MainTabListener<DownloadPageFragment>(this, "download", DownloadPageFragment.class));
        ActionBar.Tab historyTab = actionBar.newTab()
                .setText(R.string.tab_label_history)
                .setTabListener(new MainTabListener<HistoryPageFragment>(this, "history", HistoryPageFragment.class));
        ActionBar.Tab mypageTab = actionBar.newTab()
                .setText(R.string.tab_label_mypage)
                .setTabListener(new MainTabListener<MyPageFragment>(this, "mypage", MyPageFragment.class));

        actionBar.addTab(uploadTab);
        actionBar.addTab(dowloadTab);
        actionBar.addTab(historyTab);
        actionBar.addTab(mypageTab);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_IMAGE_SELECTOR) {
            Log.d(TAG, data.getData().toString());
        }
    }

    private class MainTabListener<T extends Fragment> implements ActionBar.TabListener {

        private FragmentActivity mActivity;
        private String mTag;
        private Class<T> mClass;
        private Fragment mFragment;

        public MainTabListener(FragmentActivity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            // Try to retrieve Fragment from FragmentManager.
            mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
        }

        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
         *            during a tab switch. The previous tab's unselect and this tab's select will be
         *            executed in a single transaction. This FragmentTransaction does not support
         */
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // NullPointerException occurs if mFragment is null even though ft isn't null.
            // see: http://yan-note.blogspot.jp/2012/11/android-actionbarfragmenttabandroid-2x.html
            if(mFragment != null) {
                if(mFragment.isDetached()) {
                    // only when Fragment is detached, attaches the Fragment
                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction().attach(mFragment).commit();
                }
            } else {
                // if Fragment is null, instantiate Fragment.
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_container, mFragment, mTag).commit();
            }
        }

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
         *            during a tab switch. This tab's unselect and the newly selected tab's select
         *            will be executed in a single transaction. This FragmentTransaction does not
         */
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // NullPointerException occurs if mFragment is null even though ft isn't null.
            // see: http://yan-note.blogspot.jp/2012/11/android-actionbarfragmenttabandroid-2x.html
            if(mFragment != null) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
        }

        /**
         * Called when a tab that is already selected is chosen again by the user.
         * Some applications may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         * @param ft  A {@link android.app.FragmentTransaction} for queuing fragment operations to execute
         *            once this method returns. This FragmentTransaction does not support
         */
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }
}
