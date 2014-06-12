package com.project.photoshare;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.laevatein.Laevatein;
import com.project.photoshare.api.ApiRequestService;
import com.project.photoshare.utils.BitmapDecodeLoader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="mailto:t.kodama61@gmail.com">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/04/03 kodama-t
 */
public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Bitmap> {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE_SELECTOR = 1001;

    private static final String TAG_FRAGMENT_UPLOAD = "upload";
    private static final String TAG_FRAGMENT_DOWNLOAD = "download";
    private static final String TAG_FRAGMENT_HISTORY = "history";
    private static final String TAG_FRAGMENT_MYPAGE = "mypage";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab uploadTab = actionBar.newTab()
                .setText(R.string.tab_label_upload)
                .setTabListener(new MainTabListener<UploadPageFragment>(this, TAG_FRAGMENT_UPLOAD, UploadPageFragment.class));
        ActionBar.Tab dowloadTab = actionBar.newTab()
                .setText(R.string.tab_label_download)
                .setTabListener(new MainTabListener<DownloadPageFragment>(this, TAG_FRAGMENT_DOWNLOAD, DownloadPageFragment.class));
        ActionBar.Tab historyTab = actionBar.newTab()
                .setText(R.string.tab_label_history)
                .setTabListener(new MainTabListener<HistoryPageFragment>(this, TAG_FRAGMENT_HISTORY, HistoryPageFragment.class));
        ActionBar.Tab mypageTab = actionBar.newTab()
                .setText(R.string.tab_label_mypage)
                .setTabListener(new MainTabListener<MyPageFragment>(this, TAG_FRAGMENT_MYPAGE, MyPageFragment.class));

        actionBar.addTab(uploadTab);
        actionBar.addTab(dowloadTab);
        actionBar.addTab(historyTab);
        actionBar.addTab(mypageTab);

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_IMAGE_SELECTOR) {
            List<Uri> selected = Laevatein.obtainResult(data);
            for(int i = 0; i < selected.size(); i++) {
                Log.d(TAG, i + ": " + selected.get(i));
            }

            // display selected image
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", selected.get(0));
            getSupportLoaderManager().restartLoader(0, bundle, MainActivity.this);

            // upload selected images
            Fragment uploadFragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_UPLOAD);
            EditText editText = (EditText) uploadFragment.getView().findViewById(R.id.edit_text);

            ApiRequestService requestService = new ApiRequestService(getApplicationContext());
            requestService.postImages(editText.getText().toString(), selected);
        }
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        BitmapDecodeLoader bitmapDecodeLoader
                = new BitmapDecodeLoader(getApplicationContext(), args.<Uri>getParcelable("uri"));
        bitmapDecodeLoader.forceLoad();
        return bitmapDecodeLoader;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        Log.d(TAG, "onLoadFinished");
        Fragment uploadFragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_UPLOAD);
        ImageView imageView = (ImageView) uploadFragment.getView().findViewById(R.id.image_view);
        imageView.setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        //TODO
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
            Toast.makeText(mActivity, "Reselected!!", Toast.LENGTH_LONG).show();
        }
    }
}
