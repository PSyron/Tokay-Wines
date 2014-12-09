
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.SearchItem;
import pl.tokajiwines.models.Search;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchDataSource {
    // LogCat tag
    private static final String LOG = "SearchesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "Id", "IdSearch", "Name", "SearchType", "LastUpdate"
    };

    public SearchDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertSearch(Search search) {
        Log.i(LOG, "insertSearch()");
        ContentValues values = new ContentValues();
        values.put("Id", search.mId);
        values.put("IdSearch", search.mIdSearch);
        values.put("StreetName", search.mName);
        values.put("SearchType", search.mSearchType);
        values.put("LastUpdate", search.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_SEARCH, null, values);
        Log.i(LOG, "Search with id: " + search.mIdSearch + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteSearch(Search search) {
        long id = search.mIdSearch;
        database.delete(DatabaseHelper.TABLE_SEARCH, "IdSearch" + " = " + id, null);
        Log.i(LOG, "Deleted search with id: " + id);
    }

    public void updateSearch(Search searchOld, Search searchNew) {
        ContentValues values = new ContentValues();
        values.put("IdSearch", searchOld.mIdSearch);
        values.put("StreetName", searchNew.mName);
        int rows = database.update(DatabaseHelper.TABLE_SEARCH, values, "'" + searchOld.mIdSearch
                + "' = '" + searchNew.mIdSearch + "'", null);
        Log.i(LOG, "Updated search with id: " + searchOld.mIdSearch + " on: " + rows + " row(s)");
    }

    public List<Search> getAllSearches() {
        Log.i(LOG, "getAllSearches()");
        List<Search> searches = new ArrayList<Search>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_SEARCH, allColumns, null, null, null,
                null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Search is empty");
        else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Search search = cursorToSearch(cursor);
                searches.add(search);
                cursor.moveToNext();
            }
        }
        cursor.close();
        if (searches.isEmpty()) Log.w(LOG, "Searches are empty()");
        return searches;
    }

    public SearchItem[] getSearchItems(String s) {
        SearchItem[] search = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_SEARCH, new String[] {
                "Id", "Name", "SearchType"
        }, "'Name'" + "LIKE ?", new String[] {
            "%" + s + "%"
        }, null, null, "Name");
        if (cursor.getCount() == 0)
            Log.w(LOG, "String \"" + s + "\" doesn't exists");
        else {
            search = new SearchItem[cursor.getCount()];
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                SearchItem si = cursorToSearchItem(cursor);
                search[i] = si;
                i++;
                cursor.moveToNext();
            }
        }
        return search;
    }

    public SearchItem[] getAllSearchItems() {
        SearchItem[] search = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_SEARCH, allColumns, null, null, null,
                null, "Name");
        if (cursor.getCount() == 0)
            Log.w("SearchDataSource", "Database is empty");
        else {
            search = new SearchItem[cursor.getCount()];
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                SearchItem si = cursorToSearchItem(cursor);
                search[i] = si;
                i++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return search;
    }

    private Search cursorToSearch(Cursor cursor) {
        Search search = new Search();
        search.mId = cursor.getInt(0);
        search.mIdSearch = cursor.getInt(1);
        search.mName = cursor.getString(2);
        search.mSearchType = cursor.getString(3);
        search.mLastUpdate = cursor.getString(4);
        return search;
    }

    private SearchItem cursorToSearchItem(Cursor cursor) {
        return new SearchItem(cursor.getInt(1), cursor.getString(3), cursor.getString(2));
    }
}
