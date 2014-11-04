
package pl.tokajiwines.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.RestaurantActivity;
import pl.tokajiwines.adapters.RestaurantsAdapter;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.jsonresponses.RestaurantsResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TabRestaurantsFragment extends BaseFragment {

    ListView mUiList;
    RestaurantsAdapter mAdapter;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    public static final String RESTAURANT_TAG = "restaurant";
    private RestaurantListItem[] mRestaurantList;

    public static TabRestaurantsFragment newInstance() {
        TabRestaurantsFragment fragment = new TabRestaurantsFragment();

        return fragment;
    }

    public TabRestaurantsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sUrl = getResources().getString(R.string.UrlRestaurantsList);
        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_restaurants_list);
        mContext = getActivity();

        return rootView;
    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (App.isOnline(mContext)) {
            new LoadRestaurantTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    // async task class that loads Hotel data from remote database

    class LoadRestaurantTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mContext);
            mProgDial.setMessage("Loading restaurants data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving restaurant data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            RestaurantsResponse response = gson.fromJson(reader, RestaurantsResponse.class);

            if (response != null) {
                mRestaurantList = response.restaurants;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            mAdapter = new RestaurantsAdapter(getActivity(), mRestaurantList);
            mUiList.setAdapter(mAdapter);

            mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    RestaurantListItem temp = (RestaurantListItem) mAdapter.getItem(position);
                    Intent intent = new Intent(mContext, RestaurantActivity.class);
                    intent.putExtra(RESTAURANT_TAG, temp);
                    System.out.println(temp.mIdRestaurant);
                    startActivityForResult(intent, RestaurantActivity.REQUEST);
                }
            });

        }
    }
}
