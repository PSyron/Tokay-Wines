
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Place;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class PlacesDataSource {
    // LogCat tag
    private static final String LOG = "PlacesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "Id", "IdPlace", "Name", "Address", "Latitude", "Longitude", "PlaceType", "Phone",
            "Image", "LastUpdate"
    };

    public PlacesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertPlace(Place place) {
        Log.i(LOG, "insertPlace()");
        ContentValues values = new ContentValues();
        values.put("Id", place.mId);
        values.put("IdPlace", place.mIdPlace);
        values.put("Name", place.mName);
        values.put("Address", place.mAddress);
        values.put("Latitude", place.mLat);
        values.put("Longitude", place.mLng);
        values.put("PlaceType", place.mPlaceType);
        values.put("Phone", place.mPhone);
        values.put("Image", place.mImageUrl);
        values.put("LastUpdate", place.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_PLACES, null, values);
        Log.i(LOG, "Place with id: " + place.mIdPlace + " inserted with id: " + insertId);
        return insertId;
    }

    public void deletePlace(Place place) {
        long id = place.mIdPlace;
        database.delete(DatabaseHelper.TABLE_PLACES, "IdPlace" + " = " + id, null);
        Log.i(LOG, "Deleted place with id: " + id);
    }

    public void updatePlace(Place placeOld, Place placeNew) {
        ContentValues values = new ContentValues();
        values.put("Id", placeOld.mId);
        values.put("IdPlace", placeNew.mIdPlace);
        values.put("Name", placeNew.mName);
        values.put("Address", placeNew.mAddress);
        values.put("Latitude", placeNew.mLat);
        values.put("Longitude", placeNew.mLng);
        values.put("PlaceType", placeNew.mPlaceType);
        values.put("Phone", placeNew.mPhone);
        values.put("Image", placeNew.mImageUrl);
        values.put("LastUpdate", placeNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_PLACES, values, "'" + placeOld.mIdPlace
                + "' = '" + placeNew.mIdPlace + "'", null);
        Log.i(LOG, "Updated place with id: " + placeOld.mIdPlace + " on: " + rows + " row(s)");
    }

    public List<Place> getAllPlaces() {
        Log.i(LOG, "getAllPlaces()");
        List<Place> places = new ArrayList<Place>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PLACES, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Place place = cursorToPlace(cursor);
            places.add(place);
            cursor.moveToNext();
        }
        cursor.close();
        if (places.isEmpty()) Log.w(LOG, "Places are empty()");
        return places;
    }

    public Place[] getPlaces(Double lat, Double lng, Double radius) {
        Place[] search = null;
        String[] selectionArgs = {
                (lat - radius) + "", (lat + radius) + "", (lng - radius) + "", (lng + radius) + ""
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_PLACES, allColumns,
                "Latitude > ? AND Latitude < ? AND Longitude > ? AND Latitude < ?", selectionArgs,
                null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Places are empty for:" + "Latitude: " + lat + "Longitude: " + lng
                    + "Radius: " + radius);
        else {
            search = new Place[cursor.getCount()];
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                Place si = cursorToItemPlace(cursor);
                search[i] = si;
                i++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return search;
    }

    private Place cursorToPlace(Cursor cursor) {
        Place place = new Place();
        place.mId = cursor.getInt(0);
        place.mIdPlace = cursor.getInt(1);
        place.mName = cursor.getString(2);
        place.mAddress = cursor.getString(3);
        place.mLat = cursor.getString(4);
        place.mLng = cursor.getString(5);
        place.mPlaceType = cursor.getString(6);
        place.mPhone = cursor.getString(7);
        place.mImageUrl = cursor.getString(8);
        place.mLastUpdate = cursor.getString(9);
        return place;
    }

    private Place cursorToItemPlace(Cursor cursor) {
        Place p = new Place(cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(8));
        if (!cursor.isNull(7)) p.mPhone = cursor.getString(7);
        return p;
    }
}
