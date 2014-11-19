
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.view.View.OnClickListener;
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
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsActivity extends BaseActivity {

    private int mIdNews;
    private TextView mUiName;
    private ImageView mUiImage;
    private ImageView mUiAddIcon;
    private TextView mUiDescription;
    private TextView mUiDateLabel;
    private TextView mUiDate;
    private NewsDetails mNews;
    LoadNewsDetailsTask mTask;
    ProgressDialog mProgDial;
    Context mContext;
    JSONParser mParser;
    private String sUrl;
    private String sUsername;
    private String sPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_details);

        mContext = this;
        mTask = new LoadNewsDetailsTask();

        sUrl = getResources().getString(R.string.UrlEventDetails);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

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
        mUiAddIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = format.parse(mNews.mStartDate);

                } catch (ParseException e) {

                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(Events.DESCRIPTION, mNews.mVast);
                intent.putExtra("allDay", true);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime());
                //    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", mNews.mHeader);
                startActivity(intent);

            }
        });
    }

    public void onResume() {

        super.onResume();

        if (mNews == null) {

            if (App.isOnline(mContext)) {

                mTask.execute();
            }

            // otherwise, show message

            else {
                Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    @Override
    protected void onPause() {
        if (mTask != null) {
            mTask.cancel(true);
        }
        super.onPause();
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

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null)
            {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);
    
                NewsDetailsResponse response = gson.fromJson(reader, NewsDetailsResponse.class);
                mNews = response.news;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mNews !=null)
            {
                    mUiName.setText(mNews.mHeader);
                    mUiDescription.setText(mNews.mVast);
                    Ion.with(mUiImage).placeholder(R.drawable.no_image_big).error(R.drawable.error_image)
                            .load(mNews.mImage);
    
                if (mNews.mStartDate == null && mNews.mEndDate == null) {
                    mUiDateLabel.setVisibility(View.GONE);
                    mUiDate.setVisibility(View.GONE);
                    mUiAddIcon.setVisibility(View.GONE);
                } else {
                    mUiDate.setText(mNews.mStartDate + " - " + mNews.mEndDate);
                    mUiDate.setVisibility(View.VISIBLE);
                    mUiAddIcon.setVisibility(View.VISIBLE);
                }
    
                getActionBar().setTitle(mNews.mHeader);
            }

        }

    }

}
