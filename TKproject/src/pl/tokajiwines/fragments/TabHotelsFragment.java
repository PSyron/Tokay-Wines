
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
import pl.tokajiwines.acitivities.HotelActivity;
import pl.tokajiwines.adapters.HotelsAdapter;
import pl.tokajiwines.jsonresponses.HotelListItem;
import pl.tokajiwines.jsonresponses.HotelsResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TabHotelsFragment extends BaseFragment {

    ListView mUiList;
    HotelsAdapter mAdapter;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    public static final String HOTEL_TAG = "hotel";
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
        mUiList = (ListView) rootView.findViewById(R.id.frag_hotels_list);
        mContext = getActivity();

        return rootView;
    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (App.isOnline(mContext)) {
            new LoadHotelTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    // async task class that loads Hotel data from remote database

    class LoadHotelTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mContext);
            mProgDial.setMessage("Loading Hotel data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Hotel data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            HotelsResponse response = gson.fromJson(reader, HotelsResponse.class);

            if (response != null) {
                mHotelList = response.hotels;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Hotel

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            mAdapter = new HotelsAdapter(getActivity(), mHotelList);
            mUiList.setAdapter(mAdapter);

            mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    HotelListItem temp = (HotelListItem) mAdapter.getItem(position);
                    Intent intent = new Intent(mContext, HotelActivity.class);
                    intent.putExtra(HOTEL_TAG, temp);
                    startActivityForResult(intent, HotelActivity.REQUEST);
                }
            });

        }
    }
}
