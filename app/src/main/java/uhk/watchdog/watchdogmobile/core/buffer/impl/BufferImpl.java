package uhk.watchdog.watchdogmobile.core.buffer.impl;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import uhk.watchdog.watchdogmobile.core.buffer.Buffer;
import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by Tobous on 19.02.2017.
 */

public class BufferImpl implements Buffer {

    private int analyzeSize;

    private boolean primaryBuffer;

    private boolean primaryBufferSend;

    /**
     *
     */
    private static ReentrantLock analyzeBufferLock = new ReentrantLock();

    /**
     *
     */
    private static ReentrantLock sendBufferLock = new ReentrantLock();

    /**
     *
     */
    private Deque<Sample> mBufferAnalyze;

    /**
     *
     */
    private Deque<Sample> analyzeBufferSecondary;


    /**
     *
     */
    private List<Sample> mBufferSend;

    /**
     *
     */
    private List<Sample> mBufferSendSecondary;

    public BufferImpl(int analyzeSize, int sendSize) {
        this.analyzeSize = analyzeSize;
        this.mBufferAnalyze = new LinkedBlockingDeque();
        this.analyzeBufferSecondary = new LinkedBlockingDeque<>();
        this.mBufferSend = new ArrayList<Sample>(sendSize);//Collections.synchronizedList(new ArrayList<Sample>(sendSize));
        this.mBufferSendSecondary = new ArrayList<Sample>(sendSize);
    }


    @Override
    public void addSampleToAnalyze(Sample sample) throws InterruptedException {
        if (analyzeBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            if(primaryBuffer) {
                addSample(mBufferAnalyze, sample);
            } else {
                addSample(analyzeBufferSecondary, sample);
            }
            analyzeBufferLock.unlock();
        }
    }

    @Override
    public Deque<Sample> getSamplesToAnalyze() throws InterruptedException {
        if (analyzeBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            primaryBuffer = false;
            analyzeBufferLock.unlock();
            return mBufferAnalyze;
        } else {
            return null;
        }
    }

    @Override
    public void analyzeCompleted() throws InterruptedException {
        if (analyzeBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            switchBuffers();
            analyzeBufferLock.unlock();
        }
    }

    @Override
    public void addSampleSend(Sample sample) throws InterruptedException {
        if (sendBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            if (primaryBufferSend) {
                mBufferSend.add(sample);
            } else {
                mBufferSendSecondary.add(sample);
            }
            sendBufferLock.unlock();
        }
    }

    @Override
    public List<Sample> clearBufferSend() throws InterruptedException {
        if (sendBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            if(primaryBufferSend) {
                primaryBufferSend ^= true;
                mBufferSendSecondary.clear();
                sendBufferLock.unlock();
                return mBufferSend;
            } else {
                primaryBufferSend ^= true;
                mBufferSend.clear();
                sendBufferLock.unlock();
                return mBufferSendSecondary;
            }
        } else {
            return null;
        }
    }

    @Override
    public int sizeSend() throws InterruptedException {
        if (sendBufferLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
            if(primaryBufferSend) {
                sendBufferLock.unlock();
                return mBufferSend.size();
            } else {
                sendBufferLock.unlock();
                return mBufferSendSecondary.size();
            }
        } else {
            return 0;
        }
    }


    private void addSample(Deque<Sample> buffer, Sample sample) {
        buffer.add(sample);
        if(buffer.size() > analyzeSize) {
            buffer.removeFirst();
        }
    }

    private void switchBuffers() {
        if(!primaryBuffer) {
            for(int i = 0; i < analyzeBufferSecondary.size(); i++) {
                addSample(mBufferAnalyze, analyzeBufferSecondary.pollFirst());
            }
        }
        primaryBuffer = true;
    }


}
