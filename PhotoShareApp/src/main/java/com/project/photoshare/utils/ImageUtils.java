package com.project.photoshare.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Util class for manipulating images.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 14/03/28 kodama-t
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    // とりあえず決め打ち！
    // Because Android devices are required to support this texture size to use OpenGL.
    // ref: http://androidadvent.blogspot.com/2011/12/2.html
    public static final int MAX_GL_TEXTURE_HEIGHT = 2048;
    public static final int MAX_GL_TEXTURE_WIDTH = 2048;
    public static final float MAX_BITMAP_WIDTH = 500;
    public static final float MAX_BITMAP_HEIGHT = 500;

    public static int calcInSampleSize(int imageHeight, int imageWidth) {
        int inSampleSize = 1;
        if(imageHeight > MAX_GL_TEXTURE_HEIGHT || imageWidth > MAX_GL_TEXTURE_WIDTH) {
            if(imageHeight > imageWidth) {
                inSampleSize = (int) Math.ceil((float) imageHeight / (float) MAX_GL_TEXTURE_HEIGHT);
            } else {
                inSampleSize = (int) Math.ceil((float) imageWidth / (float) MAX_GL_TEXTURE_WIDTH);
            }
        }
        Log.i(TAG, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public static byte[] getResizedByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        Bitmap resizedBitmap = resize(bitmap);
        Log.i(TAG, "resizedWidth: " + resizedBitmap.getWidth() + ", resizedHeight: " + resizedBitmap.getHeight());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap resize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min( MAX_BITMAP_WIDTH / width, MAX_BITMAP_HEIGHT / height);

        Log.i(TAG, "width: " + width + ", height: " + height);
        Log.i(TAG, "scale: " + scale);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
