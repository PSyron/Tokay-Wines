
package pl.tokajiwines.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.WineFilterResponse;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.JSONParser;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WinesFilterFragment extends BaseFragment {

    LinearLayout mUiTasteLin;
    TextView mUiTaste;
    LinearLayout mUiTypeLin;
    TextView mUiType;
    LinearLayout mUiSrainLin;
    TextView mUiStrain;
    LinearLayout mUiYearLin;
    TextView mUiYear;
    LinearLayout mUiProducerLin;
    TextView mUiProducer;
    LinearLayout mUiPriceLin;
    TextView mUiPrice;
    TextView mUiSearch;

    private JSONParser mParser;
    private String sUrl;
    private WineFilterResponse mWineFilter;
    private String[] mWineYear = {};
    private String[] mProducerName = {};
    private String[] mWineStrains = {};
    private String[] mWineGrades = {};

    AlertDialog dialog;
    String currCurrency;
    String[] currCurrencyTab;
    Context mCtx;

    public static WinesFilterFragment newInstance(Context ctx) {
        WinesFilterFragment fragment = new WinesFilterFragment(ctx);
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public WinesFilterFragment(Context ctx) {
        mCtx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wine_filter, container, false);

        sUrl = getResources().getString(R.string.UrlWineFilter);

        mUiTaste = (TextView) v.findViewById(R.id.frag_wine_taste_tV);
        mUiTasteLin = (LinearLayout) v.findViewById(R.id.frag_wine_taste_lL);
        mUiType = (TextView) v.findViewById(R.id.frag_wine_type_tV);
        mUiTypeLin = (LinearLayout) v.findViewById(R.id.frag_wine_type_lL);
        mUiStrain = (TextView) v.findViewById(R.id.frag_wine_strain_tV);
        mUiSrainLin = (LinearLayout) v.findViewById(R.id.frag_wine_strain_lL);
        mUiYear = (TextView) v.findViewById(R.id.frag_wine_year_tV);
        mUiYearLin = (LinearLayout) v.findViewById(R.id.frag_wine_year_lL);
        mUiProducer = (TextView) v.findViewById(R.id.frag_wine_producer_tV);
        mUiProducerLin = (LinearLayout) v.findViewById(R.id.frag_wine_producer_lL);
        mUiPrice = (TextView) v.findViewById(R.id.frag_wine_price_tV);
        mUiPriceLin = (LinearLayout) v.findViewById(R.id.frag_wine_price_lL);
        mUiSearch = (TextView) v.findViewById(R.id.frag_wine_search_btn);

        getSettingFromSharedPreferences();
        initResources();

        return v;
    }

    private void initResources() {

        final String[] mWineTaste = {
                mCtx.getResources().getString(R.string.wine_taste_dry),
                mCtx.getResources().getString(R.string.wine_taste_semidry),
                mCtx.getResources().getString(R.string.wine_taste_sweet),
                mCtx.getResources().getString(R.string.wine_taste_semisweet),
                mCtx.getResources().getString(R.string.wine_taste_sparkling)
        };

        //TODO Getting Producers names and wines years from SQLite

        mUiTasteLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_taste), mWineTaste,
                        mUiTaste);

            }
        });

        mUiTypeLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_type), mWineGrades,
                        mUiType);

            }
        });

        mUiSrainLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_strain), mWineStrains,
                        mUiStrain);

            }
        });

        mUiYearLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.dialog_wine_year),
                        mWineYear, mUiYear);
            }
        });

        mUiProducerLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_produder),
                        mProducerName, mUiProducer);

            }
        });
        mUiPriceLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_price_range) + ": "
                        + currCurrency, currCurrencyTab, mUiPrice);

            }
        });

        mUiSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(mCtx, "Not working yet", 10);
                toast.show();
            }
        });
    }

    private void createDialogCheckBox(String label, String[] items, TextView options) {
        final TextView currText = options;
        final String[] currItems = items;
        final ArrayList seletedItems = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle(label);
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    seletedItems.add(indexSelected);
                } else if (seletedItems.contains(indexSelected)) {
                    // Else, if the item is already in the array, remove it
                    seletedItems.remove(Integer.valueOf(indexSelected));
                }
            }
        })
        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        currText.setText(showChosenOptions(seletedItems, currItems));
                        currText.setTextColor(Color.BLACK);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        currText.setText(R.string.any);
                        currText.setTextColor(getResources().getColor(R.color.gray));
                    }
                });
        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();

    }

    private String showChosenOptions(ArrayList<Integer> list, String[] opt) {
        String chosenOptions = "";
        for (Integer a : list) {
            chosenOptions += opt[Integer.valueOf(a)] + ", ";
        }
        return chosenOptions;
    }

    private void getSettingFromSharedPreferences() {
        currCurrency = Constans.sSettingsCurrency[SharedPreferencesHelper.getSharedPreferencesInt(
                mCtx, SettingsFragment.SharedKeyCurrency, SettingsFragment.DefCurrency)];

        switch (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                SettingsFragment.SharedKeyCurrency, SettingsFragment.DefCurrency)) {
            case 0:
                currCurrencyTab = Constans.sWinePricesPLN;
                break;
            case 1:
                currCurrencyTab = Constans.sWinePricesEuro;
                break;
            case 2:
                currCurrencyTab = Constans.sWinePricesForint;
                break;
        }

    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (App.isOnline(mCtx)) {
            new LoadFilterTask().execute();
        }

        // otherwise, show message

        else {
            Toast.makeText(mCtx, "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
        }

    }

    class LoadFilterTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, Constans.sUsername,
                    Constans.sPassword, null);
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(source);

            WineFilterResponse response = gson.fromJson(reader, WineFilterResponse.class);

            if (response != null) {
                mWineFilter = response;
                mWineYear = mWineFilter.years;
                mProducerName = mWineFilter.producers;
                mWineGrades = response.grades;
                mWineStrains = response.strains;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);

        }

    }
}
