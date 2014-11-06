
package pl.tokajiwines.adapters;

import com.koushikdutta.ion.Ion;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.NewsListItem;
import pl.tokajiwines.jsonresponses.WineListItem;

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
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        // TODO zrobic
        return mWines[position];
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return mWines.length;
    }

    @Override
    public long getItemId(int position) {
        // TODO zrobic
        return mWines[position].mIdWine;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
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
        
        holder.name.setText(mWines[position].mName);
        holder.taste.setText(mWines[position].mFlavourName);
        holder.type.setText(mWines[position].mGrade);
        holder.strain.setText("");
        holder.price.setText(mWines[position].mPrice);
        holder.producer.setText(mWines[position].mProducerName);
        holder.year.setText(mWines[position].mYear);
        Ion.with(holder.img)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .load(mWines[position].mImageUrl);

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
