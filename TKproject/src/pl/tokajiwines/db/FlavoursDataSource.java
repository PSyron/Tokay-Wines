
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Flavour;
import pl.tokajiwines.utils.Log;

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

    public Flavour getFlavour(int id) {
        Flavour flavour = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_FLAVOURS, allColumns, "IdFlavour" + "="
                + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Flavour with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            flavour = cursorToFlavour(cursor);
        }
        return flavour;
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

    public Flavour[] getAllFlavours() {
        Log.i(LOG, "getAllFlavours()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_FLAVOURS, allColumns, null, null, null,
                null, null);

        Flavour[] flavours = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            flavours = new Flavour[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                Flavour flavour = cursorToFlavour(cursor);
                flavours[i] = flavour;
                cursor.moveToNext();
                i++;
            }
        } else
            Log.w(LOG, "Flavours are empty()");

        cursor.close();
        return flavours;
    }

    private Flavour cursorToFlavour(Cursor cursor) {
        Flavour flavour = new Flavour();
        flavour.mIdFlavour = cursor.getInt(0);
        flavour.mNameEng = cursor.getString(1);
        flavour.mNamePl = cursor.getString(2);
        return flavour;
    }
}
