package pl.tokajiwines.utils;

import pl.tokajiwines.jsonresponses.SearchItem;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SuggestionProvider extends ContentProvider{
    
    public static SearchItem[] sSearchItems = null; //= {new SearchItem(1, "hotel", "Toldi Fagado")};
    
    
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        String query = uri.getLastPathSegment().toLowerCase();  

        String[] columns = {
                BaseColumns._ID, 
                SearchManager.SUGGEST_COLUMN_TEXT_1, 
             };

         MatrixCursor cursor = new MatrixCursor(columns);
         
         if (sSearchItems != null)
         {
             for (int i = 0; i < sSearchItems.length; i++)
             {
                 if (sSearchItems[i].mName.toLowerCase().contains(query))
                 {
                   String[] tmp = {Integer.toString(i), sSearchItems[i].mName};
                   cursor.addRow(tmp);
                   System.out.println("QUERY IS "+query+", Name is "+sSearchItems[i].mName);
                 }
             }
         }
         return cursor;
    }

    
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
       return 0;
    }
  
    @Override
    public String getType(Uri uri) {
       return null;
    }
  
    @Override
    public Uri insert(Uri uri, ContentValues values) {
       return null;
    }
  
    @Override
    public boolean onCreate() {
       return false;
    }
    
    @Override
    public int update(Uri uri, ContentValues values, String where,
          String[] whereArgs) {
       return 0;
    }

}
