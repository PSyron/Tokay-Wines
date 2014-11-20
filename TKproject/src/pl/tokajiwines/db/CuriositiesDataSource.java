
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Curiosity;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class CuriositiesDataSource {
    // LogCat tag
    private static final String LOG = "CuriositiesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdCurriosity", "Name", "IdCuriosityType_", "IdDescription_", "IdAddress_",
            "IdImageCover_", "LastUpdate"
    };

    public CuriositiesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertOddity(Curiosity curiosity) {
        ContentValues values = new ContentValues();
        values.put("IdOddity", curiosity.mIdCuriosity);
        values.put("Name", curiosity.mName);
        values.put("IdCuriosityType_", curiosity.mIdCuriosityType_);
        values.put("IdDescription_", curiosity.mIdDescription_);
        values.put("IdAddress_", curiosity.mIdAddress_);
        values.put("IdImageCover_", curiosity.mIdImageCover_);
        values.put("LastUpdate", curiosity.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_CURIOSITIES, null, values);
        Log.i(LOG, "Oddity with id: " + curiosity.mIdCuriosity + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteOddity(Curiosity curiosity) {
        long id = curiosity.mIdCuriosity;
        database.delete(DatabaseHelper.TABLE_CURIOSITIES, "IdOddity" + " = " + id, null);
        Log.i(LOG, "Deleted curiosity with id: " + id);
    }

    public void updateOddity(Curiosity curiosityOld, Curiosity curiosityNew) {
        ContentValues values = new ContentValues();
        values.put("IdOddity", curiosityOld.mIdCuriosity);
        values.put("Name", curiosityNew.mName);
        values.put("IdCuriosityType_", curiosityNew.mIdCuriosityType_);
        values.put("IdDescription_", curiosityNew.mIdDescription_);
        values.put("IdAddress_", curiosityNew.mIdAddress_);
        values.put("IdImageCover_", curiosityNew.mIdImageCover_);
        values.put("LastUpdate", curiosityNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_CURIOSITIES, values, "'"
                + curiosityOld.mIdCuriosity + "' = '" + curiosityNew.mIdCuriosity + "'", null);
        Log.i(LOG, "Updated curiosity with id: " + curiosityOld.mIdCuriosity + " on: " + rows
                + " row(s)");
    }

    public List<Curiosity> getAllOddities() {
        Log.i(LOG, "getAllOddities()");
        List<Curiosity> curiosities = new ArrayList<Curiosity>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CURIOSITIES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Curiosity curiosity = cursorToOddity(cursor);
            curiosities.add(curiosity);
            cursor.moveToNext();
        }
        cursor.close();
        if (curiosities.isEmpty()) Log.w(LOG, "Oddities are empty()");
        return curiosities;
    }

    private Curiosity cursorToOddity(Cursor cursor) {
        Curiosity curiosity = new Curiosity();
        curiosity.mIdCuriosity = cursor.getInt(0);
        curiosity.mName = cursor.getString(1);
        curiosity.mIdCuriosityType_ = cursor.getInt(2);
        curiosity.mIdDescription_ = cursor.getInt(3);
        curiosity.mIdAddress_ = cursor.getInt(4);
        curiosity.mIdImageCover_ = cursor.getInt(5);
        curiosity.mLastUpdate = cursor.getString(6);
        return curiosity;
    }
}
