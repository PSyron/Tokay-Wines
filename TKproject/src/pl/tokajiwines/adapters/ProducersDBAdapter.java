
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.tokajiwines.R;
import pl.tokajiwines.models.Producer;

public class ProducersDBAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private Producer[] mProducers = null;

    public ProducersDBAdapter(Activity act, Producer[] producers) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mProducers = producers;
    }

    public class Holder {
        TextView title;
        TextView content;
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        // TODO zrobic
        return mProducers[position];
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return mProducers.length;
    }

    @Override
    public long getItemId(int position) {
        // TODO zrobic
        return 0;
    }

    //TODO 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_producer, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_wineyard_title);
        holder.content = (TextView) rowView.findViewById(R.id.item_wineyard_content);
        holder.img = (ImageView) rowView.findViewById(R.id.item_wineyard_image);
        holder.title.setText(mProducers[position].mName);
        holder.img.setImageResource(R.drawable.placeholder_image);
        // holder.content.setText(mProducers[position].mIdDescription_);
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
