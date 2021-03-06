
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
import pl.tokajiwines.jsonresponses.NewsListItem;
import pl.tokajiwines.utils.Log;

import java.io.File;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_news, null);
        holder.title = (TextView) rowView.findViewById(R.id.item_news_title);
        holder.content = (TextView) rowView.findViewById(R.id.item_news_content);
        holder.img = (ImageView) rowView.findViewById(R.id.item_news_image);
        holder.title.setText(mNews[position].mHeader);
        holder.img.setImageResource(R.drawable.no_image);

        //        Ion.with(holder.img).placeholder(R.drawable.placeholder_image)
        //                .error(R.drawable.error_image).load(mNews[position].mImageUrl);

        final File imgFile = new File(mActivity.getFilesDir().getAbsolutePath()
                + "/"
                + mNews[position].mImageUrl.substring(
                        mNews[position].mImageUrl.lastIndexOf('/') + 1,
                        mNews[position].mImageUrl.length()));
        Log.i("App",
                mActivity.getFilesDir().getAbsolutePath()
                        + mNews[position].mImageUrl.substring(
                                mNews[position].mImageUrl.lastIndexOf('/') + 1,
                                mNews[position].mImageUrl.length()));
        if (imgFile.exists()) {
            Picasso.with(mActivity).load(imgFile).into(holder.img);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // App.downloadImagesToSdCard(mNews[position].mImageUrl, mActivity, holder.img);

                    App.downloadAndRun(mNews[position].mImageUrl, mActivity, holder.img);

                }
            }, 50);
        }

        holder.content.setText(mNews[position].mDescription);

        return rowView;
    }
}
