package com.project.photoshare.utils.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.toolbox.ImageLoader;

/**
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/01/20 kodama-t
 */
public class LruImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mLruCache;

    public LruImageCache() {
        int maxSize = 5 * 1024 * 1024;

        mLruCache = new LruCache<String, Bitmap>(maxSize);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }
}
