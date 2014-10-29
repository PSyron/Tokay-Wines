
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
import pl.tokajiwines.jsonresponses.HotelsListItem;

public class HotelsAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private HotelsListItem[] mHotels = null;

    public HotelsAdapter(Activity act, HotelsListItem[] hotels) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mHotels = hotels;
    }

    public class Holder {
        TextView title;
        TextView phone;
        TextView adress;
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        // TODO zrobic
        return null;
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return mHotels.length;
    }

    @Override
    public long getItemId(int position) {

        return mHotels[position].mIdHotel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_hotel, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_hotel_name);
        holder.phone = (TextView) rowView.findViewById(R.id.item_hotel_phone);
        holder.adress = (TextView) rowView.findViewById(R.id.item_hotel_adress);
        holder.img = (ImageView) rowView.findViewById(R.id.item_hotel_image);
        holder.title.setText(mHotels[position].mName);
        holder.img.setImageResource(R.drawable.placeholder_image);

        Ion.with(holder.img).placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).load(mHotels[position].mImageUrl);

        //   .load("http://remzo.usermd.net/zpi/photos/akt1_thumb.jpg");
        holder.phone.setText(mHotels[position].mPhone);

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
