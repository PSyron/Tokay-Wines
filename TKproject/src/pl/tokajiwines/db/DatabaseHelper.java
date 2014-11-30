
package pl.tokajiwines.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import pl.tokajiwines.utils.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ------------------------ Database settings ----------------//
    private static final String DATABASE_NAME = "tokaji_release.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "/data/data/pl.tokajiwines/databases/";
    private static final String LOG = "DatabaseHelper"; // LogCat tag

    // ------------------------ Table names ----------------//
    public static final String TABLE_ADDRESSES = "tAddresses";
    public static final String TABLE_COLORS = "tColors";
    public static final String TABLE_CURIOSITIES = "tCuriosities";
    public static final String TABLE_CURIOSITY_IMAGES = "tCuriosityImages";
    public static final String TABLE_CURIOSITY_TYPES = "tCuriosityTypes";
    public static final String TABLE_CURRENCIES = "tCurrencies";
    public static final String TABLE_DESCRIPTIONS = "tDescriptions";
    public static final String TABLE_FLAVOURS = "tFlavours";
    public static final String TABLE_GRADES = "tGrades";
    public static final String TABLE_HOTELS = "tHotels";
    public static final String TABLE_HOTEL_IMAGES = "tHotelImages";
    public static final String TABLE_IMAGES = "tImages";
    public static final String TABLE_LANGS = "tLangs";
    public static final String TABLE_NEWS = "tNews";
    public static final String TABLE_NEWS_IMAGES = "tNewsImages";
    public static final String TABLE_PLACES = "tPlaces";
    public static final String TABLE_PRODUCERS = "tProducers";
    public static final String TABLE_PRODUCER_IMAGES = "tProducerImages";
    public static final String TABLE_RESTAURANTS = "tRestaurants";
    public static final String TABLE_RESTAURANT_IMAGES = "tRestaurantImages";
    public static final String TABLE_SEARCH = "tSearch";
    public static final String TABLE_STRAINS = "tStrains";
    public static final String TABLE_WINES = "tWines";
    public static final String TABLE_WINE_IMAGES = "tWineImages";
    public static final String TABLE_WINE_STRAINS = "tWineStrains";

    // ------------------------ Table create statements ----------------//
    private static final String CREATE_TABLE_ADDRESSES = "CREATE TABLE " + "tAddresses" + "("
            + "IdAddress INTEGER PRIMARY KEY," + "StreetName TEXT," + "StreetNumber TEXT,"
            + "HouseNumber TEXT," + "City TEXT," + "Postcode TEXT," + "Latitude FLOAT,"
            + "Longitude FLOAT," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_COLORS = "CREATE TABLE " + "tColors" + "("
            + "IdColor INTEGER PRIMARY KEY," + "NameEng TEXT," + "NamePl TEXT" + ")";

    private static final String CREATE_TABLE_CURIOSITIES = "CREATE TABLE " + "tCuriosities" + "("
            + "IdCuriosity INTEGER PRIMARY KEY," + "Name TEXT," + "IdCuriosityType_ INTEGER,"
            + "IdDescription_ INTEGER," + "IdAddress_ INTEGER," + "IdImageCover_ INTEGER,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_CURIOSITY_IMAGES = "CREATE TABLE "
            + "tCuriosityImages" + "(" + "IdCuriosityImage INTEGER PRIMARY KEY,"
            + "IdCuriosity_ INTEGER," + "IdImage_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_CURIOSITY_TYPES = "CREATE TABLE " + "tCuriosityTypes"
            + "(" + "IdCuriosityType INTEGER PRIMARY KEY," + "Name TEXT" + ")";

    private static final String CREATE_TABLE_CURRENCIES = "CREATE TABLE " + "tCurrencies" + "("
            + "IdCurrency INTEGER PRIMARY KEY," + "Name TEXT," + "NameShort TEXT," + "Ratio FLOAT,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_DESCRIPTIONS = "CREATE TABLE " + "tDescriptions" + "("
            + "IdDescription INTEGER NOT NULL," + "IdLang_ INTEGER NOT NULL,"
            + "Version INTEGER NOT NULL," + "Short TEXT," + "Vast TEXT," + "IdUser_ INTEGER,"
            + "LastUpdate TEXT," + "PRIMARY KEY(IdDescription,IdLang_,Version)" + ")";

    private static final String CREATE_TABLE_FLAVOURS = "CREATE TABLE " + "tFlavours" + "("
            + "IdFlavour INTEGER PRIMARY KEY," + "NameEng TEXT," + "NamePl TEXT" + ")";

    private static final String CREATE_TABLE_GRADES = "CREATE TABLE " + "tGrades" + "("
            + "IdGrade INTEGER PRIMARY KEY," + "Name TEXT" + ")";

    private static final String CREATE_TABLE_HOTELS = "CREATE TABLE " + "tHotels" + "("
            + "IdHotel INTEGER PRIMARY KEY," + "Email TEXT," + "Link TEXT," + "Name TEXT,"
            + "Phone TEXT," + "IdDescription_ INTEGER," + "IdAddress_ INTEGER,"
            + "IdUser_ INTEGER," + "IdImageCover_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_HOTEL_IMAGES = "CREATE TABLE " + "tHotelImages" + "("
            + "IdHotelImage INTEGER PRIMARY KEY," + "IdHotel_ INTEGER," + "IdImage_ INTEGER,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_IMAGES = "CREATE TABLE " + "tImages" + "("
            + "IdImage INTEGER PRIMARY KEY," + "Version INTEGER," + "Width INTEGER,"
            + "Height INTEGER," + "Weight FLOAT," + "Author TEXT," + "Image TEXT,"
            + "IdUser_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_LANGS = "CREATE TABLE " + "tLangs" + "("
            + "IdLang INTEGER PRIMARY KEY," + "Name TEXT," + "ShortName TEXT" + ")";

    private static final String CREATE_TABLE_NEWS = "CREATE TABLE " + "tNews" + "("
            + "IdNews INTEGER PRIMARY KEY," + "HeaderPl TEXT," + "HeaderEng TEXT,"
            + "EntryDate TEXT," + "StartDate TEXT," + "EndDate TEXT," + "IdDescription_ INTEGER,"
            + "IdAddress_ INTEGER," + "IdImageCover_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_NEWS_IMAGES = "CREATE TABLE " + "tNewsImages" + "("
            + "IdNewsImage INTEGER PRIMARY KEY," + "IdNews_ INTEGER," + "IdImage_ INTEGER,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_PLACES = "CREATE TABLE " + "tPlaces" + "("
            + "Id INTEGER PRIMARY KEY," + "IdPlace INTEGER," + "Name TEXT," + "Address TEXT,"
            + "Latitude TEXT," + "Longitude TEXT," + "PlaceType TEXT," + "Phone TEXT,"
            + "Image TEXT," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCERS = "CREATE TABLE " + "tProducers" + "("
            + "IdProducer INTEGER PRIMARY KEY," + "Email TEXT," + "Link TEXT," + "Name TEXT,"
            + "Phone TEXT," + "IdDescription_ INTEGER," + "IdAddress_ INTEGER,"
            + "IdUser_ INTEGER," + "IdImageCover_ INTEGER," + "IdWineBest_ INTEGER,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCER_IMAGES = "CREATE TABLE " + "tProducerImages"
            + "(" + "IdProducerImage INTEGER PRIMARY KEY," + "IdProducer_ INTEGER,"
            + "IdImage_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_RESTAURANTS = "CREATE TABLE " + "tRestaurants" + "("
            + "IdRestaurant INTEGER PRIMARY KEY," + "Email TEXT," + "Link TEXT," + "Name TEXT,"
            + "Phone TEXT," + "IdDescription_ INTEGER," + "IdAddress_ INTEGER,"
            + "IdUser_ INTEGER," + "IdImageCover_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_RESTAURANT_IMAGES = "CREATE TABLE "
            + "tRestaurantImages" + "(" + "IdRestaurantImage INTEGER PRIMARY KEY,"
            + "IdRestaurant_ INTEGER," + "IdImage_ INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_SEARCH = "CREATE TABLE " + "tSearch" + "("
            + "Id INTEGER PRIMARY KEY," + "IdSearch INTEGER," + "Name TEXT,"
            + "SearchType INTEGER," + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_STRAINS = "CREATE TABLE " + "tStrains" + "("
            + "IdStrain INTEGER PRIMARY KEY," + "Name TEXT" + ")";

    private static final String CREATE_TABLE_WINES = "CREATE TABLE " + "tWines" + "("
            + "IdWine INTEGER PRIMARY KEY," + "Name TEXT," + "ProdDate INTEGER," + "Price FLOAT,"
            + "Volume INTEGER," + "AvailablePL INTEGER," + "LastUpdate TEXT," + "IdColor_ INTEGER,"
            + "IdFlavour_ INTEGER," + "IdProducer_ INTEGER," + "IdDescription_ INTEGER,"
            + "IdGrade_ INTEGER," + "IdImageCover_ INTEGER" + ")";

    private static final String CREATE_TABLE_WINE_IMAGES = "CREATE TABLE " + "tWineImages" + "("
            + "IdWineImage INTEGER PRIMARY KEY," + "IdWine_ INTEGER," + "IdImage_ INTEGER,"
            + "LastUpdate TEXT" + ")";

    private static final String CREATE_TABLE_WINE_STRAINS = "CREATE TABLE " + "tWineStrains" + "("
            + "IdWineStrain INTEGER PRIMARY KEY," + "Content INTEGER," + "IdStrain_ INTEGER ,"
            + "IdWine_ INTEGER" + ")";
    Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.w(LOG, "Creating tables");
        db.execSQL(CREATE_TABLE_ADDRESSES);
        db.execSQL(CREATE_TABLE_COLORS);
        db.execSQL(CREATE_TABLE_CURIOSITIES);
        db.execSQL(CREATE_TABLE_CURIOSITY_IMAGES);
        db.execSQL(CREATE_TABLE_CURIOSITY_TYPES);
        db.execSQL(CREATE_TABLE_CURRENCIES);
        db.execSQL(CREATE_TABLE_DESCRIPTIONS);
        db.execSQL(CREATE_TABLE_FLAVOURS);
        db.execSQL(CREATE_TABLE_GRADES);
        db.execSQL(CREATE_TABLE_HOTELS);
        db.execSQL(CREATE_TABLE_HOTEL_IMAGES);
        db.execSQL(CREATE_TABLE_IMAGES);
        db.execSQL(CREATE_TABLE_LANGS);
        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_NEWS_IMAGES);
        db.execSQL(CREATE_TABLE_PRODUCERS);
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_PRODUCER_IMAGES);
        db.execSQL(CREATE_TABLE_RESTAURANTS);
        db.execSQL(CREATE_TABLE_RESTAURANT_IMAGES);
        db.execSQL(CREATE_TABLE_SEARCH);
        db.execSQL(CREATE_TABLE_STRAINS);
        db.execSQL(CREATE_TABLE_WINES);
        db.execSQL(CREATE_TABLE_WINE_IMAGES);
        db.execSQL(CREATE_TABLE_WINE_STRAINS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURIOSITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURIOSITY_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURIOSITY_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLAVOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTEL_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCER_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRAINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINE_STRAINS);
        // create new tables
        onCreate(db);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(LOG, "Error copying databases");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(LOG, "Database doesn't exists");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        Log.i(LOG, "Copying database from assets");
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /* HOW TO CHECK DATABASE
    adb -d shell
    run-as pl.tokajiwines
    cat databases/tokaji_release.db > /sdcard/tokaji_release.db  */
}
