
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Address;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class AddressesDataSource {
    // LogCat tag
    private static final String LOG = "AddressesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdAddress", "StreetName", "StreetNumber", "HouseNumber", "City", "Postcode",
            "Latitude", "Longitude", "LastUpdate"
    };

    public AddressesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public Address getAddress(int id) {
        Log.i(LOG, "getProducer(id=" + id + ")");
        Address address = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_ADDRESSES, allColumns, "IdAddress"
                + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Address with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            address = cursorToAddress(cursor);
        }
        return address;
    }

    public long insertAddress(Address address) {
        Log.i(LOG, "insertAddress()");
        ContentValues values = new ContentValues();
        values.put("IdAddress", address.mIdAddress);
        values.put("StreetName", address.mStreetName);
        values.put("StreetNumber", address.mStreetNumber);
        values.put("HouseNumber", address.mHouseNumber);
        values.put("City", address.mCity);
        values.put("Postcode", address.mPostcode);
        values.put("Latitude", address.mLatitude);
        values.put("Longitude", address.mLongitude);
        values.put("LastUpdate", address.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_ADDRESSES, null, values);
        Log.i(LOG, "Address with id: " + address.mIdAddress + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteAddress(Address address) {
        long id = address.mIdAddress;
        database.delete(DatabaseHelper.TABLE_ADDRESSES, "IdAddress" + " = " + id, null);
        Log.i(LOG, "Deleted address with id: " + id);
    }

    public void updateAddress(Address addressOld, Address addressNew) {
        ContentValues values = new ContentValues();
        values.put("IdAddress", addressOld.mIdAddress);
        values.put("StreetName", addressNew.mStreetName);
        values.put("StreetNumber", addressNew.mStreetNumber);
        values.put("HouseNumber", addressNew.mHouseNumber);
        values.put("City", addressNew.mCity);
        values.put("Postcode", addressNew.mPostcode);
        values.put("Latitude", addressNew.mLatitude);
        values.put("Longitude", addressNew.mLongitude);
        values.put("LastUpdate", addressNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_ADDRESSES, values, "'"
                + addressOld.mIdAddress + "' = '" + addressNew.mIdAddress + "'", null);
        Log.i(LOG, "Updated address with id: " + addressOld.mIdAddress + " on: " + rows + " row(s)");
    }

    public List<Address> getAllAddresses() {
        Log.i(LOG, "getAllAddresses()");
        List<Address> addresses = new ArrayList<Address>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ADDRESSES, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Address address = cursorToAddress(cursor);
            addresses.add(address);
            cursor.moveToNext();
        }
        cursor.close();
        if (addresses.isEmpty()) Log.w(LOG, "Addresses are empty()");
        return addresses;
    }

    private Address cursorToAddress(Cursor cursor) {
        Address address = new Address();
        address.mIdAddress = cursor.getInt(0);
        address.mStreetName = cursor.getString(1);
        address.mStreetNumber = cursor.getInt(2);
        address.mHouseNumber = cursor.getInt(3);
        address.mCity = cursor.getString(4);
        address.mPostcode = cursor.getString(5);
        address.mLatitude = cursor.getFloat(6);
        address.mLongitude = cursor.getFloat(7);
        address.mLastUpdate = cursor.getString(8);
        return address;
    }
}
