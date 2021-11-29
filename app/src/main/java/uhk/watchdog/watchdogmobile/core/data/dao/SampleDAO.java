package uhk.watchdog.watchdogmobile.core.data.dao;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by admin on 30.01.2017.
 */

public interface SampleDAO extends DaoBase<Sample, Long> {

    List<Sample> list(int number) throws Exception;
    int getDataNumber() throws Exception;
    void clearDatabase() throws Exception;


}
