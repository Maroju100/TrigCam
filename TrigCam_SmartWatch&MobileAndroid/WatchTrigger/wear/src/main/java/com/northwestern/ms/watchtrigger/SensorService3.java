package com.northwestern.ms.watchtrigger;


import android.app.Service;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.UUID;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;



public class SensorService3 extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,WearableListView.ClickListener {
    private boolean on_advertising = false;
    public static boolean isAdv = false;
    BluetoothLeAdvertiser advertiser;
    ParcelUuid pUuid;
    AdvertiseData data;
    AdvertiseSettings settings;

    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    private static final int WAIT_TIME_MS = 6000;
    float flag_register=0;
    private static final int WAIT_TIME_MS3 = 20000;
    private long detectTime = 0;
    private long detectTime2 = 0;
    public static SensorManager mManager;
    public static Sensor mAccel;
    public static Sensor mGyroscope;
    public static Sensor mGravity;
    public static Sensor mRotation;
    public static Sensor mMagnetic;
    public static Sensor mOrientation;
    public static Sensor mLinearAccel;

    public static Sensor mStepCounter;
    public static Sensor mStepDetector;

    public static int after20 = 0;

    public int wake_hour, wake_min, sleep_hour, sleep_min;
    public String activity;
    public int winsize = 0;
    public int start_time = 0;
    public int current_time = 0;
    public int acc_time = 0;
    public int grav_time = 0;
    public int mag_time = 0;
    public int acc_count, min_count;
    public int grav_count;
    public int mag_count;


    public float acc1_prev = 0;
    public float acc1_now = 0;
    public float energy1_sum = 0;
    public float acc2_prev = 0;
    public float acc2_now = 0;
    public float energy2_sum = 0;
    public float acc3_prev = 0;
    public float acc3_now = 0;
    public float energy3_sum = 0;

    public float mGeo1 = 0;
    public float mGeo2 = 0;

    public float mGeo3 = 0;

    public int total_count = 0;
    public float seconds = 0;
    public boolean step = false;


    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];


    private static float[] mAccelerometer = null;
    private static float[] mGeomagnetic = null;

    private String mBearing = null;


    double val[];


    private double azimuth2 = 0;
    private static final float FILTER_ALPHA = 0.8f;
    private static final float FILTER_ALPHA1 = 0.3f;
    private static final float FILTER_ALPHA_E = 0.8f;
    private static final double FILTER_ALPHA_A = 0.8f;


    private static final double FILTER_ALPHA2 = 0.85f;
    double ori11 = 0;
    double mag11 = 0;
    float ori22 = 0;
    float ori33 = 0;


//    int TempState1 = 0;
//    int TempState2 = 0;
//    int TempState3 = 0;


    int FSM_CurrentState = 0;
    int FSM_CurrentStateA = 0;
//    int FSM_CurrentState1 = 0;
//    int FSM_currentState2 = 0;
//    int FSM_CurrentState3 = 0;

    int TempState1 = 0;
    int TempState2 = 0;
    int TempState3 = 0;


    private final float[] accelerometerValues = new float[3];
    private final float[] magnetometerValues = new float[3];
    private final float[] orientation = new float[3];
    private final float[] rotMatR = new float[9];
    private final float[] rotMatI = new float[9];

    float ms1;
    float ms2;
    float ms3;
    float power2=0;
    float power2_c=0;
    float mag_diff=0;


    private float m1;
    private float m2;
    private float m3;
    private float anet;
    float temp_az;

    private static final float SHAKE_THRESHOLD = 0.9f;
    private static final int SHAKE_WAIT_TIME_MS = 500;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 500;

    private View mView;
    private TextView mTextTitle;
    private TextView mTextValues;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private int flag;
    private int flag2;
    private int flagT=0;
    private int flagT2=0;
    int a = 0;
    int b = 0;
    int d = 0;


    long now2 =0;



    private static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        /** Length of bytes for 16 bit UUID */
        final int UUID_BYTES_16_BIT = 2;
        /** Length of bytes for 32 bit UUID */
        final int UUID_BYTES_32_BIT = 4;
        /** Length of bytes for 128 bit UUID */
        final int UUID_BYTES_128_BIT = 16;
        final ParcelUuid BASE_UUID = //ParcelUuid.fromString("8CB88C3B-E0B0-4448-B1D3-EE073DDA5941");
                ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");
//                  ParcelUuid.fromString("202A4E58-D505-478A-A681-5B20B8A3D66C");

        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length != UUID_BYTES_16_BIT && length != UUID_BYTES_32_BIT &&
                length != UUID_BYTES_128_BIT) {
            throw new IllegalArgumentException("uuidBytes length invalid - " + length);
        }
        // Construct a 128 bit UUID.
        if (length == UUID_BYTES_128_BIT) {
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            long msb = buf.getLong(8);
            long lsb = buf.getLong(0);
            return new ParcelUuid(new UUID(msb, lsb));
        }
        // For 16 bit and 32 bit UUID we need to convert them to 128 bit value.
        // 128_bit_value = uuid * 2^96 + BASE_UUID
        long shortUuid;
        if (length == UUID_BYTES_16_BIT) {
            shortUuid = uuidBytes[0] & 0xFF;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
        } else {
            shortUuid = uuidBytes[0] & 0xFF;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
            shortUuid += (uuidBytes[2] & 0xFF) << 16;
            shortUuid += (uuidBytes[3] & 0xFF) << 24;
        }
        long msb = BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
        long lsb = BASE_UUID.getUuid().getLeastSignificantBits();
        return new ParcelUuid(new UUID(msb, lsb));
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()) {
                            mNode = node;
                        }
                    }
                });
    }

    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void sendMessage(String Key) {
        if (mNode != null && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //   Log.d(TAG, "-- " + mGoogleApiClient.isConnected());
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), Key + "!!!", null).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            //            Log.d(TAG, "Activity Node is : " + mNode.getId() + " - " + mNode.getDisplayName());
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                //   Log.d(TAG, "Failed to send message with status code: "
//                                        + sendMessageResult.getStatus().getStatusCode();
                            }
                        }
                    }
            );
        }
    }


    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

    }

    @Override
    public void onTopEmptyRegionClick() {
        Toast.makeText(this, "You tapped on Top empty area", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //sendMessage("OnCreated");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//      mGyroscope = mManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
////    mGravity = mManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mMagnetic = mManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
//        mRotation = mManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//      mRotation = mManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
//      mOrientation = mManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//      mLinearAccel = mManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//      mStepCounter = mManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//      mStepDetector = mManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public void unregisterListener (SensorEventListener listener, Sensor sensor)
    {

    }

    public void onDestroy() {
        mManager.unregisterListener(listener, mMagnetic);



        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(), "sensorService", Toast.LENGTH_SHORT).show();
        // sendMessage("REGISTERING SENSORS");
        mManager.registerListener(listener, mAccel, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mAccel, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
//      mManager.registerListener(listener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
//      mManager.registerListener(listener, mGravity, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mRotation, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mMagnetic, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mOrientation, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mOrientation, 100000); // 5hz
//      mManager.registerListener(listener, mLinearAccel, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mStepCounter, SensorManager.SENSOR_DELAY_UI); // 5hz
//      mManager.registerListener(listener, mStepDetector, SensorManager.SENSOR_DELAY_UI); // 5hz

        Calendar c = Calendar.getInstance();
        wake_hour = intent.getIntExtra("w_hour", -1);
        wake_min = intent.getIntExtra("w_min", -1);
        sleep_hour = intent.getIntExtra("s_hour", -1);
        sleep_min = intent.getIntExtra("s_min", -1);
        activity = intent.getStringExtra("activity");

        winsize = ((sleep_hour * 60 * 60 + sleep_min * 60) - (wake_hour * 60 * 60 + wake_min * 60)) / 5;
        start_time = c.get(Calendar.SECOND) + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.HOUR) * 60 * 60;

        Log.d("time : ", Integer.toString(wake_hour) + " - " + Integer.toString(wake_min) + " - " + Integer.toString(sleep_hour) + " - " + Integer.toString(sleep_min));
        Log.d("winsize : ", Integer.toString(winsize));

        acc_count = 0;
        min_count = 0;
        grav_count = 0;
        mag_count = 0;

        total_count = 0;
        seconds = 0;
        step = false;

        return START_STICKY;
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            Calendar c = Calendar.getInstance();
            String date = c.get(Calendar.YEAR) + "-" +
                    ((c.get(Calendar.MONTH) < 9) ? "0" : "") +
                    (c.get(Calendar.MONTH) + 1) + "-" +
                    ((c.get(Calendar.DAY_OF_MONTH) < 10) ? "0" : "") +
                    c.get(Calendar.DAY_OF_MONTH) + " " +
                    ((c.get(Calendar.HOUR_OF_DAY) < 10) ? "0" : "") +
                    c.get(Calendar.HOUR_OF_DAY) + ":" +
                    ((c.get(Calendar.MINUTE) < 10) ? "0" : "") +
                    c.get(Calendar.MINUTE) + ":" +
                    ((c.get(Calendar.SECOND) < 10) ? "0" : "") +
                    c.get(Calendar.SECOND);
            current_time = c.get(Calendar.SECOND) + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.HOUR) * 60 * 60;


            /////////////////////////////////////////writing/////////////////////////////////////////////////////////////////////////////////


            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                write_file("acc", date, activity, event.values[0], event.values[1], event.values[2]);
//            else if (sensor.getType() == Sensor.TYPE_GYROSCOPE)
//                write_file("gyro", date, activity, event.values[0], event.values[1], event.values[2]);
//            else if (sensor.getType() == Sensor.TYPE_GRAVITY)
//                write_file("grav", date, activity, event.values[0], event.values[1], event.values[2]);
            else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
                write_file("mag", date, activity, event.values[0], event.values[1], event.values[2]);
//            else if (sensor.getType() == Sensor.TYPE_ORIENTATION)
//                write_file("ori", date, activity, event.values[0], event.values[1], event.values[2]);
            else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
                write_file("rot", date, activity, event.values[0], event.values[1], event.values[2]);
//            else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
//                write_file("lin_acc", date, activity, event.values[0], event.values[1], event.values[2]);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                acc_count += 1;
                acc1_now = 10*event.values[0];
                acc2_now = 10*event.values[1];
                acc3_now = 10*event.values[2];
                energy1_sum += (acc1_prev - acc1_now) * (acc1_prev - acc1_now);
                energy2_sum += (acc2_prev - acc2_now) * (acc2_prev - acc2_now);
                energy3_sum += (acc3_prev - acc3_now) * (acc3_prev - acc3_now);

                float acc_roll = (float) (acc2_now / sqrt(acc1_now * acc2_now + acc3_now * acc3_now));
                float acc_pitch = acc1_now / acc3_now;

                if ((acc1_now > -45 )) {
                    FSM_CurrentStateA=1;
                    now2 = System.currentTimeMillis();
                    Log.d("In State 1: ", "YES");
                    if ((((now2 - detectTime)> WAIT_TIME_MS3)&&(flagT==1)) ) {
                        flagT=0;
                        flagT2=0;
//                        mManager.registerListener(listener, mRotation, SensorManager.SENSOR_DELAY_UI); // 5hz
                        mManager.registerListener(listener, mMagnetic, SensorManager.SENSOR_DELAY_UI); // 5hz
                        Toast.makeText(getApplicationContext(), "UnRegistering Orientation and Mag sensing!!", Toast.LENGTH_SHORT).show();
                        sendMessage("Unregistering-OrientationSensing&MagSensing!!");
                    }

                }

                if ((acc1_now < -44 && acc1_now > -55 && acc2_now < -20 &&  FSM_CurrentStateA==1)) {

                    Log.d("In State 2: ", "YES");
                    FSM_CurrentStateA=2;

                }

                if ((acc1_now < -54 && acc2_now < -10 && FSM_CurrentStateA==2)) {
                    Log.d("In State 3 : ", "YES");
                    FSM_CurrentStateA=3;
                    write_file("AccelTriggering", date, acc1_now, acc2_now);
                    {
                        flagT=1;
                        detectTime = System.currentTimeMillis();
                        //       detectTime = now;
                        if(flagT2==0) {
//                            mManager.registerListener(listener, mRotation, SensorManager.SENSOR_DELAY_GAME); // 50hz
                            mManager.registerListener(listener, mMagnetic, SensorManager.SENSOR_DELAY_UI); // 15hz
                            Toast.makeText(getApplicationContext(), "Registering Orientation and Mag sensing!!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Feeding Gesture Detected-Accel!!", Toast.LENGTH_SHORT).show();
                            sendMessage("Feeding Gesture Detected-Accel!!");
                            flagT2=1;
                        }
                    }

                }

            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
                mGeomagnetic = event.values;

                mGeo1= mGeomagnetic[0];
                mGeo2= mGeomagnetic[1];
                mGeo3= mGeomagnetic[2];

                m1 = mGeomagnetic[0];
                m2 = mGeomagnetic[1];
                m3 = mGeomagnetic[2];

                ms1= FILTER_ALPHA1 * ms1 + (1 - FILTER_ALPHA1) * m1;
                ms2 = FILTER_ALPHA1 * ms2 + (1 - FILTER_ALPHA1) * m2;
                ms3 = FILTER_ALPHA1 * ms3 + (1 - FILTER_ALPHA1) * m3;

                power2 = Math.round(Math.sqrt(Math.pow(ms1, 2) + Math.pow(ms2, 2) + Math.pow(ms3, 2)));
                float power3 = Math.round(Math.sqrt(Math.pow(m2, 2) +  Math.pow(m3, 2)));
                Log.d("---magpower->", Float.toString(power2));
                power2_c= (float)(FILTER_ALPHA2 * power2_c + (1 - FILTER_ALPHA2) * power2);

            }


            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
                mGeomagnetic = event.values;

                mGeo1= mGeomagnetic[0];
                mGeo2= mGeomagnetic[1];
                mGeo3= mGeomagnetic[2];

                m1 = mGeomagnetic[0];
                m2 = mGeomagnetic[1];
                m3 = mGeomagnetic[2];

                ms1= FILTER_ALPHA1 * ms1 + (1 - FILTER_ALPHA1) * m1;
                ms2 = FILTER_ALPHA1 * ms2 + (1 - FILTER_ALPHA1) * m2;
                ms3 = FILTER_ALPHA1 * ms3 + (1 - FILTER_ALPHA1) * m3;

                power2 = Math.round(Math.sqrt(Math.pow(ms1, 2) + Math.pow(ms2, 2) + Math.pow(ms3, 2)));
                float power2s = Math.round((Math.pow(ms1, 2) + Math.pow(ms2, 2) + Math.pow(ms3, 2)));

                float power3 = Math.round(Math.sqrt(Math.pow(m2, 2) +  Math.pow(m3, 2)));

//                Log.d("---magpower->", Float.toString(power2));

                power2_c= (float)(FILTER_ALPHA2 * power2_c + (1 - FILTER_ALPHA2) * power2);

            }

            {

                double mag_diff = power2_c-power2;
//                mag_diff=power2-power2_c;
                mag_diff=Math.round(Math.sqrt(mag_diff*mag_diff));
//                    Log.d("---mag_diff ->", Double.toString(mag_diff));

                if (mag_diff<41 &&  TempState1 == 0)

                {


                    Log.d("M11111111111 : ", "YES");

                    TempState1=1;
                }

                if ((mag_diff> 40 && TempState1 == 1) ) {

//&& TempState1 == 1
                    Log.d("MMMMMMMMMMM : ", "YES");
                    Toast.makeText(getApplicationContext(), "Feeding Gesture Detected (Magnetic)!!", Toast.LENGTH_SHORT).show();
                    TempState1 = 0;
                    {
//                               detectTime = now;

                        sendMessage("Feeding Gesture Detected-Mag!!");
                    }

                }

            }

        }
    };


    public void write_file(String name, String date,  float a, float b) {
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", name + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(date).append(",");
            buffer.append(Float.toString(a)).append(",");
            buffer.append(Float.toString(b)).append("\n");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public void write_file(String name, String date,  float a, float b,float c, float d) {
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", name + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(date).append(",");
            buffer.append(Float.toString(a)).append(",");
            buffer.append(Float.toString(b)).append(",");
            buffer.append(Float.toString(c)).append(",");
            buffer.append(Float.toString(d)).append("\n");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public void write_file(String name, String date, String a, float b) {
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", name + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(date).append(",");
            buffer.append(a).append(",");
            buffer.append(Float.toString(b)).append("\n");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void write_file(String name, String date, String a, float b, float c, float d) {
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", name + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(date).append(",");
            buffer.append(a).append(",");
            buffer.append(Float.toString(b)).append(",");
            buffer.append(Float.toString(c)).append(",");
            buffer.append(Float.toString(d)).append("\n");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void write_file(String name, String date, String a, float b, String c) {
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", name + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(date).append(",");
            buffer.append(a).append(",");
            buffer.append(Float.toString(b)).append(",");
            buffer.append(c).append("\n");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
