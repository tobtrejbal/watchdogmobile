package uhk.watchdog.watchdogmobile.core.sensors.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.RunnableFuture;

import uhk.watchdog.watchdogmobile.core.sensors.WatchSensorListener;

/**
 * Created by Tob on 17. 8. 2016.
 */
public class ExternalSensor {

    private BluetoothAdapter mBluetoothAdapter;

    private Set<BluetoothDevice> mPairedDevices;

    private WatchSensorListener mSensorListener;

    private Thread mSensorThread;


    private Runnable mRunnableTakeSamples = new Runnable() {
        @Override
        public void run() {
            if(mBluetoothAdapter != null) {
                if (mBluetoothAdapter.isEnabled()) {
                    try {
                        mPairedDevices = mBluetoothAdapter.getBondedDevices();
                        Object[] devices = mPairedDevices.toArray();
                        BluetoothDevice d = (BluetoothDevice) devices[0];
                        String address = d.getAddress();
                        Log.v(address+"SampleDAO","achach");
                        BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(address);
                        Log.v(btDevice.getName()+"SampleDAO","achach");
                        //BluetoothSocket btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());
                        BluetoothSocket btSocket = (BluetoothSocket) btDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDevice,1);
                        btSocket.connect();
                        InputStream input = btSocket.getInputStream();
                        DataInputStream dinput = new DataInputStream(input);
                        //BufferedReader b = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
                        BufferedReader b = new BufferedReader(new InputStreamReader(input));
                        Log.v(btDevice.getName()+"SampleDAO","achach2");
                        long lastTime = System.currentTimeMillis();
                        int previousByte = 0;
                        while(true) {
                            try {
                                //Log.v("bste", input.read()+"");
                                int currentByte = input.read();
                                Log.v("bste", previousByte+":"+currentByte);
                                if(previousByte == 255 && currentByte == 255) {
                                    process(input);
                                    previousByte = -50;
                                } else {
                                    previousByte = currentByte;
                                }
                                //mSensorListener.onValueChanged(new SensorValue(sensorEvent.sensor.getType(), sensorEvent.values));
                            } catch(Exception ex) {
                                Log.v(btDevice.getName()+"SampleDAO","ERROOOR");
                                ex.printStackTrace();
                            }
                        }
                    } catch (Exception ex) {
                        Log.v("ERRRRORRRRR","ERRRORR");
                        ex.printStackTrace();
                    }
                } else {
                }
            }
        }
    };

    void process(InputStream input) throws IOException {
        getAccelerometerData(input,1);
        getAccelerometerData(input,2);
        getAccelerometerData(input,3);
        getAccelerometerData(input,4);
        getAccelerometerData(input,5);
        getAccelerometerData(input,6);
        input.read();
        input.read();
        /**for(int i = 0; i < 7; i++) {
            printValue((input.read() & 0xFF) << 8 | input.read() & 0xFF);
            //int value = (input.read() & 0xFF) | (input.read() & 0xFF) << 8 | (input.read() & 0xFF) << 16 | input.read() << 24;
            //Log.v("x", value+"");
        }*/
    }

    public static String data;

    public ExternalSensor(WatchSensorListener sensorListener) {
        this.mSensorListener = sensorListener;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mSensorThread = new Thread(mRunnableTakeSamples);
    }

    public void startListening() {

        mSensorThread.start();

    }

    public void stopListening() {

        mSensorThread.interrupt();

    }

    public void printValue(int value) {
        float zero_G = 512.0f; //ADC is 0~1023  the zero g output equal to Vs/2
        //ADXL335 power supply by Vs 3.3V
        float scale = 102.3f;  //ADXL335330 Sensitivity is 330mv/g
        //330 * 1024/3.3/1000

        Log.v("value", ""+value);
    }

    public void getAccelerometerData(InputStream input, int accNumber) throws IOException {

        int resolution = 4095;
        float voltage = 3.3f;
        float zeroVoltage = 1.56f;
        float sensitivity = 0.33f;

        int xData = readShort(input);
        int yData = readShort(input);
        int zData = readShort(input);

        float Rx = (xData * voltage) / resolution;
        float Ry = (yData * voltage) / resolution;
        float Rz = (zData * voltage) / resolution;

        float deltaRx = Rx - zeroVoltage;
        float deltaRy = Ry - zeroVoltage;
        float deltaRz = Rz - zeroVoltage;

        float x = deltaRx / sensitivity;
        float y = deltaRy / sensitivity;
        float z = deltaRz / sensitivity;

        x = ( ( (xData * voltage) / resolution) - zeroVoltage) / sensitivity;
        y = ( ( (yData * voltage) / resolution) - zeroVoltage) / sensitivity;
        z = ( ( (zData * voltage) / resolution) - zeroVoltage) / sensitivity;

        double accData= Math.sqrt((x*x)+(y*y)+(z*z));

        if(accNumber == 1) {
            Log.v("values", "x"+accNumber+": "+ x);
            Log.v("values", "y"+accNumber+": "+ y);
            Log.v("values", "z"+accNumber+": "+ z);
        }

        if(accNumber == 2) {
            Log.v("values", "x"+accNumber+": "+ Rx);
            Log.v("values", "y"+accNumber+": "+ Ry);
            Log.v("values", "z"+accNumber+": "+ Rz);
            Log.v("values", "sqrt"+accNumber+": "+ accData);
        }

    }

    public int readShort(InputStream input) throws IOException {
        return (input.read() & 0xFF) | ((input.read() & 0xFF) << 8);
        //return (input.read() & 0xFF) << 8 | input.read() & 0xFF;
    }

}
