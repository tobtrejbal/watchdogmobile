// automatically generated, do not modify

package uhk.watchdog.watchdogmobile.core.flatbuffer.objects;

import java.nio.*;
import java.lang.*;

import com.google.flatbuffers.*;

public class DataFB extends Table {
  public static DataFB getRootAsDataFB(ByteBuffer _bb) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (new DataFB()).__init(_bb.getInt(_bb.position()) + _bb.position(), _bb); }
  public DataFB __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String userLogin() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userLoginAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public SampleFB samples(int j) { return samples(new SampleFB(), j); }
  public SampleFB samples(SampleFB obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int samplesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createDataFB(FlatBufferBuilder builder,
      int userLogin,
      int samples) {
    builder.startObject(2);
    DataFB.addSamples(builder, samples);
    DataFB.addUserLogin(builder, userLogin);
    return DataFB.endDataFB(builder);
  }

  public static void startDataFB(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addUserLogin(FlatBufferBuilder builder, int userLoginOffset) { builder.addOffset(0, userLoginOffset, 0); }
  public static void addSamples(FlatBufferBuilder builder, int samplesOffset) { builder.addOffset(1, samplesOffset, 0); }
  public static int createSamplesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startSamplesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endDataFB(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishDataFBBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
};

