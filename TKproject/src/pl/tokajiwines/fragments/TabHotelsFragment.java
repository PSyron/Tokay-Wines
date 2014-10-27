
package pl.tokajiwines.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.HotelsAdapter;
import pl.tokajiwines.models.HotelsListItem;
import pl.tokajiwines.utils.JSONParser;

public class TabHotelsFragment extends BaseFragment {

    ListView mUiList;
    HotelsAdapter mAdapter;
    Context mContext;
    JSONParser mParser;
    ProgressDialog mProgDial;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/hotels.php";
    public static final String TAG_ID_HOTEL = "IdHotel";
    private HotelsListItem[] mHotelsList;

    public static TabHotelsFragment newInstance() {
        TabHotelsFragment fragment = new TabHotelsFragment();

        return fragment;
    }

    public TabHotelsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hotels, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_hotels_list);
        return rootView;
    }

    // if there is an access to the Internet, try to load data from remote database

    /*   public void onResume() {
           // TODO Auto-generated method stub
           super.onResume();

           if (App.isOnline(mContext)) {
               new LoadHotelsTask().execute();
           }

           // otherwise, show message

           else {
               Toast.makeText(mContext, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
           }

       }

       // async task class that loads news data from remote database

       class LoadHotelsTask extends AsyncTask<String, String, String> {

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

               HotelsResponse response = gson.fromJson(reader, HotelsResponse.class);

               if (response != null) {
                   mHotelsList = response.hotels;
               }

               return null;

           }

           // create adapter that contains loaded data and show list of news

           protected void onPostExecute(String file_url) {

               super.onPostExecute(file_url);
               mProgDial.dismiss();
               mAdapter = new HotelsAdapter(getActivity(), mHotelsList);
               mUiList.setAdapter(mAdapter);
               mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                       Intent intent = new Intent(mContext, EventActivity.class);
                       intent.putExtra(TAG_ID_HOTEL, mAdapter.getItemId(position));
                       startActivity(intent);
                   }
               });

           }

       }*/
}
