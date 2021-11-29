package uhk.watchdog.watchdogmobile.core.buffer;

import java.util.Deque;
import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by Tobous on 19.02.2017.
 */

public interface Buffer {

    void addSampleToAnalyze(Sample sample) throws InterruptedException;
    Deque<Sample> getSamplesToAnalyze() throws InterruptedException;
    void analyzeCompleted() throws InterruptedException;

    void addSampleSend(Sample sample) throws InterruptedException;
    List<Sample> clearBufferSend() throws InterruptedException;
    int sizeSend() throws InterruptedException;

}
