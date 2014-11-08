
package pl.tokajiwines.acitivities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.R.drawable;
import pl.tokajiwines.R.id;
import pl.tokajiwines.R.layout;
import pl.tokajiwines.R.menu;
import pl.tokajiwines.R.string;
import pl.tokajiwines.acitivities.ProducerActivity.LoadProducerTask;
import pl.tokajiwines.adapters.ImagePagerAdapter;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.jsonresponses.ImagesResponse;
import pl.tokajiwines.jsonresponses.ProducerDetails;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.WineDetails;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WineActivity extends BaseActivity {
    
    Context mContext;
    
    TextView mUiName;
    ImageView mUiImage;
    TextView mUiProducerName;
    TextView mUiTaste;
    TextView mUiType;
    TextView mUiStrain;
    TextView mUiYear;
    TextView mUiPrice;
    TextView mUiDescription;
    
    WineListItem mWine;
    WineDetails mWineDetails;
    private String sUrl;
    ProgressDialog mProgDial;
    JSONParser mParser;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_details);
        
        sUrl = getResources().getString(R.string.UrlWineDetails);
        mContext = this;
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mWine = (WineListItem) extras.getSerializable(WinesListActivity.TAG_WINE);
        }
        
        initView();
    }
    
    public void initView()
    {
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
        
        mUiName.setText(mWine.mName);
        mUiProducerName.setText(mWine.mProducerName);
        mUiTaste.setText(mWine.mFlavourName);
        mUiType.setText(mWine.mGrade);
        mUiStrain.setText(mWine.mStrains);
        mUiYear.setText(mWine.mYear);
        mUiPrice.setText(mWine.mPrice);
        
        mUiProducerName.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, ProducerActivity.class);
                intent.putExtra(ProducersFragment.PRODUCER_TAG, new ProducerListItem(mWine.mIdProducer, mWine.mProducerName, ""));

                startActivity(intent);
            }
        });
    }
    
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if there is an access to the Internet, try to load data from remote database

        if (App.isOnline(WineActivity.this)) {
            new LoadWineTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(WineActivity.this, "Cannot connect to the Internet",
                    Toast.LENGTH_LONG).show();
        }

    }

    
    class LoadWineTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(WineActivity.this);
            mProgDial.setMessage("Loading wine details...");
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

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, params);

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            WineDetails response = gson.fromJson(reader, WineDetails.class);
            
            if (response != null) {
                mWineDetails = response;
            }


            return null;

        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWineDetails != null) {
                mUiDescription.setText(mWineDetails.mDescription);
            }
            
            if (mWineDetails.mImageUrl != null)
            {
                Ion.with(mUiImage).placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).load(mWineDetails.mImageUrl);
            }
        }

    }
    
    
}
    


  
