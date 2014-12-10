
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Description;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class DescriptionsDataSource {
    // LogCat tag
    private static final String LOG = "DescriptionsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdDescription", "IdLang_", "Version", "Short", "Vast", "IdUser_", "LastUpdate"
    };

    public DescriptionsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertDescription(Description description) {
        Log.i(LOG, "insertDescription()");
        ContentValues values = new ContentValues();
        values.put("IdDescription", description.mIdDescription);
        values.put("IdLang_", description.mIdLang_);
        values.put("Version", description.mVersion);
        values.put("Short", description.mShort);
        values.put("Vast", description.mVast);
        values.put("IdUser_", description.mIdUser_);
        values.put("LastUpdate", description.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_DESCRIPTIONS, null, values);
        Log.i(LOG, "Description with id: " + description.mIdDescription + " inserted with id: "
                + insertId);
        return insertId;
    }

    public void deleteDescription(Description description) {
        long id = description.mIdDescription;
        database.delete(DatabaseHelper.TABLE_DESCRIPTIONS, "IdDescription" + " = " + id, null);
        Log.i(LOG, "Deleted description with id: " + id);
    }

    public void updateDescription(Description descriptionOld, Description descriptionNew) {
        ContentValues values = new ContentValues();
        values.put("IdDescription", descriptionOld.mIdDescription);
        values.put("IdLang_", descriptionNew.mIdLang_);
        values.put("Version", descriptionNew.mVersion);
        values.put("Short", descriptionNew.mShort);
        values.put("Vast", descriptionNew.mVast);
        values.put("IdUser_", descriptionNew.mIdUser_);
        values.put("LastUpdate", descriptionNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_DESCRIPTIONS, values, "'"
                + descriptionOld.mIdDescription + "' = '" + descriptionNew.mIdDescription + "'",
                null);
        Log.i(LOG, "Updated description with id: " + descriptionOld.mIdDescription + " on: " + rows
                + " row(s)");
    }

    public Description getDescriptionShort(int id) {
        Log.i(LOG, "getDescriptionShort(id=" + id + ")");
        Description description = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_DESCRIPTIONS, new String[] {
                "IdLang_", "Short"
        }, "IdDescription" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Description with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            description = cursorToDescriptionShort(cursor);
        }
        return description;
    }

    public Description getDescriptionVast(int id) {
        Log.i(LOG, "getDescriptionVast(id=" + id + ")");
        Description description = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_DESCRIPTIONS, new String[] {
                "IdLang_", "Vast"
        }, "IdDescription" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Description with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            description = cursorToDescriptionVast(cursor);
        }
        return description;
    }

    public List<Description> getAllDescriptions() {
        Log.i(LOG, "getAllDescriptions()");
        List<Description> descriptions = new ArrayList<Description>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_DESCRIPTIONS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Description description = cursorToDescription(cursor);
            descriptions.add(description);
            cursor.moveToNext();
        }
        cursor.close();
        if (descriptions.isEmpty()) Log.w(LOG, "Descriptions are empty()");
        return descriptions;
    }

    private Description cursorToDescription(Cursor cursor) {
        Description description = new Description();
        description.mIdDescription = cursor.getInt(0);
        description.mIdLang_ = cursor.getInt(1);
        description.mVersion = cursor.getInt(2);
        description.mShort = cursor.getString(3).replace("\\n", "\n").replace("\\r", "");
        description.mVast = cursor.getString(4).replace("\\n", "\n").replace("\\r", "");
        description.mIdUser_ = cursor.getInt(5);
        description.mLastUpdate = cursor.getString(6);
        return description;
    }

    private Description cursorToDescriptionShort(Cursor cursor) {
        Description description = new Description();
        description.mIdLang_ = cursor.getInt(0);
        description.mShort = cursor.getString(1).replace("\\n", "\n").replace("\\r", "");
        return description;
    }

    private Description cursorToDescriptionVast(Cursor cursor) {
        Description description = new Description();
        description.mIdLang_ = cursor.getInt(0);
        description.mVast = cursor.getString(1).replace("\\n", "\n").replace("\\r", "");
        return description;
    }

    private Description cursorToDescriptionUpdate(Cursor cursor) {
        Description description = new Description();
        description.mIdDescription = cursor.getInt(0);
        description.mLastUpdate = cursor.getString(1);
        return description;
    }
}
