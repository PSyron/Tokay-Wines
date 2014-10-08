
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.NewsAdapter;

public class NewsFragment extends BaseFragment {

    ListView mUiList;
    NewsAdapter mAdapter;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mUiList = (ListView) rootView.findViewById(R.id.frag_news_list);
        mAdapter = new NewsAdapter(getActivity());
        mUiList.setAdapter(mAdapter);
        return rootView;
    }
}
