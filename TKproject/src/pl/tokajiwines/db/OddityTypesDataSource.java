
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.OddityType;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class OddityTypesDataSource {
    // LogCat tag
    private static final String LOG = "OddityTypesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdOddityType", "Name"
    };

    public OddityTypesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertOddityType(OddityType oddityType) {
        Log.i(LOG, "insertOddityType()");
        ContentValues values = new ContentValues();
        values.put("IdOddityType", oddityType.mIdOddityType);
        values.put("Name", oddityType.mName);
        long insertId = database.insert(DatabaseHelper.TABLE_ODDITY_TYPES, null, values);
        Log.i(LOG, "OddityType with id: " + oddityType.mIdOddityType + " inserted with id: "
                + insertId);
        return insertId;
    }

    public void deleteOddityType(OddityType oddityType) {
        long id = oddityType.mIdOddityType;
        database.delete(DatabaseHelper.TABLE_ODDITY_TYPES, "IdOddityType" + " = " + id, null);
        Log.i(LOG, "Deleted oddityType with id: " + id);
    }

    public void updateOddityType(OddityType oddityTypeOld, OddityType oddityTypeNew) {
        ContentValues values = new ContentValues();
        values.put("IdOddityType", oddityTypeOld.mIdOddityType);
        values.put("Name", oddityTypeNew.mName);
        int rows = database.update(DatabaseHelper.TABLE_ODDITY_TYPES, values, "'"
                + oddityTypeOld.mIdOddityType + "' = '" + oddityTypeNew.mIdOddityType + "'", null);
        Log.i(LOG, "Updated oddityType with id: " + oddityTypeOld.mIdOddityType + " on: " + rows
                + " row(s)");
    }

    public List<OddityType> getAllOddityTypes() {
        Log.i(LOG, "getAllOddityTypes()");
        List<OddityType> oddityTypes = new ArrayList<OddityType>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ODDITY_TYPES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OddityType oddityType = cursorToOddityType(cursor);
            oddityTypes.add(oddityType);
            cursor.moveToNext();
        }
        cursor.close();
        if (oddityTypes.isEmpty()) Log.w(LOG, "OddityTypes are empty()");
        return oddityTypes;
    }

    private OddityType cursorToOddityType(Cursor cursor) {
        OddityType oddityType = new OddityType();
        oddityType.mIdOddityType = cursor.getInt(0);
        oddityType.mName = cursor.getString(1);
        return oddityType;
    }
}
