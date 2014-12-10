
package pl.tokajiwines.acitivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ImagePagerAdapter;
import pl.tokajiwines.db.ProducerImagesDataSource;
import pl.tokajiwines.db.ProducersDataSource;
import pl.tokajiwines.jsonresponses.ImagePagerItem;
import pl.tokajiwines.jsonresponses.ImagesResponse;
import pl.tokajiwines.jsonresponses.ProducerDetails;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProducerActivity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private String sUrlImage;
    ProgressDialog mProgDial;
    ProducerListItem mProducer;
    ProducerDetails mProducerFromBase;

    boolean mIsViewFilled;
    boolean mIsPagerFilled;
    TextView mUiTitle;
    ImageView mUiImage;
    ViewPager mUiPager;
    CirclePageIndicator mUiPageIndicator;
    TextView mUiAddress;
    TextView mUiOpeningHours;
    TextView mUiPhoneNumber;
    TextView mUiUrl;
    TextView mUiDescription;
    TextView mUiWineName;
    ImageView mUiWineImage;
    ImageView mUiNear;
    ImageView mUiNavigate;
    TextView mUiMoreWines;
    ScrollView mUiScroll;
    ImagePagerItem[] mImagesUrl;
    ImagePagerAdapter mAdapter;

    LoadProducerTask mLoadProducerTask;
    LoadProducerOnlineTask mLoadProducerOnlineTask;
    LoadProducerImagesTask mLoadProducerImagesTask;
    LoadProducerImagesOnlineTask mLoadProducerImagesOnlineTask;

    public static final String TAG_ID_PRODUCER = "IdProducer";
    public static final String PRODUCER_TAG = "producer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_details);

        sUrl = getResources().getString(R.string.UrlProducerDetails);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        sUrlImage = getResources().getString(R.string.UrlProducerImages);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            mProducer = (ProducerListItem) extras.getSerializable(PRODUCER_TAG);

        }
        Log.e(ProducerActivity.class.getName(), mProducer + " ");
        initView();
        mIsViewFilled = false;
        mIsPagerFilled = false;

    }

    public void initView() {

        getActionBar().setTitle(mProducer.mName);
        mUiTitle = (TextView) findViewById(R.id.activity_producer_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_producer_details_image);
        mUiPager = (ViewPager) findViewById(R.id.activity_producer_details_pager);
        mUiPageIndicator = (CirclePageIndicator) findViewById(R.id.activity_producer_details_indicator);
        mUiAddress = (TextView) findViewById(R.id.activity_producer_details_adress);
        mUiOpeningHours = (TextView) findViewById(R.id.activity_producer_details_hours);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_producer_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_producer_details_url);
        mUiNear = (ImageView) findViewById(R.id.activity_producer_neighborhood);
        mUiNavigate = (ImageView) findViewById(R.id.activity_producer_navigate);
        mUiMoreWines = (TextView) findViewById(R.id.activity_producer_details_wine_button);
        mUiDescription = (TextView) findViewById(R.id.activity_producer_details_description);
        mUiWineName = (TextView) findViewById(R.id.activity_producer_details_wine_name);
        mUiWineImage = (ImageView) findViewById(R.id.activity_producer_details_wine_image);
        mUiScroll = (ScrollView) findViewById(R.id.activity_producer_details_scrollview);
        mUiNavigate.setVisibility(View.INVISIBLE);
        mUiNear.setVisibility(View.INVISIBLE);

        int[] images = {
            R.drawable.no_image_big
        };
        mAdapter = new ImagePagerAdapter(this, images);
        mUiPager.setAdapter(mAdapter);
        mUiPageIndicator.setViewPager(mUiPager);
        mUiNear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                if (App.isOnline(ProducerActivity.this))
                {
                    String tempAddress = mProducerFromBase.mStreetName + " "
                            + mProducerFromBase.mStreetNumber + " " + mProducerFromBase.mHouseNumber
                            + " " + mProducerFromBase.mCity + " " + mProducerFromBase.mPostCode;
    
                    Place extraPlace = new Place(mProducerFromBase.mIdProducer,
                            mProducerFromBase.mName, tempAddress, mProducerFromBase.mLng,
                            mProducerFromBase.mLat, "Producer", mProducerFromBase.mImageUrl);
    
                    Intent intent = new Intent(ProducerActivity.this, NearPlaceActivity.class);
                    intent.putExtra(NearPlaceActivity.TAG_PLACE, extraPlace);
    
                    startActivityForResult(intent, NearPlaceActivity.REQUEST);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProducerActivity.this);
                    builder.setMessage(getResources().getString(R.string.map_offline))
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                               }
                           });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });
        mUiNavigate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (App.isOnline(ProducerActivity.this))
                {
                    String tempAddress = mProducerFromBase.mStreetName + " "
                            + mProducerFromBase.mStreetNumber + " " + mProducerFromBase.mHouseNumber
                            + " " + mProducerFromBase.mCity + " " + mProducerFromBase.mPostCode;
    
                    Place extraPlace = new Place(mProducerFromBase.mIdProducer,
                            mProducerFromBase.mName, tempAddress, mProducerFromBase.mLng,
                            mProducerFromBase.mLat, "Producer", mProducerFromBase.mImageUrl);
    
                    Intent intent = new Intent(ProducerActivity.this, NavigateToActivity.class);
                    intent.putExtra(NavigateToActivity.TAG_PLACE_TO, extraPlace);
                    intent.putExtra(NavigateToActivity.TAG_PLACE_TO_IMAGE, mProducerFromBase.mImageUrl);
                    startActivityForResult(intent, NavigateToActivity.REQUEST);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProducerActivity.this);
                    builder.setMessage(getResources().getString(R.string.map_offline))
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                               }
                           });
                    AlertDialog alert = builder.create();
                    alert.show();   
                }

            }
        });
        mUiMoreWines.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProducerActivity.this, WinesGridViewActivity.class);
                intent.putExtra(TAG_ID_PRODUCER, mProducer);

                startActivityForResult(intent, 1);
                //  Toast.makeText(ProducerActivity.this, "not working yet", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fillView() {
        mUiNavigate.setVisibility(View.VISIBLE);
        mUiNear.setVisibility(View.VISIBLE);
        Log.e("fillView", mProducerFromBase.mIdProducer + " " + mProducerFromBase.mName + " "
                + mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber + " "
                + mProducerFromBase.mHouseNumber + " " + mProducerFromBase.mCity + " "
                + mProducerFromBase.mPostCode);
        mUiTitle.setText(mProducerFromBase.mName);
        mUiAddress.setText(mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber
                + " " + mProducerFromBase.mHouseNumber + " " + mProducerFromBase.mCity + " "
                + mProducerFromBase.mPostCode);
        if (mProducerFromBase.mLink != null) {
            mUiUrl.setText(mProducerFromBase.mLink);
        } else {
            mUiUrl.setVisibility(View.GONE);
        }
        if (mProducerFromBase.mVast != null) {
            mUiDescription.setText(mProducerFromBase.mVast);
        } else {
            mUiDescription.setVisibility(View.GONE);
        }

        if (mProducerFromBase.mPhone != null) {
            mUiPhoneNumber.setText(mProducerFromBase.mPhone);
        } else {
            mUiPhoneNumber.setVisibility(View.GONE);
        }

        mUiWineName.setText(mProducerFromBase.mWineName);

        final File imgFile = new File(ProducerActivity.this.getFilesDir().getAbsolutePath()
                + "/"
                + mProducerFromBase.mWineImageUrl.substring(
                        mProducerFromBase.mWineImageUrl.lastIndexOf('/') + 1,
                        mProducerFromBase.mWineImageUrl.length()));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            mUiWineImage.setImageBitmap(myBitmap);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mProducerFromBase.mWineImageUrl, ProducerActivity.this,
                            mUiWineImage);
                }
            }, 50);
        }
        mIsViewFilled = true;
    }

    public void fillPager() {
        Log.e("fillPager", "Pager filled");
        if (mImagesUrl.length > 0) {
            mAdapter = new ImagePagerAdapter(ProducerActivity.this, mImagesUrl);
            mUiPager.setAdapter(mAdapter);
            mUiPageIndicator.setViewPager(mUiPager);
            mIsPagerFilled = true;
        }
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if there is an access to the Internet, try to load data from remote database

        if (App.isOnline(ProducerActivity.this)) {

            if (mProducerFromBase == null) {
                mLoadProducerOnlineTask = new LoadProducerOnlineTask();
                mLoadProducerOnlineTask.execute();
            }

            else {
                if (!mIsViewFilled) {
                    fillView();
                }
            }

            if (mImagesUrl == null) {
                mLoadProducerImagesOnlineTask = new LoadProducerImagesOnlineTask();
                mLoadProducerImagesOnlineTask.execute();
            }

            else {
                if (!mIsPagerFilled) {
                    fillPager();
                }
            }
        }

        // otherwise, show message

        else {
            /*Toast.makeText(ProducerActivity.this,
                    getResources().getString(R.string.cannot_connect), Toast.LENGTH_LONG).show();
            Toast.makeText(ProducerActivity.this, "Offline database", Toast.LENGTH_LONG).show();*/
            if (mProducerFromBase == null) {
                mLoadProducerTask = new LoadProducerTask();
                mLoadProducerTask.execute();
            }

            else {
                if (!mIsViewFilled) {
                    fillView();
                }
            }

            if (mImagesUrl == null) {
                mLoadProducerImagesTask = new LoadProducerImagesTask();
                mLoadProducerImagesTask.execute();
            }

            else {
                if (!mIsPagerFilled) {
                    fillPager();
                }
            }

        }

    }

    @Override
    protected void onPause() {

        if (mLoadProducerTask != null) {
            mLoadProducerTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadProducerTask = null;
        }
        if (mLoadProducerOnlineTask != null) {
            mLoadProducerOnlineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadProducerOnlineTask = null;
        }
        if (mLoadProducerImagesTask != null) {
            mLoadProducerImagesTask.cancel(true);
            mLoadProducerImagesTask = null;
        }
        if (mLoadProducerImagesOnlineTask != null) {
            mLoadProducerImagesOnlineTask.cancel(true);
            mLoadProducerImagesOnlineTask = null;
        }
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mUiScroll.setScrollY(0);
            }
        }
    }

    class LoadProducerTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(ProducerActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_producer));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(Void... args) {

            ProducersDataSource pDs = new ProducersDataSource(ProducerActivity.this);
            pDs.open();
            mProducerFromBase = pDs.getProducerDetails(mProducer.mIdProducer);
            pDs.close();
            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mProducerFromBase != null) {
                fillView();
                /* Ion.with(mUiWineImage).placeholder(R.drawable.no_image)
                         .error(R.drawable.error_image).load(mProducerFromBase.mWineImageUrl);*/
            }
            mLoadProducerTask = null;
        }

    }

    class LoadProducerOnlineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(ProducerActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_producer));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(Void... args) {
            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mProducer.mIdProducer));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                ProducerDetails response = gson.fromJson(reader, ProducerDetails.class);

                Log.e(ProducerActivity.class.getName(), response.mIdProducer + " ");
                if (response != null) {
                    mProducerFromBase = response;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mProducerFromBase != null) {
                fillView();
                /* Ion.with(mUiWineImage).placeholder(R.drawable.no_image)
                         .error(R.drawable.error_image).load(mProducerFromBase.mWineImageUrl);*/
            }
            mLoadProducerOnlineTask = null;
        }

    }

    class LoadProducerImagesTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... args) {
            ProducerImagesDataSource piDs = new ProducerImagesDataSource(ProducerActivity.this);
            piDs.open();
            ImagePagerItem[] response = piDs.getProducerImagesPager(mProducer.mIdProducer);
            piDs.close();
            if (response != null) mImagesUrl = response;
            return null;

        }

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            //     mProgDial.dismiss();
            if (mImagesUrl != null && mImagesUrl.length > 0) {
                fillPager();
            }

            mLoadProducerImagesTask = null;

        }

    }

    class LoadProducerImagesOnlineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*            mProgDial = new ProgressDialog(ProducerActivity.this);
                        mProgDial.setMessage("Loading producer images...");
                        mProgDial.setIndeterminate(false);
                        mProgDial.setCancelable(true);
                        mProgDial.show();*/

        }

        // retrieving producers data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mProducer.mIdProducer));

            InputStream source = mParser.retrieveStream(sUrlImage, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                ImagesResponse response = gson.fromJson(reader, ImagesResponse.class);
                Log.e(ProducerActivity.class.getName(), response.images.length + " ");
                if (response != null) {
                    mImagesUrl = response.images;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            //     mProgDial.dismiss();
            if (mImagesUrl != null && mImagesUrl.length > 0) {
                fillPager();
            }

            mLoadProducerImagesOnlineTask = null;

        }

    }

}
