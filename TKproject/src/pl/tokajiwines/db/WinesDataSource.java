
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

import java.util.regex.Pattern;

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
            ColorsDataSource cDs = new ColorsDataSource(mContext);
            FlavoursDataSource fDs = new FlavoursDataSource(mContext);
            ProducersDataSource pDs = new ProducersDataSource(mContext);
            GradesDataSource gDs = new GradesDataSource(mContext);
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
            cDs.open();
            fDs.open();
            pDs.open();
            gDs.open();
            iDs.open();
            wsDs.open();
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor, cDs, fDs, pDs, gDs, iDs, wsDs);
                cursor.moveToNext();
                i++;
            }
            cDs.close();
            fDs.close();
            pDs.close();
            gDs.close();
            iDs.close();
            wsDs.close();
        }

        cursor.close();
        if (wines == null) Log.w(LOG, "Producer wines are empty()");
        return wines;
    }

    public WineListItem[] getWineItems(String s) {
        WineListItem[] wine = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, "Name LIKE ?",
                new String[] {
                    "%" + s + "%"
                }, null, null, "Name");
        System.out.println(cursor.getCount());
        if (cursor.getCount() == 0)
            Log.w(LOG, "Wine " + s + "\" doesn't exists");
        else {
            wine = new WineListItem[cursor.getCount()];
            cursor.moveToFirst();
            int i = 0;
            ColorsDataSource cDs = new ColorsDataSource(mContext);
            FlavoursDataSource fDs = new FlavoursDataSource(mContext);
            ProducersDataSource pDs = new ProducersDataSource(mContext);
            GradesDataSource gDs = new GradesDataSource(mContext);
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
            cDs.open();
            fDs.open();
            pDs.open();
            gDs.open();
            iDs.open();
            wsDs.open();
            while (!cursor.isAfterLast()) {
                WineListItem si = cursorToWineListItem(cursor, cDs, fDs, pDs, gDs, iDs, wsDs);
                wine[i] = si;
                i++;
                cursor.moveToNext();
            }
            cDs.close();
            fDs.close();
            pDs.close();
            gDs.close();
            iDs.close();
            wsDs.close();
        }
        return wine;
    }

    public WineListItem[] getFilterWines(String flavours, String grades, String strains,
            String producers, String years, String prices) {
        String sql = "select * From (tWines LEFT JOIN tWineStrains ON tWines.IdWine = tWineStrains.IdWine_) as t1";
        String query = "";
        if (flavours != null && flavours.length() > 2) {
            if (query == "") query += " WHERE (";
            query += ("IdFlavour_ IN" + fixString(flavours));
        }
        if (grades != null && grades.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdGrade_ IN" + fixString(grades));
        }
        if (strains != null && strains.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdWineStrain IN" + fixString(strains));
        }
        if (producers != null && producers.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("IdProducer_ IN " + fixString(producers));
        }
        if (years != null && years.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += ("ProdDate IN" + fixYear(fixString(years)));
        }
        if (prices != null && prices.length() > 2) {
            if (query == "")
                query += " WHERE (";
            else
                query += ") AND (";
            query += getPrice(prices);
        }
        if (query != "") query += ")";
        sql += query;
        sql += " GROUP BY IdWine ORDER BY Name";
        Cursor cursor = database.rawQuery(sql, null);
        WineListItem[] wines = null;

        if (cursor == null)
            Log.w(LOG, "Filter EMPTY");
        else {
            ColorsDataSource cDs = new ColorsDataSource(mContext);
            FlavoursDataSource fDs = new FlavoursDataSource(mContext);
            ProducersDataSource pDs = new ProducersDataSource(mContext);
            GradesDataSource gDs = new GradesDataSource(mContext);
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
            cDs.open();
            fDs.open();
            pDs.open();
            gDs.open();
            iDs.open();
            wsDs.open();
            cursor.moveToFirst();
            wines = new WineListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor, cDs, fDs, pDs, gDs, iDs, wsDs);
                cursor.moveToNext();
                i++;
            }
            cDs.close();
            fDs.close();
            pDs.close();
            gDs.close();
            iDs.close();
            wsDs.close();
        }

        cursor.close();
        return wines;
    }

    public WineListItem getWineDetails(int idWine) {
        Log.i(LOG, "getWineDetails");
        Cursor cursor = database.query(DatabaseHelper.TABLE_WINES, allColumns, "IdWine = ?",
                new String[] {
                    idWine + ""
                }, null, null, null);
        WineListItem wine = null;
        if (cursor == null)
            Log.w(LOG, "Details for wine with id= " + idWine + " don't exist");
        else {
            ColorsDataSource cDs = new ColorsDataSource(mContext);
            FlavoursDataSource fDs = new FlavoursDataSource(mContext);
            ProducersDataSource pDs = new ProducersDataSource(mContext);
            GradesDataSource gDs = new GradesDataSource(mContext);
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
            cDs.open();
            fDs.open();
            pDs.open();
            gDs.open();
            iDs.open();
            wsDs.open();
            cursor.moveToFirst();
            wine = cursorToWineListItem(cursor, cDs, fDs, pDs, gDs, iDs, wsDs);
            cDs.close();
            fDs.close();
            pDs.close();
            gDs.close();
            iDs.close();
            wsDs.close();
            cursor.close();
        }

        System.out.println("Done getting details");
        return wine;
    }

    private WineListItem cursorToWineListItem(Cursor cursor, ColorsDataSource cDs,
            FlavoursDataSource fDs, ProducersDataSource pDs, GradesDataSource gDs,
            ImagesDataSource iDs, WineStrainsDataSource wsDs) {
        Wine wine = new Wine();
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
            wine.flavour = fDs.getFlavour(wine.mIdFlavour_);
        }
        wine.mIdProducer_ = cursor.getInt(9);
        wine.mIdDescription_ = cursor.getInt(10);
        if (cursor.isNull(11) == false) {
            wine.mIdGrade_ = cursor.getInt(11);
            wine.grade = gDs.getGrade(wine.mIdGrade_);
        }
        wine.mIdImageCover_ = cursor.getInt(12);
        wine.color = cDs.getColor(wine.mIdColor_);
        wine.producer = pDs.getProducer(wine.mIdProducer_);
        wine.imageCover = iDs.getImageUrl(wine.mIdImageCover_);
        wine.strains = wsDs.getWineStrains(wine.mIdWine);

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
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            iDs.open();
            w = cursorToProducerBestWine(cursor, iDs);
            iDs.close();
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
            DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
            WineImagesDataSource wDs = new WineImagesDataSource(mContext);
            dDs.open();
            wDs.open();
            cursor.moveToFirst();
            w = cursorToWineDetailsFill(cursor, dDs, wDs);
            dDs.close();
            wDs.close();
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

    public String getPrice(String s) {
        String cena = "(";

        if (Pattern.compile(Pattern.quote("u003e 4000"), Pattern.CASE_INSENSITIVE).matcher(s)
                .find()) {
            cena += "(Price > 4000 AND Price < 8000)";
        }
        if (Pattern.compile(Pattern.quote("u003e 2000"), Pattern.CASE_INSENSITIVE).matcher(s)
                .find()) {
            if (cena != "(") cena += " OR ";
            cena += " (Price > 2000 AND Price < 4000)";
        }
        if (Pattern.compile(Pattern.quote("u003d 2000"), Pattern.CASE_INSENSITIVE).matcher(s)
                .find()) {
            if (cena != "(") cena += " OR ";
            cena += " (Price > 0 AND Price < 2000)";
        }
        if (Pattern.compile(Pattern.quote("u003e 8000"), Pattern.CASE_INSENSITIVE).matcher(s)
                .find()) {
            if (cena != "(") cena += " OR ";
            cena += "  (Price > 8000)";
        }
        cena += ")";
        return cena;
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
            ColorsDataSource cDs = new ColorsDataSource(mContext);
            FlavoursDataSource fDs = new FlavoursDataSource(mContext);
            ProducersDataSource pDs = new ProducersDataSource(mContext);
            GradesDataSource gDs = new GradesDataSource(mContext);
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            WineStrainsDataSource wsDs = new WineStrainsDataSource(mContext);
            cDs.open();
            fDs.open();
            pDs.open();
            gDs.open();
            iDs.open();
            wsDs.open();
            cursor.moveToFirst();
            wines = new WineListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                wines[i] = cursorToWineListItem(cursor, cDs, fDs, pDs, gDs, iDs, wsDs);
                cursor.moveToNext();
                i++;
            }
            cDs.close();
            fDs.close();
            pDs.close();
            gDs.close();
            iDs.close();
            wsDs.close();
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

    private Wine cursorToProducerBestWine(Cursor cursor, ImagesDataSource iDs) {
        Wine w = new Wine();
        w.mName = cursor.getString(0);
        w.imageCover = iDs.getImageUrl(cursor.getInt(1));
        return w;
    }

    private WineDetails cursorToWineDetailsFill(Cursor cursor, DescriptionsDataSource dDs,
            WineImagesDataSource wDs) {
        Wine w = new Wine();
        w.description = dDs.getDescriptionVast(cursor.getInt(1));
        w.big = wDs.getWineImage(cursor.getInt(0));
        return new WineDetails(w);
    }

}
