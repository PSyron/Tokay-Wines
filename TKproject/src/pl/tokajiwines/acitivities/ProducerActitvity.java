
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ImagePagerAdapter;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.jsonresponses.ProducerDetails;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProducerActitvity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/producerDetails.php";
    ProgressDialog mProgDial;
    ProducerListItem mProducer;
    ProducerDetails mProducerFromBase;

    TextView mUiTitle;
    ImageView mUiImage;
    ViewPager mUiPager;
    CirclePageIndicator mUiPageIndicator;
    TextView mUiAddress;
    TextView mUiOpeningHours;
    TextView mUiPhoneNumber;
    TextView mUiUrl;
    TextView mUiDescription;
    ImageView mUiNear;
    ImageView mUiNavigate;
    TextView mUiMoreWines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mProducer = (ProducerListItem) extras.getSerializable(ProducersFragment.PRODUCER_TAG);
        }
        Log.e(ProducerActitvity.class.getName(), mProducer + " ");
        initView();

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
        mUiDescription = (TextView) findViewById(R.id.activity_news_details_description);

        int[] images = {
                R.drawable.placeholder_image, R.drawable.placeholder_image
        };
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, images);
        mUiPager.setAdapter(adapter);
        mUiPageIndicator.setViewPager(mUiPager);
        mUiNear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Place extraPlace = new Place(mProducerFromBase.mIdProducer,
                        mProducerFromBase.mName, null, mProducerFromBase.mLng,
                        mProducerFromBase.mLat, "Producer");

                Intent intent = new Intent(ProducerActitvity.this, NearPlaceActivity.class);
                intent.putExtra(NearPlaceActivity.TAG_PLACE, extraPlace);

                startActivityForResult(intent, NearPlaceActivity.REQUEST);

            }
        });
        mUiNavigate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Place extraPlace = new Place(mProducerFromBase.mIdProducer,
                        mProducerFromBase.mName, null, mProducerFromBase.mLng,
                        mProducerFromBase.mLat, "Producer");

                Intent intent = new Intent(ProducerActitvity.this, NavigateToActivity.class);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO, extraPlace);

                startActivityForResult(intent, NavigateToActivity.REQUEST);

            }
        });
        mUiMoreWines.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(ProducerActitvity.this, "Not working yet", Toast.LENGTH_LONG).show();

            }

        });
    }

    public void fillView() {
        Log.e("fillView", mProducerFromBase.mIdProducer + " " + mProducerFromBase.mName + " "
                + mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber + " "
                + mProducerFromBase.mHouseNumber + " " + mProducerFromBase.mCity + " "
                + mProducerFromBase.mPostCode);
        mUiTitle.setText(mProducerFromBase.mName);
        mUiAddress.setText(mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber
                + " " + mProducerFromBase.mHouseNumber + " " + mProducerFromBase.mCity + " "
                + mProducerFromBase.mPostCode);
        mUiUrl.setText(mProducerFromBase.mLink);
        mUiDescription.setText(mProducerFromBase.mVast);
        mUiPhoneNumber.setText(mProducerFromBase.mPhone);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if there is an access to the Internet, try to load data from remote database

        if (App.isOnline(ProducerActitvity.this)) {
            new LoadProducerTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(ProducerActitvity.this, "Cannot connect to the Internet",
                    Toast.LENGTH_LONG).show();
        }

    }

    class LoadProducerTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(ProducerActitvity.this);
            mProgDial.setMessage("Loading producer details...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            // mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mProducer.mIdProducer));

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, params);

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            ProducerDetails response = gson.fromJson(reader, ProducerDetails.class);
            Log.e(ProducerActitvity.class.getName(), response.mIdProducer + " ");
            if (response != null) {
                mProducerFromBase = response;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mProducerFromBase != null) {
                fillView();
            }

            Ion.with(mUiImage).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mProducerFromBase.mImageUrl);

        }

    }

}
