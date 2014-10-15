
package pl.tokajiwines.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.tokajiwines.R;

public class MapFragment extends BaseFragment {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap googleMap;
    Context mCtx;
    MapView mMapView;

    public static MapFragment newInstance(Context ctx) {
        MapFragment fragment = new MapFragment(ctx);

        return fragment;
    }

    public MapFragment(Context ctx) {
        // TODO Auto-generated constructor stub
        mCtx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //   View rootView = inflater.inflate(R.layout.fragment_map, container, false);

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
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(51.1086408, 17.0608889)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Perform any camera updates here

        //        MapsInitializer.initialize(mCtx);
        //        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(mCtx)) {
        //            case ConnectionResult.SUCCESS:
        //                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
        //                mMapView = (MapView) rootView.findViewById(R.id.map);
        //                mMapView.onCreate(savedInstanceState);
        //                // Gets to GoogleMap from the MapView and does initialization stuff
        //                if (mMapView != null) {
        //                    map = mMapView.getMap();
        //                    map.getUiSettings().setMyLocationButtonEnabled(false);
        //                    map.setMyLocationEnabled(true);
        //                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1,
        //                            -87.9), 10);
        //                    map.animateCamera(cameraUpdate);
        //                }
        //                break;
        //            case ConnectionResult.SERVICE_MISSING:
        //                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
        //                break;
        //            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
        //                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
        //                break;
        //            default:
        //                Toast.makeText(getActivity(),
        //                        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()),
        //                        Toast.LENGTH_SHORT).show();
        //        }
        //        if (map != null) {
        //            Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
        //            Marker kiel = map.addMarker(new MarkerOptions().position(KIEL).title("Kiel")
        //                    .snippet("Kiel is cool")
        //                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        //        }
        return v;
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
