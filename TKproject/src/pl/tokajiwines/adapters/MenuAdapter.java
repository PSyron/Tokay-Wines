
package pl.tokajiwines.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pl.tokajiwines.R;
import pl.tokajiwines.utils.Constans;

public class MenuAdapter extends BaseAdapter {
    String mTitle;
    Context mCtx;

    public MenuAdapter(Context context) {
        mCtx = context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Constans.sMenuTitles.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return Constans.sMenuTitles[arg0];
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_menu, parent, false);
        TextView title;
        title = (TextView) row.findViewById(R.id.menu_title);
        title.setCompoundDrawablesWithIntrinsicBounds(Constans.sMenuIcons[position], 0, 0, 0);
        title.setText(Constans.sMenuTitles[position]);

        return (row);
    }
}
