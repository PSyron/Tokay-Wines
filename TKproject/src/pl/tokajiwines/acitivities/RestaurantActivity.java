
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.TabRestaurantsFragment;
import pl.tokajiwines.jsonresponses.RestaurantDetails;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BaseActivity {
    JSONParser mParser;
    public static int REQUEST = 997;
    private static final String sUrl = "http://remzo.usermd.net/zpi/services/restaurantDetails.php";
    ProgressDialog mProgDial;
    RestaurantListItem mRestaurant;
    RestaurantDetails mRestaurantFromBase;

    TextView mUiTitle;
    ImageView mUiImage;
    ViewPager mUiPager;
    CirclePageIndicator mUiPageIndicator;
    TextView mUiAddress;
    TextView mUiPhoneNumber;
    TextView mUiUrl;
    TextView mUiDescription;
    ImageView mUiNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRestaurant = (RestaurantListItem) extras
                    .getSerializable(TabRestaurantsFragment.RESTAURANT_TAG);
        }

        Log.e(RestaurantActivity.class.getName(), mRestaurant + " ");
        initView();

    }

    public void initView() {

        getActionBar().setTitle(mRestaurant.mName);
        mUiTitle = (TextView) findViewById(R.id.activity_restaurant_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_restaurant_details_image);
        //      mUiPager = (ViewPager) findViewById(R.id.activity_restaurant_details_pager);
        //      mUiPageIndicator = (CirclePageIndicator) findViewById(R.id.activity_restaurant_details_indicator);
        mUiAddress = (TextView) findViewById(R.id.activity_restaurant_details_adress);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_restaurant_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_restaurant_details_url);
        mUiNavigate = (ImageView) findViewById(R.id.activity_restaurant_navigate);
        mUiDescription = (TextView) findViewById(R.id.activity_restaurant_details_description);

        int[] images = {
                R.drawable.placeholder_image, R.drawable.placeholder_image
        };

        mUiNavigate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Place extraPlace = new Place(mRestaurantFromBase.mIdRestaurant,
                        mRestaurantFromBase.mName, mRestaurantFromBase.mStreetName + " "
                                + mRestaurantFromBase.mStreetNumber + " "
                                + mRestaurantFromBase.mHouseNumber + " "
                                + mRestaurantFromBase.mCity + " " + mRestaurantFromBase.mPostCode,
                        mRestaurantFromBase.mLng, mRestaurantFromBase.mLat, "Restaurant");

                Intent intent = new Intent(RestaurantActivity.this, NavigateToActivity.class);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO, extraPlace);
                intent.putExtra(NavigateToActivity.TAG_PLACE_TO_IMAGE,
                        mRestaurantFromBase.mImageUrl);
                startActivityForResult(intent, NavigateToActivity.REQUEST);
            }
        });
    }

    public void fillView() {
        Log.e("fillView", mRestaurantFromBase.mIdRestaurant + " " + mRestaurantFromBase.mName + " "
                + mRestaurantFromBase.mStreetName + " " + mRestaurantFromBase.mStreetNumber + " "
                + mRestaurantFromBase.mHouseNumber + " " + mRestaurantFromBase.mCity + " "
                + mRestaurantFromBase.mPostCode);
        mUiTitle.setText(mRestaurantFromBase.mName);
        mUiAddress.setText(mRestaurantFromBase.mStreetName + " "
                + mRestaurantFromBase.mStreetNumber + " " + mRestaurantFromBase.mHouseNumber + " "
                + mRestaurantFromBase.mCity + " " + mRestaurantFromBase.mPostCode);
        mUiUrl.setText(mRestaurantFromBase.mLink);
        mUiDescription.setText(mRestaurantFromBase.mVast);
        mUiPhoneNumber.setText(mRestaurantFromBase.mPhone);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if there is an access to the Internet, try to load data from remote database

        if (App.isOnline(RestaurantActivity.this)) {
            new LoadRestaurantTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(RestaurantActivity.this, "Cannot connect to the Internet",
                    Toast.LENGTH_LONG).show();
        }

    }

    class LoadRestaurantTask extends AsyncTask<Void, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(RestaurantActivity.this);
            mProgDial.setMessage("Loading restaurant details...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            // mProgDial.show();

        }

        // retrieving Restaurants data

        @Override
        protected String doInBackground(Void... args) {

            mParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("who", "" + mRestaurant.mIdRestaurant));

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, params);

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            RestaurantDetails response = gson.fromJson(reader, RestaurantDetails.class);
            Log.e(RestaurantActivity.class.getName(), response.mIdRestaurant + " ");
            if (response != null) {
                mRestaurantFromBase = response;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of Restaurants

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mRestaurantFromBase != null) {
                fillView();
            }

            Ion.with(mUiImage).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mRestaurantFromBase.mImageUrl);

        }

    }

}
