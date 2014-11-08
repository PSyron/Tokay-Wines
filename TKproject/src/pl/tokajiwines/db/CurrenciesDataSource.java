
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Currency;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesDataSource {
    // LogCat tag
    private static final String LOG = "CurrenciesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdCurrency", "Name", "NameShort", "Ratio", "LastUpdate"
    };

    public CurrenciesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertCurrency(Currency currency) {
        Log.i(LOG, "insertCurrency()");
        ContentValues values = new ContentValues();
        values.put("IdCurrency", currency.mIdCurrency);
        values.put("Name", currency.mName);
        values.put("NameShort", currency.mNameShort);
        values.put("Ratio", currency.mRatio);
        values.put("LastUpdate", currency.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_CURRENCIES, null, values);
        Log.i(LOG, "Currency with id: " + currency.mIdCurrency + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteCurrency(Currency currency) {
        long id = currency.mIdCurrency;
        database.delete(DatabaseHelper.TABLE_CURRENCIES, "IdCurrency" + " = " + id, null);
        Log.i(LOG, "Deleted currency with id: " + id);
    }

    public void updateCurrency(Currency currencyOld, Currency currencyNew) {
        ContentValues values = new ContentValues();
        values.put("IdCurrency", currencyOld.mIdCurrency);
        values.put("Name", currencyNew.mName);
        values.put("NameShort", currencyNew.mNameShort);
        values.put("Ratio", currencyNew.mRatio);
        values.put("LastUpdate", currencyNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_CURRENCIES, values, "'"
                + currencyOld.mIdCurrency + "' = '" + currencyNew.mIdCurrency + "'", null);
        Log.i(LOG, "Updated currency with id: " + currencyOld.mIdCurrency + " on: " + rows
                + " row(s)");
    }

    public List<Currency> getAllCurrencies() {
        Log.i(LOG, "getAllCurrencies()");
        List<Currency> currencyes = new ArrayList<Currency>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CURRENCIES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Currency currency = cursorToCurrency(cursor);
            currencyes.add(currency);
            cursor.moveToNext();
        }
        cursor.close();
        if (currencyes.isEmpty()) Log.w(LOG, "Currencies are empty()");
        return currencyes;
    }

    private Currency cursorToCurrency(Cursor cursor) {
        Currency currency = new Currency();
        currency.mIdCurrency = cursor.getInt(0);
        currency.mName = cursor.getString(1);
        currency.mNameShort = cursor.getString(2);
        currency.mRatio = cursor.getFloat(3);
        currency.mLastUpdate = cursor.getString(4);
        return currency;
    }
}
