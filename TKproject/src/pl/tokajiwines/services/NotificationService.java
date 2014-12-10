
package pl.tokajiwines.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import pl.tokajiwines.App;
import pl.tokajiwines.asyncs.GetNearPlacesAsync;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;
import pl.tokajiwines.utils.SimpleLocation;

public class NotificationService extends Service {

    // private final IBinder mBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("NotificationService", "onStartCommand ");
        if (SharedPreferencesHelper.getSharedPreferencesBoolean(this,
                SettingsFragment.SharedKeyNotif, SettingsFragment.DefNotif)) {
            Log.e("onStartCommand", "execute ");
            if (App.isOnline(this)) {
                SimpleLocation sl = new SimpleLocation(this);
                if (sl.hasLocationEnabled()) {

                    // GPSTracker gps = new GPSTracker(this);
                    //  if (gps.canGetLocation()) {
                    Log.e("serwis", "GetNearPlaces.execute");
                    new GetNearPlacesAsync(this).execute(App.getCurrentLatLngService(this));
                    //TODO check gps.stopUsingGPS();
                }
            }
        }
        // Log.e("serwis", "execute " + wyswietlone.size());

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }

}
