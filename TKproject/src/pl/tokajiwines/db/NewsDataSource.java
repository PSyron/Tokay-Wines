
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.NewsDetails;
import pl.tokajiwines.jsonresponses.NewsListItem;
import pl.tokajiwines.models.News;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class NewsDataSource {
    // LogCat tag
    private static final String LOG = "NewsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;

    private String[] allColumns = {
            "IdNews", "HeaderPl", "HeaderEng", "EntryDate", "StartDate", "EndDate",
            "IdDescription_", "IdAddress_", "IdImageCover_", "LastUpdate"
    };

    public NewsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public NewsListItem[] getNewsList() {
        Log.i(LOG, "getProducerList()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS, new String[] {
                "IdNews", "HeaderEng", "HeaderPl", "IdImageCover_", "IdDescription_"
        }, null, null, null, null, null);
        cursor.moveToFirst();
        NewsListItem[] newses = new NewsListItem[cursor.getCount()];
        int i = 0;
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
        dDs.open();
        iDs.open();
        while (!cursor.isAfterLast()) {
            NewsListItem news = cursorToNewsListItem(cursor, iDs, dDs);
            newses[i] = news;
            cursor.moveToNext();
            i++;
        }
        iDs.close();
        dDs.close();
        cursor.close();
        if (newses == null) Log.w(LOG, "Newses are empty()");

        return newses;
    }

    private NewsListItem cursorToNewsListItem(Cursor cursor, ImagesDataSource iDs,
            DescriptionsDataSource dDs) {
        News n = new News();
        n.mIdNews = cursor.getInt(0);
        n.mHeaderEng = cursor.getString(1);
        n.mHeaderPl = cursor.getString(2);
        n.mIdImageCover_ = cursor.getInt(3);
        n.imageCover = iDs.getImageUrl(n.mIdImageCover_);
        n.mIdDescription_ = cursor.getInt(4);
        n.description = dDs.getDescriptionShort(n.mIdDescription_);
        return new NewsListItem(n);
    }

    public NewsDetails getNewsDetails(int id) {
        Log.i(LOG, "getProducer(id=" + id + ")");
        NewsDetails nd = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS, new String[] {
                "IdNews", "HeaderEng", "HeaderPl", "IdImageCover_", "IdDescription_", "StartDate",
                "EndDate", "EntryDate"
        }, "IdNews" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "News with id= " + id + " doesn't exists");
        else {
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
            iDs.open();
            dDs.open();
            cursor.moveToFirst();
            nd = cursorToNewsDetails(cursor, iDs, dDs);
            iDs.close();
            dDs.close();
        }
        return nd;
    }

    private NewsDetails cursorToNewsDetails(Cursor cursor, ImagesDataSource iDs,
            DescriptionsDataSource dDs) {
        News n = new News();
        n.mIdNews = cursor.getInt(0);
        n.mHeaderEng = cursor.getString(1);
        n.mHeaderPl = cursor.getString(2);
        n.mIdImageCover_ = cursor.getInt(3);
        n.imageCover = iDs.getImageUrl(n.mIdImageCover_);
        n.mIdDescription_ = cursor.getInt(4);
        n.description = dDs.getDescriptionVast(n.mIdDescription_);
        n.mStartDate = cursor.getString(5);
        n.mEndDate = cursor.getString(6);
        n.mEntryDate = cursor.getString(7);
        return new NewsDetails(n);
    }

    public long insertNews(News news) {
        Log.i(LOG, "insertNews()");
        ContentValues values = new ContentValues();
        values.put("IdNews", news.mIdNews);
        values.put("Header", news.mHeaderPl);
        values.put("HeaderEng", news.mHeaderEng);
        values.put("EntryDate", news.mEntryDate);
        values.put("StartDate", news.mStartDate);
        values.put("EndDate", news.mEndDate);
        values.put("IdDescription_", news.mIdDescription_);
        values.put("IdAddress_", news.mIdAddress_);
        values.put("IdImageCover_", news.mIdImageCover_);
        values.put("LastUpdate", news.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_NEWS, null, values);
        Log.i(LOG, "News with id: " + news.mIdNews + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteNews(News news) {
        long id = news.mIdNews;
        database.delete(DatabaseHelper.TABLE_NEWS, "IdNews" + " = " + id, null);
        Log.i(LOG, "Deleted news with id: " + id);
    }

    public void updateNews(News newsOld, News newsNew) {
        ContentValues values = new ContentValues();
        values.put("IdNews", newsOld.mIdNews);
        values.put("Header", newsNew.mHeaderPl);
        values.put("HeaderEng", newsNew.mHeaderEng);
        values.put("EntryDate", newsNew.mEntryDate);
        values.put("StartDate", newsNew.mStartDate);
        values.put("EndDate", newsNew.mEndDate);
        values.put("IdDescription_", newsNew.mIdDescription_);
        values.put("IdAddress_", newsNew.mIdAddress_);
        values.put("IdImageCover_", newsNew.mIdImageCover_);
        values.put("LastUpdate", newsNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_NEWS, values, "'" + newsOld.mIdNews
                + "' = '" + newsNew.mIdNews + "'", null);
        Log.i(LOG, "Updated news with id: " + newsOld.mIdNews + " on: " + rows + " row(s)");
    }

    public List<News> getAllNews() {
        Log.i(LOG, "getAllNews()");
        List<News> newses = new ArrayList<News>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            News news = cursorToNews(cursor);
            newses.add(news);
            cursor.moveToNext();
        }
        cursor.close();
        if (newses.isEmpty()) Log.w(LOG, "News are empty()");
        return newses;
    }

    private News cursorToNews(Cursor cursor) {
        News news = new News();
        news.mIdNews = cursor.getInt(0);
        news.mHeaderPl = cursor.getString(1);
        news.mHeaderEng = cursor.getString(2);
        news.mEntryDate = cursor.getString(3);
        news.mStartDate = cursor.getString(4);
        news.mEndDate = cursor.getString(5);
        news.mIdDescription_ = cursor.getInt(6);
        news.mIdAddress_ = cursor.getInt(7);
        news.mIdImageCover_ = cursor.getInt(8);
        news.mLastUpdate = cursor.getString(9);
        return news;
    }
}
