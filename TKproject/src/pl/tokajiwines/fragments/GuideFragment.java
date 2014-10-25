
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
    public static GuideFragment newInstance() {
        GuideFragment fragment = new GuideFragment();

        return fragment;
    }

    public GuideFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        FragmentPagerAdapter adapter = new GuideTabsAdapter(getFragmentManager());

        ViewPager pager = (ViewPager) v.findViewById(R.id.guide_pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) v.findViewById(R.id.guide_indicator);
        indicator.setViewPager(pager);

        return v;
    }

}
