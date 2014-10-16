
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.WinesAdapter;

public class WinesFragment extends BaseFragment {

    ListView mUiList;
    WinesAdapter mAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mAdapter = new WinesAdapter(getActivity());
        mUiList.setAdapter(mAdapter);
        return rootView;
    }
}
