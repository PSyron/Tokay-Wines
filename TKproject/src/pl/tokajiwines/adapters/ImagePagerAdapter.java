
package pl.tokajiwines.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pl.tokajiwines.App;
import pl.tokajiwines.jsonresponses.ImagePagerItem;

import java.io.File;

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
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (mUrlOrImage) {
            //            Ion.with(imageView).placeholder(R.drawable.placeholder_image)
            //                    .error(R.drawable.error_image).load(mImagesUrl[position].ImageUrl);
            final File imgFile = new File(App.fileAbsPath
                    + mImagesUrl[position].ImageUrl.substring(
                            mImagesUrl[position].ImageUrl.lastIndexOf('/') + 1,
                            mImagesUrl[position].ImageUrl.length()));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                imageView.setImageBitmap(myBitmap);
            } else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        App.downloadImagesToSdCard(mImagesUrl[position].ImageUrl, context,
                                imageView);

                    }
                }, 50);
            }

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
