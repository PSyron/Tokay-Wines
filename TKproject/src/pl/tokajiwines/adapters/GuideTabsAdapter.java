
package pl.tokajiwines.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import pl.tokajiwines.fragments.TabCuriositiesFragment;
import pl.tokajiwines.fragments.TabHotelsFragment;
import pl.tokajiwines.fragments.TabRestaurantsFragment;
import pl.tokajiwines.utils.Constans;

public class GuideTabsAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    public GuideTabsAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        //TODO przerobic na wlasciwe

        switch (position) {
            case 0:
                //                return NewsFragment.newInstance(Constans.sGuideTabTitle[position
                //                        % Constans.sGuideTabTitle.length]);
                return TabCuriositiesFragment.newInstance();
                // break;
            case 1:
                //                return NewsFragment.newInstance(Constans.sGuideTabTitle[position
                //                        % Constans.sGuideTabTitle.length]);
                return TabHotelsFragment.newInstance();
            case 2:
                //                return NewsFragment.newInstance(Constans.sGuideTabTitle[position
                //                        % Constans.sGuideTabTitle.length]);
                return TabRestaurantsFragment.newInstance();

            default:
                break;
        }
        return new Fragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constans.sGuideTabTitle[position % Constans.sGuideTabTitle.length].toUpperCase();
    }

    @Override
    public int getIconResId(int index) {
        return Constans.sGuideTabIcons[index];
    }

    public int getCount() {
        return Constans.sGuideTabTitle.length;
    }
}