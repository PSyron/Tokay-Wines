
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.ImagePagerItem;
import pl.tokajiwines.models.NewsImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class NewsImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdNewsImage", "IdNews_", "IdImage_", "LastUpdate"
    };

    public NewsImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public ImagePagerItem[] getNewsImagesPager(int idNews) {
        Log.i(LOG, "getNewsImagesPager");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS_IMAGES, new String[] {
            "IdImage_"
        }, "IdNews_" + "=" + idNews, null, null, null, null);
        ImagePagerItem[] pImages = null;

        if (cursor.getCount() == 0)
            Log.w(LOG, "Images for news with id= " + idNews + " don't exist");
        else {
            cursor.moveToFirst();
            pImages = new ImagePagerItem[cursor.getCount()];
            int i = 0;
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            iDs.open();
            while (!cursor.isAfterLast()) {
                pImages[i] = new ImagePagerItem(iDs.getImageUrl(cursor.getInt(0)));
                cursor.moveToNext();
                i++;
            }
            iDs.close();
        }
        cursor.close();
        if (pImages.length == 0) Log.w(LOG, "News images are empty()");
        return pImages;
    }

    public long insertImage(NewsImage ni) {
        ContentValues values = new ContentValues();
        values.put("IdNewsImage", ni.mIdNewsImage);
        values.put("IdNews_", ni.mIdNews_);
        values.put("IdImage_", ni.mIdImage_);
        values.put("LastUpdate", ni.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_NEWS_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + ni.mIdNewsImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(NewsImage ni) {
        long id = ni.mIdNewsImage;
        database.delete(DatabaseHelper.TABLE_NEWS_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(NewsImage niOld, NewsImage niNew) {
        ContentValues values = new ContentValues();
        values.put("IdNewsImage", niOld.mIdNewsImage);
        values.put("IdNews_", niNew.mIdNews_);
        values.put("IdImage_", niNew.mIdImage_);
        values.put("LastUpdate", niNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_NEWS_IMAGES, values, "'"
                + niOld.mIdNewsImage + "' = '" + niNew.mIdNewsImage + "'", null);
        Log.i(LOG, "Updated image with id: " + niOld.mIdNewsImage + " on: " + rows + " row(s)");
    }

    public List<NewsImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<NewsImage> images = new ArrayList<NewsImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS_IMAGES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NewsImage ni = cursorToImage(cursor);
            images.add(ni);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private NewsImage cursorToImage(Cursor cursor) {
        NewsImage ni = new NewsImage();
        ni.mIdNewsImage = cursor.getInt(0);
        ni.mIdNews_ = cursor.getInt(1);
        ni.mIdImage_ = cursor.getInt(2);
        ni.mLastUpdate = cursor.getString(3);
        return ni;
    }
}
