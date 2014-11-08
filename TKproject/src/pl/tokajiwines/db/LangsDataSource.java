
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Lang;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class LangsDataSource {
    // LogCat tag
    private static final String LOG = "LangsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdLang", "Name", "ShortName"
    };

    public LangsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertLang(Lang lang) {
        Log.i(LOG, "insertLang()");
        ContentValues values = new ContentValues();
        values.put("IdLang", lang.mIdLang);
        values.put("Name", lang.mName);
        values.put("ShortName", lang.mShortName);
        long insertId = database.insert(DatabaseHelper.TABLE_LANGS, null, values);
        Log.i(LOG, "Lang with id: " + lang.mIdLang + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteLang(Lang lang) {
        long id = lang.mIdLang;
        database.delete(DatabaseHelper.TABLE_LANGS, "IdLang" + " = " + id, null);
        Log.i(LOG, "Deleted lang with id: " + id);
    }

    public void updateLang(Lang langOld, Lang langNew) {
        ContentValues values = new ContentValues();
        values.put("IdLang", langOld.mIdLang);
        values.put("Name", langNew.mName);
        values.put("ShortName", langNew.mShortName);
        int rows = database.update(DatabaseHelper.TABLE_LANGS, values, "'" + langOld.mIdLang
                + "' = '" + langNew.mIdLang + "'", null);
        Log.i(LOG, "Updated lang with id: " + langOld.mIdLang + " on: " + rows + " row(s)");
    }

    public List<Lang> getAllLangs() {
        Log.i(LOG, "getAllLangs()");
        List<Lang> langs = new ArrayList<Lang>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_LANGS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lang lang = cursorToLang(cursor);
            langs.add(lang);
            cursor.moveToNext();
        }
        cursor.close();
        if (langs.isEmpty()) Log.w(LOG, "Langs are empty()");
        return langs;
    }

    private Lang cursorToLang(Cursor cursor) {
        Lang lang = new Lang();
        lang.mIdLang = cursor.getInt(0);
        lang.mName = cursor.getString(1);
        lang.mShortName = cursor.getString(2);
        return lang;
    }
}
