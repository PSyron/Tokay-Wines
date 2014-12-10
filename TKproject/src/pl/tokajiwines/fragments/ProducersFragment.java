
package pl.tokajiwines.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.activities.ProducerActivity;
import pl.tokajiwines.adapters.ProducersAdapter;
import pl.tokajiwines.db.ProducersDataSource;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.ProducersResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ProducersFragment extends BaseFragment {

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

    public static ProducersFragment newInstance() {
        ProducersFragment fragment = new ProducersFragment();
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public ProducersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        sUrl = getResources().getString(R.string.UrlProducersList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mContext = getActivity();
        mProducersList = new ProducerListItem[0];
        mAdapter = new ProducersAdapter(getActivity(), mProducersList);
        mUiList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProducerListItem temp = (ProducerListItem) mAdapter.getItem(position);
                Log.e(ProducersFragment.class.getName(), temp + " ");
                Intent intent = new Intent(mContext, ProducerActivity.class);
                intent.putExtra(ProducerActivity.PRODUCER_TAG, temp);

                startActivityForResult(intent, ProducerActivity.REQUEST);

            }

        });
        mIsViewFilled = false;

        return rootView;
    }

    public void fillView() {
        Log.e("fillView", "View filled");
        mAdapter = new ProducersAdapter(getActivity(), mProducersList);
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
                /*Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                    Toast.LENGTH_LONG).show();
                */
                /*Toast.makeText(mContext, "Offline database", Toast.LENGTH_LONG).show();*/
                mLoadProducersTask = new LoadProducersTask();
                mLoadProducersTask.execute();
            }
        }

        else {
            if (!mIsViewFilled) {
                fillView();
            }
        }

    }

    @Override
    public void onPause() {

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

    // async task class that loads producers data from remote database

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

        // retrieving producers data

        @Override
        protected String doInBackground(String... args) {

            ProducersDataSource pDs = new ProducersDataSource(mContext);
            pDs.open();
            mProducersList = pDs.getProducerList();
            pDs.close();
            return null;

        }

        // create adapter that contains loaded data and show list of producers

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
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(String... args) {
            mParser = new JSONParser();

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

        // create adapter that contains loaded data and show list of producers

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
