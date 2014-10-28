
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pl.tokajiwines.models.Producer;

import java.util.ArrayList;
import java.util.List;

public class ProducersDataSource {
    // Logcat tag
    private static final String LOG = "ProducersDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            "IdProducer", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_",
            "IdUser_", "LastUpdate"
    };

    public ProducersDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertProducer(Producer producer) {
        Log.e(LOG, "insertProducer()");
        ContentValues values = new ContentValues();
        values.put("IdProducer", producer.mIdProducer);
        values.put("Email", producer.mEmail);
        values.put("Link", producer.mLink);
        values.put("Name", producer.mName);
        values.put("Phone", producer.mPhone);
        values.put("IdDescription_", producer.mIdDescription_);
        values.put("IdAddress_", producer.mIdAddress_);
        values.put("IdUser_", producer.mIdUser_);
        values.put("LastUpdate", producer.mLastUpdate);

        long insertId = database.insert(DatabaseHelper.TABLE_PRODUCERS, null, values);

        Log.e(LOG, "Producer with id: " + producer.mIdProducer + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteProducer(Producer producer) {
        Log.e(LOG, "deleteProducer()");
        long id = producer.mIdProducer;
        Log.e(LOG, "Deleted producer with id: " + id);
        database.delete(DatabaseHelper.TABLE_PRODUCERS, "IdProducer" + " = " + id, null);
    }

    public void updateProducer(Producer producerOld, Producer producerNew) {
        Log.e(LOG, "updateProducer()");
        ContentValues values = new ContentValues();
        values.put("IdProducer", producerNew.mIdProducer);
        values.put("Email", producerNew.mEmail);
        values.put("Link", producerNew.mLink);
        values.put("Name", producerNew.mName);
        values.put("Phone", producerNew.mPhone);
        values.put("IdDescription_", producerNew.mIdDescription_);
        values.put("IdAddress_", producerNew.mIdAddress_);
        values.put("IdUser_", producerNew.mIdUser_);
        values.put("LastUpdate", producerNew.mLastUpdate);

        int rows = database.update(DatabaseHelper.TABLE_PRODUCERS, values, "'"
                + producerOld.mIdProducer + "' = '" + producerNew.mIdProducer + "'", null);
        Log.e(LOG, "Updated producer with id: " + producerOld.mIdProducer + " on: " + rows
                + " rows");
    }

    public List<Producer> getAllProducers() {
        Log.e(LOG, "getAllProducers()");
        List<Producer> producers = new ArrayList<Producer>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, allColumns, null, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Producer producer = cursorToProducer(cursor);
            producers.add(producer);
            cursor.moveToNext();
        }
        cursor.close();
        if (producers.isEmpty()) Log.e(LOG, "Producers are empty()");
        return producers;
    }

    private Producer cursorToProducer(Cursor cursor) {
        Producer producer = new Producer();
        producer.mIdProducer = (int) cursor.getLong(0);
        producer.mName = cursor.getString(1);
        return producer;
    }
}
