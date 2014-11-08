
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Strain;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class StrainsDataSource {
    // LogCat tag
    private static final String LOG = "StrainsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdStrain", "Name"
    };

    public StrainsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertStrain(Strain strain) {
        Log.i(LOG, "insertStrain()");
        ContentValues values = new ContentValues();
        values.put("IdStrain", strain.mIdStrain);
        values.put("StreetName", strain.mName);
        long insertId = database.insert(DatabaseHelper.TABLE_STRAINS, null, values);
        Log.i(LOG, "Strain with id: " + strain.mIdStrain + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteStrain(Strain strain) {
        long id = strain.mIdStrain;
        database.delete(DatabaseHelper.TABLE_STRAINS, "IdStrain" + " = " + id, null);
        Log.i(LOG, "Deleted strain with id: " + id);
    }

    public void updateStrain(Strain strainOld, Strain strainNew) {
        ContentValues values = new ContentValues();
        values.put("IdStrain", strainOld.mIdStrain);
        values.put("StreetName", strainNew.mName);
        int rows = database.update(DatabaseHelper.TABLE_STRAINS, values, "'" + strainOld.mIdStrain
                + "' = '" + strainNew.mIdStrain + "'", null);
        Log.i(LOG, "Updated strain with id: " + strainOld.mIdStrain + " on: " + rows + " row(s)");
    }

    public List<Strain> getAllStrains() {
        Log.i(LOG, "getAllStrains()");
        List<Strain> strains = new ArrayList<Strain>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_STRAINS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Strain strain = cursorToStrain(cursor);
            strains.add(strain);
            cursor.moveToNext();
        }
        cursor.close();
        if (strains.isEmpty()) Log.w(LOG, "Strains are empty()");
        return strains;
    }

    private Strain cursorToStrain(Cursor cursor) {
        Strain strain = new Strain();
        strain.mIdStrain = cursor.getInt(0);
        strain.mName = cursor.getString(1);
        return strain;
    }
}
