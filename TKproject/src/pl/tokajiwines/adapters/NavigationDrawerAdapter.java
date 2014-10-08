
package pl.tokajiwines.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.tokajiwines.utils.Constans;

public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    public NavigationDrawerAdapter(Context context, int resource) {
        super(context, resource);
        // TODO Auto-generated constructor stub
    }

    //    mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar().getThemedContext(),
    //            android.R.layout.simple_list_item_activated_1, android.R.id.text1, new String[] {
    //                    getString(R.string.title_news), getString(R.string.title_wines),
    //                    getString(R.string.title_wineyards), getString(R.string.title_map),
    //                    getString(R.string.title_tour), getString(R.string.title_settings)
    //            }));

    private int mSelectedItem;

    /**
     * Constructor
     * 
     * @param context
     *            : Context using the adaptor
     * @param ressource
     *            : Drawer list item Layout ID
     * @param objects
     *            : String array which contains the list elements
     */
    public NavigationDrawerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Constans.sNumberOfOptions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get item TextView
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(Constans.sMenuTitles[position]);
        if (position == mSelectedItem) {
            view.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            view.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            view.setTextColor(getContext().getResources().getColor(android.R.color.white));
        }
        return view;
    }
}
