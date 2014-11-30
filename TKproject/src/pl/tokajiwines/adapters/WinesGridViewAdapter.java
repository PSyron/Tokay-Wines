
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.WineListItem;

import java.io.File;

public class WinesGridViewAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private WineListItem[] mWines = null;

    public WinesGridViewAdapter(Activity act, WineListItem[] wines) {
        mActivity = act;
        mWines = wines;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);

    }

    public class Holder {
        TextView name;
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
        rowView = inflater.inflate(R.layout.item_wine_gridview, null);
        holder.name = (TextView) rowView.findViewById(R.id.item_wine_gridView_tV);
        holder.img = (ImageView) rowView.findViewById(R.id.item_wine_gridView_image);
        holder.name.setText(mWines[position].mName);

        final File imgFile = new File(mActivity.getFilesDir().getAbsolutePath()
                + "/"
                + mWines[position].mImageUrl.substring(
                        mWines[position].mImageUrl.lastIndexOf('/') + 1,
                        mWines[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Picasso.with(mActivity).load(imgFile).into(holder.img);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // App.downloadImagesToSdCard(mWines[position].mImageUrl, mActivity, holder.img);
                    App.downloadAndRun(mWines[position].mImageUrl, mActivity, holder.img);
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
