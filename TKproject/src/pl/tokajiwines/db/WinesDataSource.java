
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Wine;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class WinesDataSource {
    // LogCat tag
    private static final String LOG = "WinesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdWine", "Name", "ProdDate", "Price", "Volume", "AvailablePL", "LastUpdate",
            "IdColor_", "IdFlavour_", "IdProducer_", "IdDescription_", "IdGrade_", "IdImageCover_"
    };

    public WinesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertWine(Wine wine) {
        Log.i(LOG, "insertWine()");
        ContentValues values = new ContentValues();
        values.put("IdWine", wine.mIdWine);
        values.put("Name", wine.mName);
        values.put("ProdDate", wine.mProdDate);
        values.put("Price", wine.mPrice);
        values.put("Volume", wine.mVolume);
        values.put("AvailablePL", wine.mAvailablePL);
        values.put("LastUpdate", wine.mLastUpdate);
        values.put("IdColor_", wine.mIdColor_);
        values.put("IdFlavour_", wine.mIdFlavour_);
        values.put("IdProducer_", wine.mIdProducer_);
        values.put("IdDescription_", wine.mIdDescription_);
        values.put("IdGrade_", wine.mIdGrade_);
        values.put("IdImageCover_", wine.mIdImageCover_);
        long insertId = database.insert(DatabaseHelper.TABLE_WINES, null, values);
        Log.i(LOG, "Wine with id: " + wine.mIdWine + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteWine(Wine wine) {
        long id = wine.mIdWine;
        database.delete(DatabaseHelper.TABLE_WINES, "IdWine" + " = " + id, null);
        Log.i(LOG, "Deleted wine with id: " + id);
    }

    public void updateWine(Wine wineOld, Wine wineNew) {
        ContentValues values = new ContentValues();
        values.put("IdWine", wineOld.mIdWine);
        values.put("Name", wineNew.mName);
        values.put("ProdDate", wineNew.mProdDate);
        values.put("Price", wineNew.mPrice);
        values.put("Volume", wineNew.mVolume);
        values.put("AvailablePL", wineNew.mAvailablePL);
        values.put("LastUpdate", wineNew.mLastUpdate);
        values.put("IdColor_", wineNew.mIdColor_);
        values.put("IdFlavour_", wineNew.mIdFlavour_);
        values.put("IdProducer_", wineNew.mIdProducer_);
        values.put("IdDescription_", wineNew.mIdDescription_);
        values.put("IdGrade_", wineNew.mIdGrade_);
        values.put("IdImageCover_", wineNew.mIdImageCover_);
        int rows = database.update(DatabaseHelper.TABLE_WINES, values, "'" + wineOld.mIdWine
                + "' = '" + wineNew.mIdWine + "'", null);
        Log.i(LOG, "Updated wine with id: " + wineOld.mIdWine + " on: " + rows + " row(s)");
    }

    public List<Wine> getAllWines() {
        Log.i(LOG, "getAllWines()");
        List<Wine> wines = new ArrayList<Wine>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Wine wine = cursorToWine(cursor);
            wines.add(wine);
            cursor.moveToNext();
        }
        cursor.close();
        if (wines.isEmpty()) Log.w(LOG, "Wines are empty()");
        return wines;
    }

    private Wine cursorToWine(Cursor cursor) {
        Wine wine = new Wine();

        wine.mIdWine = cursor.getInt(0);
        wine.mName = cursor.getString(1);
        wine.mProdDate = cursor.getInt(2);
        wine.mPrice = cursor.getDouble(3);
        wine.mVolume = cursor.getInt(4);
        wine.mAvailablePL = cursor.getInt(5);
        wine.mLastUpdate = cursor.getString(6);
        wine.mIdColor_ = cursor.getInt(7);
        wine.mIdFlavour_ = cursor.getInt(8);
        wine.mIdProducer_ = cursor.getInt(9);
        wine.mIdDescription_ = cursor.getInt(10);
        wine.mIdGrade_ = cursor.getInt(11);
        wine.mIdImageCover_ = cursor.getInt(12);
        return wine;
    }
}
