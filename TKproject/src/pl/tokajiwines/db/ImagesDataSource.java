
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Image;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class ImagesDataSource {
    // LogCat tag
    private static final String LOG = "ImagesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdImage", "Version", "Width", "Height", "Weight", "Author", "Image", "IdUser_",
            "LastUpdate"
    };

    public ImagesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertImage(Image image) {
        ContentValues values = new ContentValues();
        values.put("IdImage", image.mIdImage);
        values.put("Version", image.mVersion);
        values.put("Width", image.mWidth);
        values.put("Height", image.mHeight);
        values.put("Weight", image.mWeight);
        values.put("Author", image.mAuthor);
        values.put("Image", image.mImage);
        values.put("IdUser_", image.mIdUser_);
        values.put("LastUpdate", image.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_IMAGES, null, values);
        Log.i(LOG, "Image with id: " + image.mIdImage + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteImage(Image image) {
        long id = image.mIdImage;
        database.delete(DatabaseHelper.TABLE_IMAGES, "IdImage" + " = " + id, null);
        Log.i(LOG, "Deleted image with id: " + id);
    }

    public void updateImage(Image imageOld, Image imageNew) {
        ContentValues values = new ContentValues();
        values.put("IdImage", imageOld.mIdImage);
        values.put("Version", imageNew.mVersion);
        values.put("Width", imageNew.mWidth);
        values.put("Height", imageNew.mHeight);
        values.put("Weight", imageNew.mWeight);
        values.put("Author", imageNew.mAuthor);
        values.put("Image", imageNew.mImage);
        values.put("IdUser_", imageNew.mIdUser_);
        values.put("LastUpdate", imageNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_IMAGES, values, "'" + imageOld.mIdImage
                + "' = '" + imageNew.mIdImage + "'", null);
        Log.i(LOG, "Updated image with id: " + imageOld.mIdImage + " on: " + rows + " row(s)");
    }

    public Image getImageUrl(int id) {
        Log.i(LOG, "getImageURL(id=" + id + ")");
        Image image = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_IMAGES, new String[] {
                "IdImage", "Image"
        }, "IdImage" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Image with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            image = cursorToImageUrl(cursor);
        }
        return image;
    }

    public List<Image> getAllImages() {
        Log.i(LOG, "getAllImages()");
        List<Image> images = new ArrayList<Image>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_IMAGES, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Image image = cursorToImage(cursor);
            images.add(image);
            cursor.moveToNext();
        }
        cursor.close();
        if (images.isEmpty()) Log.w(LOG, "Images are empty()");
        return images;
    }

    private Image cursorToImage(Cursor cursor) {
        Image image = new Image();
        image.mIdImage = cursor.getInt(0);
        image.mVersion = cursor.getInt(1);
        image.mWidth = cursor.getInt(2);
        image.mHeight = cursor.getInt(3);
        image.mWeight = cursor.getInt(4);
        image.mAuthor = cursor.getString(5);
        image.mImage = cursor.getString(6).replace("\\n", "\n").replace("\\r", "");
        image.mIdUser_ = cursor.getInt(7);
        image.mLastUpdate = cursor.getString(8);
        return image;
    }

    private Image cursorToImageUrl(Cursor cursor) {
        Image image = new Image();
        image.mIdImage = cursor.getInt(0);
        image.mImage = cursor.getString(1).replace("\\n", "\n").replace("\\r", "");
        ;
        return image;
    }
}
