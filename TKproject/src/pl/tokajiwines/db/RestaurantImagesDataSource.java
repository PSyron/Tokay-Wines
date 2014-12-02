
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.ImagePagerItem;
import pl.tokajiwines.models.RestaurantImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class RestaurantImagesDataSource {
    // LogCat tag
    private static final String LOG = "RestaurantImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdRestaurantImage", "IdRestaurant_", "IdImage_", "LastUpdate"
    };

    public RestaurantImagesDataSource(Context context) {
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

    public long insertImage(RestaurantImage ri) {
        ContentValues values = new ContentValues();
        values.put("IdRestaurantImage", ri.mIdRestaurantImage);
        values.put("IdRestaurant_", ri.mIdRestaurant_);
        values.put("IdImage_", ri.mIdImage_);
        values.put("LastUpdate", ri.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_RESTAURANT_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + ri.mIdRestaurantImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(RestaurantImage ri) {
        long id = ri.mIdRestaurantImage;
        database.delete(DatabaseHelper.TABLE_RESTAURANT_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(RestaurantImage riOld, RestaurantImage riNew) {
        ContentValues values = new ContentValues();
        values.put("IdRestaurantImage", riOld.mIdRestaurantImage);
        values.put("IdRestaurant_", riNew.mIdRestaurant_);
        values.put("IdImage_", riNew.mIdImage_);
        values.put("LastUpdate", riNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_RESTAURANT_IMAGES, values, "'"
                + riOld.mIdRestaurantImage + "' = '" + riNew.mIdRestaurantImage + "'", null);
        Log.i(LOG, "Updated image with id: " + riOld.mIdRestaurantImage + " on: " + rows
                + " row(s)");
    }

    public ImagePagerItem[] getRestaurantImagesPager(int idRestaurant) {
        Log.i(LOG, "getRestaurantImagesPager");
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANT_IMAGES, new String[] {
            "IdImage_"
        }, "IdRestaurant_" + "=" + idRestaurant, null, null, null, null);
        ImagePagerItem[] pImages = null;

        if (cursor.getCount() == 0)
            Log.w(LOG, "Images for restaurant with id= " + idRestaurant + " don't exist");
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
        if (pImages.length == 0) Log.w(LOG, "Restaurant images are empty()");
        return pImages;
    }

    public List<RestaurantImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<RestaurantImage> images = new ArrayList<RestaurantImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANT_IMAGES, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RestaurantImage ri = cursorToImage(cursor);
            images.add(ri);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private RestaurantImage cursorToImage(Cursor cursor) {
        RestaurantImage ri = new RestaurantImage();
        ri.mIdRestaurantImage = cursor.getInt(0);
        ri.mIdRestaurant_ = cursor.getInt(1);
        ri.mIdImage_ = cursor.getInt(2);
        ri.mLastUpdate = cursor.getString(3);
        return ri;
    }
}
