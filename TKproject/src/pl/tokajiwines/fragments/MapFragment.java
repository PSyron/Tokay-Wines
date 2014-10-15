
package pl.tokajiwines.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import pl.tokajiwines.R;

public class MapFragment extends BaseFragment {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    Context mCtx;
    MapView mapView;

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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        MapsInitializer.initialize(mCtx);
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(mCtx)) {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) rootView.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mapView != null) {
                    map = mapView.getMap();
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    map.setMyLocationEnabled(true);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1,
                            -87.9), 10);
                    map.animateCamera(cameraUpdate);
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(),
                        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()),
                        Toast.LENGTH_SHORT).show();
        }
        //        if (map != null) {
        //            Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
        //            Marker kiel = map.addMarker(new MarkerOptions().position(KIEL).title("Kiel")
        //                    .snippet("Kiel is cool")
        //                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        //        }
        return rootView;
    }

}
