
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.CuriosityImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class CuriosityImagesDataSource {
    // LogCat tag
    private static final String LOG = "CuriosityImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdCuriosityImage", "IdCuriosity_", "IdImage_", "LastUpdate"
    };

    public CuriosityImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(CuriosityImage oi) {
        ContentValues values = new ContentValues();
        values.put("IdCuriosityImage", oi.mIdCuriosityImage);
        values.put("IdCuriosity_", oi.mIdCuriosity_);
        values.put("IdImage_", oi.mIdImage_);
        values.put("LastUpdate", oi.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_CURIOSITY_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + oi.mIdCuriosityImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(CuriosityImage oi) {
        long id = oi.mIdCuriosityImage;
        database.delete(DatabaseHelper.TABLE_CURIOSITY_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(CuriosityImage oiOld, CuriosityImage oiNew) {
        ContentValues values = new ContentValues();
        values.put("IdCuriosityImage", oiOld.mIdCuriosityImage);
        values.put("IdCuriosity_", oiNew.mIdCuriosity_);
        values.put("IdImage_", oiNew.mIdImage_);
        values.put("LastUpdate", oiNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_CURIOSITY_IMAGES, values, "'"
                + oiOld.mIdCuriosityImage + "' = '" + oiNew.mIdCuriosityImage + "'", null);
        Log.i(LOG, "Updated image with id: " + oiOld.mIdCuriosityImage + " on: " + rows + " row(s)");
    }

    public List<CuriosityImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<CuriosityImage> images = new ArrayList<CuriosityImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CURIOSITY_IMAGES, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CuriosityImage oi = cursorToImage(cursor);
            images.add(oi);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private CuriosityImage cursorToImage(Cursor cursor) {
        CuriosityImage oi = new CuriosityImage();
        oi.mIdCuriosityImage = cursor.getInt(0);
        oi.mIdCuriosity_ = cursor.getInt(1);
        oi.mIdImage_ = cursor.getInt(2);
        oi.mLastUpdate = cursor.getString(3);
        return oi;
    }
}
