package uhk.watchdog.watchdogmobile.core.controller.impl;

import android.support.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import uhk.watchdog.watchdogmobile.core.controller.Controller;

/**
 * Created by Tob on 13. 7. 2016.
 *
 */
public class ControllerImpl implements Controller {

    private ScheduledExecutorService mTimer;

    public void start(int threads) {
            mTimer = Executors.newScheduledThreadPool(threads, new ProcessPriorityThreadFactory((Thread.MAX_PRIORITY)));
        }

        public void scheduleWithFixedDelay(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
            mTimer.scheduleWithFixedDelay(runnable, delay, period, timeUnit);
        }

        public void scheduleAtFixedRate(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
            mTimer.scheduleAtFixedRate(runnable, delay, period, timeUnit);
        }

        public void stop() {
            mTimer.shutdown();
        }

        private final static class ProcessPriorityThreadFactory implements ThreadFactory {

            private final int threadPriority;

            ProcessPriorityThreadFactory(int threadPriority) {
                this.threadPriority = threadPriority;
            }

            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(threadPriority);
                return thread;
            }

    }
}
