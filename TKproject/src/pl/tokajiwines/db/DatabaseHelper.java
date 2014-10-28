
package pl.tokajiwines.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ------------------------ Database settings ----------------//  

    private static final String DATABASE_NAME = "1183_release";
    private static final int DATABASE_VERSION = 1;
    private static final String LOG = "DatabaseHelper"; // LogCat tag       

    // ------------------------ Table names ----------------//  

    public static final String TABLE_ADDRESSES = "tAddresses";
    public static final String TABLE_COLORS = "tColors";
    public static final String TABLE_CURRENCIES = "tCurrencies";
    public static final String TABLE_DESCRIPTIONS = "tDescriptions";
    public static final String TABLE_FLAVOURS = "tFlavours";
    public static final String TABLE_GRADES = "tGrades";
    public static final String TABLE_HOTELS = "tHotels";
    public static final String TABLE_HOTEL_IMAGES = "tHotelImages";
    public static final String TABLE_IMAGES = "tImages";
    public static final String TABLE_LANGS = "tLangs";
    public static final String TABLE_LOOKOUTS = "tLookouts";
    public static final String TABLE_LOOKOUTIMAGES = "tLookoutImages";
    public static final String TABLE_MONUMENTS = "tMonuments";
    public static final String TABLE_MONUMENTIMAGES = "tMonumentImages";
    public static final String TABLE_NEWS = "tNews";
    public static final String TABLE_NEWSIMAGES = "tNewsImages";
    public static final String TABLE_ODDITIES = "tOddities";
    public static final String TABLE_ODDITYIMAGES = "tOddityImages";
    public static final String TABLE_ODDITYTYPES = "tOddityTypes";
    public static final String TABLE_PRODUCERS = "tProducers";
    public static final String TABLE_PRODUCERIMAGES = "tProducerImages";
    public static final String TABLE_RESTAURANTS = "tRestaurants";
    public static final String TABLE_RESTAURANTIMAGES = "tRestaurantImages";
    public static final String TABLE_STRAINS = "tStrains";
    public static final String TABLE_WINES = "tWines";
    public static final String TABLE_WINEIMAGES = "tWineImages";
    public static final String TABLE_WINESTRAINS = "tWineStrains";

    // ------------------------ Table create statements ----------------//  

    private static final String CREATE_TABLE_ADDRESSES = "CREATE TABLE " + "tAddresses" + "("
            + "IdAddress INTEGER PRIMARY KEY," + "StreetName TEXT," + "StreetNumber TEXT,"
            + "HouseNumber TEXT," + "City TEXT," + "Postcode TEXT," + "Latitude FLOAT,"
            + "Longitude FLOAT," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCERS = "CREATE TABLE " + "tProducers" + "("
            + "IdProducer INTEGER PRIMARY KEY," + "Email TEXT," + "Link TEXT," + "Name TEXT,"
            + "Phone TEXT," + "IdDescription_ INTEGER," + "IdAddress_ INTEGER,"
            + "IdUser_ INTEGER," + "LastUpdate TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ADDRESSES);
        /*
        db.execSQL(CREATE_TABLE_COLORS);
        db.execSQL(CREATE_TABLE_CURRENCIES);
        db.execSQL(CREATE_TABLE_DESCRIPTIONS);
        db.execSQL(CREATE_TABLE_FLAVOURS);
        db.execSQL(CREATE_TABLE_GRADES);
        db.execSQL(CREATE_TABLE_HOTELS);
        db.execSQL(CREATE_TABLE_HOTEL_IMAGES);
        db.execSQL(CREATE_TABLE_IMAGES);
        db.execSQL(CREATE_TABLE_LANGS);
        db.execSQL(CREATE_TABLE_LOOKOUTS);
        db.execSQL(CREATE_TABLE_LOOKOUTIMAGES);
        db.execSQL(CREATE_TABLE_MONUMENTS);
        db.execSQL(CREATE_TABLE_MONUMENTIMAGES);
        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_NEWSIMAGES);
        db.execSQL(CREATE_TABLE_ODDITIES);
        db.execSQL(CREATE_TABLE_ODDITYIMAGES);
        db.execSQL(CREATE_TABLE_ODDITYTYPES);
        */
        db.execSQL(CREATE_TABLE_PRODUCERS);
        /*
        db.execSQL(CREATE_TABLE_PRODUCERIMAGES);
        db.execSQL(CREATE_TABLE_RESTAURANTS);
        db.execSQL(CREATE_TABLE_RESTAURANTIMAGES);
        db.execSQL(CREATE_TABLE_STRAINS);
        db.execSQL(CREATE_TABLE_WINES);
        db.execSQL(CREATE_TABLE_WINEIMAGES);
        db.execSQL(CREATE_TABLE_WINESTRAINS);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLAVOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTEL_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKOUTIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ODDITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ODDITYIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ODDITYTYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCERIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRAINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINEIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINESTRAINS);
        // create new tables
        onCreate(db);
    }

}
