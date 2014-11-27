
package pl.tokajiwines.acitivities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.NewsActivity.LoadNewsDetailsTask;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.NewsListItem;
import pl.tokajiwines.jsonresponses.NewsResponse;
import pl.tokajiwines.jsonresponses.SearchResultResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SharedPreferencesHelper;

public class SearchableActivity extends BaseActivity {
    
    ProgressDialog mProgDial;
    boolean mIsViewFilled;
    Context mContext;
    JSONParser mParser;
    String query;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private SearchResultResponse mSearchResult;
    private LoadSearchResultTask mLoadSearchResult;
    
    TextView mWineName;
    TextView mWineTaste;
    TextView mWineType;
    TextView mWineStrain;
    TextView mWineYear;
    TextView mWinePrice;
    TextView mWineProducer;
    
    TextView mProducerName;
    TextView mProducerDecription;
    
    TextView mHotelName;
    TextView mHotelPhone;
    TextView mHotelAddress;
    
    TextView mRestaurantName;
    TextView mRestaurantPhone;
    TextView mRestaurantAddress;

    
    
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        
        sUrl = getResources().getString(R.string.UrlSearchResults);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        mContext = this;
        
        initView();
        
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_with_home, menu);
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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
    
    public void initView()
    {
        mWineName = (TextView) findViewById(R.id.activity_search_wine_name);
        mWineTaste = (TextView) findViewById(R.id.activity_search_wine_taste);
        mWineType = (TextView) findViewById(R.id.activity_search_wine_type);
        mWineStrain = (TextView) findViewById(R.id.activity_search_wine_strain);
        mWineYear = (TextView) findViewById(R.id.activity_search_wine_year);
        mWinePrice = (TextView) findViewById(R.id.activity_search_wine_price);
        mWineProducer = (TextView) findViewById(R.id.activity_search_wine_producer);
        
        mProducerName = (TextView) findViewById(R.id.activity_search_wineyard_title);
        mProducerDecription = (TextView) findViewById(R.id.activity_search_wineyard_content);
        
        mHotelName = (TextView) findViewById(R.id.activity_search_hotel_name);
        mHotelPhone = (TextView) findViewById(R.id.activity_search_hotel_phone);
        mHotelAddress = (TextView) findViewById(R.id.activity_search_hotel_address);
        
        mRestaurantName = (TextView) findViewById(R.id.activity_search_restaurant_name);
        mRestaurantPhone = (TextView) findViewById(R.id.activity_search_restaurant_phone);
        mRestaurantAddress = (TextView) findViewById(R.id.activity_search_restaurant_address);
    }
    
    public void fillView()
    {
        if (mSearchResult.wine != null)
        {
            mWineName.setText(""+mSearchResult.wine.mName);
            mWineTaste.setText(""+mSearchResult.wine.mFlavourName);
            mWineType.setText(""+mSearchResult.wine.mGrade);
            mWineStrain.setText(""+mSearchResult.wine.mStrains);
            mWineYear.setText(""+mSearchResult.wine.mYear);
            mWinePrice.setText(""+mSearchResult.wine.mPrice);
            mWineProducer.setText(""+mSearchResult.wine.mProducerName);
        }
        
        if (mSearchResult.producer != null)
        {
            mProducerName.setText(""+mSearchResult.producer.mName);
            mProducerDecription.setText(""+mSearchResult.producer.mDescription);
        }
        
        if (mSearchResult.hotel != null)
        {
            mHotelName.setText(""+mSearchResult.hotel.mName);
            mHotelPhone.setText(""+mSearchResult.hotel.mPhone);
            mHotelAddress.setText(""+mSearchResult.hotel.mStreetName + " " + mSearchResult.hotel.mStreetNumber + " "
                    + mSearchResult.hotel.mHouseNumber + " " + mSearchResult.hotel.mCity + " "
                    + mSearchResult.hotel.mPostCode);
        }
        if (mSearchResult.restaurant != null)
        {
            mRestaurantName.setText(""+mSearchResult.restaurant.mName);
            mRestaurantPhone.setText(""+mSearchResult.restaurant.mPhone);
            mRestaurantAddress.setText(""+mSearchResult.restaurant.mStreetName + " " + mSearchResult.restaurant.mStreetNumber + " "
                    + mSearchResult.restaurant.mHouseNumber + " " + mSearchResult.restaurant.mCity + " "
                    + mSearchResult.restaurant.mPostCode);  
        }
    }
    
    public void onResume() {

        super.onResume();

        if (mSearchResult == null) {

            if (App.isOnline(mContext)) {
                mLoadSearchResult = new LoadSearchResultTask();
                mLoadSearchResult.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if (!mIsViewFilled)
            {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {
        if (mLoadSearchResult != null) {

            mLoadSearchResult.cancel(true);
            if (mProgDial != null)
            {
                mProgDial.dismiss();
            }
            
            mLoadSearchResult = null;
        }
        super.onPause();
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }
    
    class LoadSearchResultTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null)
            {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage("Loading news data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();
        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter", query));
            params.add(new BasicNameValuePair("lang", ""
                    + SharedPreferencesHelper.getSharedPreferencesInt(mContext,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage)));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                SearchResultResponse response = gson.fromJson(reader, SearchResultResponse.class);

                if (response != null) {
                    System.out.println(response.message);
                    mSearchResult = response;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mSearchResult != null) {
                fillView();
            }
            mLoadSearchResult = null;
        }

    }
}
