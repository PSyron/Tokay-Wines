
package pl.tokajiwines.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import pl.tokajiwines.fragments.TabHotelsFragment;
import pl.tokajiwines.fragments.TabRestaurantsFragment;
import pl.tokajiwines.utils.Constans;

public class GuideTabsAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    Context mCtx;

    public GuideTabsAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        mCtx = ctx;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        //TODO przerobic na wlasciwe

        switch (position) {
        /*            case 0:

                        return TabCuriositiesFragment.newInstance();
                        */
        // break;
            case 0:

                return TabHotelsFragment.newInstance();
            case 1:

                return TabRestaurantsFragment.newInstance();

            default:
                break;
        }
        return new Fragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return (mCtx.getResources().getString(Constans.sGuideTabTitle[position
                % Constans.sGuideTabTitle.length])).toUpperCase();

    }

    @Override
    public int getIconResId(int index) {
        return Constans.sGuideTabIcons[index];
    }

    public int getCount() {
        return Constans.sGuideTabTitle.length;
    }
}
