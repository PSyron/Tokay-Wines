package pl.tokajiwines.adapters;


import pl.tokajiwines.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToursAdapter extends BaseAdapter{
    
    Activity mActivity;
    private static LayoutInflater inflater = null;
    private String[] mTours;

    public ToursAdapter(Activity act, String[] tours) {
        mActivity = act;
        mTours = tours;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
    }
    
    public class Holder {
        TextView title;
        ImageView img;
    }

    @Override
    public Object getItem(int position) {
        // TODO zrobic
        return mTours[position];
    }

    @Override
    public int getCount() {
        // TODO po pobraniu
        return mTours.length;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_tour, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_tour_name);
        holder.img = (ImageView) rowView.findViewById(R.id.item_tour_image);
        holder.title.setText(mTours[position]);
        holder.img.setImageResource(R.drawable.no_image);
        return rowView;
    }
}

