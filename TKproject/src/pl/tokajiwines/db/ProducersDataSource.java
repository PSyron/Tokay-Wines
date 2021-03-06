
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.jsonresponses.ProducerDetails;
import pl.tokajiwines.jsonresponses.ProducerListItem;
import pl.tokajiwines.models.Producer;
import pl.tokajiwines.utils.Log;

public class ProducersDataSource {
    // LogCat tag
    private static final String LOG = "ProducersDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private String[] allColumns = {
            "IdProducer", "Email", "Link", "Name", "Phone", "IdDescription_", "IdAddress_",
            "IdUser_", "IdImageCover_", "IdWineBest_", "LastUpdate"
    };

    public ProducersDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //Log.i(LOG, database + " ");
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

    public Producer getProducer(int id) {
        Log.i(LOG, "getProducer(id=" + id + ")");
        Producer producer = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, allColumns, "IdProducer"
                + "=" + id, null, null, null, null);
        if (cursor == null && cursor.getCount() == 0)
            Log.w(LOG, "Producer with id= " + id + " doesn't exists");
        else {
            cursor.moveToFirst();
            producer = cursorToProducer(cursor);
        }
        cursor.close();
        return producer;
    }

    public ProducerDetails getProducerDetails(int id) {
        Log.i(LOG, "getProducer(id=" + id + ")");
        ProducerDetails pd = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, allColumns, "IdProducer"
                + "=" + id, null, null, null, null);
        if (cursor.getCount() == 0)
            Log.w(LOG, "Producer with id= " + id + " doesn't exists");
        else {
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
            AddressesDataSource aDs = new AddressesDataSource(mContext);
            WinesDataSource wDs = new WinesDataSource(mContext);
            dDs.open();
            aDs.open();
            iDs.open();
            wDs.open();
            cursor.moveToFirst();
            pd = cursorToProducerDetails(cursor, iDs, dDs, aDs, wDs);
            dDs.close();
            aDs.close();
            iDs.close();
            wDs.close();
        }
        cursor.close();
        return pd;
    }

    public Producer[] getAllProducers() {
        Log.i(LOG, "getAllStrains()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, allColumns, null, null,
                null, null, null);

        Producer[] producers = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            producers = new Producer[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                Producer producer = cursorToProducer(cursor);
                producers[i] = producer;
                cursor.moveToNext();
                i++;
            }
        } else
            Log.w(LOG, "Producers are empty()");
        cursor.close();
        return producers;
    }

    public ProducerListItem[] getProducerList() {
        Log.i(LOG, "getProducerList()");
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, new String[] {
                "IdProducer", "Name", "IdDescription_", "IdImageCover_"
        }, null, null, null, null, "Name");
        ImagesDataSource iDs = new ImagesDataSource(mContext);
        iDs.open();
        DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
        dDs.open();
        cursor.moveToFirst();
        ProducerListItem[] producers = new ProducerListItem[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            ProducerListItem producer = cursorToProducerListItem(cursor, iDs, dDs);
            producers[i] = producer;
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        if (producers == null) Log.w(LOG, "Producers are empty()");

        iDs.close();
        dDs.close();
        return producers;
    }

    public ProducerListItem[] getProducers(String s) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCERS, new String[] {
                "IdProducer", "Name", "IdDescription_", "IdImageCover_"
        }, "Name LIKE ?", new String[] {
            "%" + s + "%"
        }, null, null, "Name");
        ProducerListItem[] producers = null;
        if (cursor.getCount() == 0) {
            Log.e("Producers", "empty");
        } else {
            ImagesDataSource iDs = new ImagesDataSource(mContext);
            iDs.open();
            DescriptionsDataSource dDs = new DescriptionsDataSource(mContext);
            dDs.open();
            cursor.moveToFirst();
            producers = new ProducerListItem[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                ProducerListItem producer = cursorToProducerListItem(cursor, iDs, dDs);
                producers[i] = producer;
                Log.e("Producer", producer.mName);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
            iDs.close();
            dDs.close();
        }
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

    private ProducerListItem cursorToProducerListItem(Cursor cursor, ImagesDataSource iDs,
            DescriptionsDataSource dDs) {
        Producer p = new Producer();
        p.mIdProducer = cursor.getInt(0);
        p.mName = cursor.getString(1);
        p.imageCover = iDs.getImageUrl(cursor.getInt(3));
        p.description = dDs.getDescriptionShort(cursor.getInt(2));

        return new ProducerListItem(p);
    }

    private ProducerDetails cursorToProducerDetails(Cursor cursor, ImagesDataSource iDs,
            DescriptionsDataSource dDs, AddressesDataSource aDs, WinesDataSource wDs) {
        Producer producer = new Producer();
        producer.mIdProducer = cursor.getInt(0);
        producer.mEmail = cursor.getString(1);
        producer.mLink = cursor.getString(2);
        producer.mName = cursor.getString(3);
        producer.mPhone = cursor.getString(4);
        producer.description = dDs.getDescriptionVast(cursor.getInt(5));
        producer.address = aDs.getAddress(cursor.getInt(6));
        producer.mIdUser_ = cursor.getInt(7);
        producer.imageCover = iDs.getImageUrl(cursor.getInt(8));
        producer.wineBest = wDs.getProducerWineBest(cursor.getInt(9));
        producer.mLastUpdate = cursor.getString(10);

        return new ProducerDetails(producer);
    }
}
