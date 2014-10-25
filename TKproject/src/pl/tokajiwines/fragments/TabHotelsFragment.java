
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.tokajiwines.R;

public class TabHotelsFragment extends BaseFragment {
    public static TabHotelsFragment newInstance() {
        TabHotelsFragment fragment = new TabHotelsFragment();

        return fragment;
    }

    public TabHotelsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offline_map, container, false);

        return v;
    }
}
