
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.jsonresponses.WineDetails;
import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.models.Wine;
import pl.tokajiwines.utils.Log;

public class WinesDataSource {
    // LogCat tag
    private static final String LOG = "WinesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;

    private String[] allColumns = {
            "IdWine", "Name", "ProdDate", "Price", "Volume", "AvailablePL", "LastUpdate",
            "IdColor_", "IdFlavour_", "IdProducer_", "IdDescription_", "IdGrade_", "IdImageCover_"
    };

    public WinesDataSource(Context context) {
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

    public WineListItem[] getProducerWines(int idProducer) {
        Log.i(LOG, "getProducerWines");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, "IdProducer_ = ?",
                new String[] {
                    idProducer + ""
                }, null, null, "Name");
        WineListItem[] wines = null;

        if (cursor == null && cursor.getCount() == 0)
            Log.w(LOG, "Wines for producer with id= " + idProducer + " don't exist");
        else {
            cursor.moveToFirst();
            wines = new WineListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor);
                cursor.moveToNext();
                i++;
            }
        }

        cursor.close();
        if (wines == null) Log.w(LOG, "Producer wines are empty()");
        return wines;
    }

    public WineListItem[] getWineItems(String s) {
        WineListItem[] wine = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, "'Name'" + "LIKE ?",
                new String[] {
                    "%" + s + "%"
                }, null, null, "Name");
        if (cursor.getCount() == 0)
            Log.w(LOG, "String \"" + s + "\" doesn't exists");
        else {
            wine = new WineListItem[cursor.getCount()];
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                WineListItem si = cursorToWineListItem(cursor);
                wine[i] = si;
                i++;
                cursor.moveToNext();
            }
        }
        return wine;
    }

    public void test() {
        Log.i(LOG, "getProducerWines");
        String flavours = "(1,2,4)";
        String producers = "(1,2,3)";
        String strains = "(1,3,4)";
        String sql = "select IdWine From (tWines LEFT JOIN tWineStrains ON tWines.IdWine = tWineStrains.IdWine_) as t1 where (IdFlavour_ IN "
                + flavours
                + "AND IdProducer_ IN "
                + producers
                + "AND IdWineStrain IN "
                + strains
                + ")";
        Cursor cursor = database.rawQuery(sql, null);
        WineListItem[] wines = null;

        if (cursor == null && cursor.getCount() == 0)
            Log.e(LOG, "EMPTY");
        else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e(LOG, cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public WineListItem[] getFilterWines(String flavours, String grades, String strains,
            String producers, String years, String prices) {
        String sql = "select * From (tWines LEFT JOIN tWineStrains ON tWines.IdWine = tWineStrains.IdWine_) as t1";
        String query = "";
        if (flavours.length() > 2) {
            if (query == "") query += " WHERE (";
            query += ("IdFlavour_ IN" + fixString(flavours));
        }
        if (grades.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdGrade_ IN" + fixString(grades));
        }
        if (strains.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdWineStrain IN" + fixString(strains));
        }
        if (producers.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdProducer_ IN " + fixString(producers));
        }
        if (years.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("ProdDate IN" + fixYear(fixString(years)));
        }
        if (prices.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            String[] price = getPrice(fixString(prices));
            query += ("Price >" + price[0] + " AND Price <" + price[1]);
        }
        if (query != "") query += ")";
        sql += query;
        sql += " GROUP BY IdWine ORDER BY Name";
        Cursor cursor = database.rawQuery(sql, null);
        WineListItem[] wines = null;

        if (cursor == null)
            Log.w(LOG, "Filter EMPTY");
        else {
            cursor.moveToFirst();
            wines = new WineListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor);
                cursor.moveToNext();
                i++;
            }
        }

        cursor.close();
        return wines;
    }

    public WineListItem getWineDetails(int idWine) {
        Log.i(LOG, "getWineDetails");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, "IdProducer_ = ?",
                new String[] {
                    idWine + ""
                }, null, null, null);
        WineListItem wine = null;
        if (cursor == null)
            Log.w(LOG, "Details for wine with id= " + idWine + " don't exist");
        else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                wine = cursorToWineListItem(cursor);
            }
        }
        cursor.close();
        return wine;
    }

    private WineListItem cursorToWineListItem(Cursor cursor) {
        Wine wine = new Wine();
        ColorsDataSource cDs = new ColorsDataSource(mContext);
        FlavoursDataSource fDs = new FlavoursDataSource(mContext);
        ProducersDataSource pDs = new ProducersDataSource(mContext);
        GradesDataSource gDs = new GradesDataSource(mContext);
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
        wine.mIdWine = cursor.getInt(0);
        wine.mName = cursor.getString(1);
        wine.mProdDate = cursor.getInt(2);
        if (cursor.isNull(3) == false) {
            wine.mPrice = cursor.getDouble(3);
        }
        wine.mVolume = cursor.getInt(4);
        wine.mAvailablePL = cursor.getInt(5);
        wine.mLastUpdate = cursor.getString(6);
        wine.mIdColor_ = cursor.getInt(7);
        if (cursor.isNull(8) == false) {
            wine.mIdFlavour_ = cursor.getInt(8);
            fDs.open();
            wine.flavour = fDs.getFlavour(wine.mIdFlavour_);
            fDs.close();
        }
        wine.mIdProducer_ = cursor.getInt(9);
        wine.mIdDescription_ = cursor.getInt(10);
        if (cursor.isNull(11) == false) {
            wine.mIdGrade_ = cursor.getInt(11);
            gDs.open();
            wine.grade = gDs.getGrade(wine.mIdGrade_);
            gDs.close();
        }
        wine.mIdImageCover_ = cursor.getInt(12);
        cDs.open();
        wine.color = cDs.getColor(wine.mIdColor_);
        cDs.close();
        pDs.open();
        wine.producer = pDs.getProducer(wine.mIdProducer_);
        pDs.close();
        iDs.open();
        wine.imageCover = iDs.getImageUrl(wine.mIdImageCover_);
        iDs.close();
        wsDs.open();
        wine.strains = wsDs.getWineStrains(wine.mIdWine);
        wsDs.close();

        return new WineListItem(wine);
    }

    public Wine getProducerWineBest(int id) {
        Log.i(LOG, "getWine(id=" + id + ")");
        Wine w = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, new String[] {
                "Name", "IdImageCover_"
        }, "IdWine" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Wine with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            w = cursorToProducerBestWine(cursor);
        }
        return w;
    }

    public WineDetails getProducerWineDetailsFill(int id) {
        Log.i(LOG, "getWine(id=" + id + ")");
        WineDetails w = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, new String[] {
                "IdWine", "IdDescription_"
        }, "IdWine" + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Wine with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            w = cursorToWineDetailsFill(cursor);
        }
        return w;
    }

    public String fixString(String s) {
        s = s.replace("[", "(");
        s = s.replace("]", ")");
        return s;
    }

    public String fixYear(String s) {
        s = s.replace("\"", "");
        return s;
    }

    public String[] getPrice(String s) {
        s = s.replace("\"", " ");
        s = s.replace("u003", " ");
        s = s.replace("(", " ");
        s = s.replace(")", " ");
        String max = "";
        String min = "";

        if (s.contains("Price \u003e 4000")) {
            min = 4000 + "";
            max = 8000 + "";
        }
        if (s.contains("\u003e 2000")) {
            min = 2000 + "";
            if (max == "") max = 4000 + "";
        }
        if (s.contains("\u003c\u003d 2000")) {
            min = 0 + "";
            if (max == "") max = 2000 + "";
        }
        if (s.contains("\u003e 8000")) {
            if (min == "") min = 8000 + "";
            max = 2000000 + "";
        }
        String[] sa = {
                min, max
        };
        return sa;
    }

    public long insertWine(Wine wine) {
        Log.i(LOG, "insertWine()");
        ContentValues values = new ContentValues();
        values.put("IdWine", wine.mIdWine);
        values.put("Name", wine.mName);
        values.put("ProdDate", wine.mProdDate);
        values.put("Price", wine.mPrice);
        values.put("Volume", wine.mVolume);
        values.put("AvailablePL", wine.mAvailablePL);
        values.put("LastUpdate", wine.mLastUpdate);
        values.put("IdColor_", wine.mIdColor_);
        values.put("IdFlavour_", wine.mIdFlavour_);
        values.put("IdProducer_", wine.mIdProducer_);
        values.put("IdDescription_", wine.mIdDescription_);
        values.put("IdGrade_", wine.mIdGrade_);
        values.put("IdImageCover_", wine.mIdImageCover_);
        long insertId = database.insert(DatabaseHelper.TABLE_WINES, null, values);
        Log.i(LOG, "Wine with id: " + wine.mIdWine + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteWine(Wine wine) {
        long id = wine.mIdWine;
        database.delete(DatabaseHelper.TABLE_WINES, "IdWine" + " = " + id, null);
        Log.i(LOG, "Deleted wine with id: " + id);
    }

    public void updateWine(Wine wineOld, Wine wineNew) {
        ContentValues values = new ContentValues();
        values.put("IdWine", wineOld.mIdWine);
        values.put("Name", wineNew.mName);
        values.put("ProdDate", wineNew.mProdDate);
        values.put("Price", wineNew.mPrice);
        values.put("Volume", wineNew.mVolume);
        values.put("AvailablePL", wineNew.mAvailablePL);
        values.put("LastUpdate", wineNew.mLastUpdate);
        values.put("IdColor_", wineNew.mIdColor_);
        values.put("IdFlavour_", wineNew.mIdFlavour_);
        values.put("IdProducer_", wineNew.mIdProducer_);
        values.put("IdDescription_", wineNew.mIdDescription_);
        values.put("IdGrade_", wineNew.mIdGrade_);
        values.put("IdImageCover_", wineNew.mIdImageCover_);
        int rows = database.update(DatabaseHelper.TABLE_WINES, values, "'" + wineOld.mIdWine
                + "' = '" + wineNew.mIdWine + "'", null);
        Log.i(LOG, "Updated wine with id: " + wineOld.mIdWine + " on: " + rows + " row(s)");
    }

    public WineListItem[] getAllWines() {
        Log.i(LOG, "getProducerWines");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, null, null, null,
                null, "Name");
        WineListItem[] wines = null;

        if (cursor == null && cursor.getCount() == 0)
            Log.w(LOG, "Wines  don't exist");
        else {
            cursor.moveToFirst();
            wines = new WineListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor);
                cursor.moveToNext();
                i++;
            }
        }

        cursor.close();
        return wines;
    }

    public String[] getProdDates() {
        Log.i(LOG, "getProdDates()");
        Cursor cursor = database.query(true, DatabaseHelper.TABLE_WINES, new String[] {
            "ProdDate"
        }, null, null, "ProdDate", null, "ProdDate", null);
        String[] dates = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            dates = new String[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                dates[i] = cursor.getString(0);
                cursor.moveToNext();
                i++;
            }
        } else
            Log.w(LOG, "Grades are empty()");
        cursor.close();
        return dates;
    }

    private Wine cursorToWine(Cursor cursor) {
        Wine wine = new Wine();

        wine.mIdWine = cursor.getInt(0);
        wine.mName = cursor.getString(1);
        wine.mProdDate = cursor.getInt(2);
        wine.mPrice = cursor.getDouble(3);
        wine.mVolume = cursor.getInt(4);
        wine.mAvailablePL = cursor.getInt(5);
        wine.mLastUpdate = cursor.getString(6);
        wine.mIdColor_ = cursor.getInt(7);
        wine.mIdFlavour_ = cursor.getInt(8);
        wine.mIdProducer_ = cursor.getInt(9);
        wine.mIdDescription_ = cursor.getInt(10);
        wine.mIdGrade_ = cursor.getInt(11);
        wine.mIdImageCover_ = cursor.getInt(12);
        return wine;
    }

    @SerializedName("wineImage")
    public String mWineImageUrl;
    @SerializedName("wineName")
    public String mWineName;

    private Wine cursorToProducerBestWine(Cursor cursor) {
        Wine w = new Wine();
        w.mName = cursor.getString(0);
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        iDs.open();
        w.imageCover = iDs.getImageUrl(cursor.getInt(1));
        iDs.close();
        return w;
    }

    private WineDetails cursorToWineDetailsFill(Cursor cursor) {
        Wine w = new Wine();
        DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
        dDs.open();
        w.description = dDs.getDescriptionVast(cursor.getInt(1));
        dDs.close();
        WineImagesDataSource wDs = new WineImagesDataSource(mContext);
        wDs.open();
        w.big = wDs.getWineImage(cursor.getInt(0));
        wDs.close();
        return new WineDetails(w);
    }

}
