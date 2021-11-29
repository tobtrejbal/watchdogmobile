package uhk.watchdog.watchdogmobile.core.data.base;

/**
 * Created by admin on 30.01.2017.
 */

public class TransactionManager {

    /**
     *
     */
    private static TransactionManager sInstance;

    /**
     *
     */
    DatabaseCore databaseCore = DatabaseCore.getInstance();

    /**
     *
     * @return
     */
    public static TransactionManager getInstance() {
        if (sInstance == null) {
            sInstance = new TransactionManager();
        }
        return sInstance;
    }


    public void startTransaction() throws Exception {
        switch (getType()) {
            case 0:
                databaseCore.openSession();
                System.out.println("starting transaction");
                break;

            default:

                break;
        }
    }

    public void endTransaction() throws Exception {
        switch (getType()) {
            case 0:
                databaseCore.commitSession();
                System.out.println("ending transaction");
                break;

            default:

                break;
        }
    }

    public void rollbackTransaction() throws Exception {
        switch (getType()) {
            case 0:
                databaseCore.rollback();
                System.out.println("rolling back transaction");
                break;

            default:

                break;
        }
    }

    public static int getType() {
        return 0;
    }

}
