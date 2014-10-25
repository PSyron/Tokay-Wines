
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;

public class SettingsFilterFragment extends BaseFragment {
    ListView mUiList;

    public static SettingsFilterFragment newInstance() {
        SettingsFilterFragment fragment = new SettingsFilterFragment();
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        //        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);

        return rootView;
    }
}
