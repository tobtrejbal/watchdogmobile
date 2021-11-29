package uhk.watchdog.watchdogmobile.core.data.impl;

import android.content.Context;
import android.util.Log;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.data.base.DatabaseCore;
import uhk.watchdog.watchdogmobile.core.data.DatabaseManager;
import uhk.watchdog.watchdogmobile.core.data.base.TransactionManager;
import uhk.watchdog.watchdogmobile.core.data.dao.SampleDAO;
import uhk.watchdog.watchdogmobile.core.data.dao.impl.SampleDAOImpl;
import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by Tobous on 19. 10. 2014
 *
 *
 *
 */
public class DatabaseManagerImpl implements DatabaseManager {

    /**
     *
     */
    private DatabaseCore mDatabaseCore;

    /**
     *
     */
    private TransactionManager transactionManager;

    /**
     *
     */
    private SampleDAO sampleDAO;

    /**
     *
     * @param context
     */
    public DatabaseManagerImpl(Context context) {
        DatabaseCore.createInstance(context);
        mDatabaseCore = DatabaseCore.getInstance();
        transactionManager = TransactionManager.getInstance();
        sampleDAO = new SampleDAOImpl();
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public int getDataNumber() throws Exception {
        try {
            transactionManager.startTransaction();
            int dataNumber = sampleDAO.getDataNumber();
            transactionManager.endTransaction();
            return dataNumber;
        } catch (Exception ex) {
            Log.v("test","test");
            transactionManager.rollbackTransaction();
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @param data
     * @throws InterruptedException
     */
    public void insertData(List<Sample> data)  throws Exception {
        try {
            transactionManager.startTransaction();
            for(Sample sample : data) {
                sampleDAO.persist(sample);
            }
            transactionManager.endTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            transactionManager.rollbackTransaction();
        }
    }

    @Override
    public void clearDatabase() throws Exception {
        try {
            transactionManager.startTransaction();
            sampleDAO.clearDatabase();
            transactionManager.endTransaction();
        } catch (Exception ex) {
            transactionManager.rollbackTransaction();
        }
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public List<Sample> getData(int rowNumber) throws Exception {
        try {
            transactionManager.startTransaction();
            List<Sample> samples = sampleDAO.list(rowNumber);
            transactionManager.endTransaction();
            return samples;
        } catch (Exception ex) {
            transactionManager.rollbackTransaction();
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     */
    public void open() {
        mDatabaseCore.open();
    }

    /**
     *
     */
    public void close() {
        mDatabaseCore.close();
    }
}
