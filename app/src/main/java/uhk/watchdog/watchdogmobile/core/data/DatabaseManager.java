package uhk.watchdog.watchdogmobile.core.data;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 13.10.2016.
 *
 */

public interface DatabaseManager {

    int getDataNumber() throws Exception;
    void insertData(List<Sample> data)  throws Exception;
    List<Sample> getData(int rowNumber) throws Exception;
    void clearDatabase() throws Exception;
    void open();
    void close();

}
