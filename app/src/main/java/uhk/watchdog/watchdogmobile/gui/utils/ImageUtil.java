package uhk.watchdog.watchdogmobile.gui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tobous on 22. 2. 2015.
 */
public class ImageUtil {

    /**
     *
     * @param src
     * @return
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    /**
     *
     * @param url
     * @return
     */
    public static Bitmap getImageBitmapFromUrl(URL url) {
            Bitmap bm = null;
            try {
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn.getResponseCode() != 200) {
                    return bm;
                }
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                try {
                    bm = BitmapFactory.decodeStream(bis);
                }
                catch(OutOfMemoryError ex) {
                    bm = null;
                }
                bis.close();
                is.close();
            } catch (Exception e) {}

            return bm;
        }
}
