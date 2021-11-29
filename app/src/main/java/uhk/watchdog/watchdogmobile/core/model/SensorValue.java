package uhk.watchdog.watchdogmobile.core.model;

/**
 * Created by Tobous on 5. 11. 2014.
 *
 */
public class SensorValue {

    /**
     *
     */
    final private int type;

    /**
     *
     */
    final private float[] values;

    /**
     *
     * @param type
     * @param values
     */
    public SensorValue(int type, float[] values) {
        this.type = type;
        this.values = new float[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public float[] getValues() {
        return values;
    }

}
