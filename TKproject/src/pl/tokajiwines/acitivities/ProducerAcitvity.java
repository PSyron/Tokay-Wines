
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.models.ProducerDetails;
import pl.tokajiwines.models.ProducerListItem;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ProducerAcitvity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/producerDetails.php";
    ProgressDialog mProgDial;
    ProducerListItem mProducer;
    ProducerDetails mProducerFromBase;

    TextView mUiTitle;
    ImageView mUiImage;
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
        Log.e(ProducerAcitvity.class.getName(), mProducer + " ");
        initView();

    }

    public void initView() {
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(mProducer.mName);
        mUiTitle = (TextView) findViewById(R.id.activity_news_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_news_details_image);
        mUiAddress = (TextView) findViewById(R.id.activity_producer_details_adress);
        mUiOpeningHours = (TextView) findViewById(R.id.activity_producer_details_hours);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_producer_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_producer_details_url);
        mUiNear = (ImageView) findViewById(R.id.activity_producer_navigate);
        mUiNavigate = (ImageView) findViewById(R.id.activity_producer_neighborhood);
        mUiMoreWines = (TextView) findViewById(R.id.activity_producer_details_wine_button);
        mUiDescription = (TextView) findViewById(R.id.activity_news_details_description);
    }

    public void fillView() {
        Log.e("fillView", mProducerFromBase.mIdProducer + " " + mProducerFromBase.mName + " "
                + mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber + " " + mProducerFromBase.mHouseNumber + " "
                + mProducerFromBase.mCity + " " + mProducerFromBase.mPostCode);
        mUiTitle.setText(mProducerFromBase.mName);
        mUiAddress.setText(mProducerFromBase.mStreetName + " " + mProducerFromBase.mStreetNumber+ " " + mProducerFromBase.mHouseNumber
                + " " + mProducerFromBase.mCity + " " + mProducerFromBase.mPostCode);
        mUiUrl.setText(mProducerFromBase.mLink);
        mUiDescription.setText(mProducerFromBase.mVast);
        mUiPhoneNumber.setText(mProducerFromBase.mPhone);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if there is an access to the Internet, try to load data from remote database

        if (App.isOnline(ProducerAcitvity.this)) {
            new LoadProducerTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(ProducerAcitvity.this, "Cannot connect to the Internet",
                    Toast.LENGTH_LONG).show();
        }

    }

    class LoadProducerTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(ProducerAcitvity.this);
            mProgDial.setMessage("Loading producer details...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            // mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();
  //          String tempUrl = sUrl + "?who=" + mProducer.mIdProducer;
    //        Log.e(ProducerAcitvity.class.getName(), tempUrl);
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mProducer.mIdProducer));
            
            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, params);

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            ProducerDetails response = gson.fromJson(reader, ProducerDetails.class);
            Log.e(ProducerAcitvity.class.getName(), response.mIdProducer + " ");
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
        }

    }

}
