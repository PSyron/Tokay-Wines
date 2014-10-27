
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
import android.widget.Toast;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.EventActivity;
import pl.tokajiwines.adapters.NewsAdapter;
import pl.tokajiwines.models.NewsListItem;
import pl.tokajiwines.models.NewsResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public class NewsFragment extends BaseFragment {

    ListView mUiList;
    NewsAdapter mAdapter;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/news.php";
    public static final String TAG_ID_NEWS = "IdNews";
    private NewsListItem[] mNewsList;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mContext = getActivity();

        return rootView;
    }

    // if there is an access to the Internet, try to load data from remote database

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (App.isOnline(mContext)) {
            new LoadNewsTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    // async task class that loads news data from remote database

    class LoadNewsTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mContext);
            mProgDial.setMessage("Loading news data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            NewsResponse response = gson.fromJson(reader, NewsResponse.class);

            if (response != null) {
                mNewsList = response.news;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            mAdapter = new NewsAdapter(getActivity(), mNewsList);
            mUiList.setAdapter(mAdapter);

            mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(mContext, EventActivity.class);
                    intent.putExtra(TAG_ID_NEWS, mAdapter.getItemId(position));
                    startActivity(intent);
                }
            });

        }

    }
}
