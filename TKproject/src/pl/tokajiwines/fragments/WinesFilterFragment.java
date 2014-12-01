
package pl.tokajiwines.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import pl.tokajiwines.acitivities.WinesListActivity;
import pl.tokajiwines.db.FlavoursDataSource;
import pl.tokajiwines.db.GradesDataSource;
import pl.tokajiwines.db.ProducersDataSource;
import pl.tokajiwines.db.StrainsDataSource;
import pl.tokajiwines.db.WinesDataSource;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.jsonresponses.WineFilterResponse;
import pl.tokajiwines.models.Flavour;
import pl.tokajiwines.models.Grade;
import pl.tokajiwines.models.Strain;
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
    ProgressDialog mProgDial;
    private String sUrl;
    private String sUsername;
    private String sPassword;

    private WineFilterResponse mWineFilter;
    private Flavour[] mWineFlavours = {};
    private Strain[] mWineStrains = {};
    private Grade[] mWineGrades = {};
    private ProducerListItem[] mWineProducers = {};
    private String[] mWineYear = {};
    private String[] mWinePrices = {};
    private ArrayList<Integer> mSelectedTastes;
    private ArrayList<Integer> mSelectedGrades;
    private ArrayList<Integer> mSelectedStrains;
    private ArrayList<Integer> mSelectedProducers;
    private ArrayList<String> mSelectedYears;
    private ArrayList<String> mSelectedPrices;

    private boolean[] mCheckedTastes = {};
    private boolean[] mCheckedTypes = {};
    private boolean[] mCheckedStrains = {};
    private boolean[] mCheckedProducers = {};
    private boolean[] mCheckedYears = {};
    private boolean[] mCheckedPrices = {};

    AlertDialog dialog;
    String currCurrency;
    String[] currCurrencyTab;
    Context mCtx;

    public static final int TAG_FLAVOURS = 1;
    public static final int TAG_GRADES = 2;
    public static final int TAG_STRAINS = 3;
    public static final int TAG_PRODUCERS = 4;
    public static final int TAG_YEARS = 5;
    public static final int TAG_PRICES = 6;

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
        sUsername = getResources().getString(R.string.Username);
        sPassword = getResources().getString(R.string.Password);

        mSelectedTastes = new ArrayList<Integer>();
        mSelectedGrades = new ArrayList<Integer>();
        mSelectedStrains = new ArrayList<Integer>();
        mSelectedProducers = new ArrayList<Integer>();
        mSelectedYears = new ArrayList<String>();
        mSelectedPrices = new ArrayList<String>();

        mWinePrices = Constans.sWinePricesQuery;

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
        /*
                final String[] mWineTaste = {
                        mCtx.getResources().getString(R.string.wine_taste_dry),
                        mCtx.getResources().getString(R.string.wine_taste_semidry),
                        mCtx.getResources().getString(R.string.wine_taste_sweet),
                        mCtx.getResources().getString(R.string.wine_taste_semisweet),
                        mCtx.getResources().getString(R.string.wine_taste_sparkling)
                };*/

        //TODO Getting Producers names and wines years from SQLite

        mUiTasteLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String[] flavours = new String[mWineFlavours.length];
                for (int i = 0; i < flavours.length; i++) {
                    if (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage) == 0) {
                        flavours[i] = mWineFlavours[i].mNamePl;
                    } else if (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage) == 1) {
                        flavours[i] = mWineFlavours[i].mNameEng;
                    }
                }
                createDialogCheckBox(getResources().getString(R.string.wine_taste), flavours,
                        mUiTaste, TAG_FLAVOURS, mCheckedTastes);

            }
        });

        mUiTypeLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String[] grades = new String[mWineGrades.length];

                for (int i = 0; i < grades.length; i++) {
                    grades[i] = mWineGrades[i].mName;
                }

                createDialogCheckBox(getResources().getString(R.string.wine_type), grades, mUiType,
                        TAG_GRADES, mCheckedTypes);

            }
        });

        mUiSrainLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String[] strains = new String[mWineStrains.length];

                for (int i = 0; i < strains.length; i++) {
                    strains[i] = mWineStrains[i].mName;
                }

                createDialogCheckBox(getResources().getString(R.string.wine_strain), strains,
                        mUiStrain, TAG_STRAINS, mCheckedStrains);

            }
        });

        mUiYearLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.dialog_wine_year),
                        mWineYear, mUiYear, TAG_YEARS, mCheckedYears);
            }
        });

        mUiProducerLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String[] producers = new String[mWineProducers.length];

                for (int i = 0; i < producers.length; i++) {
                    producers[i] = mWineProducers[i].mName;
                }

                createDialogCheckBox(getResources().getString(R.string.wine_produder), producers,
                        mUiProducer, TAG_PRODUCERS, mCheckedProducers);

            }
        });
        mUiPriceLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currCurrency == Constans.sSettingsCurrency[2])
                    createDialogCheckBox(getResources().getString(R.string.wine_price_range) + ": "
                            + Constans.sSettingsCurrency[2], currCurrencyTab, mUiPrice, TAG_PRICES,
                            mCheckedPrices);
                else {
                    createDialogCheckBox(getResources().getString(R.string.wine_price_range) + ": "
                            + Constans.sSettingsCurrency[2] + " (" + currCurrency + ")",
                            currCurrencyTab, mUiPrice, TAG_PRICES, mCheckedPrices);
                }

            }
        });

        mUiSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, WinesListActivity.class);

                Gson gson = new Gson();
                String tastes = gson.toJson(mSelectedTastes.toArray());
                String grades = gson.toJson(mSelectedGrades.toArray());
                String strains = gson.toJson(mSelectedStrains.toArray());
                String producers = gson.toJson(mSelectedProducers.toArray());
                String years = gson.toJson(mSelectedYears.toArray());
                String prices = gson.toJson(mSelectedPrices.toArray());

                intent.putExtra("" + TAG_FLAVOURS, tastes);
                intent.putExtra("" + TAG_GRADES, grades);
                intent.putExtra("" + TAG_STRAINS, strains);
                intent.putExtra("" + TAG_PRODUCERS, producers);
                intent.putExtra("" + TAG_YEARS, years);
                intent.putExtra("" + TAG_PRICES, prices);

                startActivity(intent);
                /*                Toast toast = Toast.makeText(mCtx, "Not working yet", 10);
                                toast.show();*/
            }
        });
    }

    private void createDialogCheckBox(String label, String[] items, TextView options, int tag,
            boolean[] checkedItems) {
        final boolean[] currCheckedItems = checkedItems;
        final TextView currText = options;
        final String[] currItems = items;
        final int currTag = tag;
        final ArrayList<Integer> seletedItems = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle(label);
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

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
                        String text = showChosenOptions(currCheckedItems, currItems, currTag);
                        currText.setText(text);
                        if (text == getResources().getText(R.string.any))
                            currText.setTextColor(getResources().getColor(R.color.gray));
                        else
                            currText.setTextColor(Color.BLACK);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        for (int i = 0; i < currCheckedItems.length; i++) {
                            currCheckedItems[i] = false;
                        }
                        currText.setText(R.string.any);
                        currText.setTextColor(getResources().getColor(R.color.gray));
                    }
                });
        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();

    }

    private String showChosenOptions(boolean[] checkedItems, String[] opt, int tag) {
        String chosenOptions = "";
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) chosenOptions += opt[i] + ", ";
        }

        switch (tag) {
            case TAG_FLAVOURS: {

                mSelectedTastes.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedTastes.add(mWineFlavours[i].mIdFlavour);
                    }
                }
                break;
            }
            case TAG_GRADES: {

                mSelectedGrades.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedGrades.add(mWineGrades[i].mIdGrade);
                    }
                }
                break;
            }
            case TAG_STRAINS: {

                mSelectedStrains.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedStrains.add(mWineStrains[i].mIdStrain);
                    }
                }
                break;
            }
            case TAG_PRODUCERS: {

                mSelectedProducers.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedProducers.add(mWineProducers[i].mIdProducer);
                    }
                }
                break;
            }
            case TAG_YEARS: {

                mSelectedYears.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedYears.add(opt[i]);
                    }
                }
                break;
            }

            case TAG_PRICES: {

                mSelectedPrices.clear();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mSelectedPrices.add(mWinePrices[i]);
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
        if (chosenOptions == "")
            return getResources().getString(R.string.any);
        else
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
        if (mWineStrains.length == 0) {

            if (App.isOnline(mCtx)) {
                new LoadFilterOnlineTask().execute();
            } else {
                /*Toast.makeText(mCtx, getResources().getString(R.string.cannot_connect),
                        Toast.LENGTH_LONG).show();*/
                Toast.makeText(mCtx, "Offline Database", Toast.LENGTH_LONG).show();
                new LoadFilterTask().execute();
            }
        }
    }

    class LoadFilterTask extends AsyncTask<String, String, String> {
        boolean failure = false;

        // while data are loading, show progress dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mCtx);
            mProgDial.setMessage(getResources().getString(R.string.loading));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();
        }

        // retrieving news data
        @Override
        protected String doInBackground(String... args) {
            WinesDataSource wDs = new WinesDataSource(getActivity());
            StrainsDataSource sDs = new StrainsDataSource(getActivity());
            GradesDataSource gDs = new GradesDataSource(getActivity());
            FlavoursDataSource fDs = new FlavoursDataSource(getActivity());
            ProducersDataSource pDs = new ProducersDataSource(getActivity());
            wDs.open();
            mWineYear = wDs.getProdDates();
            wDs.close();
            sDs.open();
            mWineStrains = sDs.getAllStrains();
            sDs.close();
            gDs.open();
            mWineGrades = gDs.getAllGrades();
            gDs.close();
            fDs.open();
            mWineFlavours = fDs.getAllFlavours();
            fDs.close();
            pDs.open();
            mWineProducers = pDs.getProducerList();
            pDs.close();

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();

            if (mWineFlavours != null) mCheckedTastes = new boolean[mWineFlavours.length];
            if (mWineGrades != null) mCheckedTypes = new boolean[mWineGrades.length];
            if (mWineStrains != null) mCheckedStrains = new boolean[mWineStrains.length];
            if (mWineYear != null) mCheckedYears = new boolean[mWineYear.length];
            if (mWineProducers != null) mCheckedProducers = new boolean[mWineProducers.length];
            if (mWinePrices != null) mCheckedPrices = new boolean[mWinePrices.length];
        }
    }

    class LoadFilterOnlineTask extends AsyncTask<String, String, String> {

        boolean failure = false;

        // while data are loading, show progress dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDial = new ProgressDialog(mCtx);
            mProgDial.setMessage(getResources().getString(R.string.loading));
            mProgDial.setIndeterminate(false);
            mProgDial.setCancelable(true);
            mProgDial.show();

        }

        // retrieving news data

        @Override
        protected String doInBackground(String... args) {

            mParser = new JSONParser();

            InputStream source = mParser.retrieveStream(sUrl, sUsername, sPassword, null);
            if (source != null) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(source);

                WineFilterResponse response = gson.fromJson(reader, WineFilterResponse.class);

                if (response != null) {
                    mWineFilter = response;
                    mWineYear = mWineFilter.years;
                    mWineProducers = mWineFilter.producers;
                    mWineGrades = response.grades;
                    mWineStrains = response.strains;
                    mWineFlavours = response.flavours;

                }
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();

            if (mWineFlavours != null) mCheckedTastes = new boolean[mWineFlavours.length];
            if (mWineGrades != null) mCheckedTypes = new boolean[mWineGrades.length];
            if (mWineStrains != null) mCheckedStrains = new boolean[mWineStrains.length];
            if (mWineYear != null) mCheckedYears = new boolean[mWineYear.length];
            if (mWineProducers != null) mCheckedProducers = new boolean[mWineProducers.length];
            if (mWinePrices != null) mCheckedPrices = new boolean[mWinePrices.length];
        }
    }
}
