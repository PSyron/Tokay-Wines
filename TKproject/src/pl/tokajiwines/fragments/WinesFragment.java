
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.WineSearchAdapter;

public class WinesFragment extends BaseFragment {

    ListView mUiList;
    WineSearchAdapter mAdapter;

    public static WinesFragment newInstance() {
        WinesFragment fragment = new WinesFragment();
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public WinesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wine_old_filter, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_wine_filter_list);
        mAdapter = new WineSearchAdapter(getActivity());
        mUiList.setAdapter(mAdapter);
        return rootView;
    }
}
