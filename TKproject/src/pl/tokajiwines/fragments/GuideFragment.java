
package pl.tokajiwines.fragments;

import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.GuideTabsAdapter;

public class GuideFragment extends BaseFragment {

    ViewPager pager;
    TabPageIndicator indicator;

    public static GuideFragment newInstance() {
        GuideFragment fragment = new GuideFragment();

        return fragment;
    }

    public GuideFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_guide, container, false);

        pager = (ViewPager) v.findViewById(R.id.guide_pager);

        indicator = (TabPageIndicator) v.findViewById(R.id.guide_indicator);
        initView();

        return v;
    }

    public void initView() {
        FragmentPagerAdapter adapter = new GuideTabsAdapter(getFragmentManager(), getActivity());
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    public void onResume() {

        super.onResume();

    }

}
