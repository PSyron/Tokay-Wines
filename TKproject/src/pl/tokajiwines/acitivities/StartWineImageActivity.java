
package pl.tokajiwines.acitivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.DownloadImagesRespons;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.jsonresponses.WinesResponse;
import pl.tokajiwines.models.Image;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StartWineImageActivity extends BaseActivity {

    TextView mUiSkipBtn;

    ImageView mUiImage;
    ProgressDialog mProgDial;
    JSONParser mParser;
    private WineListItem[] mWinesList;
    public static final String DOWNLOAD = "learnedAlready";
    //WineDetails mWineDetails;
    WineListItem mChosedItem;
    private String sUrl;
    private String sImagesUrl;
    private String sUsername;
    private String sPassword;
    Image[] mImagesList;
    LoadImages mDownloadImagesTask;
    LoadWineImageTask mLoadWine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        sUrl = getResources().getString(R.string.UrlRandomWine);
        sImagesUrl = getResources().getString(R.string.UrlAllImagesDownload);
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);
        setContentView(R.layout.activity_starting_image);
        mUiSkipBtn = (TextView) findViewById(R.id.start_button);
        mUiImage = (ImageView) findViewById(R.id.start_image);
        initLanguage();
        mLoadWine = new LoadWineImageTask();
        if (!App.isOnline(this)) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }
        mUiSkipBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();

            }
        });

    }

    public void onResume() {

        super.onResume();

        if (App.isOnline(this)) {
            
            if (mChosedItem == null)
            {
                mLoadWine.execute();
            }
            boolean learned = SharedPreferencesHelper.getSharedPreferencesBoolean(this, DOWNLOAD,
                    false);
            if (!learned) {
                mDownloadImagesTask = new LoadImages();
                mDownloadImagesTask.execute();
            }
        }

            // otherwise, show message

            else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }

    }

    @Override
    protected void onPause() {
        
        System.out.println("PAUSE");
        if (mLoadWine != null) {
            mLoadWine.cancel(true);
        }
        if (mDownloadImagesTask != null) {
            mDownloadImagesTask.cancel(true);
            if (mProgDial != null)
            {
                mProgDial.dismiss();
            }
        }
        super.onPause();
    }

    public void initLanguage() {
        int language;
        if (Locale.getDefault().getDisplayLanguage().contains("polsk")
                || Locale.getDefault().getDisplayLanguage().contains("pl")) {
            language = SharedPreferencesHelper.getSharedPreferencesInt(this,
                    SettingsFragment.SharedKeyLanguage, 0);
            if (language == 1) {
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                this.getApplicationContext().getResources().updateConfiguration(config, null);
                //odswież napisy w menu
                Intent intent = new Intent(this, StartWineImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        } else {
            language = SharedPreferencesHelper.getSharedPreferencesInt(this,
                    SettingsFragment.SharedKeyLanguage, 0);
            if (language == 0) {
                Locale locale = new Locale("pl");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                this.getApplicationContext().getResources().updateConfiguration(config, null);
                //odswież napisy w menu
                Intent intent = new Intent(this, StartWineImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }

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
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

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

    class LoadImages extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgDial == null)
            {
                mProgDial = new ProgressDialog(StartWineImageActivity.this);
                mProgDial.setMessage(getResources().getString(R.string.downloading_content));
                mProgDial.setIndeterminate(false);
                mProgDial.setCancelable(false);
                mProgDial.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            }
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sImagesUrl, sUsername, sPassword, null);
            if (source != null)
            {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);
    
                DownloadImagesRespons response = gson.fromJson(reader, DownloadImagesRespons.class);
    
                if (response != null) {
                    mImagesList = response.images;
                    mProgDial.setMax(mImagesList.length);
                    int value = 0;
                    mProgDial.setProgress(value);
                    for (Image i : mImagesList) {
                        final File imgFile = new File(StartWineImageActivity.this.getFilesDir()
                                .getAbsolutePath()
                                + "/"
                                + i.mImage.substring(i.mImage.lastIndexOf('/') + 1, i.mImage.length()));
                        if (!imgFile.exists()) {
                            try {
                                App.downloadToInternal(i.mImage, StartWineImageActivity.this);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        mProgDial.setProgress(++value);
                    }
                    SharedPreferencesHelper.putSharedPreferencesBoolean(StartWineImageActivity.this,
                            DOWNLOAD, true);
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();

        }

    }

    class LoadWineImageTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("StartWineImage", "onPreExecute");
            //            mProgDial = new ProgressDialog(StartWineImageActivity.this);
            //            mProgDial.setMessage("Loading random wine data...");
            //            mProgDial.setIndeterminate(false);
            //            mProgDial.setCancelable(false);
            //            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, params);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                WinesResponse response = gson.fromJson(reader, WinesResponse.class);

                if (response != null) {
                    mWinesList = response.wines;
                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {
            Log.e("StartWineImage", "onPostExecute" + mWinesList.length);
            super.onPostExecute(file_url);
            //      mProgDial.dismiss();
            if (mWinesList != null) {
                //                mAdapter = new WinesGridViewAdapter(mAct, mWinesList);
                //                mUiList.setAdapter(mAdapter);
                initView(mWinesList);
            }

        }

    }

}
