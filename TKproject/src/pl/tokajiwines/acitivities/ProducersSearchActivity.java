
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
import org.json.JSONArray;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ProducersAdapter;
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
    Context mContext;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    private static final String TAG_ID = "idProducer";
    private static final String TAG_NAME = "name";
    private static final String TAG_SHORT_MESSAGE = "short";
    private static final String TAG_PRODUCERS = "producers";
    private JSONArray mProducersJSON = null;
    private ProducerListItem[] mProducersList;
    public static final String PRODUCER_TAG = "producer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_search);
        getActionBar().setTitle(getResources().getString(R.string.title_wineyards));
        Bundle extras = getIntent().getExtras();
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
                mLoadProducersTask = new LoadProducersTask();
                mLoadProducersTask.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();
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
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, null);
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

            mLoadProducersTask = null;

        }

    }

}
