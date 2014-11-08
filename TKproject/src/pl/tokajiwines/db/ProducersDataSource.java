
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Producer;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class ProducersDataSource {
    // LogCat tag
    private static final String LOG = "ProducersDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdProducer", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_",
            "IdUser_", "IdImageCover_", "IdWineBest_", "LastUpdate"
    };

    public ProducersDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) {
            dbHelper.close();
            Log.i(LOG, "database closed");
        }
    }

    public long insertProducer(Producer producer) {
        ContentValues values = new ContentValues();
        values.put("IdProducer", producer.mIdProducer);
        values.put("Email", producer.mEmail);
        values.put("Link", producer.mLink);
        values.put("Name", producer.mName);
        values.put("Phone", producer.mPhone);
        values.put("IdDescription_", producer.mIdDescription_);
        values.put("IdAddress_", producer.mIdAddress_);
        values.put("IdUser_", producer.mIdUser_);
        values.put("IdImageCover_", producer.mIdImageCover_);
        values.put("IdWineBest_", producer.mIdWineBest_);
        values.put("LastUpdate", producer.mLastUpdate);
        long insertId = database.insert(DatabaseHelper.TABLE_PRODUCERS, null, values);
        Log.i(LOG, "Producer with id: " + producer.mIdProducer + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteProducer(Producer producer) {
        long id = producer.mIdProducer;
        database.delete(DatabaseHelper.TABLE_PRODUCERS, "IdProducer" + " = " + id, null);
        Log.i(LOG, "Deleted producer with id: " + id);
    }

    public void updateProducer(Producer producerOld, Producer producerNew) {
        ContentValues values = new ContentValues();
        values.put("IdProducer", producerNew.mIdProducer);
        values.put("Email", producerNew.mEmail);
        values.put("Link", producerNew.mLink);
        values.put("Name", producerNew.mName);
        values.put("Phone", producerNew.mPhone);
        values.put("IdDescription_", producerNew.mIdDescription_);
        values.put("IdAddress_", producerNew.mIdAddress_);
        values.put("IdUser_", producerNew.mIdUser_);
        values.put("IdImageCover_", producerNew.mIdImageCover_);
        values.put("IdWineBest_", producerNew.mIdWineBest_);
        values.put("LastUpdate", producerNew.mLastUpdate);
        int rows = database.update(DatabaseHelper.TABLE_PRODUCERS, values, "'"
                + producerOld.mIdProducer + "' = '" + producerNew.mIdProducer + "'", null);
        Log.i(LOG, "Updated producer with id: " + producerOld.mIdProducer + " on: " + rows
                + " row(s)");
    }

    public List<Producer> getAllProducers() {
        Log.i(LOG, "getAllProducers()");
        List<Producer> producers = new ArrayList<Producer>();
        /*
        insertProducer(new Producer(1, "Email", "www.test.pl", "Testowy", "666666666", 1, 1, 1, 1,
                1, "Dawno"));
        insertProducer(new Producer(2, "Email", "www.test.pl", "Testowy", "666666666", 1, 1, 1, 1,
                1, "Dawno"));
        insertProducer(new Producer(3, "Emailcos", "www.xxx.pl", "cos", "666666666", 4, 2, 1, 3, 5,
                "omg"));*/
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Producer producer = cursorToProducer(cursor);
            producers.add(producer);
            cursor.moveToNext();
        }
        cursor.close();
        if (producers.isEmpty()) Log.w(LOG, "Producers are empty()");
        return producers;
    }

    private Producer cursorToProducer(Cursor cursor) {
        Producer producer = new Producer();
        producer.mIdProducer = cursor.getInt(0);
        producer.mEmail = cursor.getString(1);
        producer.mLink = cursor.getString(2);
        producer.mName = cursor.getString(3);
        producer.mPhone = cursor.getString(4);
        producer.mIdDescription_ = cursor.getInt(5);
        producer.mIdAddress_ = cursor.getInt(6);
        producer.mIdUser_ = cursor.getInt(7);
        producer.mIdImageCover_ = cursor.getInt(8);
        producer.mIdWineBest_ = cursor.getInt(9);
        producer.mLastUpdate = cursor.getString(10);
        return producer;
    }
}
