
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.MonumentImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class MonumentImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdMonumentImage", "IdMonument_", "IdImage_", "LastUpdate"
    };

    public MonumentImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(MonumentImage mi) {
        ContentValues values = new ContentValues();
        values.put("IdMonumentImage", mi.mIdMonumentImage);
        values.put("IdMonument_", mi.mIdMonument_);
        values.put("IdImage_", mi.mIdImage_);
        values.put("LastUpdate", mi.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_MONUMENT_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + mi.mIdMonumentImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(MonumentImage mi) {
        long id = mi.mIdMonumentImage;
        database.delete(DatabaseHelper.TABLE_MONUMENT_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(MonumentImage miOld, MonumentImage miNew) {
        ContentValues values = new ContentValues();
        values.put("IdMonumentImage", miOld.mIdMonumentImage);
        values.put("IdMonument_", miNew.mIdMonument_);
        values.put("IdImage_", miNew.mIdImage_);
        values.put("LastUpdate", miNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_MONUMENT_IMAGES, values, "'"
                + miOld.mIdMonumentImage + "' = '" + miNew.mIdMonumentImage + "'", null);
        Log.i(LOG, "Updated image with id: " + miOld.mIdMonumentImage + " on: " + rows + " row(s)");
    }

    public List<MonumentImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<MonumentImage> images = new ArrayList<MonumentImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MONUMENT_IMAGES, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MonumentImage mi = cursorToImage(cursor);
            images.add(mi);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private MonumentImage cursorToImage(Cursor cursor) {
        MonumentImage mi = new MonumentImage();
        mi.mIdMonumentImage = cursor.getInt(0);
        mi.mIdMonument_ = cursor.getInt(1);
        mi.mIdImage_ = cursor.getInt(2);
        mi.mLastUpdate = cursor.getString(3);
        return mi;
    }
}
