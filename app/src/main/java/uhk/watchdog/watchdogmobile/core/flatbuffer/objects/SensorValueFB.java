// automatically generated, do not modify

package uhk.watchdog.watchdogmobile.core.flatbuffer.objects;

import java.nio.*;

import com.google.flatbuffers.*;

public class SensorValueFB extends Table {
  public static SensorValueFB getRootAsSensorValueFB(ByteBuffer _bb) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (new SensorValueFB()).__init(_bb.getInt(_bb.position()) + _bb.position(), _bb); }
  public SensorValueFB __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int sensorId() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float sensorValues(int j) { int o = __offset(6); return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0; }
  public int sensorValuesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer sensorValuesAsByteBuffer() { return __vector_as_bytebuffer(6, 4); }

  public static int createSensorValueFB(FlatBufferBuilder builder,
      int sensorId,
      int sensorValues) {
    builder.startObject(2);
    SensorValueFB.addSensorValues(builder, sensorValues);
    SensorValueFB.addSensorId(builder, sensorId);
    return SensorValueFB.endSensorValueFB(builder);
  }

  public static void startSensorValueFB(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addSensorId(FlatBufferBuilder builder, int sensorId) { builder.addInt(0, sensorId, 0); }
  public static void addSensorValues(FlatBufferBuilder builder, int sensorValuesOffset) { builder.addOffset(1, sensorValuesOffset, 0); }
  public static int createSensorValuesVector(FlatBufferBuilder builder, float[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addFloat(data[i]); return builder.endVector(); }
  public static void startSensorValuesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endSensorValueFB(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

