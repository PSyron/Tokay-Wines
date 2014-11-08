
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Lookout;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class LookoutsDataSource {
    // LogCat tag
    private static final String LOG = "LookoutsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdLookout", "Name", "IdDescription_", "IdAddress_", "IdUser_", "IdImageCover_",
            "LastUpdate"
    };

    public LookoutsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertLookout(Lookout lookout) {
        ContentValues values = new ContentValues();
        values.put("IdLookout", lookout.mIdLookout);
        values.put("Name", lookout.mName);
        values.put("IdDescription_", lookout.mIdDescription_);
        values.put("IdAddress_", lookout.mIdAddress_);
        values.put("IdImageCover_", lookout.mIdImageCover_);
        values.put("LastUpdate", lookout.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_LOOKOUTS, null, values);
        Log.i(LOG, "Lookout with id: " + lookout.mIdLookout + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteLookout(Lookout lookout) {
        long id = lookout.mIdLookout;
        database.delete(DatabaseHelper.TABLE_LOOKOUTS, "IdLookout" + " = " + id, null);
        Log.i(LOG, "Deleted lookout with id: " + id);
    }

    public void updateLookout(Lookout lookoutOld, Lookout lookoutNew) {
        ContentValues values = new ContentValues();
        values.put("IdLookout", lookoutOld.mIdLookout);
        values.put("Name", lookoutNew.mName);
        values.put("IdDescription_", lookoutNew.mIdDescription_);
        values.put("IdAddress_", lookoutNew.mIdAddress_);
        values.put("IdImageCover_", lookoutNew.mIdImageCover_);
        values.put("LastUpdate", lookoutNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_LOOKOUTS, values, "'"
                + lookoutOld.mIdLookout + "' = '" + lookoutNew.mIdLookout + "'", null);
        Log.i(LOG, "Updated lookout with id: " + lookoutOld.mIdLookout + " on: " + rows + " row(s)");
    }

    public List<Lookout> getAllLookouts() {
        Log.i(LOG, "getAllLookouts()");
        List<Lookout> lookouts = new ArrayList<Lookout>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_LOOKOUTS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lookout lookout = cursorToLookout(cursor);
            lookouts.add(lookout);
            cursor.moveToNext();
        }
        cursor.close();
        if (lookouts.isEmpty()) Log.w(LOG, "Lookouts are empty()");
        return lookouts;
    }

    private Lookout cursorToLookout(Cursor cursor) {
        Lookout lookout = new Lookout();
        lookout.mIdLookout = cursor.getInt(0);
        lookout.mName = cursor.getString(1);
        lookout.mIdDescription_ = cursor.getInt(2);
        lookout.mIdAddress_ = cursor.getInt(3);
        lookout.mIdImageCover_ = cursor.getInt(4);
        lookout.mLastUpdate = cursor.getString(5);
        return lookout;
    }
}
