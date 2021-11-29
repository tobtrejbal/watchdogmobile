package uhk.watchdog.watchdogmobile.core.controller;

import java.util.concurrent.TimeUnit;

/**
 * Created by tobou on 13.10.2016.
 *
 */

public interface Controller {

    void start(int threads);
    void stop();
    void scheduleWithFixedDelay(Runnable runnable, long delay, long period, TimeUnit timeUnit);
    void scheduleAtFixedRate(Runnable runnable, long delay, long period, TimeUnit timeUnit);

}
