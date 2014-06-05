package com.project.photoshare.utils;

import android.util.Log;

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
}
