
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.tokajiwines.R;

public class WineyardsAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;

    public WineyardsAdapter(Activity act) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView title;
        TextView content;
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

    //TODO zmienic kurwa
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_wineyard, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_wineyard_title);
        holder.content = (TextView) rowView.findViewById(R.id.item_wineyard_content);
        holder.img = (ImageView) rowView.findViewById(R.id.item_wineyard_image);
        holder.title.setText("Tytuł " + position);
        holder.img.setImageResource(R.drawable.ic_launcher);
        holder.content.setText("Tekst o pozycji " + position + "\n\n ma trzy linijki");
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
