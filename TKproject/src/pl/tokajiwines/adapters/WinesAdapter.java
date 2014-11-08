
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.WineListItem;

import java.io.File;

public class WinesAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private WineListItem[] mWines = null;

    public WinesAdapter(Activity act, WineListItem[] wines) {
        mActivity = act;
        mWines = wines;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);

    }

    public class Holder {
        TextView name;
        TextView taste;
        TextView type;
        TextView strain;
        TextView year;
        TextView price;
        TextView producer;
        LinearLayout tasteLayout;
        LinearLayout typeLayout;
        LinearLayout strainLayout;
        LinearLayout yearLayout;
        LinearLayout priceLayout;
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        return mWines[position];
    }

    @Override
    public int getCount() {
        return mWines.length;
    }

    @Override
    public long getItemId(int position) {
        return mWines[position].mIdWine;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_wine, null);
        holder.name = (TextView) rowView.findViewById(R.id.item_wine_name);
        holder.taste = (TextView) rowView.findViewById(R.id.item_wine_taste);
        holder.type = (TextView) rowView.findViewById(R.id.item_wine_type);
        holder.strain = (TextView) rowView.findViewById(R.id.item_wine_strain);
        holder.price = (TextView) rowView.findViewById(R.id.item_wine_price);
        holder.year = (TextView) rowView.findViewById(R.id.item_wine_year);
        holder.producer = (TextView) rowView.findViewById(R.id.item_wine_producer);
        holder.img = (ImageView) rowView.findViewById(R.id.item_wine_image);

        holder.tasteLayout = (LinearLayout) rowView.findViewById(R.id.item_wine_taste_layout);
        holder.typeLayout = (LinearLayout) rowView.findViewById(R.id.item_wine_type_layout);
        holder.strainLayout = (LinearLayout) rowView.findViewById(R.id.item_wine_strain_layout);
        holder.priceLayout = (LinearLayout) rowView.findViewById(R.id.item_wine_price_layout);
        holder.yearLayout = (LinearLayout) rowView.findViewById(R.id.item_wine_year_layout);

        holder.name.setText(mWines[position].mName);

        if (mWines[position].mFlavourName != null) {
            holder.taste.setText(mWines[position].mFlavourName);
        } else {
            holder.tasteLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mGrade != null) {
            holder.type.setText(mWines[position].mGrade);
        } else {
            holder.typeLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mStrains != null) {
            holder.strain.setText(mWines[position].mStrains);
        } else {
            holder.strainLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mPrice != null) {
            holder.price.setText(mWines[position].mPrice);
        } else {
            holder.priceLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mYear != null) {
            holder.year.setText(mWines[position].mYear);
        } else {
            holder.yearLayout.setVisibility(View.GONE);
        }

        holder.producer.setText(mWines[position].mProducerName);

        holder.year.setText(mWines[position].mYear);
        //        Ion.with(holder.img)
        //        .placeholder(R.drawable.placeholder_image)
        //        .error(R.drawable.error_image)
        //        .load(mWines[position].mImageUrl);

        final File imgFile = new File(App.fileAbsPath
                + mWines[position].mImageUrl.substring(
                        mWines[position].mImageUrl.lastIndexOf('/') + 1,
                        mWines[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            holder.img.setImageBitmap(myBitmap);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    App.downloadImagesToSdCard(mWines[position].mImageUrl, mActivity, holder.img);

                }
            }, 50);
        }

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
