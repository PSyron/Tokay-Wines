
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.WineDetails;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WineActivity extends BaseActivity {

    Context mContext;
    BaseActivity mWinesGridViewActivity;

    boolean mIsViewFilled;
    TextView mUiName;
    ImageView mUiImage;
    TextView mUiProducerName;
    TextView mUiTaste;
    TextView mUiType;
    TextView mUiStrain;
    TextView mUiYear;
    TextView mUiPrice;
    TextView mUiDescription;
    LinearLayout mUiTasteLayout;
    LinearLayout mUiTypeLayout;
    LinearLayout mUiStrainLayout;
    LinearLayout mUiYearLayout;
    LinearLayout mUiPriceLayout;

    WineListItem mWine;
    WineDetails mWineDetails;

    private String sUrl;
    private String sUsername;
    private String sPassword;

    ProgressDialog mProgDial;
    JSONParser mParser;
    LoadWineTask mLoadWineTask;

    public static String TAG_CALLED_FROM_PRODUCER = "called_from_producer";
    private boolean mIsFromProducer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_details);

        sUrl = getResources().getString(R.string.UrlWineDetails);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mContext = this;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mWine = (WineListItem) extras.getSerializable(WinesListActivity.TAG_WINE);
            mIsFromProducer = extras.getBoolean(WineActivity.TAG_CALLED_FROM_PRODUCER);
        }

        initView();
        mIsViewFilled = false;
    }

    public void initView() {
        getActionBar().setTitle(mWine.mName);
        mUiName = (TextView) findViewById(R.id.activity_wine_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_wine_details_image);
        mUiProducerName = (TextView) findViewById(R.id.activity_wine_available);
        mUiTaste = (TextView) findViewById(R.id.activity_wine_taste);
        mUiType = (TextView) findViewById(R.id.activity_wine_type);
        mUiStrain = (TextView) findViewById(R.id.activity_wine_strain);
        mUiYear = (TextView) findViewById(R.id.activity_wine_year);
        mUiPrice = (TextView) findViewById(R.id.activity_wine_price);
        mUiDescription = (TextView) findViewById(R.id.activity_wine_description);

        mUiTasteLayout = (LinearLayout) findViewById(R.id.activity_wine_taste_layout);
        mUiTypeLayout = (LinearLayout) findViewById(R.id.activity_wine_type_layout);
        mUiStrainLayout = (LinearLayout) findViewById(R.id.activity_wine_strain_layout);
        mUiYearLayout = (LinearLayout) findViewById(R.id.activity_wine_year_layout);
        mUiPriceLayout = (LinearLayout) findViewById(R.id.activity_wine_price_layout);

        mUiName.setText(mWine.mName);
        mUiProducerName.setText(mWine.mProducerName);

        if (mWine.mFlavourName != null) {
            mUiTaste.setText(mWine.mFlavourName);
        } else {
            mUiTasteLayout.setVisibility(View.GONE);
        }

        if (mWine.mGrade != null) {
            mUiType.setText(mWine.mGrade);
        } else {
            mUiTypeLayout.setVisibility(View.GONE);
        }

        if (mWine.mStrains != null) {
            mUiStrain.setText(mWine.mStrains);
        } else {
            mUiStrainLayout.setVisibility(View.GONE);
        }

        if (mWine.mYear != null) {
            mUiYear.setText(mWine.mYear);
        } else {
            mUiYearLayout.setVisibility(View.GONE);
        }

        if (mWine.mPrice != null) {
            mUiPrice.setText(mWine.mPrice);
        } else {
            mUiPriceLayout.setVisibility(View.GONE);
        }

        mUiProducerName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsFromProducer) {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, ProducerActivity.class);
                    intent.putExtra(ProducersFragment.PRODUCER_TAG, new ProducerListItem(
                            mWine.mIdProducer, mWine.mProducerName, ""));

                    startActivity(intent);
                }
            }
        });
    }

    public void fillView() {
        Log.e("fillView", "View filled");
        mUiDescription.setText(mWineDetails.mDescription);

        if (mWineDetails.mImageUrl != null) {
            final File imgFile = new File(WineActivity.this.getFilesDir().getAbsolutePath()
                    + "/"
                    + mWineDetails.mImageUrl.substring(mWineDetails.mImageUrl.lastIndexOf('/') + 1,
                            mWineDetails.mImageUrl.length()));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                mUiImage.setImageBitmap(myBitmap);
            } else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                        App.downloadAndRun(mWineDetails.mImageUrl, WineActivity.this, mUiImage);
                    }
                }, 50);
            }
        }
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mWineDetails == null) {

            // if there is an access to the Internet, try to load data from remote database

            if (App.isOnline(WineActivity.this)) {
                mLoadWineTask = new LoadWineTask();
                mLoadWineTask.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(WineActivity.this,
                        getResources().getString(R.string.cannot_connect), Toast.LENGTH_LONG)
                        .show();
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

        if (mLoadWineTask != null) {

            mLoadWineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadWineTask = null;
        }
        super.onPause();
    }

    class LoadWineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(WineActivity.this);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_wine));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving wine data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idWine", "" + mWine.mIdWine));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);

            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                WineDetails response = gson.fromJson(reader, WineDetails.class);

                if (response != null) {
                    mWineDetails = response;
                }
            }
            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWineDetails != null) {
                fillView();
            }

            mLoadWineTask = null;
        }

    }

}
