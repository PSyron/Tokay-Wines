
package pl.tokajiwines.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ProducersAdapter extends BaseAdapter {
    //wypierdolic
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }
    /*
        Activity mActivity;
        private static LayoutInflater inflater = null;
        private ProducerListItem[] mProducers = null;

        public ProducersAdapter(Activity act, ProducerListItem[] producers) {
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
            return null;
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
            rowView = inflater.inflate(R.layout.item_vineyard, null);
            holder.title = (TextView) rowView.findViewById(R.id.item_wineyard_title);
            holder.content = (TextView) rowView.findViewById(R.id.item_wineyard_content);
            holder.img = (ImageView) rowView.findViewById(R.id.item_wineyard_image);
            holder.title.setText(mProducers[position].mName);
            holder.img.setImageResource(R.drawable.ic_launcher);
            holder.content.setText(mProducers[position].mDescription);
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
        */

}
