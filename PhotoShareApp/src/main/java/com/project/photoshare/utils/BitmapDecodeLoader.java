package com.project.photoshare.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * AsycTaskLoader for decoding Bitmap from ByteArray,
 * whose purpose is to avoid to process this heavy task on UI thread.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 * @version 2.00 14/06/02 kodama-t
 */
public class BitmapDecodeLoader extends AsyncTaskLoader<Bitmap> {

    private static final String TAG = BitmapDecodeLoader.class.getSimpleName();

    private Context context;
    private Uri uri;

    public BitmapDecodeLoader(Context context, Uri uri) {
        super(context);

        this.context = context;
        this.uri = uri;
    }

    @Override
    public Bitmap loadInBackground() {
        Log.d(TAG, "loadInBackground");

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(this.uri);
            BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            Utils.closeStream(inputStream);
        }

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        Log.i(TAG, "height: " + imageHeight + ", width: " + imageWidth + ", MimeType: " + imageType);

        options.inSampleSize = ImageUtils.calcInSampleSize(imageHeight, imageWidth);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = null;
        try {
            inputStream = context.getContentResolver().openInputStream(this.uri);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            Utils.closeStream(inputStream);
        }

        return bitmap;
    }
}
