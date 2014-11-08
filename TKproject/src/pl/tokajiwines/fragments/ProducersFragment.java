
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
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.acitivities.ProducerActivity;
import pl.tokajiwines.adapters.ProducersAdapter;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.ProducersResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ProducersFragment extends BaseFragment {

    ListView mUiList;
    ProducersAdapter mAdapter;
    JSONParser mParser;
    ProgressDialog mProgDial;
    Context mContext;
    private String sUrl;
    private static final String TAG_ID = "idProducer";
    private static final String TAG_NAME = "name";
    private static final String TAG_SHORT_MESSAGE = "short";
    private static final String TAG_PRODUCERS = "producers";
    private JSONArray mProducersJSON = null;
    private ProducerListItem[] mProducersList;
    public static final String PRODUCER_TAG = "producer";

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
                intent.putExtra(PRODUCER_TAG, temp);

                startActivityForResult(intent, ProducerActivity.REQUEST);

            }

        });

        return rootView;
    }

    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // if there is an access to the Internet, try to load data from remote database
        
        if (mProducersList.length == 0)
        {

            if (App.isOnline(mContext)) {
                new LoadProducersTask().execute();
    
                //            ProducersDataSource pDs = new ProducersDataSource(mContext);
                //            pDs.open();
                //
                //            List<Producer> prodlist = pDs.getAllProducers();
                //            Log.i("Producent List", prodlist.toString());
                //            ProducerListItem[] producers = {
                //                    new ProducerListItem(prodlist.get(0)), new ProducerListItem(prodlist.get(1)),
                //                    new ProducerListItem(prodlist.get(2))
                //            };
                //            ProducersAdapter mAdapter = new ProducersAdapter(getActivity(), producers);
                //            mUiList.setAdapter(mAdapter);
            }
    
            // otherwise, show message
    
            else {
                Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }

    }

    // async task class that loads producers data from remote database

    class LoadProducersTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mContext);
            mProgDial.setMessage("Loading producers data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving producers data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            ProducersResponse response = gson.fromJson(reader, ProducersResponse.class);

            if (response != null) {
                System.out.println(response.producers[0].mName);
                mProducersList = response.producers;
            }

            return null;
            /*       // TODO Auto-generated method stub
                   
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
            */
        }

        // create adapter that contains loaded data and show list of producers

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            mAdapter = new ProducersAdapter(getActivity(), mProducersList);
            mUiList.setAdapter(mAdapter);

        }

    }
}
