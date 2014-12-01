
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.ImagePagerItem;
import pl.tokajiwines.models.ProducerImage;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class ProducerImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdProducerImage", "IdProducer_", "IdImage_", "LastUpdate"
    };

    public ProducerImagesDataSource(Context context) {
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

    public long insertImage(ProducerImage pi) {
        ContentValues values = new ContentValues();
        values.put("IdProducerImage", pi.mIdProducerImage);
        values.put("IdProducer_", pi.mIdProducer_);
        values.put("IdImage_", pi.mIdImage_);
        values.put("LastUpdate", pi.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_PRODUCER_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + pi.mIdProducerImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(ProducerImage pi) {
        long id = pi.mIdProducerImage;
        database.delete(DatabaseHelper.TABLE_PRODUCER_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(ProducerImage piOld, ProducerImage piNew) {
        ContentValues values = new ContentValues();
        values.put("IdProducerImage", piOld.mIdProducerImage);
        values.put("IdProducer_", piNew.mIdProducer_);
        values.put("IdImage_", piNew.mIdImage_);
        values.put("LastUpdate", piNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_PRODUCER_IMAGES, values, "'"
                + piOld.mIdProducerImage + "' = '" + piNew.mIdProducerImage + "'", null);
        Log.i(LOG, "Updated image with id: " + piOld.mIdProducerImage + " on: " + rows + " row(s)");
    }

    public List<ProducerImage> getAllImages() {
        Log.i(LOG, "getAllProducerImages()");
        List<ProducerImage> images = new ArrayList<ProducerImage>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCER_IMAGES, allColumns, null,
                null, null, null, null);

        while (!cursor.isAfterLast()) {
            ProducerImage pi = cursorToImage(cursor);
            images.add(pi);
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private ProducerImage cursorToImage(Cursor cursor) {
        ProducerImage pi = new ProducerImage();
        pi.mIdProducerImage = cursor.getInt(0);
        pi.mIdProducer_ = cursor.getInt(1);
        pi.mIdImage_ = cursor.getInt(2);
        pi.mLastUpdate = cursor.getString(3);
        return pi;
    }

    public ImagePagerItem[] getProducerImagesPager(int idProducer) {
        Log.i(LOG, "getProducerImagesPager");
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCER_IMAGES, new String[] {
            "IdImage_"
        }, "IdProducer_" + "=" + idProducer, null, null, null, null);
        ImagePagerItem[] pImages = null;

        if (cursor.getCount() == 0)
            Log.w(LOG, "Images for producer with id= " + idProducer + " don't exist");
        else {
            cursor.moveToFirst();
            pImages = new ImagePagerItem[cursor.getCount()];
            int i = 0;
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            iDs.open();
            while (!cursor.isAfterLast()) {
                pImages[i] = new ImagePagerItem(iDs.getImageUrl(cursor.getInt(0)));
                cursor.moveToNext();
            }
            iDs.close();
            cursor.close();
        }

        if (pImages.length == 0) Log.w(LOG, "Producer images are empty()");
        return pImages;
    }
}
