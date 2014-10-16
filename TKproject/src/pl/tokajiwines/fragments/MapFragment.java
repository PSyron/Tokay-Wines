
package pl.tokajiwines.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.tokajiwines.R;
import pl.tokajiwines.utils.Constans;

public class MapFragment extends BaseFragment {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap googleMap;
    Context mCtx;
    MapView mMapView;
    Spinner mUiRange;
    Button mUiTours;

    public static MapFragment newInstance(Context ctx) {
        MapFragment fragment = new MapFragment(ctx);

        return fragment;
    }

    public MapFragment(Context ctx) {
        mCtx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude
        double latitude = 51.1086408;
        double longitude = 17.0608889;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(
                "Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        marker = new MarkerOptions().position(new LatLng(51.11272, 17.05908)).title(
                "Pasaż Grunwaldzki");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(marker);
        marker = new MarkerOptions().position(new LatLng(51.10822, 17.06052)).title("C6 Polibuda");
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(51.1086408, 17.0608889)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mUiRange = (Spinner) v.findViewById(R.id.map_range_spinner);
        mUiTours = (Button) v.findViewById(R.id.map_tours);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_spinner_item, Constans.sMapRange);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUiRange.setAdapter(dataAdapter);
        mUiRange.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mCtx, "OnClickListener : " + position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        return v;
    }

    //    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
    //        int Radius=6371;//radius of earth in Km         
    //        double lat1 = StartP.latitude;
    //        double lat2 = EndP.latitude;
    //        double lon1 = StartP.longitude;
    //        double lon2 = EndP.longitude;
    //        double dLat = Math.toRadians(lat2-lat1);
    //        double dLon = Math.toRadians(lon2-lon1);
    //        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    //        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
    //        Math.sin(dLon/2) * Math.sin(dLon/2);
    //        double c = 2 * Math.asin(Math.sqrt(a));
    //        double valueResult= Radius*c;
    //        double km=valueResult/1;
    //        DecimalFormat newFormat = new DecimalFormat("####");
    //        int kmInDec =  Integer.valueOf(newFormat.format(km));
    //        double meter=valueResult%1000;
    //        int  meterInDec= Integer.valueOf(newFormat.format(meter));
    //        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);
    //
    //        return Radius * c;
    //     }

    public float Distance(LatLng first, LatLng second) {
        float[] results = new float[1];
        Location.distanceBetween(first.latitude, first.longitude, second.latitude,
                second.longitude, results);
        return results[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
