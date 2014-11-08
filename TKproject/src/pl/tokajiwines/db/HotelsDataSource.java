
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Hotel;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class HotelsDataSource {
    // LogCat tag
    private static final String LOG = "HotelsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdHotel", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_", "IdUser_",
            "IdImageCover_", "LastUpdate"
    };

    public HotelsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertHotel(Hotel hotel) {
        ContentValues values = new ContentValues();
        values.put("IdHotel", hotel.mIdHotel);
        values.put("Email", hotel.mEmail);
        values.put("Link", hotel.mLink);
        values.put("Name", hotel.mName);
        values.put("Phone", hotel.mPhone);
        values.put("IdDescription_", hotel.mIdDescription_);
        values.put("IdAddress_", hotel.mIdAddress_);
        values.put("IdUser_", hotel.mIdUser_);
        values.put("IdImageCover_", hotel.mIdImageCover_);
        values.put("LastUpdate", hotel.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_HOTELS, null, values);
        Log.i(LOG, "Hotel with id: " + hotel.mIdHotel + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteHotel(Hotel hotel) {
        long id = hotel.mIdHotel;
        database.delete(DatabaseHelper.TABLE_HOTELS, "IdHotel" + " = " + id, null);
        Log.i(LOG, "Deleted hotel with id: " + id);
    }

    public void updateHotel(Hotel hotelOld, Hotel hotelNew) {
        ContentValues values = new ContentValues();
        values.put("IdHotel", hotelNew.mIdHotel);
        values.put("Email", hotelNew.mEmail);
        values.put("Link", hotelNew.mLink);
        values.put("Name", hotelNew.mName);
        values.put("Phone", hotelNew.mPhone);
        values.put("IdDescription_", hotelNew.mIdDescription_);
        values.put("IdAddress_", hotelNew.mIdAddress_);
        values.put("IdUser_", hotelNew.mIdUser_);
        values.put("IdImageCover_", hotelNew.mIdImageCover_);
        values.put("LastUpdate", hotelNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_HOTELS, values, "'" + hotelOld.mIdHotel
                + "' = '" + hotelNew.mIdHotel + "'", null);
        Log.i(LOG, "Updated hotel with id: " + hotelOld.mIdHotel + " on: " + rows + " row(s)");
    }

    public List<Hotel> getAllHotels() {
        Log.i(LOG, "getAllHotels()");
        List<Hotel> hotels = new ArrayList<Hotel>();
        /* insertHotel(new Hotel(1, "Email", "www.test.pl", "Testowy", "666666666", 1, 1, 1, 1,
        "Dawno"));*/
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTELS, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hotel hotel = cursorToHotel(cursor);
            hotels.add(hotel);
            cursor.moveToNext();
        }
        cursor.close();
        if (hotels.isEmpty()) Log.w(LOG, "Hotels are empty()");
        return hotels;
    }

    private Hotel cursorToHotel(Cursor cursor) {
        Hotel hotel = new Hotel();
        hotel.mIdHotel = cursor.getInt(0);
        hotel.mEmail = cursor.getString(1);
        hotel.mLink = cursor.getString(2);
        hotel.mName = cursor.getString(3);
        hotel.mPhone = cursor.getString(4);
        hotel.mIdDescription_ = cursor.getInt(5);
        hotel.mIdAddress_ = cursor.getInt(6);
        hotel.mIdUser_ = cursor.getInt(7);
        hotel.mIdImageCover_ = cursor.getInt(8);
        hotel.mLastUpdate = cursor.getString(9);
        return hotel;
    }
}
