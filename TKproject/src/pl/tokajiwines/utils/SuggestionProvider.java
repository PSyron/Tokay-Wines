package pl.tokajiwines.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

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
        String query = uri.getLastPathSegment().toLowerCase(Locale.getDefault());  

        String[] columns = {
                BaseColumns._ID, 
                SearchManager.SUGGEST_COLUMN_TEXT_1, 
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
             };

         MatrixCursor cursor = new MatrixCursor(columns);
         
         if (sSearchItems != null)
         {
             for (int i = 0; i < sSearchItems.length; i++)
             {
                 if (removeDiacriticalMarks(sSearchItems[i].mName.toLowerCase(Locale.getDefault())).contains(query))
                 {
                   String[] tmp = {Integer.toString(i), sSearchItems[i].mName, Integer.toString(i)};
                   cursor.addRow(tmp);
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
    
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
