
package pl.tokajiwines;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;

public class App extends Application {

    public static boolean debug_mode = false;// emulating position
    public static String fileAbsPath = Environment.getExternalStorageDirectory() + "/Tokaji Wines/";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void downloadImagesToSdCard(final String downloadUrl, Context ctx,
            final ImageView img) {

        final File direct = new File(Environment.getExternalStorageDirectory() + "/Tokaji Wines");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setTitle("Downloading images")
                .setDescription("Images for Application")
                .setDestinationInExternalPublicDir(
                        "/Tokaji Wines",
                        downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1,
                                downloadUrl.length()));

        mgr.enqueue(request);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Bitmap myBitmap = BitmapFactory.decodeFile(direct.getPath()
                        + "/"
                        + downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1,
                                downloadUrl.length()));

                img.setImageBitmap(myBitmap);

            }
        }, 1200);

    }

}
