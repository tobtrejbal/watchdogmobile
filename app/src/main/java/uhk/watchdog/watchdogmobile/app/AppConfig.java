package uhk.watchdog.watchdogmobile.app;

import android.hardware.Sensor;

/**
 * Created by Tob on 1. 7. 2016.
 */
public class AppConfig {

    /**
     *
     */
    private static AppConfig instance;

    /**
     *
     */
    private AppConfig() {


    }

    /**
     *
     * @return
     */
    public static AppConfig getInstance() {

        if(instance==null){
            instance=new AppConfig();
        }
        return instance;

    }

    /**
     *
     */
    private String serverAddress;

    /**
     *
     */
    private double[] polygonCoordX;

    /**
     *
     */
    private double[] polygonCoordY;

    /**
     *
     */
    private int serverPort;

    /**
     *
     */
    private String userPassword;

    /**
     *
     */
    private String userLogin;

    /**
     *
     */
    private int sampleRate;

    /**
     *
     */
    private int analyzeRate;

    /**
     *
     */
    private int clearBufferRate;

    /**
     *
     */
    private boolean horizontalMenu = true;

    /**
     *
     */
    private int[] sensorList = new int[] {Sensor.TYPE_ACCELEROMETER,Sensor.TYPE_GYROSCOPE,Sensor.TYPE_AMBIENT_TEMPERATURE};

    /**
     *
     * @return
     */
    public int[] getSensorList() {
        return sensorList;
    }

    /**
     *
     * @param sensorList
     */
    public void setSensorList(int[] sensorList) {
        this.sensorList = sensorList;
    }

    /**
     *
     * @return
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     *
     * @param serverAddress
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     *
     * @return
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     *
     * @param serverPort
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     *
     * @return
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     *
     * @return
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     *
     * @param userLogin
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     *
     * @return
     */
    public double[] getPolygonCoordX() {
        return polygonCoordX;
    }

    /**
     *
     * @param polygonCoordX
     */
    public void setPolygonCoordX(double[] polygonCoordX) {
        this.polygonCoordX = polygonCoordX;
    }

    /**
     *
     * @return
     */
    public double[] getPolygonCoordY() {
        return polygonCoordY;
    }

    /**
     *
     * @param polygonCoordY
     */
    public void setPolygonCoordY(double[] polygonCoordY) {
        this.polygonCoordY = polygonCoordY;
    }

    /**
     *
     * @return
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     *
     * @param sampleRate
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     *
     * @return
     */
    public int getAnalyzeRate() {
        return analyzeRate;
    }

    /**
     *
     * @param analyzeRate
     */
    public void setAnalyzeRate(int analyzeRate) {
        this.analyzeRate = analyzeRate;
    }

    /**
     *
     * @return
     */
    public int getClearBufferRate() {
        return clearBufferRate;
    }

    /**
     *
     * @param clearBufferRate
     */
    public void setClearBufferRate(int clearBufferRate) {
        this.clearBufferRate = clearBufferRate;
    }

    /**
     *
     * @return
     */
    public boolean isHorizontalMenu() {
        return horizontalMenu;
    }

    /**
     *
     * @param horizontalMenu
     */
    public void setHorizontalMenu(boolean horizontalMenu) {
        this.horizontalMenu = horizontalMenu;
    }
}
