
package pl.tokajiwines.acitivities;

import android.app.Activity;

public class TrackActivity extends Activity {
    /*
        static LatLng StartingPoint = null;
        private GoogleMap googleMap;
        private ArrayList<LatLng> points = null;
        PolylineOptions lineOptions;
        Button mUiBtStart;
        Button mUiBtStop;
        GPSTracker mGps;
        Boolean mStarted;
        ArrayList<LatLng> general = null;
        PolylineOptions generalPoli;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_track);
            points = new ArrayList<LatLng>();
            general = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();
            generalPoli = new PolylineOptions();
            mStarted = false;
            mGps = new GPSTracker(this);

            init();
            try {
                if (googleMap == null) {
                    googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                            .getMap();
                }
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.setBuildingsEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void init() {

            mUiBtStart = (Button) findViewById(R.id.track_start);
            mUiBtStop = (Button) findViewById(R.id.track_stop);
            mUiBtStart.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mGps.canGetLocation()) {
                        Log.e("log", "true");

                        double lat = mGps.getLatitude(); // returns latitude
                        double lng = mGps.getLongitude(); // returns longitude
                        StartingPoint = new LatLng(lat, lng);
                        general.add(StartingPoint);
                        Marker TP = googleMap.addMarker(new MarkerOptions().position(StartingPoint)
                                .title("Starting Point"));
                    }

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mGps.getLatitude(), mGps.getLongitude()), 20.0f));
                    Toast.makeText(getApplicationContext(),
                            "Rozpoczęto pomiar, naciśnij STOP aby zobaczyć trase wynikową",
                            Toast.LENGTH_LONG).show();
                    mStarted = true;
                    startProgress();
                }
            });
            mUiBtStop.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mStarted = false;
                    Toast.makeText(getApplicationContext(), "Zakończono pomiar", Toast.LENGTH_LONG)
                            .show();
                    Marker TS = googleMap.addMarker(new MarkerOptions().position(
                            new LatLng(mGps.getLatitude(), mGps.getLongitude())).title("Finish Point"));

                    mGps.stopUsingGPS();
                    // general.add(new LatLng(mGps.getLocation().getLatitude(), mGps
                    // .getLocation().getLongitude()));
                    lineOptions.addAll(points);
                    lineOptions.width(2);
                    lineOptions.color(Color.RED);
                    // generalPoli.addAll(general);
                    Log.e("log size", "" + points.size() + "  " + lineOptions.getPoints().size());
                    // googleMap.addPolyline(generalPoli);
                    googleMap.addPolyline(lineOptions);
                    points.clear();
                    lineOptions.getPoints().clear();

                }
            });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.track, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        public void startProgress() {

            points = new ArrayList<LatLng>();

            new Thread(new Task()).start();

        }

        // Kontrola wycieków pamięci
        @Override
        protected void onPause() {
            mStarted = false;
            mGps.stopUsingGPS();
            super.onPause();
        }

        @Override
        public void onBackPressed() {
            mStarted = false;
            mGps.stopUsingGPS();
            super.onBackPressed();
        }

        class Task implements Runnable {
            double lat;
            double lng;
            double prevLat = 0;
            double prevLng = 0;

            @Override
            public void run() {

                // double counter = 0;
                Location lok;
                while (mStarted) {
                    try {
                        Thread.sleep(1000); // TODO Tu ustawiasz czas pomiedzy
                        lok = mGps.getLocation(); // pomiarami
                        lat = lok.getLatitude();
                        lng = lok.getLongitude();
                        points.add(new LatLng(lat, lng));
                        // if (lat != prevLat || lng != prevLng) {
                        // Toast.makeText(
                        // getApplicationContext(),
                        // "Lat " + (prevLat - lat) * 1000 + " Lng "
                        // + (prevLng - lng) * 1000,
                        // Toast.LENGTH_LONG).show();
                        // }
                        prevLat = lat;
                        prevLng = lng;
                        // points.add(new LatLng(lat + counter, lng + counter));
                        // counter += 0.0005;
                        Log.e("log", lat + " " + lng + " size " + points.size());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        */
}
