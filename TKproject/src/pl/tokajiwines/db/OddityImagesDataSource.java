
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.OddityImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class OddityImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdOddityImage", "IdOddity_", "IdImage_", "LastUpdate"
    };

    public OddityImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(OddityImage oi) {
        ContentValues values = new ContentValues();
        values.put("IdOddityImage", oi.mIdOddityImage);
        values.put("IdOddity_", oi.mIdOddity_);
        values.put("IdImage_", oi.mIdImage_);
        values.put("LastUpdate", oi.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_ODDITY_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + oi.mIdOddityImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(OddityImage oi) {
        long id = oi.mIdOddityImage;
        database.delete(DatabaseHelper.TABLE_ODDITY_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(OddityImage oiOld, OddityImage oiNew) {
        ContentValues values = new ContentValues();
        values.put("IdOddityImage", oiOld.mIdOddityImage);
        values.put("IdOddity_", oiNew.mIdOddity_);
        values.put("IdImage_", oiNew.mIdImage_);
        values.put("LastUpdate", oiNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_ODDITY_IMAGES, values, "'"
                + oiOld.mIdOddityImage + "' = '" + oiNew.mIdOddityImage + "'", null);
        Log.i(LOG, "Updated image with id: " + oiOld.mIdOddityImage + " on: " + rows + " row(s)");
    }

    public List<OddityImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<OddityImage> images = new ArrayList<OddityImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ODDITY_IMAGES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OddityImage oi = cursorToImage(cursor);
            images.add(oi);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private OddityImage cursorToImage(Cursor cursor) {
        OddityImage oi = new OddityImage();
        oi.mIdOddityImage = cursor.getInt(0);
        oi.mIdOddity_ = cursor.getInt(1);
        oi.mIdImage_ = cursor.getInt(2);
        oi.mLastUpdate = cursor.getString(3);
        return oi;
    }
}
