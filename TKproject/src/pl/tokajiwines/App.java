
package pl.tokajiwines;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.widget.ImageView;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import pl.tokajiwines.activities.BaseActivity;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SimpleLocation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class App extends Application {

    public static boolean debug_mode = false;// emulating positionublic static String fileAbsPath = Environment.getExternalStorageDirectory() + "/Tokaji Wines/";
    public static BaseActivity sMapAct = null;
    LocationClient mLocationClient;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static Bitmap downloadImagesToSdCardAndReturnBitman(final String downloadUrl, Context ctx) {

        final File direct = new File(Environment.getExternalStorageDirectory() + "/Tokaji Wines");
        // String internalPath = ctx.getFilesDir().getAbsolutePath();

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
                .setVisibleInDownloadsUi(debug_mode)
                .setDestinationInExternalFilesDir(
                        ctx,
                        "/Tokaji Wines",
                        downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1,
                                downloadUrl.length()));

        Log.i("App", ctx.getFilesDir().getAbsolutePath());

        mgr.enqueue(request);

        Bitmap myBitmap = BitmapFactory.decodeFile(direct.getPath() + "/"
                + downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1, downloadUrl.length()));

        return myBitmap;

    }

    public static void downloadAndRun(final String url, final Context ctx, final ImageView iv) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    downloadToInternal(url, ctx);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        };

        thread.start();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // App.downloadImagesToSdCard(mNews[position].mImageUrl, mActivity, holder.img);

                try {
                    iv.setImageBitmap(getFromInternal(url, ctx));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, 1500);

    }

    public static void downloadToInternal(String url, Context ctx) throws IOException {
        URL imageURL = new URL(url);

        URLConnection connection = imageURL.openConnection();

        InputStream inputStream = new BufferedInputStream(imageURL.openStream(), 10240);

        File internalDir = ctx.getFilesDir();

        File cacheFile = new File(internalDir,
                url.substring(url.lastIndexOf('/') + 1, url.length()));

        FileOutputStream outputStream = new FileOutputStream(cacheFile);

        byte buffer[] = new byte[1024];

        int dataSize;

        //  int loadedSize = 0;

        while ((dataSize = inputStream.read(buffer)) != -1) {

            outputStream.write(buffer, 0, dataSize);

        }

        outputStream.close();

    }

    public static Bitmap getFromInternal(String url, Context ctx) throws FileNotFoundException {
        File cacheDir = ctx.getFilesDir();
        String filename = url.substring(url.lastIndexOf('/') + 1, url.length());
        File cacheFile = new File(cacheDir, filename);

        InputStream fileInputStream = new FileInputStream(cacheFile);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = false;

        Bitmap wallpaperBitmap = BitmapFactory.decodeStream(fileInputStream, null, bitmapOptions);

        return wallpaperBitmap;
    }

    public static LatLng getCurrentLatLng(Context ctx) {

        // construct a new instance for this library
        SimpleLocation location = new SimpleLocation(ctx);

        if (location.hasLocationEnabled()) {
            // ask the device to update the location data
            location.beginUpdates();

            // get the location from the device (alternative A)
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            // get the location from the device (alternative B)
            //SimpleLocation.Point coords = location.getPosition();

            // ask the device to stop location updates to save battery
            location.endUpdates();
            return new LatLng(lat, lng);
        } else {
            // ask the user to enable location access
            SimpleLocation.openSettings(ctx);

        }
        return null;

    }

    public static LatLng getCurrentLatLngService(Context ctx) {

        // construct a new instance for this library
        SimpleLocation location = new SimpleLocation(ctx);

        if (location.hasLocationEnabled()) {
            // ask the device to update the location data
            location.beginUpdates();

            // get the location from the device (alternative A)
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            // get the location from the device (alternative B)
            //SimpleLocation.Point coords = location.getPosition();

            // ask the device to stop location updates to save battery
            location.endUpdates();
            return new LatLng(lat, lng);
        }
        return null;

    }

    public void showDialogWhenNoLocationService() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.no_google_services_location_title))
                .setMessage(getResources().getString(R.string.no_google_services_location))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Getting URL to the Google Directions API

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }

}
