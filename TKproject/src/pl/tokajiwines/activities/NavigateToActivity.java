
package pl.tokajiwines.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.DirectionsJSONParser;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

public class NavigateToActivity extends BaseActivity {

    public static final String TAG_PLACE_TO = "nearPlacefinderTo";
    public static final String TAG_PLACE_TO_IMAGE = "nearPlacefinderToImage";
    public static final String LOG_TAG = "NearPlaceActivity";
    public static int REQUEST = 113;

    Place mPlaceTo;
    Polyline mRoute;
    MapView mMapView;
    LatLng mStartPosition;
    LatLng mFinishPosition;
    Boolean mFirstRun = true;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private GoogleMap googleMap;
    static String sUrl;
    private Place[] mNearbyPlaces;
    // GPSTracker mGPStrack;
    ProgressDialog mProgressDialog;
    View mUiPlaceBox;
    ImageView mUiPlaceImage;
    TextView mUiPlaceTitle;
    TextView mUiPlaceAddress;

    TextView mUiPlaceDistance;
    TextView mUiPlaceDuration;
    ImageView mUiNavigateTo;
    ImageView mUiInfo;
    String mUiPlaceImageUrl = "";

    LocationListener mListener;
    LocationManager mLocManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (App.sMapAct != null) {
            App.sMapAct.finish();
        }

        App.sMapAct = this;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            mPlaceTo = (Place) extras.getSerializable(NavigateToActivity.TAG_PLACE_TO);
            if (extras.containsKey(NavigateToActivity.TAG_PLACE_TO_IMAGE)) {

                mUiPlaceImageUrl = extras.getString(NavigateToActivity.TAG_PLACE_TO_IMAGE);
                if( mUiPlaceImageUrl == null) {
                    mUiPlaceImageUrl = "";
                }

                Log.e(LOG_TAG, mUiPlaceImageUrl);
            }
        }

        if (mPlaceTo == null) {
            Toast.makeText(NavigateToActivity.this, "404 Error", Toast.LENGTH_LONG).show();
            finish();
        } else {

            mListener = new LocationHelp();
            mLocManager =   (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean isGPSEnabled = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


// finally require updates at -at least- the desired rate
            if(isNetworkEnabled){
                mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2500, 10, mListener);
                showDialogProgress();
            }
             else if(isGPSEnabled){
                mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 10, mListener);
                showDialogProgress();
            }
             else{
                Toast.makeText(this,"No provider enabled" , Toast.LENGTH_SHORT ).show();
            }

            //mStartPosition = App.getCurrentLatLng(this);
            mFinishPosition = mPlaceTo.getLatLng();
        }
        getActionBar().setTitle(
                getResources().getString(R.string.navigation_to) + " " + mPlaceTo.mName);
        mMapView = (MapView) findViewById(R.id.activity_map_map);
        mUiPlaceBox = findViewById(R.id.activity_map_box);
        mUiPlaceImage = (ImageView) mUiPlaceBox.findViewById(R.id.item_map_image);
        mUiPlaceTitle = (TextView) mUiPlaceBox.findViewById(R.id.item_map_title);
        mUiPlaceAddress = (TextView) mUiPlaceBox.findViewById(R.id.item_map_address);

        mUiPlaceDistance = (TextView) mUiPlaceBox.findViewById(R.id.item_map_distance);
        mUiPlaceDuration = (TextView) mUiPlaceBox.findViewById(R.id.item_map_duration);
        mUiNavigateTo = (ImageView) mUiPlaceBox.findViewById(R.id.item_map_navigate);
        mUiNavigateTo.setVisibility(View.GONE);
        mUiInfo = (ImageView) mUiPlaceBox.findViewById(R.id.item_map_info);
        mUiInfo.setVisibility(View.GONE);

        fillBox();
        mMapView.onCreate(savedInstanceState);
       // initView();

    }

    public void showDialogProgress(){
       // mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.please_wait), getResources().getString(R.string.acquiring_position), true);

       // mProgressDialog.setCancelable(true);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(getResources().getString(R.string.acquiring_position));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public void fillBox() {

        mUiPlaceBox.setVisibility(View.VISIBLE);
        mUiPlaceTitle.setText(mPlaceTo.mName);

        mUiPlaceAddress.setText(mPlaceTo.mAddress);
        if (!mUiPlaceImageUrl.equals("")) {
            final File imgFile = new File(NavigateToActivity.this.getFilesDir().getAbsolutePath()
                    + "/"
                    + mUiPlaceImageUrl.substring(mUiPlaceImageUrl.lastIndexOf('/') + 1,
                            mUiPlaceImageUrl.length()));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                mUiPlaceImage.setImageBitmap(myBitmap);
            } else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                        App.downloadAndRun(mUiPlaceImageUrl, NavigateToActivity.this, mUiPlaceImage);
                    }
                }, 50);
            }
        }

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
    public void onBackPressed() {
        super.onBackPressed();
        App.sMapAct = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void initView() {
        sUrl = getResources().getString(R.string.UrlNearLatLngPlace);
        try {
            MapsInitializer.initialize(NavigateToActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        // create marker
        googleMap.setMyLocationEnabled(false);

        MarkerOptions markerFrom = new MarkerOptions().position(mStartPosition).title(
                getResources().getString(R.string.here_u_started));

        // Changing marker icon
        markerFrom.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        googleMap.addMarker(markerFrom);

        MarkerOptions markerTo = new MarkerOptions().position(mFinishPosition)
                .title(mPlaceTo.mName);

        markerTo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // adding marker
        googleMap.addMarker(markerTo);

        String url = getDirectionsUrl(mStartPosition, mFinishPosition);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(mStartPosition)
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
                data = NavigateToActivity.this.downloadUrl(url[0]);
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
                Toast.makeText(NavigateToActivity.this,
                        getResources().getString(R.string.no_points), Toast.LENGTH_SHORT).show();
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

            // Drawing polyline in the Google Map for the i-th route
            mRoute = NavigateToActivity.this.googleMap.addPolyline(lineOptions);
            //Brzydko ale dziala
            mUiPlaceDistance.setText(distance);
            mUiPlaceDuration.setText(duration);
        }
    }

    private class LocationHelp implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            mStartPosition = new LatLng(location.getLatitude(), location.getLongitude());
            mLocManager.removeUpdates(mListener);
            if(mProgressDialog != null)
            mProgressDialog.dismiss();
            mListener = null;
            initView();

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
