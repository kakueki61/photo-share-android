package com.project.photoshare.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AsycTaskLoader for decoding Bitmap from ByteArray,
 * whose purpose is to avoid to process this heavy task on UI thread.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 * @version 2.00 14/06/02 kodama-t
 */
public class BitmapDecodeLoader extends AsyncTaskLoader<Map<Uri, Bitmap>> {

    private static final String TAG = BitmapDecodeLoader.class.getSimpleName();

    private Context context;
    private List<Uri> mUris;

    public BitmapDecodeLoader(Context context, Uri uri) {
        super(context);

        this.context = context;

        List<Uri> uris = new ArrayList<Uri>();
        uris.add(uri);
        this.mUris = uris;
    }

    public BitmapDecodeLoader(Context context, List<Uri> uris) {
        super(context);

        this.context = context;
        this.mUris = uris;
    }

    @Override
    public Map<Uri, Bitmap> loadInBackground() {
        Log.d(TAG, "loadInBackground");

        BitmapFactory.Options options = new BitmapFactory.Options();

        InputStream inputStream = null;
        int imageHeight;
        int imageWidth;
        Uri uri;
        Bitmap bitmap = null;
        Map<Uri, Bitmap> bitmapMap = new HashMap<Uri, Bitmap>();

        for (int i = 0; i < mUris.size(); i++) {
            options.inJustDecodeBounds = true;

            uri = mUris.get(i);
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream, null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                Utils.closeStream(inputStream);
            }

            imageHeight = options.outHeight;
            imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            Log.i(TAG, "height: " + imageHeight + ", width: " + imageWidth + ", MimeType: " + imageType);

            options.inSampleSize = ImageUtils.calcInSampleSize(imageHeight, imageWidth);
            options.inJustDecodeBounds = false;

            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                Utils.closeStream(inputStream);
            }
            bitmapMap.put(uri, bitmap);
        }

        return bitmapMap;
    }
}
