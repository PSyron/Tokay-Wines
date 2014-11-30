
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
import pl.tokajiwines.jsonresponses.HotelListItem;

import java.io.File;

public class HotelsAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private HotelListItem[] mHotels = null;

    public HotelsAdapter(Activity act, HotelListItem[] hotels) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mHotels = hotels;
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
        return mHotels[position];
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_hotel, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_hotel_name);
        holder.phone = (TextView) rowView.findViewById(R.id.item_hotel_phone);
        holder.address = (TextView) rowView.findViewById(R.id.item_hotel_address);
        holder.img = (ImageView) rowView.findViewById(R.id.item_hotel_image);
        holder.title.setText(mHotels[position].mName);
        holder.img.setImageResource(R.drawable.no_image);
        holder.phone.setText(mHotels[position].mPhone);
        holder.address.setText(mHotels[position].mStreetName + " "
                + mHotels[position].mStreetNumber + " " + mHotels[position].mHouseNumber + " "
                + mHotels[position].mCity + " " + mHotels[position].mPostCode);

        //        Ion.with(holder.img).placeholder(R.drawable.placeholder_image)
        //                .error(R.drawable.error_image).load(mHotels[position].mImageUrl);
        final File imgFile = new File(mActivity.getFilesDir().getAbsolutePath()
                + "/"
                + mHotels[position].mImageUrl.substring(
                        mHotels[position].mImageUrl.lastIndexOf('/') + 1,
                        mHotels[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Picasso.with(mActivity).load(imgFile).into(holder.img);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //  App.downloadImagesToSdCard(mHotels[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mHotels[position].mImageUrl, mActivity, holder.img);
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
