package uhk.watchdog.watchdogmobile.core.communication.constants;

/**
 * Created by Tobous on 1. 11. 2014.
 *
 */
public class NetworkCommunication {

    public static final String INTENT_FILTER_WATCH_DOG_MAIN = "filterMain";

    public static int BROADCASTS_TYPE = 1;

    public static int BROADCASTS_SEND_STATUS = 2;

    public static int BROADCASTS_SENDING_STATUS = 3;

    public static int BROADCASTS_SENDING_CONFIG = 6;

    public static final int BROADCASTS_RESTART = 10;

    public static final int BROADCASTS_KILL = 15;

    public static final int BROADCASTS_AUTHENTISATION_FAILED = 8;

    public static final int BROADCASTS_AUTHENTICATE = 24;

    public static final int BROADCASTS_AUTHENTISATION_SUCCESS = 25;

    public static int BROADCASTS_CONTENT_DATE = 4;

    public static int BROADCASTS_CONTENT_ONLINE = 5;

    public static int BROADCASTS_CONTENT_CONFIG = 7;

    public static String MQTT_SEND_DATA_TOPIC = "watchdog_data";

}
