
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.HotelImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class HotelImagesDataSource {
    // LogCat tag
    private static final String LOG = "HotelImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdHotelImage", "IdHotel_", "IdImage_", "LastUpdate"
    };

    public HotelImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(HotelImage hi) {
        ContentValues values = new ContentValues();
        values.put("IdHotelImage", hi.mIdHotelImage);
        values.put("IdHotel_", hi.mIdHotel_);
        values.put("IdImage_", hi.mIdImage_);
        values.put("LastUpdate", hi.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_HOTEL_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + hi.mIdHotelImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(HotelImage hi) {
        long id = hi.mIdHotelImage;
        database.delete(DatabaseHelper.TABLE_HOTEL_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(HotelImage hiOld, HotelImage hiNew) {
        ContentValues values = new ContentValues();
        values.put("IdHotelImage", hiOld.mIdHotelImage);
        values.put("IdHotel_", hiNew.mIdHotel_);
        values.put("IdImage_", hiNew.mIdImage_);
        values.put("LastUpdate", hiNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_HOTEL_IMAGES, values, "'"
                + hiOld.mIdHotelImage + "' = '" + hiNew.mIdHotelImage + "'", null);
        Log.i(LOG, "Updated image with id: " + hiOld.mIdHotelImage + " on: " + rows + " row(s)");
    }

    public List<HotelImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<HotelImage> images = new ArrayList<HotelImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTEL_IMAGES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HotelImage hi = cursorToImage(cursor);
            images.add(hi);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private HotelImage cursorToImage(Cursor cursor) {
        HotelImage hi = new HotelImage();
        hi.mIdHotelImage = cursor.getInt(0);
        hi.mIdHotel_ = cursor.getInt(1);
        hi.mIdImage_ = cursor.getInt(2);
        hi.mLastUpdate = cursor.getString(3);
        return hi;
    }
}
