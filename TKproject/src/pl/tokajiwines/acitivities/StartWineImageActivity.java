
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.jsonresponses.WinesResponse;
import pl.tokajiwines.utils.JSONParser;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartWineImageActivity extends BaseActivity {

    TextView mUiSkipBtn;
    TextView mUiHeader;
    ImageView mUiImage;
    ProgressDialog mProgDial;
    JSONParser mParser;
    private WineListItem[] mWinesList;
    //WineDetails mWineDetails;
    WineListItem mChosedItem;
    private String sUrl;
    private String sUsername;
    private String sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        sUrl = getResources().getString(R.string.UrlRandomWine);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        setContentView(R.layout.activity_starting_image);
        mUiSkipBtn = (TextView) findViewById(R.id.start_button);
        mUiImage = (ImageView) findViewById(R.id.start_image);
        mUiHeader = (TextView) findViewById(R.id.start_header);
        mUiSkipBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        new LoadWineImageTask().execute();
    }

    public void initView(final WineListItem[] winesList) {

        int randomNum;
        Random rand = new Random();
        while (true) {
            randomNum = rand.nextInt(winesList.length);
            final int temp = randomNum;
            Log.e("StartWineImage", "image " + winesList[randomNum].mImageUrl);
            if (!(winesList[randomNum].mImageUrl).contains("no_image")) {
                mChosedItem = winesList[randomNum];
                mUiHeader.setText(winesList[randomNum].mName);

                final File imgFile = new File(this.getFilesDir().getAbsolutePath()
                        + "/"
                        + winesList[randomNum].mImageUrl.substring(
                                winesList[randomNum].mImageUrl.lastIndexOf('/') + 1,
                                winesList[randomNum].mImageUrl.length()));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mUiImage.setImageBitmap(myBitmap);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            App.downloadAndRun(winesList[temp].mImageUrl,
                                    StartWineImageActivity.this, mUiImage);
                        }
                    }, 50);
                }
                mUiImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartWineImageActivity.this, WineActivity.class);
                        intent.putExtra(WinesListActivity.TAG_WINE, mChosedItem);
                        startActivity(intent);
                        finish();
                    }
                });
                return;
            }

        }

    }

    class LoadWineImageTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("StartWineImage", "onPreExecute");
            mProgDial = new ProgressDialog(StartWineImageActivity.this);
            mProgDial.setMessage("Loading wines data...");
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(false);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            WinesResponse response = gson.fromJson(reader, WinesResponse.class);

            if (response != null) {
                mWinesList = response.wines;
                // System.out.println(response);
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {
            Log.e("StartWineImage", "onPostExecute" + mWinesList.length);
            super.onPostExecute(file_url);
            mProgDial.dismiss();
            if (mWinesList != null) {
                //                mAdapter = new WinesGridViewAdapter(mAct, mWinesList);
                //                mUiList.setAdapter(mAdapter);
                initView(mWinesList);
            }

        }

    }

}
