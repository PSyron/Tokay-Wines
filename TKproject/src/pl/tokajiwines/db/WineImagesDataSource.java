
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.ImagePagerItem;
import pl.tokajiwines.models.Image;
import pl.tokajiwines.models.WineImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class WineImagesDataSource {
    // LogCat tag
    private static final String LOG = "WineImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdWineImage", "IdWine_", "IdImage_", "LastUpdate"
    };

    public WineImagesDataSource(Context context) {
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

    public long insertImage(WineImage ri) {
        ContentValues values = new ContentValues();
        values.put("IdWineImage", ri.mIdWineImage);
        values.put("IdWine_", ri.mIdWine_);
        values.put("IdImage_", ri.mIdImage_);
        values.put("LastUpdate", ri.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_WINE_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + ri.mIdWineImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(WineImage ri) {
        long id = ri.mIdWineImage;
        database.delete(DatabaseHelper.TABLE_WINE_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(WineImage riOld, WineImage riNew) {
        ContentValues values = new ContentValues();
        values.put("IdWineImage", riOld.mIdWineImage);
        values.put("IdWine_", riNew.mIdWine_);
        values.put("IdImage_", riNew.mIdImage_);
        values.put("LastUpdate", riNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_WINE_IMAGES, values, "'"
                + riOld.mIdWineImage + "' = '" + riNew.mIdWineImage + "'", null);
        Log.i(LOG, "Updated image with id: " + riOld.mIdWineImage + " on: " + rows + " row(s)");
    }

    public ImagePagerItem[] getWineImagesPager(int idWine) {
        Log.i(LOG, "getWineImagesPager");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINE_IMAGES, new String[] {
            "IdImage_"
        }, "IdWine_" + "=" + idWine, null, null, null, null);
        ImagePagerItem[] pImages = null;

        if (cursor.getCount() == 0)
            Log.w(LOG, "Images for wine with id= " + idWine + " don't exist");
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
        if (pImages.length == 0) Log.w(LOG, "Wine images are empty()");
        return pImages;
    }

    public Image getWineImage(int idWine) {
        Log.i(LOG, "getWineImagesPager");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINE_IMAGES, new String[] {
            "IdImage_"
        }, "IdWine_" + "=" + idWine, null, null, null, null);
        Image image = null;
        if (cursor.getCount() == 0)
            Log.w(LOG, "Image for wine with id= " + idWine + " doesn't exist");
        else {
            cursor.moveToFirst();
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            iDs.open();
            image = iDs.getImageUrl(cursor.getInt(0));
            iDs.close();
        }
        cursor.close();
        return image;
    }

    public List<WineImage> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<WineImage> images = new ArrayList<WineImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINE_IMAGES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WineImage ri = cursorToImage(cursor);
            images.add(ri);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private WineImage cursorToImage(Cursor cursor) {
        WineImage ri = new WineImage();
        ri.mIdWineImage = cursor.getInt(0);
        ri.mIdWine_ = cursor.getInt(1);
        ri.mIdImage_ = cursor.getInt(2);
        ri.mLastUpdate = cursor.getString(3);
        return ri;
    }
}
