
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
import pl.tokajiwines.db.RestaurantsDataSource;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.jsonresponses.RestaurantsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TabRestaurantsFragment extends BaseFragment {

    ListView mUiList;
    RestaurantsAdapter mAdapter;
    boolean mIsViewFilled;
    LoadRestaurantTask mLoadRestaurantTask;
    LoadRestaurantOnlineTask mLoadRestaurantOnlineTask;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;

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
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_restaurants_list);
        mRestaurantList = new RestaurantListItem[0];
        mAdapter = new RestaurantsAdapter(getActivity(), mRestaurantList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                RestaurantListItem temp = (RestaurantListItem) mAdapter.getItem(position);
                Intent intent = new Intent(mContext, RestaurantActivity.class);
                intent.putExtra(RestaurantActivity.RESTAURANT_TAG, temp);
                System.out.println(temp.mIdRestaurant);
                startActivityForResult(intent, RestaurantActivity.REQUEST);
            }
        });
        mContext = getActivity();
        mIsViewFilled = false;

        return rootView;
    }

    public void fillView() {
        Log.e("fillView", "Restaurant view filled");
        mAdapter = new RestaurantsAdapter(getActivity(), mRestaurantList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mRestaurantList.length == 0) {

            if (App.isOnline(mContext)) {
                mLoadRestaurantOnlineTask = new LoadRestaurantOnlineTask();
                mLoadRestaurantOnlineTask.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();
                mLoadRestaurantTask = new LoadRestaurantTask();
                mLoadRestaurantTask.execute();
            }
        } else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    public void onPause() {

        if (mLoadRestaurantTask != null) {

            mLoadRestaurantTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadRestaurantTask = null;
        }
        
        if (mLoadRestaurantOnlineTask != null) {

            mLoadRestaurantOnlineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadRestaurantOnlineTask = null;
        }
        super.onPause();
    }

    // async task class that loads Hotel data from remote database

    class LoadRestaurantTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_restaurants));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving restaurant data

        @Override
        protected String doInBackground(String... args) {

            RestaurantsDataSource rDs = new RestaurantsDataSource(mContext);
            rDs.open();
            mRestaurantList = rDs.getRestaurantList();
            rDs.close();

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantList != null) {
                fillView();
            }
            mLoadRestaurantTask = null;

        }
    }

    class LoadRestaurantOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_restaurants));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving restaurant data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, null);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                RestaurantsResponse response = gson.fromJson(reader, RestaurantsResponse.class);

                if (response != null) {
                    mRestaurantList = response.restaurants;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantList != null) {
                fillView();
            }
            mLoadRestaurantOnlineTask = null;

        }
    }
}
