
package pl.tokajiwines.acitivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.WinesGridViewAdapter;
import pl.tokajiwines.db.WinesDataSource;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.jsonresponses.WinesResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WinesGridViewActivity extends BaseActivity {

    GridView mUiList;
    WinesGridViewAdapter mAdapter;
    private WineListItem[] mWinesList;
    private ProducerListItem mProducer;
    private Activity mAct;
    private JSONParser mParser;
    private ProgressDialog mProgDial;
    LoadWinesTask mLoadWinesTask;
    LoadWinesOnlineTask mLoadWinesOnlineTask;
    boolean mIsViewFilled;
    private Context mContext;
    private String sUrl;
    private String sUsername;
    private String sPassword;

    public static String TAG_WINE = "WINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wines_gridview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mProducer = (ProducerListItem) extras.getSerializable(ProducerActivity.TAG_ID_PRODUCER);
        }
        getActionBar().setTitle(
                mProducer.mName + " - " + getResources().getString(R.string.all_wines_available));
        mAct = this;
        mContext = this;
        sUrl = getResources().getString(R.string.UrlWinesGridViewList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mWinesList = new WineListItem[0];
        mUiList = (GridView) findViewById(R.id.activity_wines_gridView);
        mAdapter = new WinesGridViewAdapter(this, mWinesList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mAct, WineActivity.class);
                intent.putExtra(TAG_WINE, (WineListItem) mAdapter.getItem(position));
                intent.putExtra(WineActivity.TAG_CALLED_FROM_PRODUCER, true);
                startActivityForResult(intent, 1);
            }
        });
        mIsViewFilled = false;
    }

    public void fillView() {
        Log.e("fillView", "View filled");
        mAdapter = new WinesGridViewAdapter(mAct, mWinesList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mWinesList.length == 0) {

            if (App.isOnline(mAct)) {
                mLoadWinesOnlineTask = new LoadWinesOnlineTask();
                mLoadWinesOnlineTask.execute();
            }

            // otherwise, show message

            else {
                /*Toast.makeText(mAct, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();*/
                Toast.makeText(mAct, "Baza offline", Toast.LENGTH_LONG).show();
                mLoadWinesTask = new LoadWinesTask();
                mLoadWinesTask.execute();
            }
        } else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {

        if (mLoadWinesTask != null) {

            mLoadWinesTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadWinesTask = null;
        }
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    class LoadWinesTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mAct);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_wines));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {
            WinesDataSource wDs = new WinesDataSource(WinesGridViewActivity.this);
            wDs.open();
            WineListItem[] response = wDs.getProducerWines(mProducer.mIdProducer);
            if (response != null) {
                mWinesList = response;
            }
            wDs.close();
            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWinesList != null) {
                fillView();
            }

            mLoadWinesTask = null;

        }

    }

    class LoadWinesOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mAct);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_wines));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
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
            params.add(new BasicNameValuePair("idProducer", "" + mProducer.mIdProducer));

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                WinesResponse response = gson.fromJson(reader, WinesResponse.class);

                if (response != null) {
                    mWinesList = response.wines;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWinesList != null) {
                fillView();
            }

            mLoadWinesOnlineTask = null;

        }

    }

}
