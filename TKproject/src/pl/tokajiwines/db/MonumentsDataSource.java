
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Monument;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class MonumentsDataSource {
    // LogCat tag
    private static final String LOG = "MonumentsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdMonument", "Name", "IdDescription_", "IdAddress_", "IdImageCover_", "LastUpdate"
    };

    public MonumentsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertMonument(Monument monument) {
        ContentValues values = new ContentValues();
        values.put("IdMonument", monument.mIdMonument);
        values.put("Name", monument.mName);
        values.put("IdDescription_", monument.mIdDescription_);
        values.put("IdAddress_", monument.mIdAddress_);
        values.put("IdImageCover_", monument.mIdImageCover_);
        values.put("LastUpdate", monument.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_MONUMENTS, null, values);
        Log.i(LOG, "Monument with id: " + monument.mIdMonument + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteMonument(Monument monument) {
        long id = monument.mIdMonument;
        database.delete(DatabaseHelper.TABLE_MONUMENTS, "IdMonument" + " = " + id, null);
        Log.i(LOG, "Deleted monument with id: " + id);
    }

    public void updateMonument(Monument monumentOld, Monument monumentNew) {
        ContentValues values = new ContentValues();
        values.put("IdMonument", monumentOld.mIdMonument);
        values.put("Name", monumentNew.mName);
        values.put("IdDescription_", monumentNew.mIdDescription_);
        values.put("IdAddress_", monumentNew.mIdAddress_);
        values.put("IdImageCover_", monumentNew.mIdImageCover_);
        values.put("LastUpdate", monumentNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_MONUMENTS, values, "'"
                + monumentOld.mIdMonument + "' = '" + monumentNew.mIdMonument + "'", null);
        Log.i(LOG, "Updated monument with id: " + monumentOld.mIdMonument + " on: " + rows
                + " row(s)");
    }

    public List<Monument> getAllMonuments() {
        Log.i(LOG, "getAllMonuments()");
        List<Monument> monuments = new ArrayList<Monument>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MONUMENTS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Monument monument = cursorToMonument(cursor);
            monuments.add(monument);
            cursor.moveToNext();
        }
        cursor.close();
        if (monuments.isEmpty()) Log.w(LOG, "Monuments are empty()");
        return monuments;
    }

    private Monument cursorToMonument(Cursor cursor) {
        Monument monument = new Monument();
        monument.mIdMonument = cursor.getInt(0);
        monument.mName = cursor.getString(1);
        monument.mIdDescription_ = cursor.getInt(2);
        monument.mIdAddress_ = cursor.getInt(3);
        monument.mIdImageCover_ = cursor.getInt(4);
        monument.mLastUpdate = cursor.getString(5);
        return monument;
    }
}
