
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
import pl.tokajiwines.acitivities.NewsActivity;
import pl.tokajiwines.acitivities.WinesListActivity;
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

import org.json.JSONArray;

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
    private WineFilterResponse mWineFilter;
    private Flavour[] mWineFlavours = {};
/*    private String[] mProducerName = {};
    private String[] mWineStrains = {};
    private String[] mWineGrades = {};*/
    private Strain[] mWineStrains = {};
    private Grade[] mWineGrades = {};    
    private ProducerListItem[] mWineProducers = {};
    private String[] mWineYear = {};
    private ArrayList<Integer> mSelectedTastes;
    private ArrayList<Integer> mSelectedGrades;
    private ArrayList<Integer> mSelectedStrains;
    private ArrayList<Integer> mSelectedProducers;
    private ArrayList<String> mSelectedYears;
    

    AlertDialog dialog;
    String currCurrency;
    String[] currCurrencyTab;
    Context mCtx;
    
    public static final int TAG_FLAVOURS = 1;
    public static final int TAG_GRADES = 2; 
    public static final int TAG_STRAINS = 3; 
    public static final int TAG_PRODUCERS = 4; 
    public static final int TAG_YEARS = 5; 

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
        
        mSelectedTastes = new ArrayList<Integer>();
        mSelectedGrades = new ArrayList<Integer>();
        mSelectedStrains = new ArrayList<Integer>();
        mSelectedProducers = new ArrayList<Integer>();
        mSelectedYears = new ArrayList<String>();

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
                
                for (int i = 0; i < flavours.length; i++)
                {
                    if (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage) == 0)
                    {
                        flavours[i] = mWineFlavours[i].mNamePl;
                    }
                    else if (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                            SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage) == 1)
                    {
                        flavours[i] = mWineFlavours[i].mNameEng; 
                    }
                }
                createDialogCheckBox(getResources().getString(R.string.wine_taste), flavours,
                        mUiTaste, TAG_FLAVOURS);

            }
        });

        mUiTypeLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                final String[] grades = new String[mWineGrades.length];
                
                for (int i = 0; i < grades.length; i++)
                {
                    grades[i] = mWineGrades[i].mName;
                }
                
                createDialogCheckBox(getResources().getString(R.string.wine_type), grades,
                        mUiType, TAG_GRADES);

            }
        });

        mUiSrainLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                final String[] strains = new String[mWineStrains.length];
                
                for (int i = 0; i < strains.length; i++)
                {
                    strains[i] = mWineStrains[i].mName;
                }
                
                createDialogCheckBox(getResources().getString(R.string.wine_strain), strains,
                        mUiStrain, TAG_STRAINS);

            }
        });

        mUiYearLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.dialog_wine_year),
                        mWineYear, mUiYear, TAG_YEARS);
            }
        });

        mUiProducerLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                final String[] producers = new String[mWineProducers.length];
                
                for (int i = 0; i < producers.length; i++)
                {
                    producers[i] = mWineProducers[i].mName;
                }
                
                createDialogCheckBox(getResources().getString(R.string.wine_produder),
                        producers, mUiProducer, TAG_PRODUCERS);

            }
        });
        mUiPriceLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogCheckBox(getResources().getString(R.string.wine_price_range) + ": "
                        + currCurrency, currCurrencyTab, mUiPrice, 0);

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
                
                intent.putExtra(""+TAG_FLAVOURS, tastes);
                intent.putExtra(""+TAG_GRADES, grades);
                intent.putExtra(""+TAG_STRAINS, strains);
                intent.putExtra(""+TAG_PRODUCERS, producers);
                intent.putExtra(""+TAG_YEARS, years);
                
                startActivity(intent);
/*                Toast toast = Toast.makeText(mCtx, "Not working yet", 10);
                toast.show();*/
            }
        });
    }

    private void createDialogCheckBox(String label, String[] items, TextView options, int tag) {
        final TextView currText = options;
        final String[] currItems = items;
        final int currTag = tag;
        final ArrayList<Integer> seletedItems = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle(label);
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    switch(currTag)
                    {
                        case TAG_FLAVOURS : { if (!mSelectedTastes.contains(mWineFlavours[indexSelected].mIdFlavour)) mSelectedTastes.add(mWineFlavours[indexSelected].mIdFlavour); break;}
                        case TAG_GRADES : { if (!mSelectedGrades.contains(mWineGrades[indexSelected].mIdGrade))mSelectedGrades.add(mWineGrades[indexSelected].mIdGrade);break;}
                        case TAG_STRAINS : { if (!mSelectedStrains.contains(mWineStrains[indexSelected].mIdStrain))mSelectedStrains.add(mWineStrains[indexSelected].mIdStrain);break;}
                        case TAG_PRODUCERS : { if (!mSelectedProducers.contains(mWineProducers[indexSelected].mIdProducer))mSelectedProducers.add(mWineProducers[indexSelected].mIdProducer);break;}
                        case TAG_YEARS : { if (!mSelectedYears.contains(currItems[indexSelected]))mSelectedYears.add(currItems[indexSelected]);break;}
                        default: {break;}
                    }
                    seletedItems.add(indexSelected);
                } else if (seletedItems.contains(indexSelected)) {
                    // Else, if the item is already in the array, remove it
                    switch(currTag)
                    {
                        case TAG_FLAVOURS : {mSelectedTastes.remove(Integer.valueOf(mWineFlavours[indexSelected].mIdFlavour)); break;}
                        case TAG_GRADES : {mSelectedGrades.remove(Integer.valueOf(mWineGrades[indexSelected].mIdGrade));break;}
                        case TAG_STRAINS : {mSelectedStrains.remove(Integer.valueOf(mWineStrains[indexSelected].mIdStrain));break;}
                        case TAG_PRODUCERS : {mSelectedProducers.remove(Integer.valueOf(mWineProducers[indexSelected].mIdProducer));break;}
                        case TAG_YEARS : {mSelectedYears.remove(currItems[indexSelected]);break;}
                        default: {break;}
                    }
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
            mProgDial = new ProgressDialog(mCtx);
            mProgDial.setMessage("Loading...");
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

            WineFilterResponse response = gson.fromJson(reader, WineFilterResponse.class);

            if (response != null) {
                mWineFilter = response;
                mWineYear = mWineFilter.years;
                mWineProducers = mWineFilter.producers;
                mWineGrades = response.grades;
                mWineStrains = response.strains;
                mWineFlavours = response.flavours;
            }

            return null;

        }

        // create adapter that contains loaded data and show list of news

        protected void onPostExecute(String file_url) {

            super.onPostExecute(file_url);
            mProgDial.dismiss();

        }

    }
}
