
package pl.tokajiwines.activities;

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
import pl.tokajiwines.db.HotelImagesDataSource;
import pl.tokajiwines.db.HotelsDataSource;
import pl.tokajiwines.jsonresponses.HotelDetails;
import pl.tokajiwines.jsonresponses.HotelListItem;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HotelActivity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;
    private String sUrl;
    ProgressDialog mProgDial;
    HotelListItem mHotel;
    HotelDetails mHotelFromBase;

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
    LoadHotelTask mLoadHotelTask;
    LoadHotelOnlineTask mLoadHotelOnlineTask;
    private String sUsername;
    private String sPassword;

    public static final String HOTEL_TAG = "hotel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

        sUrl = getResources().getString(R.string.UrlHotelDetails);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHotel = (HotelListItem) extras.getSerializable(HotelActivity.HOTEL_TAG);
        }

        Log.e(HotelActivity.class.getName(), mHotel + " ");
        initView();
        mIsViewFilled = false;

    }

    public void initView() {

        getActionBar().setTitle(mHotel.mName);
        mUiTitle = (TextView) findViewById(R.id.activity_hotel_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_hotel_details_image);
        //      mUiPager = (ViewPager) findViewById(R.id.activity_hotel_details_pager);
        //      mUiPageIndicator = (CirclePageIndicator) findViewById(R.id.activity_hotel_details_indicator);
        mUiAddress = (TextView) findViewById(R.id.activity_hotel_details_adress);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_hotel_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_hotel_details_url);
        mUiNavigate = (ImageView) findViewById(R.id.activity_hotel_navigate);
        mUiDescription = (TextView) findViewById(R.id.activity_hotel_details_description);

        mUiNavigate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Place extraPlace = new Place(mHotelFromBase.mIdHotel, mHotelFromBase.mName,
                        mHotelFromBase.mStreetName + " " + mHotelFromBase.mStreetNumber + " "
                                + mHotelFromBase.mHouseNumber + " " + mHotelFromBase.mCity + " "
                                + mHotelFromBase.mPostCode, mHotelFromBase.mLng,
                        mHotelFromBase.mLat, "Hotel", mHotelFromBase.mImageUrl);

                Intent intent = new Intent(HotelActivity.this, NavigateToActivity.class);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO, extraPlace);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO_IMAGE, mHotelFromBase.mImageUrl);
                startActivityForResult(intent, NavigateToActivity.REQUEST);
            }
        });
    }

    public void fillView() {
        Log.e("fillView", mHotelFromBase.mIdHotel + " " + mHotelFromBase.mName + " "
                + mHotelFromBase.mStreetName + " " + mHotelFromBase.mStreetNumber + " "
                + mHotelFromBase.mHouseNumber + " " + mHotelFromBase.mCity + " "
                + mHotelFromBase.mPostCode);
        mUiTitle.setText(mHotelFromBase.mName);
        mUiAddress.setText(mHotelFromBase.mStreetName + " " + mHotelFromBase.mStreetNumber + " "
                + mHotelFromBase.mHouseNumber + " " + mHotelFromBase.mCity + " "
                + mHotelFromBase.mPostCode);
        if (mHotelFromBase.mLink != null) {
            mUiUrl.setText(mHotelFromBase.mLink);
        } else {
            mUiUrl.setVisibility(View.GONE);
        }

        if (mHotelFromBase.mVast != null) {
            mUiDescription.setText(mHotelFromBase.mVast);
        } else {
            mUiDescription.setVisibility(View.GONE);
        }

        if (mHotelFromBase.mPhone != null) {
            mUiPhoneNumber.setText(mHotelFromBase.mPhone);
        } else {
            mUiPhoneNumber.setVisibility(View.GONE);
        }

        final File imgFile = new File(HotelActivity.this.getFilesDir().getAbsolutePath()
                + "/"
                + mHotelFromBase.mImageUrl.substring(mHotelFromBase.mImageUrl.lastIndexOf('/') + 1,
                        mHotelFromBase.mImageUrl.length()));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            mUiImage.setImageBitmap(myBitmap);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mHotelFromBase.mImageUrl, HotelActivity.this, mUiImage);
                }
            }, 50);
        }

        mIsViewFilled = true;

    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mHotelFromBase == null) {

            // if there is an access to the Internet, try to load data from remote database

            if (App.isOnline(HotelActivity.this)) {
                mLoadHotelOnlineTask = new LoadHotelOnlineTask();
                mLoadHotelOnlineTask.execute();
            }

            // otherwise, show message

            else {
                /*Toast.makeText(HotelActivity.this, "Cannot connect to the Internet",
                        Toast.LENGTH_LONG).show();*/
                Toast.makeText(HotelActivity.this, "Offline database", Toast.LENGTH_LONG).show();
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
    protected void onPause() {

        if (mLoadHotelTask != null) {

            mLoadHotelTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadHotelTask = null;
        }
        super.onPause();
    }

    class LoadHotelTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(HotelActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_hotel));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Hotels data

        @Override
        protected String doInBackground(Void... args) {
            HotelImagesDataSource hiDs = new HotelImagesDataSource(HotelActivity.this);
            HotelsDataSource hDs = new HotelsDataSource(HotelActivity.this);
            hDs.open();
            hiDs.open();
            mHotelFromBase = hDs.getHotelDetails(mHotel.mIdHotel);
            mHotelFromBase.mImageUrl = (hiDs.getHotelImagesPager(mHotel.mIdHotel))[0].ImageUrl;
            hDs.close();
            hiDs.close();
            return null;

        }

        // create adapter that contains loaded data and show list of Hotels

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mHotelFromBase != null) {
                fillView();
            }

            mLoadHotelTask = null;

        }

    }

    class LoadHotelOnlineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(HotelActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_hotel));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving Hotels data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mHotel.mIdHotel));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                HotelDetails response = gson.fromJson(reader, HotelDetails.class);
                Log.e(HotelActivity.class.getName(), response.mIdHotel + " ");
                if (response != null) {
                    mHotelFromBase = response;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Hotels

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mHotelFromBase != null) {
                fillView();
            }

            mLoadHotelOnlineTask = null;

        }

    }

}
