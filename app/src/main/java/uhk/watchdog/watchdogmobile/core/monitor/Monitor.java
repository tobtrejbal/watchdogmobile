package uhk.watchdog.watchdogmobile.core.monitor;

import java.util.Deque;
import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 13.10.2016.
 */

public interface Monitor {

    void analyze(Deque<Sample> samples) throws InterruptedException;

}
