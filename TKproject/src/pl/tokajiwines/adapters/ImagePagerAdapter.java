
package pl.tokajiwines.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.ImagePagerItem;

public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    private int[] mImages;
    ImagePagerItem[] mImagesUrl;
    boolean mUrlOrImage = true; // true = Url , false = images from Res

    public ImagePagerAdapter(Context context, int[] images) {
        mUrlOrImage = false;
        mImages = images;
        this.context = context;
    }

    public ImagePagerAdapter(Context context, ImagePagerItem[] imagesUrl) {
        mUrlOrImage = true;
        mImagesUrl = imagesUrl;
        this.context = context;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        if (mUrlOrImage) {
            return this.mImagesUrl.length;
        } else {
            return this.mImages.length;
        }

    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (mUrlOrImage) {
            Ion.with(imageView).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mImagesUrl[position].ImageUrl);
        } else {
            imageView.setImageResource(this.mImages[position]);
        }

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((ImageView) object);
    }
}
