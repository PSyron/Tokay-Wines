
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
import android.widget.ListView;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.activities.NewsActivity;
import pl.tokajiwines.adapters.NewsAdapter;
import pl.tokajiwines.db.NewsDataSource;
import pl.tokajiwines.jsonresponses.NewsListItem;
import pl.tokajiwines.jsonresponses.NewsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class NewsFragment extends BaseFragment {

    ListView mUiList;
    NewsAdapter mAdapter;
    boolean mIsViewFilled;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;
    public static final String TAG_ID_NEWS = "IdNews";
    private NewsListItem[] mNewsList;
    LoadNewsTask mLoadNewsTask;
    LoadNewsOnlineTask mLoadNewsOnlineTask;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        sUrl = getResources().getString(R.string.UrlNewsList);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mNewsList = new NewsListItem[0];
        mAdapter = new NewsAdapter(getActivity(), mNewsList);
        mUiList.setAdapter(mAdapter);
        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                intent.putExtra(TAG_ID_NEWS, mAdapter.getItemId(position));
                startActivity(intent);
            }
        });
        mContext = getActivity();
        mIsViewFilled = false;

        return rootView;
    }

    public void fillView() {
        Log.e("fillView", "View filled");
        mAdapter = new NewsAdapter(getActivity(), mNewsList);
        mUiList.setAdapter(mAdapter);
        mIsViewFilled = true;

    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mNewsList.length == 0) {

            if (App.isOnline(mContext)) {
                mLoadNewsOnlineTask = new LoadNewsOnlineTask();
                mLoadNewsOnlineTask.execute();
            }

            // otherwise, show message

            else {
                /*Toast.makeText(mContext, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();*/
                /*Toast.makeText(mContext, "Baza offline", Toast.LENGTH_LONG).show();*/
                mLoadNewsTask = new LoadNewsTask();
                mLoadNewsTask.execute();
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

        if (mLoadNewsTask != null) {

            mLoadNewsTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadNewsTask = null;
        }
        
        if (mLoadNewsOnlineTask != null) {

            mLoadNewsOnlineTask.cancel(true);
            if (mProgDial != null) {
                mProgDial.dismiss();
            }

            mLoadNewsOnlineTask = null;
        }
        super.onPause();
    }

    // async task class that loads news data from remote database
    class LoadNewsTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_news));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();
        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            NewsDataSource nDs = new NewsDataSource(mContext);
            nDs.open();
            mNewsList = nDs.getNewsList();
            nDs.close();
            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mNewsList != null) {
                fillView();
            }

            mLoadNewsTask = null;
        }

    }

    class LoadNewsOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null) {
                mProgDial = new ProgressDialog(mContext);
            }
            mProgDial.setMessage(getResources().getString(R.string.loading_news));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();
        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, null);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                NewsResponse response = gson.fromJson(reader, NewsResponse.class);

                if (response != null) {
                    mNewsList = response.news;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mNewsList != null) {
                fillView();
            }

            mLoadNewsOnlineTask = null;
        }

    }
}
