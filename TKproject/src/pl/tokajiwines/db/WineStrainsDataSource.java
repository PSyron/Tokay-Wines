
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.WineStrain;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class WineStrainsDataSource {
    // LogCat tag
    private static final String LOG = "WineStrainsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdWineStrain", "Content", "IdStrain_", "IdWine_"
    };

    public WineStrainsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public WineStrain[] getWineStrains(int idWine) {
        Log.i(LOG, "getWineStrains");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINE_STRAINS, allColumns, "IdWine_"
                + "=" + idWine, null, null, null, null);
        WineStrain[] strains = null;

        if (cursor.getCount() == 0)
            Log.w(LOG, "Wine strains for wine with id= " + idWine + " don't exist");
        else {
            StrainsDataSource sDs = new StrainsDataSource(mContext);
            sDs.open();
            cursor.moveToFirst();
            strains = new WineStrain[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                strains[i] = cursorToWineStrain(cursor, sDs);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
            sDs.close();
        }

        if (strains == null) Log.w(LOG, "Wine's strains are empty()");
        return strains;
    }

    public long insertWineStrain(WineStrain wineStrain) {
        ContentValues values = new ContentValues();
        values.put("IdWineStrain", wineStrain.mIdWineStrain);
        values.put("Content", wineStrain.mContent);
        values.put("IdStrain_", wineStrain.mIdStrain_);
        values.put("IdWine_", wineStrain.mIdWine_);
        long insertId = database.insert(DatabaseHelper.TABLE_WINE_STRAINS, null, values);
        Log.i(LOG, "WineStrain with id: " + wineStrain.mIdWineStrain + " inserted with id: "
                + insertId);
        return insertId;
    }

    public void deleteWineStrain(WineStrain wineStrain) {
        long id = wineStrain.mIdWineStrain;
        database.delete(DatabaseHelper.TABLE_WINE_STRAINS, "IdWineStrain" + " = " + id, null);
        Log.i(LOG, "Deleted wineStrain with id: " + id);
    }

    public void updateWineStrain(WineStrain wineStrainOld, WineStrain wineStrainNew) {
        ContentValues values = new ContentValues();
        values.put("IdWineStrain", wineStrainOld.mIdWineStrain);
        values.put("Content", wineStrainNew.mContent);
        values.put("IdStrain_", wineStrainNew.mIdStrain_);
        values.put("IdWine_", wineStrainNew.mIdWine_);
        int rows = database.update(DatabaseHelper.TABLE_WINE_STRAINS, values, "'"
                + wineStrainOld.mIdWineStrain + "' = '" + wineStrainNew.mIdWineStrain + "'", null);
        Log.i(LOG, "Updated wineStrain with id: " + wineStrainOld.mIdWineStrain + " on: " + rows
                + " row(s)");
    }

    public List<WineStrain> getAllWineStrains() {
        Log.i(LOG, "getAllWineStrains()");
        List<WineStrain> wineStrains = new ArrayList<WineStrain>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINE_STRAINS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        StrainsDataSource sDs = new StrainsDataSource(mContext);
        sDs.open();
        while (!cursor.isAfterLast()) {
            WineStrain wineStrain = cursorToWineStrain(cursor, sDs);
            wineStrains.add(wineStrain);
            cursor.moveToNext();
        }
        sDs.close();
        cursor.close();
        if (wineStrains.isEmpty()) Log.w(LOG, "WineStrains are empty()");
        return wineStrains;
    }

    private WineStrain cursorToWineStrain(Cursor cursor, StrainsDataSource sDs) {
        WineStrain wineStrain = new WineStrain();
        wineStrain.mIdWineStrain = cursor.getInt(0);
        wineStrain.mContent = cursor.getInt(1);
        wineStrain.mIdStrain_ = cursor.getInt(2);
        wineStrain.mIdWine_ = cursor.getInt(3);
        wineStrain.strain = sDs.getStrain(wineStrain.mIdStrain_);
        return wineStrain;
    }

}
