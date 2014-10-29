
package pl.tokajiwines.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import pl.tokajiwines.R;
import pl.tokajiwines.jsonresponses.NewsListItem;

public class NewsAdapter extends BaseAdapter {

    Activity mActivity;
    private static LayoutInflater inflater = null;
    private NewsListItem[] mNews = null;

    public NewsAdapter(Activity act, NewsListItem[] news) {
        mActivity = act;
        inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        mNews = news;
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
        return mNews.length;
    }

    @Override
    public long getItemId(int position) {
        
        return mNews[position].mIdNews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_news, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_news_title);
        holder.content = (TextView) rowView.findViewById(R.id.item_news_content);
        holder.img = (ImageView) rowView.findViewById(R.id.item_news_image);
        holder.title.setText(mNews[position].mHeader);
        holder.img.setImageResource(R.drawable.placeholder_image);
        
        Ion.with(holder.img)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .load(mNews[position].mImageUrl);
        
     //   .load("http://remzo.usermd.net/zpi/photos/akt1_thumb.jpg");
        holder.content.setText(mNews[position].mDescription);
        
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
