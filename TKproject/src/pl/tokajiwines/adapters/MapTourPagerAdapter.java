package pl.tokajiwines.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.io.File;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.models.Place;

public class MapTourPagerAdapter extends PagerAdapter {

    Place[] mPlaceList;
    Context mCtx;
    private static LayoutInflater inflater = null;



    public MapTourPagerAdapter(Context ctx, Place[] items){
        mCtx = ctx;
        mPlaceList = items;
        inflater = (LayoutInflater) mCtx.getSystemService(mCtx.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mPlaceList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // Inflate (and modify) the desired layout to add to the ViewPager.
        LinearLayout item = (LinearLayout) inflater.inflate(R.layout.item_map, container, false);
        TextView placeTitle = (TextView) item.findViewById(R.id.item_map_title);
        TextView placeAddress = (TextView) item.findViewById(R.id.item_map_address);
        final ImageView img = (ImageView) item.findViewById(R.id.item_map_image);
        // Add the inflated item to the ViewPager
        container.addView(item);
        placeTitle.setText(mPlaceList[position].mName);
        placeAddress.setText(mPlaceList[position].mAddress);

        if (!mPlaceList[position].mImageUrl.equals("")) {
            Ion.with(img).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mPlaceList[position].mImageUrl);
            final File imgFile = new File(mCtx.getFilesDir().getAbsolutePath()
                    + "/"
                    + mPlaceList[position].mImageUrl.substring(mPlaceList[position].mImageUrl.lastIndexOf('/') + 1,
                    mPlaceList[position].mImageUrl.length()));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

               img.setImageBitmap(myBitmap);
            } else {

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                        App.downloadAndRun(mPlaceList[position].mImageUrl, mCtx, img);
                    }
                }, 50);
            }
        }
        // Ensure the item created and added to the container is returned
        return item;
    }






/*
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_map, null);

            holder.placeTitle = (TextView) convertView.findViewById(R.id.item_map_title);
            holder.placeAddress = (TextView) convertView.findViewById(R.id.item_map_address);
            holder.img = (ImageView) convertView.findViewById(R.id.item_map_image);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.placeTitle.setText(mPlaceList[position].mName);
        holder.placeAddress.setText(mPlaceList[position].mAddress);

        if (!mPlaceList[position].mImageUrl.equals("")) {
            Ion.with( holder.img).placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image).load(mPlaceList[position].mImageUrl);
            final File imgFile = new File(mCtx.getFilesDir().getAbsolutePath()
                    + "/"
                    + mPlaceList[position].mImageUrl.substring(mPlaceList[position].mImageUrl.lastIndexOf('/') + 1,
                    mPlaceList[position].mImageUrl.length()));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                holder.img.setImageBitmap(myBitmap);
            } else {

                final Holder finalHolder = holder;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                        App.downloadAndRun(mPlaceList[position].mImageUrl, mCtx,  finalHolder.img);
                    }
                }, 50);
            }
        }

        return convertView;
    }
    */
}
