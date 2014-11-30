
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.RestaurantListItem;

import java.io.File;

public class RestaurantsAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private RestaurantListItem[] mRestaurants = null;

    public RestaurantsAdapter(Activity act, RestaurantListItem[] restaurants) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mRestaurants = restaurants;
    }

    public class Holder {
        TextView title;
        TextView phone;
        TextView address;
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        // TODO zrobic
        return mRestaurants[position];
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return mRestaurants.length;
    }

    @Override
    public long getItemId(int position) {

        return mRestaurants[position].mIdRestaurant;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_restaurant, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_restaurant_name);
        holder.phone = (TextView) rowView.findViewById(R.id.item_restaurant_phone);
        holder.address = (TextView) rowView.findViewById(R.id.item_restaurant_address);
        holder.img = (ImageView) rowView.findViewById(R.id.item_restaurant_image);
        holder.title.setText(mRestaurants[position].mName);
        holder.img.setImageResource(R.drawable.no_image);
        holder.phone.setText(mRestaurants[position].mPhone);
        holder.address.setText(mRestaurants[position].mStreetName + " "
                + mRestaurants[position].mStreetNumber + " " + mRestaurants[position].mHouseNumber
                + " " + mRestaurants[position].mCity + " " + mRestaurants[position].mPostCode);
        //
        //        Ion.with(holder.img).placeholder(R.drawable.placeholder_image)
        //                .error(R.drawable.error_image).load(mRestaurants[position].mImageUrl);

        final File imgFile = new File(mActivity.getFilesDir().getAbsolutePath()
                + "/"
                + mRestaurants[position].mImageUrl.substring(
                        mRestaurants[position].mImageUrl.lastIndexOf('/') + 1,
                        mRestaurants[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Picasso.with(mActivity).load(imgFile).into(holder.img);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //                    App.downloadImagesToSdCard(mRestaurants[position].mImageUrl, mActivity,
                    //                            holder.img);
                    App.downloadAndRun(mRestaurants[position].mImageUrl, mActivity, holder.img);
                }
            }, 50);
        }

        //   .load("http://remzo.usermd.net/zpi/photos/akt1_thumb.jpg");

        //        rowView.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                // TODO Auto-generated method stub
        //                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG)
        //                        .show();
        //            }
        //        });
        return rowView;
    }
}
