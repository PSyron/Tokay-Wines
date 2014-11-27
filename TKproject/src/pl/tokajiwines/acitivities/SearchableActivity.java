
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.fragments.TabHotelsFragment;
import pl.tokajiwines.fragments.TabRestaurantsFragment;
import pl.tokajiwines.jsonresponses.HotelListItem;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.jsonresponses.SearchItem;
import pl.tokajiwines.jsonresponses.SearchResultResponse;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SharedPreferencesHelper;
import pl.tokajiwines.utils.SuggestionProvider;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends BaseActivity {

    ProgressDialog mProgDial;
    boolean mIsViewFilled;
    int mSelectedId;
    SearchItem mSelectedItem;
    Context mContext;
    JSONParser mParser;
    String query;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private SearchResultResponse mSearchResult;
    private LoadSearchResultTask mLoadSearchResult;

    LinearLayout mWineLayout;
    LinearLayout mWineTasteLayout;
    LinearLayout mWineTypeLayout;
    LinearLayout mWineStrainLayout;
    LinearLayout mWineYearLayout;
    LinearLayout mWinePriceLayout;
    LinearLayout mWineProducerLayout;
    TextView mWineName;
    TextView mWineTaste;
    TextView mWineType;
    TextView mWineStrain;
    TextView mWineYear;
    TextView mWinePrice;
    TextView mWineProducer;
    ImageView mWineImage;

    LinearLayout mProducerLayout;
    TextView mProducerName;
    TextView mProducerDecription;
    ImageView mProducerImage;

    LinearLayout mHotelLayout;
    TextView mHotelName;
    TextView mHotelPhone;
    TextView mHotelAddress;
    ImageView mHotelImage;

    LinearLayout mRestaurantLayout;
    TextView mRestaurantName;
    TextView mRestaurantPhone;
    TextView mRestaurantAddress;
    ImageView mRestaurantImage;
   
    public String TAG_WINE_ID = "wineId";

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

    public void initView() {
        mWineLayout = (LinearLayout) findViewById(R.id.activity_search_wine_lL);
        mWineTasteLayout = (LinearLayout) findViewById(R.id.activity_search_wine_taste_layout);
        mWineTypeLayout = (LinearLayout) findViewById(R.id.activity_search_wine_type_layout);
        mWineStrainLayout = (LinearLayout) findViewById(R.id.activity_search_wine_strain_layout);
        mWineYearLayout = (LinearLayout) findViewById(R.id.activity_search_wine_year_layout);
        mWinePriceLayout = (LinearLayout) findViewById(R.id.activity_search_wine_price_layout);
        mWineProducerLayout = (LinearLayout) findViewById(R.id.activity_search_wine_producer_layout);

        mWineName = (TextView) findViewById(R.id.activity_search_wine_name);
        mWineTaste = (TextView) findViewById(R.id.activity_search_wine_taste);
        mWineType = (TextView) findViewById(R.id.activity_search_wine_type);
        mWineStrain = (TextView) findViewById(R.id.activity_search_wine_strain);
        mWineYear = (TextView) findViewById(R.id.activity_search_wine_year);
        mWinePrice = (TextView) findViewById(R.id.activity_search_wine_price);
        mWineProducer = (TextView) findViewById(R.id.activity_search_wine_producer);
        mWineImage = (ImageView) findViewById(R.id.activity_search_wine_image);

        mProducerLayout = (LinearLayout) findViewById(R.id.activity_search_producer_lL);
        mProducerName = (TextView) findViewById(R.id.activity_search_wineyard_title);
        mProducerDecription = (TextView) findViewById(R.id.activity_search_wineyard_content);
        mProducerImage = (ImageView) findViewById(R.id.activity_search_wineyard_image);

        mHotelLayout = (LinearLayout) findViewById(R.id.activity_search_hotel_lL);
        mHotelName = (TextView) findViewById(R.id.activity_search_hotel_name);
        mHotelPhone = (TextView) findViewById(R.id.activity_search_hotel_phone);
        mHotelAddress = (TextView) findViewById(R.id.activity_search_hotel_address);
        mHotelImage = (ImageView) findViewById(R.id.activity_search_hotel_image);

        mRestaurantLayout = (LinearLayout) findViewById(R.id.activity_search_restaurant_lL);
        mRestaurantName = (TextView) findViewById(R.id.activity_search_restaurant_name);
        mRestaurantPhone = (TextView) findViewById(R.id.activity_search_restaurant_phone);
        mRestaurantAddress = (TextView) findViewById(R.id.activity_search_restaurant_address);
        mRestaurantImage = (ImageView) findViewById(R.id.activity_search_restaurant_image);
    }

    public void fillView() {
        if (mSearchResult.wine != null) {
            if (mSearchResult.wine.mName != null) {
                mWineName.setText("" + mSearchResult.wine.mName);
            } else {
                mWineName.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mFlavourName != null) {
                mWineTaste.setText("" + mSearchResult.wine.mFlavourName);
            } else {
                mWineTasteLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mGrade != null) {
                mWineType.setText("" + mSearchResult.wine.mGrade);
            } else {
                mWineTypeLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mStrains != null) {
                mWineStrain.setText("" + mSearchResult.wine.mStrains);
            } else {
                mWineStrainLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mYear != null) {
                mWineYear.setText("" + mSearchResult.wine.mYear);
            } else {
                mWineYearLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mPrice != null) {
                mWinePrice.setText("" + mSearchResult.wine.mPrice);
            } else {
                mWinePriceLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mProducerName != null) {
                mWineProducer.setText("" + mSearchResult.wine.mProducerName);
            } else {
                mWineProducerLayout.setVisibility(View.GONE);
            }

            if (mSearchResult.wine.mImageUrl != null) {
                final File imgFile = new File(mContext.getFilesDir().getAbsolutePath()
                        + "/"
                        + mSearchResult.wine.mImageUrl.substring(
                                mSearchResult.wine.mImageUrl.lastIndexOf('/') + 1,
                                mSearchResult.wine.mImageUrl.length()));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mWineImage.setImageBitmap(myBitmap);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                            App.downloadAndRun(mSearchResult.wine.mImageUrl, mContext, mWineImage);
                        }
                    }, 50);
                }
            }
        } else {
            mWineLayout.setVisibility(View.GONE);
        }

        if (mSearchResult.producer != null) {
            if (mSearchResult.producer.mName != null) {
                mProducerName.setText("" + mSearchResult.producer.mName);
            } else {
                mProducerName.setVisibility(View.GONE);
            }

            if (mSearchResult.producer.mDescription != null) {
                mProducerDecription.setText("" + mSearchResult.producer.mDescription);
            } else {
                mProducerDecription.setVisibility(View.GONE);
            }

            if (mSearchResult.producer.mImageUrl != null) {
                final File imgFile = new File(mContext.getFilesDir().getAbsolutePath()
                        + "/"
                        + mSearchResult.producer.mImageUrl.substring(
                                mSearchResult.producer.mImageUrl.lastIndexOf('/') + 1,
                                mSearchResult.producer.mImageUrl.length()));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mProducerImage.setImageBitmap(myBitmap);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                            App.downloadAndRun(mSearchResult.producer.mImageUrl, mContext,
                                    mProducerImage);
                        }
                    }, 50);
                }
            }
        } else {
            mProducerLayout.setVisibility(View.GONE);
        }

        if (mSearchResult.hotel != null) {
            if (mSearchResult.hotel.mName != null) {
                mHotelName.setText("" + mSearchResult.hotel.mName);
            } else {
                mHotelName.setVisibility(View.GONE);
            }

            if (mSearchResult.hotel.mPhone != null) {
                mHotelPhone.setText("" + mSearchResult.hotel.mPhone);
            } else {
                mHotelPhone.setVisibility(View.GONE);
            }

            if (mSearchResult.hotel.mStreetName != null
                    && mSearchResult.hotel.mStreetNumber != null
                    && mSearchResult.hotel.mHouseNumber != null
                    && mSearchResult.hotel.mCity != null && mSearchResult.hotel.mPostCode != null) {
                mHotelAddress.setText("" + mSearchResult.hotel.mStreetName + " "
                        + mSearchResult.hotel.mStreetNumber + " "
                        + mSearchResult.hotel.mHouseNumber + " " + mSearchResult.hotel.mCity + " "
                        + mSearchResult.hotel.mPostCode);
            } else {
                mHotelAddress.setVisibility(View.GONE);
            }

            if (mSearchResult.hotel.mImageUrl != null) {
                final File imgFile = new File(mContext.getFilesDir().getAbsolutePath()
                        + "/"
                        + mSearchResult.hotel.mImageUrl.substring(
                                mSearchResult.hotel.mImageUrl.lastIndexOf('/') + 1,
                                mSearchResult.hotel.mImageUrl.length()));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mHotelImage.setImageBitmap(myBitmap);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                            App.downloadAndRun(mSearchResult.hotel.mImageUrl, mContext, mHotelImage);
                        }
                    }, 50);
                }
            }
        } else {
            mHotelLayout.setVisibility(View.GONE);

        }
        if (mSearchResult.restaurant != null) {
            if (mSearchResult.restaurant.mName != null) {
                mRestaurantName.setText("" + mSearchResult.restaurant.mName);
            } else {
                mRestaurantName.setVisibility(View.GONE);
            }

            if (mSearchResult.restaurant.mPhone != null) {
                mRestaurantPhone.setText("" + mSearchResult.restaurant.mPhone);
            } else {
                mRestaurantPhone.setVisibility(View.GONE);
            }

            if (mSearchResult.restaurant.mStreetName != null
                    && mSearchResult.restaurant.mStreetNumber != null
                    && mSearchResult.restaurant.mHouseNumber != null
                    && mSearchResult.restaurant.mCity != null
                    && mSearchResult.restaurant.mPostCode != null) {
                mRestaurantAddress
                        .setText("" + mSearchResult.restaurant.mStreetName + " "
                                + mSearchResult.restaurant.mStreetNumber + " "
                                + mSearchResult.restaurant.mHouseNumber + " "
                                + mSearchResult.restaurant.mCity + " "
                                + mSearchResult.restaurant.mPostCode);
            } else {
                mRestaurantAddress.setVisibility(View.GONE);
            }

            if (mSearchResult.restaurant.mImageUrl != null) {
                final File imgFile = new File(mContext.getFilesDir().getAbsolutePath()
                        + "/"
                        + mSearchResult.restaurant.mImageUrl.substring(
                                mSearchResult.restaurant.mImageUrl.lastIndexOf('/') + 1,
                                mSearchResult.restaurant.mImageUrl.length()));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mRestaurantImage.setImageBitmap(myBitmap);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                            App.downloadAndRun(mSearchResult.restaurant.mImageUrl, mContext,
                                    mRestaurantImage);
                        }
                    }, 50);
                }
            }

        } else {
            mRestaurantLayout.setVisibility(View.GONE);
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
        } else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {
        if (mLoadSearchResult != null) {

            mLoadSearchResult.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadSearchResult = null;
        }
        super.onPause();
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
                mSelectedId = Integer.parseInt(intent.getDataString());
                mSelectedItem = SuggestionProvider.sSearchItems[mSelectedId];
                
                Intent startIntent;
                
                if (mSelectedItem != null)
                {
                   if (mSelectedItem.mItemType.equals(SearchItem.TAG_WINE))
                   {
                       startIntent = new Intent(mContext, WineActivity.class);
                       startIntent.putExtra(WinesListActivity.TAG_WINE, new WineListItem(mSelectedItem.mId, mSelectedItem.mName));
                       startIntent.putExtra(WineActivity.TAG_CALLED_FROM_PRODUCER, false);
                       startActivity(startIntent);
                   }
                   else if (mSelectedItem.mItemType.equals(SearchItem.TAG_PRODUCER))
                   {
                       startIntent = new Intent(mContext, ProducerActivity.class);
                       startIntent.putExtra(ProducersFragment.PRODUCER_TAG, new ProducerListItem(
                               mSelectedItem.mId, mSelectedItem.mName, ""));
                       startActivity(startIntent);       
                   }
                   
                   else if (mSelectedItem.mItemType.equals(SearchItem.TAG_HOTEL))
                   {
                       startIntent = new Intent(mContext, HotelActivity.class);
                       startIntent.putExtra(TabHotelsFragment.HOTEL_TAG, new HotelListItem(
                               mSelectedItem.mId, mSelectedItem.mName));
                       startActivity(startIntent);       
                   }
                   
                   else if (mSelectedItem.mItemType.equals(SearchItem.TAG_RESTAURANT))
                   {
                       startIntent = new Intent(mContext, RestaurantActivity.class);
                       startIntent.putExtra(TabRestaurantsFragment.RESTAURANT_TAG, new RestaurantListItem(
                               mSelectedItem.mId, mSelectedItem.mName));
                       startActivity(startIntent);       
                   }
                }
                
                finish();
                
            }
    }

    class LoadSearchResultTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_search));
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
