// automatically generated, do not modify

package uhk.watchdog.watchdogmobile.core.flatbuffer.objects;

import java.nio.*;

import com.google.flatbuffers.*;

public class SampleFB extends Table {
  public static SampleFB getRootAsSampleFB(ByteBuffer _bb) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (new SampleFB()).__init(_bb.getInt(_bb.position()) + _bb.position(), _bb); }
  public SampleFB __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public long date() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0; }
  public int batteryLevel() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public double lat() { int o = __offset(8); return o != 0 ? bb.getDouble(o + bb_pos) : 0; }
  public double lon() { int o = __offset(10); return o != 0 ? bb.getDouble(o + bb_pos) : 0; }
  public SensorValueFB sensorValues(int j) { return sensorValues(new SensorValueFB(), j); }
  public SensorValueFB sensorValues(SensorValueFB obj, int j) { int o = __offset(12); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int sensorValuesLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }

  public static int createSampleFB(FlatBufferBuilder builder,
      long date,
      int batteryLevel,
      double lat,
      double lon,
      int sensorValues) {
    builder.startObject(5);
    SampleFB.addLon(builder, lon);
    SampleFB.addLat(builder, lat);
    SampleFB.addDate(builder, date);
    SampleFB.addSensorValues(builder, sensorValues);
    SampleFB.addBatteryLevel(builder, batteryLevel);
    return SampleFB.endSampleFB(builder);
  }

  public static void startSampleFB(FlatBufferBuilder builder) { builder.startObject(5); }
  public static void addDate(FlatBufferBuilder builder, long date) { builder.addLong(0, date, 0); }
  public static void addBatteryLevel(FlatBufferBuilder builder, int batteryLevel) { builder.addInt(1, batteryLevel, 0); }
  public static void addLat(FlatBufferBuilder builder, double lat) { builder.addDouble(2, lat, 0); }
  public static void addLon(FlatBufferBuilder builder, double lon) { builder.addDouble(3, lon, 0); }
  public static void addSensorValues(FlatBufferBuilder builder, int sensorValuesOffset) { builder.addOffset(4, sensorValuesOffset, 0); }
  public static int createSensorValuesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startSensorValuesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endSampleFB(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

