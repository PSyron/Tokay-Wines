
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.tokajiwines.R;

public class TabRestaurantsFragment extends BaseFragment {
    public static TabRestaurantsFragment newInstance() {
        TabRestaurantsFragment fragment = new TabRestaurantsFragment();

        return fragment;
    }

    public TabRestaurantsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offline_map, container, false);

        return v;
    }
}
