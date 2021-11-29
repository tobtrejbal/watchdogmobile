package uhk.watchdog.watchdogmobile.core.data.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uhk.watchdog.watchdogmobile.core.data.base.DatabaseCore;
import uhk.watchdog.watchdogmobile.core.data.dao.SampleDAO;
import uhk.watchdog.watchdogmobile.core.flatbuffer.Transformer;
import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by admin on 30.01.2017.
 *
 */

public class SampleDAOImpl implements SampleDAO{

    private SQLiteDatabase database;

    private DatabaseCore databaseCore;

    public SampleDAOImpl() {
        databaseCore = DatabaseCore.getInstance();
    }


    @Override
    public void createOrUpdate(Sample entity) throws Exception {

    }

    @Override
    public void persist(Sample sample) throws Exception {
        database = databaseCore.getDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseCore.DATABASE_COLUMN_USER, sample.getUserID());
        values.put(DatabaseCore.DATABASE_COLUMN_DATE, sample.getDate());
        values.put(DatabaseCore.DATABASE_COLUMN_DATA, Transformer.objectToSampleFB(sample));
        database.insert(DatabaseCore.DATABASE_TABLE, null, values);
    }

    @Override
    public void remove(Sample entity) throws Exception {

    }

    @Override
    public Sample findById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<Sample> list() throws Exception {
        return null;
    }

    @Override
    public void persistMultiple(List<Sample> samples) throws Exception {

    }

    @Override
    public List<Sample> list(int number) throws Exception {
        database = databaseCore.getDatabase();
        String query = "select * from " + DatabaseCore.DATABASE_TABLE + " limit ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(number)});
        List<Sample> data = new ArrayList<>();
        int[] ids = new int[cursor.getCount()];
        cursor.moveToFirst();
        Log.i("got data with count : " + cursor.getCount(),"     ");
        for(int i = 0; !cursor.isAfterLast(); cursor.moveToNext(), i++) {
            Sample sample = Transformer.fbSampleToObjects(cursor.getBlob(3), cursor.getString(1));
            ids[i] = cursor.getInt(0);
            data.add(sample);
        }
        cursor.close();
        for(int i = 0; i < ids.length; i++) {
            String queryDel = "DELETE FROM "+DatabaseCore.DATABASE_TABLE+" WHERE "+DatabaseCore.DATABASE_COLUMN_ID + " = "+ids[i];
            database.execSQL(queryDel);
        }
        return data;
    }

    @Override
    public int getDataNumber() throws Exception {
        database = databaseCore.getDatabase();
        String query = "select * from " + DatabaseCore.DATABASE_TABLE;
        Cursor cursor = database.rawQuery(query,new String[] {});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void clearDatabase() throws Exception {
        database = databaseCore.getDatabase();
        String query = "delete * from " + DatabaseCore.DATABASE_TABLE;
        database.execSQL(query);
    }
}
