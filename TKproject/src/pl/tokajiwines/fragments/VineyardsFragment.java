
package pl.tokajiwines.fragments;

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

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.VineyardsAdapter;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.models.ProducerListItem;

import java.util.ArrayList;

public class VineyardsFragment extends BaseFragment {

    ListView mUiList;
    VineyardsAdapter mAdapter;
    JSONParser mParser;
    private static final String sUrl = "http://remzo.usermd.net/zpi/producers.php";
    private static final String TAG_ID = "idProducer";
    private static final String TAG_NAME = "name";
    private static final String TAG_SHORT_MESSAGE = "short";
    private static final String TAG_PRODUCERS ="producers";
    private JSONArray mProducersJSON = null;
    private ArrayList<ProducerListItem> mProducersList;

    public static VineyardsFragment newInstance() {
        VineyardsFragment fragment = new VineyardsFragment();
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public VineyardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        new ConnectingToDatabaseTask().execute();
        mAdapter = new VineyardsAdapter(getActivity());
        mUiList.setAdapter(mAdapter);
        return rootView;
    }
    
    class ConnectingToDatabaseTask extends AsyncTask<String, String, String> {

        /**
        * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();

       }

       @Override
       protected String doInBackground(String... args) {
           // TODO Auto-generated method stub
           
           mParser = new JSONParser();
           JSONObject json = mParser.getJSONFromUrl(sUrl, Constans.sUsername, Constans.sPassword);
               // check your log for json response
           
           mProducersList = new ArrayList<ProducerListItem>();
           
           try
           {
               mProducersJSON = json.getJSONArray(TAG_PRODUCERS);
               
               for (int i =0; i < mProducersJSON.length(); i++)
               {
                   JSONObject  p = mProducersJSON.getJSONObject(i);
                   mProducersList.add(new ProducerListItem(p.getInt(TAG_ID), p.getString(TAG_NAME), p.getString(TAG_SHORT_MESSAGE)));
                   
               }
           }
           
           catch(JSONException e)
           {
               e.printStackTrace();
           }

           return null;

       }
       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
    
       }
 

       }
}
