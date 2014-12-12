
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ProducersAdapter;
import pl.tokajiwines.db.HotelsDataSource;
import pl.tokajiwines.db.ProducersDataSource;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.ProducersResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProducersSearchActivity extends BaseActivity {

    boolean mIsViewFilled;
    ListView mUiList;
    ProducersAdapter mAdapter;
    JSONParser mParser;
    ProgressDialog mProgDial;
    LoadProducersTask mLoadProducersTask;
    LoadProducersOnlineTask mLoadProducersOnlineTask;
    Context mContext;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private ProducerListItem[] mProducersList;
    private String mName;
    public static final String PRODUCER_TAG = "producer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_search);
        getActionBar().setTitle(getResources().getString(R.string.title_wineyards));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = (String) extras.getString(SearchableActivity.TAG_NAME);
        }
        mContext = this;

        sUrl = getResources().getString(R.string.UrlProducersList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mProducersList = new ProducerListItem[0];
        mUiList = (ListView) findViewById(R.id.activity_producers_search_list);
        mAdapter = new ProducersAdapter(this, mProducersList);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProducerListItem temp = (ProducerListItem) mAdapter.getItem(position);
                Log.e(ProducersFragment.class.getName(), temp + " ");
                Intent intent = new Intent(mContext, ProducerActivity.class);
                intent.putExtra(PRODUCER_TAG, temp);

                startActivityForResult(intent, ProducerActivity.REQUEST);

            }

        });
        mIsViewFilled = false;
    }

    public void fillView() {

        Log.e("fillView", "View filled");
        mAdapter = new ProducersAdapter(this, mProducersList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mProducersList.length == 0) {

            if (App.isOnline(mContext)) {
                mLoadProducersOnlineTask = new LoadProducersOnlineTask();
                mLoadProducersOnlineTask.execute();
            }

            // otherwise, show message

            else {
                mLoadProducersTask = new LoadProducersTask();
                mLoadProducersTask.execute();
            }
        } else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    protected void onPause() {

        if (mLoadProducersTask != null) {

            mLoadProducersTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadProducersTask = null;
        }
        
        if (mLoadProducersOnlineTask != null) {

            mLoadProducersOnlineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadProducersOnlineTask = null;
        }
        super.onPause();
    }

    class LoadProducersTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_producers));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            ProducersDataSource pDs = new ProducersDataSource(ProducersSearchActivity.this);
            pDs.open();
            mProducersList = pDs.getProducers(mName);
            pDs.close();

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mProducersList != null) {
                fillView();
            }

            mLoadProducersTask = null;

        }

    }
    
    class LoadProducersOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_producers));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", mName));            

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                ProducersResponse response = gson.fromJson(reader, ProducersResponse.class);

                if (response != null) {
                    mProducersList = response.producers;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mProducersList != null) {
                fillView();
            }

            mLoadProducersOnlineTask = null;

        }

    }

}
