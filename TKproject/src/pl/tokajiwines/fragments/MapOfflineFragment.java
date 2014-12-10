
package pl.tokajiwines.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pl.tokajiwines.R;
import pl.tokajiwines.utils.SharedPreferencesHelper;

public class MapOfflineFragment extends BaseFragment {

    Context mCtx;
    ImageView mUiImage;

    public MapOfflineFragment(Context ctx) {
        mCtx = ctx;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map_offline, container, false);
        mUiImage = (ImageView) v.findViewById(R.id.fragment_map_offline_image);
        if (SharedPreferencesHelper.getSharedPreferencesInt(mCtx,
                SettingsFragment.SharedKeyLanguage, SettingsFragment.DefLanguage) == 0) {
            mUiImage.setImageResource(R.drawable.connection_failed_bg_pl);
        } else {
            mUiImage.setImageResource(R.drawable.connection_failed_bg_eng);
        }
        return v;
    }

    public static MapOfflineFragment newInstance(Context ctx) {
        MapOfflineFragment fragment = new MapOfflineFragment(ctx);

        return fragment;
    }
}
