
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Color;
import pl.tokajiwines.utils.Log;

public class ColorsDataSource {
    // LogCat tag
    private static final String LOG = "ColorsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdColor", "NameEng", "NamePl"
    };

    public ColorsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public Color getColor(int id) {
        Color color = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_COLORS, allColumns, "IdColor" + "="
                + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Color with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            color = cursorToColor(cursor);
        }
        return color;
    }

    public long insertColor(Color color) {
        Log.i(LOG, "insertColor()");
        ContentValues values = new ContentValues();
        values.put("IdColor", color.mIdColor);
        values.put("NameEng", color.mNameEng);
        values.put("NamePl", color.mNamePl);
        long insertId = database.insert(DatabaseHelper.TABLE_COLORS, null, values);
        Log.i(LOG, "Color with id: " + color.mIdColor + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteColor(Color color) {
        long id = color.mIdColor;
        database.delete(DatabaseHelper.TABLE_COLORS, "IdColor" + " = " + id, null);
        Log.i(LOG, "Deleted color with id: " + id);
    }

    public void updateColor(Color colorOld, Color colorNew) {
        ContentValues values = new ContentValues();
        values.put("IdColor", colorOld.mIdColor);
        values.put("NameEng", colorNew.mNameEng);
        values.put("NamePl", colorNew.mNamePl);
        int rows = database.update(DatabaseHelper.TABLE_COLORS, values, "'" + colorOld.mIdColor
                + "' = '" + colorNew.mIdColor + "'", null);
        Log.i(LOG, "Updated color with id: " + colorOld.mIdColor + " on: " + rows + " row(s)");
    }

    public Color[] getAllColors() {
        Log.i(LOG, "getAllStrains()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_COLORS, allColumns, null, null, null,
                null, null);

        Color[] colors = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            colors = new Color[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                Color color = cursorToColor(cursor);
                colors[i] = color;
                cursor.moveToNext();
                i++;
            }
        } else
            Log.w(LOG, "Colors are empty()");
        cursor.close();
        return colors;
    }

    private Color cursorToColor(Cursor cursor) {
        Color color = new Color();
        color.mIdColor = cursor.getInt(0);
        color.mNameEng = cursor.getString(1);
        color.mNamePl = cursor.getString(2);
        return color;
    }
}
