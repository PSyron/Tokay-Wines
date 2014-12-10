
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

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.HotelActivity;
import pl.tokajiwines.adapters.HotelsAdapter;
import pl.tokajiwines.db.HotelsDataSource;
import pl.tokajiwines.jsonresponses.HotelListItem;
import pl.tokajiwines.jsonresponses.HotelsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TabHotelsFragment extends BaseFragment {

    ListView mUiList;
    HotelsAdapter mAdapter;
    boolean mIsViewFilled;
    LoadHotelTask mLoadHotelTask;
    LoadHotelOnlineTask mLoadHotelOnlineTask;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private HotelListItem[] mHotelList;

    public static TabHotelsFragment newInstance() {
        TabHotelsFragment fragment = new TabHotelsFragment();

        return fragment;
    }

    public TabHotelsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hotels, container, false);

        sUrl = getResources().getString(R.string.UrlHotelsList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mUiList = (ListView) rootView.findViewById(R.id.frag_hotels_list);
        mHotelList = new HotelListItem[0];
        mAdapter = new HotelsAdapter(getActivity(), mHotelList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                HotelListItem temp = (HotelListItem) mAdapter.getItem(position);
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra(HotelActivity.HOTEL_TAG, temp);
                startActivityForResult(intent, HotelActivity.REQUEST);
            }
        });
        mContext = getActivity();
        mIsViewFilled = false;

        return rootView;
    }

    public void fillView() {
        Log.e("fillView", "View filled");
        mAdapter = new HotelsAdapter(getActivity(), mHotelList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mHotelList.length == 0) {
            if (App.isOnline(mContext)) {
                mLoadHotelOnlineTask = new LoadHotelOnlineTask();
                mLoadHotelOnlineTask.execute();
            }

            // otherwise, show message

            else {
                /*  Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                          Toast.LENGTH_LONG).show();*/
                mLoadHotelTask = new LoadHotelTask();
                mLoadHotelTask.execute();
            }
        }

        else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    public void onPause() {

        if (mLoadHotelTask != null) {

            mLoadHotelTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadHotelTask = null;
        }
        
        if (mLoadHotelOnlineTask != null) {

            mLoadHotelOnlineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadHotelOnlineTask = null;
        }
        super.onPause();
    }

    // async task class that loads Hotel data from remote database

    class LoadHotelTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_hotels));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Hotel data

        @Override
        protected String doInBackground(String... args) {

            HotelsDataSource hDs = new HotelsDataSource(mContext);
            hDs.open();
            mHotelList = hDs.getHotelList();
            hDs.close();

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mHotelList != null) {
                fillView();
            }
            mLoadHotelTask = null;

        }
    }

    class LoadHotelOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_hotels));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Hotel data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, null);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                HotelsResponse response = gson.fromJson(reader, HotelsResponse.class);

                if (response != null) {
                    mHotelList = response.hotels;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mHotelList != null) {
                fillView();
            }
            mLoadHotelOnlineTask = null;

        }
    }
}
