
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.db.RestaurantImagesDataSource;
import pl.tokajiwines.db.RestaurantsDataSource;
import pl.tokajiwines.jsonresponses.RestaurantDetails;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;

    private String sUrl;
    private String sUsername;
    private String sPassword;

    ProgressDialog mProgDial;
    RestaurantListItem mRestaurant;
    RestaurantDetails mRestaurantFromBase;

    boolean mIsViewFilled;
    TextView mUiTitle;
    ImageView mUiImage;
    ViewPager mUiPager;
    CirclePageIndicator mUiPageIndicator;
    TextView mUiAddress;
    TextView mUiPhoneNumber;
    TextView mUiUrl;
    TextView mUiDescription;
    ImageView mUiNavigate;

    LoadRestaurantOnlineTask mLoadRestaurantOnlineTask;
    LoadRestaurantTask mLoadRestaurantTask;
    public static final String RESTAURANT_TAG = "restaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        sUrl = getResources().getString(R.string.UrlRestaurantDetails);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRestaurant = (RestaurantListItem) extras
                    .getSerializable(RestaurantActivity.RESTAURANT_TAG);
        }

        Log.e(RestaurantActivity.class.getName(), mRestaurant + " ");
        initView();
        mIsViewFilled = false;

    }

    public void initView() {

        getActionBar().setTitle(mRestaurant.mName);
        mUiTitle = (TextView) findViewById(R.id.activity_restaurant_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_restaurant_details_image);
        //      mUiPager = (ViewPager) findViewById(R.id.activity_restaurant_details_pager);
        //      mUiPageIndicator = (CirclePageIndicator) findViewById(R.id.activity_restaurant_details_indicator);
        mUiAddress = (TextView) findViewById(R.id.activity_restaurant_details_adress);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_restaurant_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_restaurant_details_url);
        mUiNavigate = (ImageView) findViewById(R.id.activity_restaurant_navigate);
        mUiDescription = (TextView) findViewById(R.id.activity_restaurant_details_description);

        mUiNavigate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Place extraPlace = new Place(mRestaurantFromBase.mIdRestaurant,
                        mRestaurantFromBase.mName, mRestaurantFromBase.mStreetName + " "
                                + mRestaurantFromBase.mStreetNumber + " "
                                + mRestaurantFromBase.mHouseNumber + " "
                                + mRestaurantFromBase.mCity + " " + mRestaurantFromBase.mPostCode,
                        mRestaurantFromBase.mLng, mRestaurantFromBase.mLat, "Restaurant",
                        mRestaurantFromBase.mImageUrl);

                Intent intent = new Intent(RestaurantActivity.this, NavigateToActivity.class);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO, extraPlace);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO_IMAGE,
                        mRestaurantFromBase.mImageUrl);
                startActivityForResult(intent, NavigateToActivity.REQUEST);
            }
        });
    }

    public void fillView() {
        Log.e("fillView", mRestaurantFromBase.mIdRestaurant + " " + mRestaurantFromBase.mName + " "
                + mRestaurantFromBase.mStreetName + " " + mRestaurantFromBase.mStreetNumber + " "
                + mRestaurantFromBase.mHouseNumber + " " + mRestaurantFromBase.mCity + " "
                + mRestaurantFromBase.mPostCode);
        mUiTitle.setText(mRestaurantFromBase.mName);
        mUiAddress.setText(mRestaurantFromBase.mStreetName + " "
                + mRestaurantFromBase.mStreetNumber + " " + mRestaurantFromBase.mHouseNumber + " "
                + mRestaurantFromBase.mCity + " " + mRestaurantFromBase.mPostCode);
        mUiUrl.setText(mRestaurantFromBase.mLink);
        mUiDescription.setText(mRestaurantFromBase.mVast);
        mUiPhoneNumber.setText(mRestaurantFromBase.mPhone);

        final File imgFile = new File(RestaurantActivity.this.getFilesDir().getAbsolutePath()
                + "/"
                + mRestaurantFromBase.mImageUrl.substring(
                        mRestaurantFromBase.mImageUrl.lastIndexOf('/') + 1,
                        mRestaurantFromBase.mImageUrl.length()));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            mUiImage.setImageBitmap(myBitmap);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mRestaurantFromBase.mImageUrl, RestaurantActivity.this,
                            mUiImage);
                }
            }, 50);
        }
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mRestaurantFromBase == null) {

            // if there is an access to the Internet, try to load data from remote database

            if (App.isOnline(RestaurantActivity.this)) {
                mLoadRestaurantOnlineTask = new LoadRestaurantOnlineTask();
                mLoadRestaurantOnlineTask.execute();
            }

            // otherwise, show message

            else {
                /*Toast.makeText(RestaurantActivity.this,
                        getResources().getString(R.string.cannot_connect), Toast.LENGTH_LONG)
                        .show();*/
                Toast.makeText(RestaurantActivity.this, "Offline database", Toast.LENGTH_LONG)
                        .show();
                mLoadRestaurantTask = new LoadRestaurantTask();
                mLoadRestaurantTask.execute();
            }
        }

        else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {

        if (mLoadRestaurantTask != null) {

            System.out.println("TASK NOT NULL");

            mLoadRestaurantTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadRestaurantTask = null;
        }
        super.onPause();
    }

    class LoadRestaurantTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(RestaurantActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_restaurant));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Restaurants data

        @Override
        protected String doInBackground(Void... args) {

            RestaurantImagesDataSource riDs = new RestaurantImagesDataSource(
                    RestaurantActivity.this);
            RestaurantsDataSource rDs = new RestaurantsDataSource(RestaurantActivity.this);
            rDs.open();
            riDs.open();
            mRestaurantFromBase = rDs.getRestaurantDetails(mRestaurant.mIdRestaurant);
            mRestaurantFromBase.mImageUrl = (riDs
                    .getRestaurantImagesPager(mRestaurant.mIdRestaurant))[0].ImageUrl;
            rDs.close();
            riDs.close();

            return null;

        }

        // create adapter that contains loaded data and show list of Restaurants

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantFromBase != null) {
                fillView();
            }

            mLoadRestaurantTask = null;

        }

    }

    class LoadRestaurantOnlineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(RestaurantActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_restaurant));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Restaurants data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mRestaurant.mIdRestaurant));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                RestaurantDetails response = gson.fromJson(reader, RestaurantDetails.class);
                Log.e(RestaurantActivity.class.getName(), response.mIdRestaurant + " ");
                if (response != null) {
                    mRestaurantFromBase = response;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Restaurants

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantFromBase != null) {
                fillView();
            }

            mLoadRestaurantOnlineTask = null;

        }

    }
}
