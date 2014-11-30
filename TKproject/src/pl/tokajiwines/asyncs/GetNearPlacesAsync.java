
package pl.tokajiwines.asyncs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.MainActivity;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.NearPlacesResponse;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.recivers.RepeatServiceNotificationReceiver;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNearPlacesAsync extends AsyncTask<LatLng, Void, Void> {

    public static final String SHARED_ARRAY = "shared_array ";
    private Context mContext;

    private static String sUrl;
    private String sUsername;
    private String sPassword;
    private Place[] mNearbyPlaces;

    JSONParser mParser;
    //LatLng myPosition;
    int mRangePicked;

    public GetNearPlacesAsync(Context context) {
        mContext = context;
    }

    boolean failure = false;

    // while data are loading, show progress dialog

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // myPosition = new GPSTracker(mContext).getLocationLatLng();
        sUrl = mContext.getResources().getString(R.string.UrlNearLatLngPlace);
        sUsername = mContext.getResources().getString(R.string.Username);
        sPassword = mContext.getResources().getString(R.string.Password);
        mRangePicked = SharedPreferencesHelper.getSharedPreferencesInt(mContext,
                SettingsFragment.SharedKeyGPSRange, SettingsFragment.DefGPSRange);

    }

    // retrieving places near data 

    @Override
    protected Void doInBackground(LatLng... args) {

        mParser = new JSONParser();
        double tempRange = Constans.sMapRadiusInKm[mRangePicked];
        String tempUrl;
        if (!App.debug_mode) {
            tempUrl = sUrl + "?lat=" + args[0].latitude + "&lng=" + args[0].longitude + "&radius="
                    + tempRange;
        } else {
            LatLng myPosition = new LatLng(48.1295, 21.4089);
            tempUrl = sUrl + "?lat=" + myPosition.latitude + "&lng=" + myPosition.longitude
                    + "&radius=" + tempRange;
        }
        //TODO change below sUrl for tempUrl
        Log.e("pobieranie URL", tempUrl + "     ");
        InputStream source = mParser.retrieveStream(tempUrl, sUsername, sPassword, null);
        if (source != null) {

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            NearPlacesResponse response = gson.fromJson(reader, NearPlacesResponse.class);

            if (response != null) {

                if (response.success == 1)
                    mNearbyPlaces = response.places;

                else
                    mNearbyPlaces = new Place[0];
            }
        }

        else {
            mNearbyPlaces = new Place[0];
        }
        return null;

    }

    protected void onPostExecute(Void result) {

        if (!(mNearbyPlaces.length < 1)) {
            Log.e("async", "sa punkty " + mNearbyPlaces.length);
            notyfikuj();
        } else {
            Log.e("async", "brak punktow blisko");
        }

    }

    public void notyfikuj() {

        for (final Place p : mNearbyPlaces) {
            boolean tempBoolean = SharedPreferencesHelper.getSharedPreferencesBoolean(mContext,
                    SHARED_ARRAY + p.mIdPlace, false);
            if (!tempBoolean) {
                SharedPreferencesHelper.putSharedPreferencesBoolean(mContext, SHARED_ARRAY
                        + p.mIdPlace, true);
                RepeatServiceNotificationReceiver.wyswietlone.add(p);
                NotificationManager nm = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                Intent inter = new Intent(mContext, MainActivity.class);
                inter.putExtra("dawaj dawaj", p.mIdPlace);
                inter.setAction(Long.toString(System.currentTimeMillis()));
                if (!SharedPreferencesHelper.getSharedPreferencesBoolean(mContext,
                        SettingsFragment.SharedKeyNotifVibrat, SettingsFragment.DefNotifVibrat)) {
                    Uri alarmSound = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);
                }
                // inter.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                // | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, inter,
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                builder.setTicker("Near place").setDefaults(Notification.DEFAULT_LIGHTS)
                        .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_launcher_white).setAutoCancel(true)
                        .setContentTitle("Near place");
                builder.setContentText("Near " + p.mPlaceType + ":" + p.mName);

                //                    builder.setSound(Uri.parse("android.resource://" + mContext.getPackageName()
                //                            + "/" + p.getSound()));

                final File imgFile = new File(mContext.getFilesDir().getAbsolutePath()
                        + "/"
                        + p.mImageUrl.substring(p.mImageUrl.lastIndexOf('/') + 1,
                                p.mImageUrl.length()));
                Bitmap myBitmap = null;
                if (imgFile.exists()) {
                    Log.e("Notifikacja", "zdjecie istnieje");
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                } else {
                    Log.e("Notifikacja", "nie zdjecie istnieje");
                    //  myBitmap = App.downloadImagesToSdCardAndReturnBitman(p.mImageUrl, mContext);
                    try {
                        App.downloadToInternal(p.mImageUrl, mContext);
                        myBitmap = App.getFromInternal(p.mImageUrl, mContext);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                builder.setLargeIcon(myBitmap);

                Notification notification = builder.build();
                nm.notify(p.mIdPlace, notification);

            }
        }

    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
