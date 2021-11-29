package uhk.watchdog.watchdogmobile.core.data.dao;

import java.util.List;

/**
 * Created by admin on 30.01.2017.
 */

public interface DaoBase<E,K> {

    void createOrUpdate(E entity) throws Exception;
    void persist(E entity) throws Exception;
    void remove(E entity) throws Exception;
    E findById(K id) throws Exception;
    List<E> list() throws Exception;
    void persistMultiple(List<E> eList) throws Exception;

}
