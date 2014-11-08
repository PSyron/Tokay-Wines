
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Oddity;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class OdditiesDataSource {
    // LogCat tag
    private static final String LOG = "OdditiesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdOddity", "Name", "Header", "IdDescription_", "IdAddress_", "IdUser_",
            "IdImageCover_", "LastUpdate"
    };

    public OdditiesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertOddity(Oddity oddity) {
        ContentValues values = new ContentValues();
        values.put("IdOddity", oddity.mIdOddity);
        values.put("Name", oddity.mName);
        values.put("Header", oddity.mHeader);
        values.put("IdDescription_", oddity.mIdDescription_);
        values.put("IdAddress_", oddity.mIdAddress_);
        values.put("IdImageCover_", oddity.mIdImageCover_);
        values.put("LastUpdate", oddity.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_ODDITIES, null, values);
        Log.i(LOG, "Oddity with id: " + oddity.mIdOddity + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteOddity(Oddity oddity) {
        long id = oddity.mIdOddity;
        database.delete(DatabaseHelper.TABLE_ODDITIES, "IdOddity" + " = " + id, null);
        Log.i(LOG, "Deleted oddity with id: " + id);
    }

    public void updateOddity(Oddity oddityOld, Oddity oddityNew) {
        ContentValues values = new ContentValues();
        values.put("IdOddity", oddityOld.mIdOddity);
        values.put("Name", oddityNew.mName);
        values.put("Header", oddityNew.mHeader);
        values.put("IdDescription_", oddityNew.mIdDescription_);
        values.put("IdAddress_", oddityNew.mIdAddress_);
        values.put("IdImageCover_", oddityNew.mIdImageCover_);
        values.put("LastUpdate", oddityNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_ODDITIES, values, "'" + oddityOld.mIdOddity
                + "' = '" + oddityNew.mIdOddity + "'", null);
        Log.i(LOG, "Updated oddity with id: " + oddityOld.mIdOddity + " on: " + rows + " row(s)");
    }

    public List<Oddity> getAllOddities() {
        Log.i(LOG, "getAllOddities()");
        List<Oddity> oddities = new ArrayList<Oddity>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ODDITIES, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Oddity oddity = cursorToOddity(cursor);
            oddities.add(oddity);
            cursor.moveToNext();
        }
        cursor.close();
        if (oddities.isEmpty()) Log.w(LOG, "Oddities are empty()");
        return oddities;
    }

    private Oddity cursorToOddity(Cursor cursor) {
        Oddity oddity = new Oddity();
        oddity.mIdOddity = cursor.getInt(0);
        oddity.mName = cursor.getString(1);
        oddity.mHeader = cursor.getString(2);
        oddity.mIdDescription_ = cursor.getInt(3);
        oddity.mIdAddress_ = cursor.getInt(4);
        oddity.mIdImageCover_ = cursor.getInt(5);
        oddity.mLastUpdate = cursor.getString(6);
        return oddity;
    }
}
