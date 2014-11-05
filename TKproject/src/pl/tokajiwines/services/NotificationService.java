
package pl.tokajiwines.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import pl.tokajiwines.asyncs.GetNearPlacesAsync;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.utils.GPSTracker;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;

public class NotificationService extends Service {

    // private final IBinder mBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("NotificationService", "onStartCommand ");
        if (SharedPreferencesHelper.getSharedPreferencesBoolean(this,
                SettingsFragment.SharedKeyNotif, SettingsFragment.DefNotif)) {
            Log.e("onStartCommand", "execute ");
            new GetNearPlacesAsync(this).execute(new GPSTracker(this).getLocationLatLng());
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
