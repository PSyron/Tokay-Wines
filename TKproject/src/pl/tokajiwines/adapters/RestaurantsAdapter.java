
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.RestaurantListItem;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_restaurant, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_restaurant_name);
        holder.phone = (TextView) rowView.findViewById(R.id.item_restaurant_phone);
        holder.address = (TextView) rowView.findViewById(R.id.item_restaurant_address);
        holder.img = (ImageView) rowView.findViewById(R.id.item_restaurant_image);
        holder.title.setText(mRestaurants[position].mName);
        holder.img.setImageResource(R.drawable.placeholder_image);
        holder.phone.setText(mRestaurants[position].mPhone);
        holder.address.setText(mRestaurants[position].mStreetName + " "
                + mRestaurants[position].mStreetNumber + " " + mRestaurants[position].mHouseNumber
                + " " + mRestaurants[position].mCity + " " + mRestaurants[position].mPostCode);

        Ion.with(holder.img).placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).load(mRestaurants[position].mImageUrl);

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
