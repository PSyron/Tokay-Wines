
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.RestaurantDetails;
import pl.tokajiwines.jsonresponses.RestaurantListItem;
import pl.tokajiwines.models.Restaurant;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsDataSource {
    // LogCat tag
    private static final String LOG = "RestaurantsDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdRestaurant", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_",
            "IdUser_", "IdImageCover_", "LastUpdate"
    };

    public RestaurantsDataSource(Context context) {
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

    public Restaurant getRestaurant(int id) {
        Log.i(LOG, "getRestaurant(id=" + id + ")");
        Restaurant restaurant = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANTS, allColumns, "IdRestaurant"
                + "=" + id, null, null, null, null);
        if (cursor == null)
            Log.w(LOG, "Restaurant with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            restaurant = cursorToRestaurant(cursor);
        }
        cursor.close();
        return restaurant;
    }

    public RestaurantDetails getRestaurantDetails(int id) {
        Log.i(LOG, "getRestaurantDetails(id=" + id + ")");
        RestaurantDetails restaurant = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANTS, allColumns, "IdRestaurant"
                + "=" + id, null, null, null, null);
        if (cursor == null)
            Log.w(LOG, "Restaurant with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            restaurant = cursorToRestaurantDetails(cursor);
        }
        cursor.close();
        return restaurant;
    }

    public RestaurantListItem[] getRestaurantList() {
        Log.i(LOG, "getRestaurantList()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANTS, new String[] {
                "IdRestaurant", "Name", "IdAddress_", "IdImageCover_", "Phone"
        }, null, null, null, null, "Name");
        cursor.moveToFirst();
        RestaurantListItem[] restaurants = new RestaurantListItem[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            RestaurantListItem restaurant = cursorToRestaurantListItem(cursor);
            restaurants[i] = restaurant;
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        if (restaurants == null) Log.w(LOG, "Restaurants are empty()");

        return restaurants;
    }

    public RestaurantListItem[] getRestaurants(String s) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANTS, new String[] {
                "IdRestaurant", "Name", "IdAddress_", "IdImageCover_", "Phone"
        }, "Name LIKE ?", new String[] {
            "%" + s + "%"
        }, null, null, "Name");
        RestaurantListItem[] restaurants = null;
        if (cursor.getCount() == 0) {
            Log.e(LOG, "Restaurants are empty()");
        } else {
            cursor.moveToFirst();
            restaurants = new RestaurantListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                RestaurantListItem restaurant = cursorToRestaurantListItem(cursor);
                restaurants[i] = restaurant;
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        }

        return restaurants;
    }

    public long insertRestaurant(Restaurant restaurant) {
        ContentValues values = new ContentValues();
        values.put("IdRestaurant", restaurant.mIdRestaurant);
        values.put("Email", restaurant.mEmail);
        values.put("Link", restaurant.mLink);
        values.put("Name", restaurant.mName);
        values.put("Phone", restaurant.mPhone);
        values.put("IdDescription_", restaurant.mIdDescription_);
        values.put("IdAddress_", restaurant.mIdAddress_);
        values.put("IdUser_", restaurant.mIdUser_);
        values.put("IdImageCover_", restaurant.mIdImageCover_);
        values.put("LastUpdate", restaurant.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_RESTAURANTS, null, values);
        Log.i(LOG, "Restaurant with id: " + restaurant.mIdRestaurant + " inserted with id: "
                + insertId);
        return insertId;
    }

    public void deleteRestaurant(Restaurant restaurant) {
        long id = restaurant.mIdRestaurant;
        database.delete(DatabaseHelper.TABLE_RESTAURANTS, "IdRestaurant" + " = " + id, null);
        Log.i(LOG, "Deleted restaurant with id: " + id);
    }

    public void updateRestaurant(Restaurant restaurantOld, Restaurant restaurantNew) {
        ContentValues values = new ContentValues();
        values.put("IdRestaurant", restaurantNew.mIdRestaurant);
        values.put("Email", restaurantNew.mEmail);
        values.put("Link", restaurantNew.mLink);
        values.put("Name", restaurantNew.mName);
        values.put("Phone", restaurantNew.mPhone);
        values.put("IdDescription_", restaurantNew.mIdDescription_);
        values.put("IdAddress_", restaurantNew.mIdAddress_);
        values.put("IdUser_", restaurantNew.mIdUser_);
        values.put("IdImageCover_", restaurantNew.mIdImageCover_);
        values.put("LastUpdate", restaurantNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_RESTAURANTS, values, "'"
                + restaurantOld.mIdRestaurant + "' = '" + restaurantNew.mIdRestaurant + "'", null);
        Log.i(LOG, "Updated restaurant with id: " + restaurantOld.mIdRestaurant + " on: " + rows
                + " row(s)");
    }

    public List<Restaurant> getAllRestaurants() {
        Log.i(LOG, "getAllRestaurants()");
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESTAURANTS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Restaurant restaurant = cursorToRestaurant(cursor);
            restaurants.add(restaurant);
            cursor.moveToNext();
        }
        cursor.close();
        if (restaurants.isEmpty()) Log.w(LOG, "Restaurants are empty()");
        return restaurants;
    }

    private Restaurant cursorToRestaurant(Cursor cursor) {
        Restaurant restaurant = new Restaurant();
        restaurant.mIdRestaurant = cursor.getInt(0);
        restaurant.mEmail = cursor.getString(1);
        restaurant.mLink = cursor.getString(2);
        restaurant.mName = cursor.getString(3);
        restaurant.mPhone = cursor.getString(4);
        restaurant.mIdDescription_ = cursor.getInt(5);
        restaurant.mIdAddress_ = cursor.getInt(6);
        restaurant.mIdUser_ = cursor.getInt(7);
        restaurant.mIdImageCover_ = cursor.getInt(8);
        restaurant.mLastUpdate = cursor.getString(9);
        return restaurant;
    }

    private RestaurantListItem cursorToRestaurantListItem(Cursor cursor) {
        Restaurant r = new Restaurant();
        r.mIdRestaurant = cursor.getInt(0);
        r.mName = cursor.getString(1);
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        AddressesDataSource aDs = new AddressesDataSource(mContext);
        iDs.open();
        r.imageCover = iDs.getImageUrl(cursor.getInt(3));
        iDs.close();
        aDs.open();
        r.address = aDs.getAddress(cursor.getInt(2));
        aDs.close();
        r.mPhone = cursor.getString(4);
        return new RestaurantListItem(r);
    }

    private RestaurantDetails cursorToRestaurantDetails(Cursor cursor) {
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
        AddressesDataSource aDs = new AddressesDataSource(mContext);
        Restaurant restaurant = new Restaurant();
        restaurant.mIdRestaurant = cursor.getInt(0);
        restaurant.mEmail = cursor.getString(1);
        restaurant.mLink = cursor.getString(2);
        restaurant.mName = cursor.getString(3);
        restaurant.mPhone = cursor.getString(4);
        dDs.open();
        restaurant.description = dDs.getDescriptionVast(cursor.getInt(5));
        dDs.close();
        aDs.open();
        restaurant.address = aDs.getAddress(cursor.getInt(6));
        aDs.close();
        restaurant.mIdUser_ = cursor.getInt(7);
        iDs.open();
        restaurant.imageCover = iDs.getImageUrl(cursor.getInt(8));
        iDs.close();
        restaurant.mLastUpdate = cursor.getString(9);

        return new RestaurantDetails(restaurant);
    }
}
