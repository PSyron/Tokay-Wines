
package pl.tokajiwines.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ProducersAdapter;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.models.ProducerListItem;

import java.util.ArrayList;

public class ProducersFragment extends BaseFragment {

    ListView mUiList;
    ProducersAdapter mAdapter;
    JSONParser mParser;
    ProgressDialog mProgDial;
    Context mContext;
    private static final String sUrl = "http://remzo.usermd.net/zpi/producers.php";
    private static final String TAG_ID = "idProducer";
    private static final String TAG_NAME = "name";
    private static final String TAG_SHORT_MESSAGE = "short";
    private static final String TAG_PRODUCERS ="producers";
    private JSONArray mProducersJSON = null;
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
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mContext = getActivity();

        return rootView;
    }
    
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
        if (App.isOnline(mContext))
        {
            new ConnectingToDatabaseTask().execute();
        }
        
        else
        {
            Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    
    class ConnectingToDatabaseTask extends AsyncTask<String, String, String> {


       boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           mProgDial = new ProgressDialog(mContext);
           mProgDial.setMessage("Loading producers data...");
           mProgDial.setIndeterminate(false);
           mProgDial.setCancelable(true);
           mProgDial.show();

       }

       @Override
       protected String doInBackground(String... args) {
           // TODO Auto-generated method stub
           
           mParser = new JSONParser();
           JSONObject json = mParser.getJSONFromUrl(sUrl, Constans.sUsername, Constans.sPassword);
           
           
           try
           {
               mProducersJSON = json.getJSONArray(TAG_PRODUCERS);
               
               mProducersList = new ProducerListItem[mProducersJSON.length()];
               
               System.out.println(mProducersJSON.length());
               
               for (int i = 0; i < mProducersJSON.length(); i++)
               {
                   JSONObject  p = mProducersJSON.getJSONObject(i);
                   mProducersList[i] = new ProducerListItem(p.getInt(TAG_ID), p.getString(TAG_NAME), p.getString(TAG_SHORT_MESSAGE));
                   
               }
           }
           
           catch(JSONException e)
           {
               e.printStackTrace();
           }


           return null;

       }

       protected void onPostExecute(String file_url) {
           
           super.onPostExecute(file_url);
           mProgDial.dismiss();
           mAdapter = new ProducersAdapter(getActivity(),mProducersList);
           mUiList.setAdapter(mAdapter);
    
       }
 

       }
}
