
package pl.tokajiwines.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.models.NearPlacesResponse;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.DirectionsJSONParser;
import pl.tokajiwines.utils.GPSTracker;
import pl.tokajiwines.utils.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends BaseFragment {

    private GoogleMap googleMap;
    Context mCtx;
    MapView mMapView;
    Spinner mUiRange;
    Button mUiTours;
    ProgressDialog mProgDial;
    JSONParser mParser;
    LatLng myPosition = new LatLng(51.1086408, 17.0608889); //TODO temp
    //    LatLng myMate1 = new LatLng(51.1486408, 17.0608889);
    //    LatLng myMate2 = new LatLng(51.1386408, 17.0808889);
    //    LatLng myMate3 = new LatLng(51.1286408, 17.0308889);
    private Place[] mNearbyPlaces;

    private static String sUrl;
    public static final String TAG_ID_PLACE = "IdPlace";

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
        sUrl = getResources().getString(R.string.UrlNearLatLngPlace);
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        myPosition = new GPSTracker(mCtx).getLocationLatLng();
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mUiRange = (Spinner) v.findViewById(R.id.map_range_spinner);
        mUiTours = (Button) v.findViewById(R.id.map_tours);

        initView();
        return v;
    }

    public void initView() {

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        // create marker
        googleMap.setMyLocationEnabled(true);
        MarkerOptions marker = new MarkerOptions().position(myPosition).title(
                getResources().getString(R.string.here_u_are));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // adding marker
        googleMap.addMarker(marker);
        //        addMarkers(new LatLng[] {
        //                myMate1, myMate2, myMate3
        //        });
        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Toast.makeText(
                        mCtx,
                        "Dysntns w lini prostej : " + Distance(myPosition, arg0.getPosition())
                                + "m", Toast.LENGTH_SHORT).show();

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(myPosition, arg0.getPosition());

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                return false;
            }
        });
        //        CameraPosition cameraPosition = new CameraPosition.Builder().target(myPosition).zoom(15)
        //                .build();
        //        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //TODO z opcji pobierac jaka miara
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_spinner_item, Constans.sMapRangeKm);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUiRange.setAdapter(dataAdapter);
        mUiRange.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public void addMarkers(LatLng[] pozycje) {
        for (LatLng pozycja : pozycje) {
            MarkerOptions marker = new MarkerOptions().position(pozycja).title("POI");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

            // adding marker
            googleMap.addMarker(marker);
        }
    }

    public void addMarkers(Place[] pozycje) {
        for (Place pozycja : pozycje) {

            MarkerOptions marker = new MarkerOptions().position(pozycja.getLatLng()).title("POI");

            // Changing marker icon
            if (pozycja.mPlaceType.contains("Hotel")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            } else if (pozycja.mPlaceType.contains("Restaurant")) {
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (pozycja.mPlaceType.contains("Producer")) {
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            } else {//Producer
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
            // adding marker
            googleMap.addMarker(marker);
        }
    }

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
        if (App.isOnline(mCtx)) {
            new LoadNearPlaces().execute();
        }
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

    class LoadNearPlaces extends AsyncTask<LatLng, Void, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mCtx);
            mProgDial.setMessage(getResources().getString(R.string.loading_near));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(LatLng... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            NearPlacesResponse response = gson.fromJson(reader, NearPlacesResponse.class);

            if (response != null) {
                mNearbyPlaces = response.places;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            addMarkers(mNearbyPlaces);
            //            mAdapter = new NewsAdapter(getActivity(), mNewsList);
            //            mUiList.setAdapter(mAdapter);
            //            mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            //                    Intent intent = new Intent(mContext, EventActivity.class);
            //                    intent.putExtra(TAG_ID_NEWS, mAdapter.getItemId(position));
            //                    startActivity(intent);
            //                }
            //            });

        }

    }

    //DALEJ JEST Google Maps distance.

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = MapFragment.this.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(MapFragment.this.mCtx, "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { // Get distance from the list
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            //            MainActivity.this.tvDistanceDuration.setText("Distance:" + distance + ", Duration:"
            //                    + duration);

            Toast.makeText(mCtx, "Distance:" + distance + ", Duration:" + duration,
                    Toast.LENGTH_SHORT).show();

            // Drawing polyline in the Google Map for the i-th route
            MapFragment.this.googleMap.addPolyline(lineOptions);
        }
    }

}
