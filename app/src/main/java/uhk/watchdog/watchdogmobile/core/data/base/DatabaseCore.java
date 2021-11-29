package uhk.watchdog.watchdogmobile.core.data.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Tobous on 18. 10. 2014.
 *
 */
public class DatabaseCore extends SQLiteOpenHelper {

    /**
     *
     */
    private static DatabaseCore sInstance;

    /**
     *
     */
    private static ReentrantLock databaseLock = new ReentrantLock();


    /**
     *
     */
    private SQLiteDatabase mDatabase = null;

    public static final String DATABASE_NAME = "sensorDBS";
    public static final String DATABASE_TABLE = "data";
    public static final String DATABASE_COLUMN_ID = "id";
    public static final String DATABASE_COLUMN_DATA = "sample";
    public static final String DATABASE_COLUMN_DATE = "date";
    public static final String DATABASE_COLUMN_USER = "user";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE_QUERY =
            "CREATE TABLE " + DATABASE_TABLE +
                    " (" + DATABASE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    DATABASE_COLUMN_USER + " TEXT, " +
                    DATABASE_COLUMN_DATE + " INTEGER, " +
                    DATABASE_COLUMN_DATA + " BLOB ); ";

    /**
     *
     * @return
     */
    public static DatabaseCore getInstance() {
        return sInstance;
    }

    /**
     *
     * @param context
     * @return
     */
    public static void createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseCore(context.getApplicationContext());
        }
    }


    /**
     *
     */
    private DatabaseCore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    /**
     *
     * @return
     */
    public synchronized void open() {
        Log.v("blablaaa","lasdasd");
        mDatabase = getWritableDatabase();
    }

    /**
     *
     */
    public synchronized void close() {
        mDatabase.close();
        mDatabase = null;
    }

    /**
     *
     */
    public SQLiteDatabase getDatabase() throws Exception {
       return mDatabase;
    }

    /**
     *
     */
    protected void openSession() throws Exception {
        // need to call before you interact with database
        if (databaseLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            mDatabase.beginTransaction();
            System.out.println("opened");
        }
    }

    /**
     *
     */
    protected void commitSession() throws Exception {
        // need to call after you interact with database
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        databaseLock.unlock();
        System.out.println("closed");
    }

    /**
     *
     */
    protected void rollback() throws Exception {
        // need to call after you interact with database
        mDatabase.endTransaction();
        databaseLock.unlock();
        System.out.println("closed");
    }
}