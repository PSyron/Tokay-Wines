
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.tokajiwines.R;

public class TabCuriositiesFragment extends BaseFragment {
    public static TabCuriositiesFragment newInstance() {
        TabCuriositiesFragment fragment = new TabCuriositiesFragment();

        return fragment;
    }

    public TabCuriositiesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_curiosities, container, false);
        Log.e(getTag(), "OnCreate Curiosities");
        return v;
    }
}
