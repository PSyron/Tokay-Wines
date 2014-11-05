
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.tokajiwines.R;

public class WinesAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;

    public WinesAdapter(Activity act) {
        mActivity = act;
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
        return null;
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return 100;
    }

    @Override
    public long getItemId(int position) {
        // TODO zrobic
        return 0;
    }

    // #zmienic #kutwa #zaKodPawłaBaluj
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
        holder.producer = (TextView) rowView.findViewById(R.id.item_wine_producer);
        holder.img = (ImageView) rowView.findViewById(R.id.item_wine_image);

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
