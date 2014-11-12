
package pl.tokajiwines.acitivities;

import android.app.Activity;
import android.app.ProgressDialog;
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
import pl.tokajiwines.adapters.WinesAdapter;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.fragments.WinesFilterFragment;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.jsonresponses.WinesResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WinesListActivity extends BaseActivity {

    ListView mUiList;
    WinesAdapter mAdapter;
    private WineListItem[] mWinesList;
    private Activity mAct;
    private JSONParser mParser;
    private ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private String mFlavours;
    private String mStrains;
    private String mGrades;
    private String mYears;
    private String mProducers;
    private String mPrices;
    public static String TAG_WINE = "WINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wines);
        getActionBar().setTitle(getResources().getString(R.string.title_wines));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mFlavours = (String) extras.getString(WinesFilterFragment.TAG_FLAVOURS + "");
            mGrades = (String) extras.getString(WinesFilterFragment.TAG_GRADES + "");
            mStrains = (String) extras.getString(WinesFilterFragment.TAG_STRAINS + "");
            mProducers = (String) extras.getString(WinesFilterFragment.TAG_PRODUCERS + "");
            mYears = (String) extras.getString(WinesFilterFragment.TAG_YEARS + "");
            mPrices = (String) extras.getString(WinesFilterFragment.TAG_PRICES + "");


        }

        mAct = this;
        
        sUrl = getResources().getString(R.string.UrlWinesList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        
        mWinesList = new WineListItem[0];
        mUiList = (ListView) findViewById(R.id.activity_wines_list);
        mAdapter = new WinesAdapter(this, mWinesList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(mAct, WineActivity.class);
                intent.putExtra(TAG_WINE, (WineListItem) mAdapter.getItem(position));
                intent.putExtra(WineActivity.TAG_CALLED_FROM_PRODUCER, false);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        if (mWinesList.length == 0) {

            if (App.isOnline(mAct)) {
                new LoadWinesTask().execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mAct, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }

    }

    class LoadWinesTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mAct);
            mProgDial.setMessage("Loading wines data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lang", ""
                    + SharedPreferencesHelper.getSharedPreferencesInt(mAct,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage)));
            params.add(new BasicNameValuePair("flavours", mFlavours));
            params.add(new BasicNameValuePair("grades", mGrades));
            params.add(new BasicNameValuePair("strains", mStrains));
            params.add(new BasicNameValuePair("producers", mProducers));
            params.add(new BasicNameValuePair("years", mYears));
            params.add(new BasicNameValuePair("prices", mPrices));

            InputStream source = mParser.retrieveStream(sUrl, sUsername,
                    sPassword, params);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            WinesResponse response = gson.fromJson(reader, WinesResponse.class);

            if (response != null) {
                mWinesList = response.wines;
                System.out.println(response);
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWinesList != null) {
                mAdapter = new WinesAdapter(mAct, mWinesList);
                mUiList.setAdapter(mAdapter);
            }

        }

    }

}
