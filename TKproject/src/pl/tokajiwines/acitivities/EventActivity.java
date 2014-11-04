
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.NewsFragment;
import pl.tokajiwines.jsonresponses.NewsDetails;
import pl.tokajiwines.jsonresponses.NewsDetailsResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends BaseActivity {

    private int mIdNews;
    private TextView mUiName;
    private ImageView mUiImage;
    private ImageView mUiAddIcon;
    private TextView mUiDescription;
    private TextView mUiDateLabel;
    private TextView mUiDate;
    private NewsDetails mNews;

    ProgressDialog mProgDial;
    Context mContext;
    JSONParser mParser;
    private String sUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_details);

        mContext = this;
        
        sUrl = getResources().getString(R.string.UrlEventDetails);

        mUiName = (TextView) findViewById(R.id.activity_news_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_news_details_image);
        mUiDescription = (TextView) findViewById(R.id.activity_news_details_description);
        mUiDateLabel = (TextView) findViewById(R.id.activity_news_details_datelabel);
        mUiDate = (TextView) findViewById(R.id.activity_news_details_date);
        mUiAddIcon = (ImageView) findViewById(R.id.activity_news_details_add_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mIdNews = (int) extras.getLong(NewsFragment.TAG_ID_NEWS);

        }
    }

    public void onResume() {

        super.onResume();

        if (App.isOnline(mContext)) {
            new LoadNewsDetailsTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    // async task class that loads news data from remote database

    class LoadNewsDetailsTask extends AsyncTask<String, String, String> {

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

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idNews", "" + mIdNews));

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, params);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            NewsDetailsResponse response = gson.fromJson(reader, NewsDetailsResponse.class);
            mNews = response.news;

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            mUiName.setText(mNews.mHeader);
            mUiDescription.setText(mNews.mVast);
            Ion.with(mUiImage).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mNews.mImage);

            if (mNews.mStartDate == null && mNews.mEndDate == null) {
                mUiDateLabel.setVisibility(View.INVISIBLE);
                mUiDate.setVisibility(View.INVISIBLE);
                mUiAddIcon.setVisibility(View.INVISIBLE);
            }
            else
            {
                mUiDate.setText(mNews.mStartDate+" - \n"+mNews.mEndDate);
            }
            getActionBar().setTitle(mNews.mHeader);

        }

    }

}
