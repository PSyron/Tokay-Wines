package pl.tokajiwines.asyncs;

import java.io.InputStream;
import java.io.InputStreamReader;

import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.SearchItemsResponse;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SuggestionProvider;

import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;


public class GetSearchItemsAsync extends AsyncTask<Void, String, String>{
    
    JSONParser mParser;
    private String sUsername;
    private String sPassword;
    private String sUrl;
    private Context mContext;
    
    public GetSearchItemsAsync(Context context)
    {
        mContext = context;
        sUrl = mContext.getResources().getString(R.string.UrlSearchItems);
        sUsername = mContext.getString(R.string.Username);
        sPassword = mContext.getString(R.string.Password);
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    // retrieving search items data

    @Override
    protected String doInBackground(Void... args) {

        mParser = new JSONParser();

        InputStream source = mParser.retrieveStream(sUrl, sUsername,
                sPassword, null);
        if (source != null)
        {

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            SearchItemsResponse response = gson.fromJson(reader, SearchItemsResponse.class);

            if (response != null) {
                SuggestionProvider.sSearchItems = response.items;
            }
        }

        return null;

    }

 

    protected void onPostExecute(String file_url) {

    }




}
