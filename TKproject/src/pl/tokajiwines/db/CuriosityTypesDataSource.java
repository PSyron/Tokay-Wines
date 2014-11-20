
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.CuriosityType;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class CuriosityTypesDataSource {
    // LogCat tag
    private static final String LOG = "CuriosityTypesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdCuriosityType", "Name"
    };

    public CuriosityTypesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertOddityType(CuriosityType curiosityType) {
        Log.i(LOG, "insertOddityType()");
        ContentValues values = new ContentValues();
        values.put("IdCuriosityType", curiosityType.mIdCuriosityType);
        values.put("Name", curiosityType.mName);
        long insertId = database.insert(DatabaseHelper.TABLE_CURIOSITY_TYPES, null, values);
        Log.i(LOG, "OddityType with id: " + curiosityType.mIdCuriosityType + " inserted with id: "
                + insertId);
        return insertId;
    }

    public void deleteOddityType(CuriosityType curiosityType) {
        long id = curiosityType.mIdCuriosityType;
        database.delete(DatabaseHelper.TABLE_CURIOSITY_TYPES, "IdOddityType" + " = " + id, null);
        Log.i(LOG, "Deleted curiosityType with id: " + id);
    }

    public void updateOddityType(CuriosityType curiosityTypeOld, CuriosityType curiosityTypeNew) {
        ContentValues values = new ContentValues();
        values.put("IdCuriosityType", curiosityTypeOld.mIdCuriosityType);
        values.put("Name", curiosityTypeNew.mName);
        int rows = database.update(DatabaseHelper.TABLE_CURIOSITY_TYPES, values, "'"
                + curiosityTypeOld.mIdCuriosityType + "' = '" + curiosityTypeNew.mIdCuriosityType
                + "'", null);
        Log.i(LOG, "Updated curiosityType with id: " + curiosityTypeOld.mIdCuriosityType + " on: "
                + rows + " row(s)");
    }

    public List<CuriosityType> getAllOddityTypes() {
        Log.i(LOG, "getAllOddityTypes()");
        List<CuriosityType> curiosityTypes = new ArrayList<CuriosityType>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CURIOSITY_TYPES, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CuriosityType curiosityType = cursorToOddityType(cursor);
            curiosityTypes.add(curiosityType);
            cursor.moveToNext();
        }
        cursor.close();
        if (curiosityTypes.isEmpty()) Log.w(LOG, "OddityTypes are empty()");
        return curiosityTypes;
    }

    private CuriosityType cursorToOddityType(Cursor cursor) {
        CuriosityType curiosityType = new CuriosityType();
        curiosityType.mIdCuriosityType = cursor.getInt(0);
        curiosityType.mName = cursor.getString(1);
        return curiosityType;
    }
}
