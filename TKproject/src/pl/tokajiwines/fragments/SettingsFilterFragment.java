
package pl.tokajiwines.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.SharedPreferencesHelper;

public class SettingsFilterFragment extends BaseFragment {

    public static final String SharedKeyNotif = "notifications";
    public static final String SharedKeyNotifVibrat = "vibrations";
    public static final String SharedKeyLanguage = "language";
    public static final String SharedKeyCurrency = "currency";
    public static final String SharedKeyDistMeasure = "distMeasure";
    public static final String SharedKeyGPSRange = "GPSRange";
    public static final String SharedKeyGPSData = "mobileData";

    public static final boolean DefNotif = true;
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

    Context mCtx;

    public static SettingsFilterFragment newInstance(Context ctx) {
        SettingsFilterFragment fragment = new SettingsFilterFragment(ctx);
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFilterFragment(Context ctx) {
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

                    }
                });

        mUiLanguageRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                languageDialog.show();

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

    }

    private void initFromShared() {
        mUiNotif.setChecked(SharedPreferencesHelper.getSharedPreferencesBoolean(mCtx,
                SharedKeyNotif, DefNotif));
        mUiNotifVib.setChecked(SharedPreferencesHelper.getSharedPreferencesBoolean(mCtx,
                SharedKeyNotifVibrat, DefNotifVibrat));
        mUiLanguageTV.setText(Constans.sSettingsLanguage[SharedPreferencesHelper
                .getSharedPreferencesInt(mCtx, SharedKeyLanguage, DefLanguage)]);
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
