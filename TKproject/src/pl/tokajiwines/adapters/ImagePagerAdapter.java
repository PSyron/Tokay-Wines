
package pl.tokajiwines.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    private int[] mImages;

    public ImagePagerAdapter(Context context, int[] images) {
        mImages = images;
        this.context = context;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return this.mImages.length;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(this.mImages[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((ImageView) object);
    }
}
