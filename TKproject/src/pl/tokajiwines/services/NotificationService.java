
package pl.tokajiwines.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import pl.tokajiwines.App;
import pl.tokajiwines.asyncs.GetNearPlacesAsync;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;

public class NotificationService extends Service {

    // private final IBinder mBinder = new MyBinder();

    LocationListener locaList;
    LocationManager lm;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("NotificationService", "onStartCommand ");
        if (SharedPreferencesHelper.getSharedPreferencesBoolean(this,
                SettingsFragment.SharedKeyNotif, SettingsFragment.DefNotif)) {
            Log.e("onStartCommand", "execute ");
            if (App.isOnline(this)) {

                 locaList = new LocationHelp();
                 lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(isNetworkEnabled){
                    lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 50, locaList);
                }else
                if(isGPSEnabled){
                    lm.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 50, locaList);
                }

                    // GPSTracker gps = new GPSTracker(this);
                    //  if (gps.canGetLocation()) {




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

    private class LocationHelp implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            Log.e("onLocationChanged Service", location.getLongitude() + "  " + location.getLatitude());
            new GetNearPlacesAsync(NotificationService.this).execute(App.fromLocationToLatLng(location));
            lm.removeUpdates(locaList);
            locaList = null;

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
