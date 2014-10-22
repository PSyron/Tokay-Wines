
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.NewsFragment;
import pl.tokajiwines.models.NewsDetailsResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends BaseActivity {
    
    private CharSequence mTitle;
    private int mIdNews;
    ProgressDialog mProgDial;
    Context mContext;
    JSONParser mParser;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/newsdetail.php";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.item_wine_filter);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mIdNews = (int)extras.getLong(NewsFragment.TAG_ID_NEWS);  
        }
    }
    
    public void onResume() {
        
        super.onResume();
        
        if (App.isOnline(mContext))
        {
            new LoadNewsDetailsTask().execute();
        }
        
        // otherwise, show message
        
        else
        {
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
           params.add(new BasicNameValuePair("idNews", ""+mIdNews));

           
           InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername, Constans.sPassword, params);
           Gson gson = new Gson();
           InputStreamReader reader = new InputStreamReader(source);
           
           NewsDetailsResponse response = gson.fromJson(reader, NewsDetailsResponse.class);
           
           if (response != null)
           {
               System.out.println(response.success);
               System.out.println(response.message);
               System.out.println(response.news.header);

           }
           
           
           return null;

       }
       
       // create adapter that contains loaded data and show list of news

       protected void onPostExecute(String file_url) {
           
           super.onPostExecute(file_url);
           mProgDial.dismiss();
    
       }
 

    }

}
