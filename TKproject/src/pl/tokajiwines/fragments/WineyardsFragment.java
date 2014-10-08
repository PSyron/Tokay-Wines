
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.WineyardsAdapter;

public class WineyardsFragment extends BaseFragment {

    ListView mUiList;
    WineyardsAdapter mAdapter;

    public static WineyardsFragment newInstance() {
        WineyardsFragment fragment = new WineyardsFragment();
        //        Bundle args = new Bundle();
        //        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //        fragment.setArguments(args);
        return fragment;
    }

    public WineyardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mAdapter = new WineyardsAdapter(getActivity());
        mUiList.setAdapter(mAdapter);
        return rootView;
    }
}
