
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.utils.Constans;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.io.File;

public class WinesAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private WineListItem[] mWines = null;
    private int mCurrency;

    public WinesAdapter(Activity act, WineListItem[] wines) {
        mActivity = act;
        mWines = wines;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mCurrency = SharedPreferencesHelper.getSharedPreferencesInt(
                mActivity, SettingsFragment.SharedKeyCurrency, SettingsFragment.DefCurrency);

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
         Holder holder = new Holder();
       // View rowView;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_wine, null);

            holder.name = (TextView) convertView.findViewById(R.id.item_wine_name);
            holder.taste = (TextView) convertView.findViewById(R.id.item_wine_taste);
            holder.type = (TextView) convertView.findViewById(R.id.item_wine_type);
            holder.strain = (TextView) convertView.findViewById(R.id.item_wine_strain);
            holder.price = (TextView) convertView.findViewById(R.id.item_wine_price);
            holder.year = (TextView) convertView.findViewById(R.id.item_wine_year);
            holder.producer = (TextView) convertView.findViewById(R.id.item_wine_producer);
            holder.img = (ImageView) convertView.findViewById(R.id.item_wine_image);

            holder.tasteLayout = (LinearLayout) convertView.findViewById(R.id.item_wine_taste_layout);
            holder.typeLayout = (LinearLayout) convertView.findViewById(R.id.item_wine_type_layout);
            holder.strainLayout = (LinearLayout) convertView.findViewById(R.id.item_wine_strain_layout);
            holder.priceLayout = (LinearLayout) convertView.findViewById(R.id.item_wine_price_layout);
            holder.yearLayout = (LinearLayout) convertView.findViewById(R.id.item_wine_year_layout);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(mWines[position].mName);

        if (mWines[position].mFlavourName != null) {
            holder.taste.setText(mWines[position].mFlavourName);
            holder.tasteLayout.setVisibility(View.VISIBLE);
        } else {
            holder.tasteLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mGrade != null) {
            holder.type.setText(mWines[position].mGrade);
            holder.typeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.typeLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mStrains != null) {
            holder.strain.setText(mWines[position].mStrains);
            holder.strainLayout.setVisibility(View.VISIBLE);
        } else {
            holder.strainLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mPrice != null) {
            holder.priceLayout.setVisibility(View.VISIBLE);
            StringBuilder price = new StringBuilder();
            price.append(mWines[position].mPrice);
            price.append(" ft");
            
            if (mCurrency != 2)
            {
                price.append(" (");
                price.append(String.format("%.2f", Float.parseFloat(mWines[position].mPrice)*Constans.sCurrencyRatio[mCurrency]));
                price.append(" ");
                price.append(Constans.sCurrencyShorts[mCurrency]);
                price.append(")"); 
            }
                holder.price.setText(price);               
            
        } else {
            holder.priceLayout.setVisibility(View.GONE);
        }

        if (mWines[position].mYear != null) {
            holder.yearLayout.setVisibility(View.VISIBLE);
            holder.year.setText(mWines[position].mYear);
        } else {
            holder.yearLayout.setVisibility(View.GONE);
        }

        holder.producer.setText(mWines[position].mProducerName);

        //        Ion.with(holder.img)
        //        .placeholder(R.drawable.placeholder_image)
        //        .error(R.drawable.error_image)
        //        .load(mWines[position].mImageUrl);

        final File imgFile = new File(mActivity.getFilesDir().getAbsolutePath()
                + "/"
                + mWines[position].mImageUrl.substring(
                        mWines[position].mImageUrl.lastIndexOf('/') + 1,
                        mWines[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Picasso.with(mActivity).load(imgFile).into(holder.img);
        } else {
            final ImageView tempIv = holder.img;
            holder.img.setImageResource(R.drawable.no_image);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // App.downloadImagesToSdCard(mWines[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mWines[position].mImageUrl, mActivity, tempIv);
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
        return convertView;
    }
}
