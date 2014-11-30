
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.HotelsAdapter;
import pl.tokajiwines.jsonresponses.HotelListItem;
import pl.tokajiwines.jsonresponses.HotelsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HotelsSearchActivity extends BaseActivity {

    ListView mUiList;
    HotelsAdapter mAdapter;
    boolean mIsViewFilled;
    LoadHotelsTask mLoadHotelsTask;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private String mName;
    private HotelListItem[] mHotelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels_search);
        getActionBar().setTitle(getResources().getString(R.string.hotels));
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
            mName = (String) extras.getString(SearchableActivity.TAG_NAME);
        }
        
        mContext = this;

        sUrl = getResources().getString(R.string.UrlHotelsList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mHotelsList = new HotelListItem[0];
        mUiList = (ListView) findViewById(R.id.activity_hotels_search_list);
        mAdapter = new HotelsAdapter(this, mHotelsList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                HotelListItem temp = (HotelListItem) mAdapter.getItem(position);
                Intent intent = new Intent(mContext, HotelActivity.class);
                intent.putExtra(HotelActivity.HOTEL_TAG, temp);
                startActivityForResult(intent, HotelActivity.REQUEST);
            }
        });
        mIsViewFilled = false;
    }

    public void fillView() {

        Log.e("fillView", "View filled");
        mAdapter = new HotelsAdapter(this, mHotelsList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mHotelsList.length == 0) {

            if (App.isOnline(mContext)) {
                mLoadHotelsTask = new LoadHotelsTask();
                mLoadHotelsTask.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {

        if (mLoadHotelsTask != null) {

            mLoadHotelsTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadHotelsTask = null;
        }
        super.onPause();
    }

    class LoadHotelsTask extends AsyncTask<String, String, String> {

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

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", mName));  

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                HotelsResponse response = gson.fromJson(reader, HotelsResponse.class);

                if (response != null) {
                    mHotelsList = response.hotels;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mHotelsList != null) {
                fillView();
            }

            mLoadHotelsTask = null;

        }

    }

}
