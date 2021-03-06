
package pl.tokajiwines.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.ToursAdapter;

public class TabToursFragment extends BaseFragment {

    ListView mUiList;
    ToursAdapter mAdapter;
    Context mContext;
    String[] mTours;

    public static TabToursFragment newInstance() {
        TabToursFragment fragment = new TabToursFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tours, container, false);

        mTours = new String[2];
        mTours[0] = "W strone Tokaju";
        mTours[1] = "W Tokaju";

        mUiList = (ListView) rootView.findViewById(R.id.frag_tours_list);
        mAdapter = new ToursAdapter(getActivity(), mTours);
        mUiList.setAdapter(mAdapter);

        mUiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    ((OnTourPickedListener) getActivity()).OnTourPicked(position);
                } catch (ClassCastException cce) {

                }
            }
        });
        mContext = getActivity();

        return rootView;
    }

    public interface OnTourPickedListener {
        public void OnTourPicked(int position);
    }

}
