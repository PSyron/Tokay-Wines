
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
import pl.tokajiwines.adapters.RestaurantsAdapter;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.jsonresponses.RestaurantsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestaurantsSearchActivity extends BaseActivity {

    ListView mUiList;
    RestaurantsAdapter mAdapter;
    boolean mIsViewFilled;
    LoadRestaurantsTask mLoadRestaurantsTask;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private String mName;
    private RestaurantListItem[] mRestaurantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_search);
        getActionBar().setTitle(getResources().getString(R.string.restaurants));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = (String) extras.getString(SearchableActivity.TAG_NAME);
        }
        mContext = this;

        sUrl = getResources().getString(R.string.UrlRestaurantsList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mRestaurantsList = new RestaurantListItem[0];
        mUiList = (ListView) findViewById(R.id.activity_restaurants_search_list);
        mAdapter = new RestaurantsAdapter(this, mRestaurantsList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                RestaurantListItem temp = (RestaurantListItem) mAdapter.getItem(position);
                Intent intent = new Intent(mContext, RestaurantActivity.class);
                intent.putExtra(RestaurantActivity.RESTAURANT_TAG, temp);
                startActivityForResult(intent, RestaurantActivity.REQUEST);
            }
        });
        mIsViewFilled = false;
    }

    public void fillView() {

        Log.e("fillView", "View filled");
        mAdapter = new RestaurantsAdapter(this, mRestaurantsList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mRestaurantsList.length == 0) {

            if (App.isOnline(mContext)) {
                mLoadRestaurantsTask = new LoadRestaurantsTask();
                mLoadRestaurantsTask.execute();
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

        if (mLoadRestaurantsTask != null) {

            mLoadRestaurantsTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadRestaurantsTask = null;
        }
        super.onPause();
    }

    class LoadRestaurantsTask extends AsyncTask<String, String, String> {

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

                RestaurantsResponse response = gson.fromJson(reader, RestaurantsResponse.class);

                if (response != null) {
                    mRestaurantsList = response.restaurants;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantsList != null) {
                fillView();
            }

            mLoadRestaurantsTask = null;

        }

    }

}
