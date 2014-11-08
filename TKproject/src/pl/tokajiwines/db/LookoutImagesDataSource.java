
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.LookoutImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class LookoutImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdLookoutImage", "IdLookout_", "IdImage_", "LastUpdate"
    };

    public LookoutImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(LookoutImage li) {
        ContentValues values = new ContentValues();
        values.put("IdLookoutImage", li.mIdLookoutImage);
        values.put("IdLookout_", li.mIdLookout_);
        values.put("IdImage_", li.mIdImage_);
        values.put("LastUpdate", li.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_NEWS_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + li.mIdLookoutImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(LookoutImage li) {
        long id = li.mIdLookoutImage;
        database.delete(DatabaseHelper.TABLE_NEWS_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(LookoutImage liOld, LookoutImage liNew) {
        ContentValues values = new ContentValues();
        values.put("IdLookoutImage", liOld.mIdLookoutImage);
        values.put("IdLookout_", liNew.mIdLookout_);
        values.put("IdImage_", liNew.mIdImage_);
        values.put("LastUpdate", liNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_NEWS_IMAGES, values, "'"
                + liOld.mIdLookoutImage + "' = '" + liNew.mIdLookoutImage + "'", null);
        Log.i(LOG, "Updated image with id: " + liOld.mIdLookoutImage + " on: " + rows + " row(s)");
    }

    public List<LookoutImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<LookoutImage> images = new ArrayList<LookoutImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS_IMAGES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LookoutImage li = cursorToImage(cursor);
            images.add(li);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private LookoutImage cursorToImage(Cursor cursor) {
        LookoutImage li = new LookoutImage();
        li.mIdLookoutImage = cursor.getInt(0);
        li.mIdLookout_ = cursor.getInt(1);
        li.mIdImage_ = cursor.getInt(2);
        li.mLastUpdate = cursor.getString(3);
        return li;
    }
}
