
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.HotelDetails;
import pl.tokajiwines.jsonresponses.HotelListItem;
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
    private Context mContext;
    private String[] allColumns = {
            "IdHotel", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_", "IdUser_",
            "IdImageCover_", "LastUpdate"
    };

    public HotelsDataSource(Context context) {
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

    public Hotel getHotel(int id) {
        Log.i(LOG, "getHotel(id=" + id + ")");
        Hotel hotel = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTELS, allColumns, "IdHotel" + "="
                + id, null, null, null, null);
        if (cursor == null)
            Log.w(LOG, "Hotel with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            hotel = cursorToHotel(cursor);
        }
        cursor.close();
        return hotel;
    }

    public HotelDetails getHotelDetails(int id) {
        Log.i(LOG, "getHotelDetails(id=" + id + ")");
        HotelDetails hotel = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTELS, allColumns, "IdHotel" + "="
                + id, null, null, null, null);
        if (cursor == null)
            Log.w(LOG, "Hotel with id= " + id + " doesn't exists");
        else {
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
            AddressesDataSource aDs = new AddressesDataSource(mContext);
            dDs.open();
            aDs.open();
            iDs.open();
            cursor.moveToFirst();
            hotel = cursorToHotelDetails(cursor, iDs, dDs, aDs);
            iDs.close();
            aDs.close();
            dDs.close();
        }
        cursor.close();
        return hotel;
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

    public HotelListItem[] getHotelList() {
        Log.i(LOG, "getHotelList()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTELS, new String[] {
                "IdHotel", "Name", "IdAddress_", "IdImageCover_", "Phone"
        }, null, null, null, null, "Name");
        cursor.moveToFirst();
        HotelListItem[] hotels = new HotelListItem[cursor.getCount()];
        int i = 0;
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        AddressesDataSource aDs = new AddressesDataSource(mContext);
        iDs.open();
        aDs.open();
        while (!cursor.isAfterLast()) {
            HotelListItem hotel = cursorToHotelListItem(cursor, iDs, aDs);
            hotels[i] = hotel;
            cursor.moveToNext();
            i++;
        }
        aDs.close();
        iDs.close();
        cursor.close();
        if (hotels == null) Log.w(LOG, "Hotels are empty()");

        return hotels;
    }

    public HotelListItem[] getHotels(String s) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_HOTELS, new String[] {
                "IdHotel", "Name", "IdAddress_", "IdImageCover_", "Phone"
        }, "Name LIKE ?", new String[] {
            "%" + s + "%"
        }, null, null, "Name");
        HotelListItem[] hotels = null;
        if (cursor.getCount() == 0) {
            Log.e(LOG, "Hotels are empty()");
        } else {
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            AddressesDataSource aDs = new AddressesDataSource(mContext);
            iDs.open();
            aDs.open();
            cursor.moveToFirst();
            hotels = new HotelListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                HotelListItem hotel = cursorToHotelListItem(cursor, iDs, aDs);
                hotels[i] = hotel;
                Log.e("Hotel", hotel.mName);
                cursor.moveToNext();
                i++;
            }
            aDs.close();
            iDs.close();
            cursor.close();
        }

        return hotels;
    }

    public List<Hotel> getAllHotels() {
        Log.i(LOG, "getAllHotels()");
        List<Hotel> hotels = new ArrayList<Hotel>();
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

    private HotelDetails cursorToHotelDetails(Cursor cursor, ImagesDataSource iDs,
            DescriptionsDataSource dDs, AddressesDataSource aDs) {
        Hotel hotel = new Hotel();
        hotel.mIdHotel = cursor.getInt(0);
        hotel.mEmail = cursor.getString(1);
        hotel.mLink = cursor.getString(2);
        hotel.mName = cursor.getString(3);
        hotel.mPhone = cursor.getString(4);
        hotel.description = dDs.getDescriptionVast(cursor.getInt(5));
        hotel.address = aDs.getAddress(cursor.getInt(6));
        hotel.mIdUser_ = cursor.getInt(7);
        hotel.imageCover = iDs.getImageUrl(cursor.getInt(8));
        hotel.mLastUpdate = cursor.getString(9);

        return new HotelDetails(hotel);
    }

    private HotelListItem cursorToHotelListItem(Cursor cursor, ImagesDataSource iDs,
            AddressesDataSource aDs) {
        Hotel h = new Hotel();
        h.mIdHotel = cursor.getInt(0);
        h.mName = cursor.getString(1);
        h.imageCover = iDs.getImageUrl(cursor.getInt(3));
        h.address = aDs.getAddress(cursor.getInt(2));
        h.mPhone = cursor.getString(4);
        return new HotelListItem(h);
    }

}
