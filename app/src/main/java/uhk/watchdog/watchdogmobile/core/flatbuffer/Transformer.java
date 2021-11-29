package uhk.watchdog.watchdogmobile.core.flatbuffer;

import android.annotation.SuppressLint;

import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uhk.watchdog.watchdogmobile.core.flatbuffer.objects.*;
import uhk.watchdog.watchdogmobile.core.model.Sample;
import uhk.watchdog.watchdogmobile.core.model.SensorValue;

/**
 * Created by tobou on 15.10.2016.
 * d
 */

public class Transformer {

    public static byte[] objectToDataFB(List<Sample> samples) {
        FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
        int[] samplesOffs = new int[samples.size()];
        String userId = samples.get(0).getUserID();
        for(int i = 0; i < samples.size(); i++) {
            samplesOffs[i] = createSampleFB(flatBufferBuilder, samples.get(i));
        }
        int samplesOff = DataFB.createSamplesVector(flatBufferBuilder, samplesOffs);
        int totalOff = DataFB.createDataFB(flatBufferBuilder, flatBufferBuilder.createString(userId), samplesOff);
        flatBufferBuilder.finish(totalOff);
        return flatBufferBuilder.sizedByteArray();
    }

    public static List<Sample> fbDataToObjects(byte[] bytes) {
        List<Sample> samples = new ArrayList<>();
        DataFB data = DataFB.getRootAsDataFB(ByteBuffer.wrap(bytes));
        for(int i = 0; i < data.samplesLength(); i++) {
            Sample sample = createSampleFromFB(data.samples(i), data.userLogin());
            samples.add(sample);
        }
        return samples;
    }

    public static byte[] objectToSampleFB(Sample sample) {
        FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
        int totalOff = createSampleFB(flatBufferBuilder, sample);
        flatBufferBuilder.finish(totalOff);
        return flatBufferBuilder.sizedByteArray();
    }

    public static Sample fbSampleToObjects(byte[] bytes, String userLogin) {
        SampleFB sampleFB = SampleFB.getRootAsSampleFB(ByteBuffer.wrap(bytes));
        return createSampleFromFB(sampleFB, userLogin);
    }

    private static Sample createSampleFromFB(SampleFB sampleFB, String userLogin) {
        @SuppressLint("UseSparseArrays")
        Map<Integer, SensorValue> sampleValueMap = new HashMap<>();
        for(int i = 0; i < sampleFB.sensorValuesLength(); i++) {
            SensorValue sensorValue = createSensorValueFromFB(sampleFB.sensorValues(i));
            sampleValueMap.put(sensorValue.getType(), sensorValue);
        }
        return new Sample(sampleValueMap, sampleFB.date(), userLogin, sampleFB.batteryLevel(), sampleFB.lat(), sampleFB.lon());
    }

    private static SensorValue createSensorValueFromFB(SensorValueFB sensorValueFB) {
        float[] values = new float[sensorValueFB.sensorValuesLength()];
        for(int i = 0; i < values.length; i++) {
            values[i] = sensorValueFB.sensorValues(i);
        }
        return new SensorValue(sensorValueFB.sensorId(), values);
    }

    private static int createSampleFB(FlatBufferBuilder flatBufferBuilder, Sample sample) {
        int[] sensorValuesOffs = new int[sample.getValues().size()];
        Iterator it = sample.getValues().entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            sensorValuesOffs[i] = createSensorValueFB(flatBufferBuilder, (SensorValue) pair.getValue());
            i++;
        }
        int sensorValuesOff = SampleFB.createSensorValuesVector(flatBufferBuilder, sensorValuesOffs);
        return SampleFB.createSampleFB(flatBufferBuilder, sample.getDate(), sample.getBatteryLevel(), sample.getLat(), sample.getLon(), sensorValuesOff);
    }

    private static int createSensorValueFB(FlatBufferBuilder flatBufferBuilder, SensorValue sensorValue) {
        int valueOffset = SensorValueFB.createSensorValuesVector(flatBufferBuilder, sensorValue.getValues());
        return SensorValueFB.createSensorValueFB(flatBufferBuilder, sensorValue.getType(), valueOffset);
    }

}
