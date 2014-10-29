
package pl.tokajiwines.acitivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.NearPlacesResponse;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.DirectionsJSONParser;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearPlaceActivity extends BaseActivity {

    public static final String TAG_PLACE = "nearPlacefinder";
    public static final String LOG_TAG = "NearPlaceActivity";
    Place mPlace;
    Polyline mRoute;
    MapView mMapView;
    LatLng mPlacePosition;
    Boolean mFirstRun = true;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private GoogleMap googleMap;
    static String sUrl;
    private Place[] mNearbyPlaces;
    public static int REQUEST = 112;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPlace = (Place) extras.getSerializable(NearPlaceActivity.TAG_PLACE);

        }
        Log.e(LOG_TAG, mPlace + "");
        if (mPlace == null) {
            Toast.makeText(NearPlaceActivity.this, "404 Error", Toast.LENGTH_LONG).show();
            finish();
        } else {
            mPlacePosition = mPlace.getLatLng();
        }
        getActionBar()
                .setTitle(getResources().getString(R.string.places_near) + " " + mPlace.mName);
        mMapView = (MapView) findViewById(R.id.activity_map_map);

        mMapView.onCreate(savedInstanceState);
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView.onResume();
        initView();
        if (App.isOnline(NearPlaceActivity.this)) {
            new LoadNearPlaces().execute(mPlacePosition);
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

    public void initView() {
        sUrl = getResources().getString(R.string.UrlNearLatLngPlace);
        try {
            MapsInitializer.initialize(NearPlaceActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        // create marker
        googleMap.setMyLocationEnabled(true);

        MarkerOptions marker = new MarkerOptions().position(mPlacePosition).title(mPlace.mName);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // adding marker
        googleMap.addMarker(marker);

        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker arg0) {
                if (arg0.getPosition() != mPlacePosition) {
                    new AlertDialog.Builder(NearPlaceActivity.this)
                            .setTitle("Route to " + arg0.getTitle())
                            .setMessage("Do You want to check route?")
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Getting URL to the Google Directions API
                                            if (mRoute != null) {
                                                mRoute.remove();
                                            }
                                            String url = getDirectionsUrl(mPlacePosition,
                                                    arg0.getPosition());

                                            DownloadTask downloadTask = new DownloadTask();

                                            // Start downloading json data from Google Directions API
                                            downloadTask.execute(url);
                                        }
                                    })
                            .setNegativeButton(android.R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                return false;
            }

        });
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mPlacePosition)
                .zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

    public void addMarkers(Place[] pozycje) {

        for (Place pozycja : pozycje) {

            MarkerOptions marker = new MarkerOptions().position(pozycja.getLatLng()).title(
                    pozycja.mPlaceType + ": " + pozycja.mName);

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

    class LoadNearPlaces extends AsyncTask<LatLng, Void, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("Async on PreExecute", "Executing create progress bar");
            mProgDial = new ProgressDialog(NearPlaceActivity.this);
            mProgDial.setMessage(getResources().getString(R.string.loading_near));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();
            // clearing markers
            googleMap.clear();

        }

        // retrieving places near data 

        @Override
        protected String doInBackground(LatLng... args) {

            mParser = new JSONParser();
            int tempShared = SharedPreferencesHelper.getSharedPreferencesInt(
                    NearPlaceActivity.this, SettingsFragment.SharedKeyGPSRange,
                    SettingsFragment.DefGPSRange);
            double tempRange = Constans.sMapRadiusInKm[tempShared];
            String tempUrl = sUrl + "?lat=" + args[0].latitude + "&lng=" + args[0].longitude
                    + "&radius=" + tempRange;
            //TODO change below sUrl for tempUrl
            Log.e("pobieranie URL", tempUrl + "     " + sUrl);
            InputStream source = mParser.retrieveStream(tempUrl, Constans.sUsername,
                    Constans.sPassword, null);

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            NearPlacesResponse response = gson.fromJson(reader, NearPlacesResponse.class);

            if (response != null) {

                if (response.success == 1)
                    mNearbyPlaces = response.places;

                else
                    mNearbyPlaces = new Place[0];
            }
            return null;

        }

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            Log.e("Async on PostExecute", "Executing dissmis progress bar");
            mProgDial.dismiss();

            if (!(mNearbyPlaces.length < 1)) {
                addMarkers(mNearbyPlaces);
            } else {
                Log.e("async", "brak punktow blisko");
            }
            mFirstRun = false;

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
                data = NearPlaceActivity.this.downloadUrl(url[0]);
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
                Toast.makeText(NearPlaceActivity.this, "No Points", Toast.LENGTH_SHORT).show();
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
                lineOptions.width(4);
                lineOptions.color(Color.RED);
            }

            //            MainActivity.this.tvDistanceDuration.setText("Distance:" + distance + ", Duration:"
            //                    + duration);

            Toast.makeText(
                    NearPlaceActivity.this,
                    getResources().getString(R.string.distance) + distance
                            + getResources().getString(R.string.duration) + duration,
                    Toast.LENGTH_LONG).show();

            // Drawing polyline in the Google Map for the i-th route
            mRoute = NearPlaceActivity.this.googleMap.addPolyline(lineOptions);
        }
    }

}
