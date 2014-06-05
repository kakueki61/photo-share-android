package com.project.photoshare.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TakuyaKodama on 14/06/05.
 */
public class Utils {

    public static void closeStream(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
