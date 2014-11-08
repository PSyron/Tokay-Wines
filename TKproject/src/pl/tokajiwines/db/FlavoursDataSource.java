
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Flavour;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class FlavoursDataSource {
    // LogCat tag
    private static final String LOG = "FlavoursDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdFlavour", "NameEng", "NamePl"
    };

    public FlavoursDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertFlavour(Flavour flavour) {
        Log.i(LOG, "insertFlavour()");
        ContentValues values = new ContentValues();
        values.put("IdFlavour", flavour.mIdFlavour);
        values.put("NameEng", flavour.mNameEng);
        values.put("NamePl", flavour.mNamePl);
        long insertId = database.insert(DatabaseHelper.TABLE_FLAVOURS, null, values);
        Log.i(LOG, "Flavour with id: " + flavour.mIdFlavour + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteFlavour(Flavour flavour) {
        long id = flavour.mIdFlavour;
        database.delete(DatabaseHelper.TABLE_FLAVOURS, "IdFlavour" + " = " + id, null);
        Log.i(LOG, "Deleted flavour with id: " + id);
    }

    public void updateFlavour(Flavour flavourOld, Flavour flavourNew) {
        ContentValues values = new ContentValues();
        values.put("IdFlavour", flavourOld.mIdFlavour);
        values.put("NameEng", flavourNew.mNameEng);
        values.put("NamePl", flavourNew.mNamePl);
        int rows = database.update(DatabaseHelper.TABLE_FLAVOURS, values, "'"
                + flavourOld.mIdFlavour + "' = '" + flavourNew.mIdFlavour + "'", null);
        Log.i(LOG, "Updated flavour with id: " + flavourOld.mIdFlavour + " on: " + rows + " row(s)");
    }

    public List<Flavour> getAllFlavours() {
        Log.i(LOG, "getAllFlavours()");
        List<Flavour> flavoures = new ArrayList<Flavour>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_FLAVOURS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Flavour flavour = cursorToFlavour(cursor);
            flavoures.add(flavour);
            cursor.moveToNext();
        }
        cursor.close();
        if (flavoures.isEmpty()) Log.w(LOG, "Flavours are empty()");
        return flavoures;
    }

    private Flavour cursorToFlavour(Cursor cursor) {
        Flavour flavour = new Flavour();
        flavour.mIdFlavour = cursor.getInt(0);
        flavour.mNameEng = cursor.getString(1);
        flavour.mNamePl = cursor.getString(2);
        return flavour;
    }
}
