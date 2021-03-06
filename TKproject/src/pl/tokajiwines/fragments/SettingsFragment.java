
package pl.tokajiwines.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.tokajiwines.R;
import pl.tokajiwines.activities.AboutAppActivity;
import pl.tokajiwines.activities.MainActivity;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.util.Locale;

public class SettingsFragment extends BaseFragment {

    public static final String SharedKeyNotif = "notifications";
    public static final String SharedKeyNotifVibrat = "vibrations";
    public static final String SharedKeyLanguage = "language";
    public static final String SharedKeyCurrency = "currency";
    public static final String SharedKeyDistMeasure = "distMeasure";
    public static final String SharedKeyGPSRange = "GPSRange";
    public static final String SharedKeyGPSData = "mobileData";

    public static final boolean DefNotif = false;
    public static final boolean DefNotifVibrat = false;
    public static final int DefLanguage = 0; // 0 - Polish ; 1 - English
    public static final int DefCurrency = 0; // 0 - PLN ; 1 - Euro ; 2 - Forint
    public static final int DefDistMeasure = 0; // 0 - km ; 1 - miles
    public static final int DefGPSRange = 0; // 0 - 5km , 1 - 10km , 2 - 15km , 3 - 20km , 4 - 25km
    public static final boolean DefGPSData = true;

    CheckBox mUiNotif;
    CheckBox mUiNotifVib;
    RelativeLayout mUiLanguageRel;
    TextView mUiLanguageTV;
    RelativeLayout mUiCurrencyRel;
    TextView mUiCurrencyTV;
    RelativeLayout mUiDistMeasureRel;
    TextView mUiDistMeasureTV;
    RelativeLayout mUiGPSRangeRel;
    TextView mUiGPSRangeTV;
    CheckBox mUiGPSData;
    TextView mUiAbout;
    Context mCtx;

    public static SettingsFragment newInstance(Context ctx) {
        SettingsFragment fragment = new SettingsFragment(ctx);
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment(Context ctx) {
        mCtx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mUiNotif = (CheckBox) v.findViewById(R.id.settings_enable_notofications_cB);
        mUiNotifVib = (CheckBox) v.findViewById(R.id.settings_vibrations_only_cB);
        mUiLanguageRel = (RelativeLayout) v.findViewById(R.id.settings_general_language_rL);
        mUiLanguageTV = (TextView) v.findViewById(R.id.settings_general_language_small_tV);
        mUiCurrencyRel = (RelativeLayout) v.findViewById(R.id.settings_general_currency_rL);
        mUiCurrencyTV = (TextView) v.findViewById(R.id.settings_general_currency_small_tV);
        mUiDistMeasureRel = (RelativeLayout) v.findViewById(R.id.settings_general_distance_rL);
        mUiDistMeasureTV = (TextView) v.findViewById(R.id.settings_general_distance_small_tV);
        mUiGPSRangeRel = (RelativeLayout) v.findViewById(R.id.settings_gps_rL);
        mUiGPSRangeTV = (TextView) v.findViewById(R.id.settings_gps_small_tV);
        mUiGPSData = (CheckBox) v.findViewById(R.id.settings_gps_data);
        mUiAbout = (TextView) v.findViewById(R.id.settings_about_application_tV);
        initFromShared();
        initListeners();
        return v;
    }

    private void initListeners() {
        mUiNotif.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesHelper
                        .putSharedPreferencesBoolean(mCtx, SharedKeyNotif, isChecked);

            }
        });
        mUiNotifVib.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesHelper.putSharedPreferencesBoolean(mCtx, SharedKeyNotifVibrat,
                        isChecked);

            }
        });

        mUiGPSData.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesHelper.putSharedPreferencesBoolean(mCtx, SharedKeyGPSData,
                        isChecked);

            }
        });

        final AlertDialog.Builder languageDialog = new AlertDialog.Builder(mCtx);

        languageDialog.setTitle(getResources().getString(R.string.dialog_title_language)).setItems(
                Constans.sSettingsLanguage, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUiLanguageTV.setText(Constans.sSettingsLanguage[which]);
                        SharedPreferencesHelper.putSharedPreferencesInt(mCtx, SharedKeyLanguage,
                                which);
                        Locale locale = new Locale("pl");

                        switch (which) {
                            case 0:
                                locale = new Locale("pl");
                                break;
                            case 1:
                                locale = new Locale("en_US");
                                break;

                        }
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getApplicationContext().getResources()
                                .updateConfiguration(config, null);
                        //odswież napisy w menu
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                });

        mUiLanguageRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                languageDialog.show();

                //changing color of divider
                /*                AlertDialog dialog = languageDialog.show();
                                int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
                                View titleDivider = dialog.findViewById(titleDividerId);
                                if (titleDivider != null)
                                    titleDivider.setBackgroundColor(getResources().getColor(
                                            R.color.filter_text));*/

            }
        });

        final AlertDialog.Builder currencyDialog = new AlertDialog.Builder(mCtx);

        currencyDialog.setTitle(getResources().getString(R.string.dialog_title_currency)).setItems(
                Constans.sSettingsCurrency, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUiCurrencyTV.setText(Constans.sSettingsCurrency[which]);
                        SharedPreferencesHelper.putSharedPreferencesInt(mCtx, SharedKeyCurrency,
                                which);

                    }
                });

        mUiCurrencyRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currencyDialog.show();

            }
        });

        final AlertDialog.Builder measureDialog = new AlertDialog.Builder(mCtx);

        String[] temp = {
                getResources().getString(Constans.sSettingsMeasure[0]),
                getResources().getString(Constans.sSettingsMeasure[1])
        };

        measureDialog.setTitle(getResources().getString(R.string.dialog_title_measure)).setItems(
                temp, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUiDistMeasureTV.setText(getResources().getString(
                                Constans.sSettingsMeasure[which]));
                        SharedPreferencesHelper.putSharedPreferencesInt(mCtx, SharedKeyDistMeasure,
                                which);

                    }
                });

        mUiDistMeasureRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                measureDialog.show();

            }
        });

        final AlertDialog.Builder rangeDialog = new AlertDialog.Builder(mCtx);

        rangeDialog.setTitle(getResources().getString(R.string.dialog_title_range)).setItems(
                Constans.sSettingsRange, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUiGPSRangeTV.setText(Constans.sSettingsRange[which]);
                        SharedPreferencesHelper.putSharedPreferencesInt(mCtx, SharedKeyGPSRange,
                                which);

                    }
                });

        mUiGPSRangeRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rangeDialog.show();

            }
        });
        mUiAbout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AboutAppActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initFromShared() {
        mUiNotif.setChecked(SharedPreferencesHelper.getSharedPreferencesBoolean(mCtx,
                SharedKeyNotif, DefNotif));
        mUiNotifVib.setChecked(SharedPreferencesHelper.getSharedPreferencesBoolean(mCtx,
                SharedKeyNotifVibrat, DefNotifVibrat));
        mUiLanguageTV.setText(Constans.sSettingsLanguage[SharedPreferencesHelper
                .getSharedPreferencesInt(mCtx, SharedKeyLanguage, DefLanguage)]);
        if (Locale.getDefault().getDisplayLanguage().contains("polsk")
                || Locale.getDefault().getDisplayLanguage().contains("pl")) {
            mUiLanguageTV.setText(Constans.sSettingsLanguage[0]);
        } else
            mUiLanguageTV.setText(Constans.sSettingsLanguage[1]);

        mUiCurrencyTV.setText(Constans.sSettingsCurrency[SharedPreferencesHelper
                .getSharedPreferencesInt(mCtx, SharedKeyCurrency, DefCurrency)]);
        mUiDistMeasureTV.setText(getResources().getString(
                Constans.sSettingsMeasure[SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                        SharedKeyDistMeasure, DefDistMeasure)]));
        mUiGPSRangeTV.setText(Constans.sSettingsRange[SharedPreferencesHelper
                .getSharedPreferencesInt(mCtx, SharedKeyGPSRange, DefGPSRange)]);
        mUiGPSData.setChecked(SharedPreferencesHelper.getSharedPreferencesBoolean(mCtx,
                SharedKeyGPSData, DefGPSData));
    }
}
