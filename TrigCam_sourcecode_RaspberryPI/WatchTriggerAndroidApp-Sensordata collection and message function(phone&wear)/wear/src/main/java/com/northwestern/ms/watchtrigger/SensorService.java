package com.northwestern.ms.watchtrigger;

/**
 * Created by ms on 7/26/17.
 */


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

/**
 * Created by ms on 8/14/17.
 */


/**
 * Created by ms on 7/26/17.
 */






//package com.example.appwhysitservice2;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.UUID;

//import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;



public class SensorService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,WearableListView.ClickListener {
    private boolean on_advertising = false;
    public static boolean isAdv = false;
    BluetoothLeAdvertiser advertiser;
    ParcelUuid pUuid;
    AdvertiseData data;
    AdvertiseSettings settings;

    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    private static final int WAIT_TIME_MS = 500;
    private long detectTime = 0;
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

    public int win1 = 0;
    public int win2 = 0;
    public int win3 = 0;
    public int win4 = 0;
    public int win5 = 0;
    public boolean iswin1 = false;
    public boolean iswin2 = false;
    public boolean iswin3 = false;
    public boolean iswin4 = false;
    public boolean iswin5 = false;

    public float acc1_prev = 0;
    public float acc1_now = 0;
    public float energy1_sum = 0;
    public float acc2_prev = 0;
    public float acc2_now = 0;
    public float energy2_sum = 0;
    public float acc3_prev = 0;
    public float acc3_now = 0;
    public float energy3_sum = 0;

    public int total_count = 0;
    public float seconds = 0;
    public boolean step = false;





    private static final float SHAKE_THRESHOLD = 0.9f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 200;

    private View mView;
    private TextView mTextTitle;
    private TextView mTextValues;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private int flag;
    private int flag2;








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
/*
    AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d("BLE", "Advertising onStartSuccess: " + "success");
            on_advertising = true;
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.d("BLE", "Advertising onStartFailure: " + errorCode);
            on_advertising = false;
            super.onStartFailure(errorCode);
        }
    };*/

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
/*
        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        settings = new AdvertiseSettings.Builder().setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY).setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH).setConnectable(false).build();
        int serviceUuid = 0xFEAA;
        byte[] serviceUuidBytes = new byte[]{(byte) (serviceUuid & 0xff), (byte) ((serviceUuid >> 8) & 0xff)};
        pUuid = parseUuidFrom(serviceUuidBytes);
        data = new AdvertiseData.Builder().setIncludeDeviceName(true).addServiceUuid(pUuid).addServiceData(pUuid, "Data".getBytes(Charset.forName("UTF-8"))).build();
        Log.d("TEST : ", data.toString());
*/
        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGravity = mManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mMagnetic = mManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mRotation = mManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mOrientation = mManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mLinearAccel = mManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mStepCounter = mManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetector = mManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public void onDestroy() {
        mManager.unregisterListener(listener);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(), "sensorService", Toast.LENGTH_SHORT).show();
        // sendMessage("REGISTERING SENSORS");
        mManager.registerListener(listener, mAccel, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mGravity, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mRotation, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mOrientation, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mLinearAccel, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL); // 5hz
        mManager.registerListener(listener, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL); // 5hz

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

        win1 = start_time + winsize;
        win2 = start_time + winsize * 2;
        win3 = start_time + winsize * 3;
        win4 = start_time + winsize * 4;
        win5 = start_time + winsize * 5;

        Log.d("win1 : ", Integer.toString(win1));
        Log.d("win2 : ", Integer.toString(win2));
        Log.d("win3 : ", Integer.toString(win3));
        Log.d("win4 : ", Integer.toString(win4));
        Log.d("win5 : ", Integer.toString(win5));
//        sendMessage("You are Sitting!");

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

            if(sensor.getType() == Sensor.TYPE_STEP_COUNTER)
                write_file("step_counter", date, activity, event.values[0]);
            else if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                step = true;
                write_file("step_detector", date, activity, event.values[0]);
            }
            else if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                write_file("acc", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_GYROSCOPE)
                write_file("gyro", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_GRAVITY)
                write_file("grav", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                write_file("mag", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_ORIENTATION)
                write_file("ori", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
                write_file("rot", date, activity, event.values[0], event.values[1], event.values[2]);
            else if(sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
                write_file("lin_acc", date, activity, event.values[0], event.values[1], event.values[2]);

            if (sensor.getType() == Sensor.TYPE_GRAVITY) {
                if (g_x(event.values[0]) == 1)
                    grav_count += 1;

                if (current_time == grav_time)
                    return;
                else
                    grav_time = current_time;
            }

            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float ori1 = event.values[0];
//                Toast.makeText(getApplicationContext(), "HandLEFT/RIGHT:A:z" + Float.toString(ori1), Toast.LENGTH_SHORT).show();
                float ori2 = event.values[1];
//                Toast.makeText(getApplicationContext(), "WristROTATION:P:x:" + Float.toString(ori2), Toast.LENGTH_SHORT).show();
                float ori3 = event.values[2];
//                Toast.makeText(getApplicationContext(), "HandUP/DOWNR:y:" + Float.toString(ori3), Toast.LENGTH_SHORT).show();

                long now = System.currentTimeMillis();
                if ((now - detectTime) > WAIT_TIME_MS) {
                    detectTime = now;

                    if ((ori3 < -30 && ori2 > 20)) {
                        //  flag=1;


                        //    Toast.makeText(getApplicationContext(), "Gyroo!!", Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(getApplicationContext(), (int) event.values[0], Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Food Grab & lift Detected", Toast.LENGTH_SHORT).show();
                        sendMessage("EatingDetected");
                    }


                }
            }

            if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float gyro1 = event.values[0];
                Toast.makeText(getApplicationContext(), "Gyro:"+ Float.toString(gyro1), Toast.LENGTH_SHORT).show();




                if ((gyro1> 3 )) {
                    //  flag=1;

                    //    Toast.makeText(getApplicationContext(), "Gyroo!!", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(), (int) event.values[0], Toast.LENGTH_SHORT).show();
                    sendMessage("RotationDetected");
                }

                // }
                if (m_x(event.values[0]) == 1)
                    mag_count += 1;

                if (current_time == mag_time)
                    return;
                else
                    mag_time = current_time;
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (a_x(event.values[0]) == 1)
                    acc_count += 1;

                if (current_time == acc_time)
                    return;
                else
                    acc_time = current_time;

                Log.d("current - start : ", Integer.toString(current_time - start_time));
                Log.d("current : ", Integer.toString(current_time));


                // long now = System.currentTimeMillis();

//                if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
//                    mShakeTime = now;
//                    {
                acc1_now = event.values[0];
                acc2_now = event.values[1];
                acc3_now = event.values[2];
                energy1_sum += (acc1_prev - acc1_now) * (acc1_prev - acc1_now);
                energy2_sum += (acc2_prev - acc2_now) * (acc2_prev - acc2_now);
                energy3_sum += (acc3_prev - acc3_now) * (acc3_prev - acc3_now);

             //   Toast.makeText(getApplicationContext(), "Accl:"+ Float.toString(acc2_now), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(), Float.toString(acc1_now), Toast.LENGTH_SHORT).show();

                if ((acc2_now > 20)&&(flag2==0)) {
                    //   Toast.makeText(getApplicationContext(), "Accc!!", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(), Float.toString(acc1_now), Toast.LENGTH_SHORT).show();
                    Log.d("current acc : ", Float.toString(acc1_now));
                    //  flag2=1;
             //       sendMessage("Acceleration Detected");
                }
                // }
                // }












                if (after20 - start_time >= 60) {
                    //        advertiser.stopAdvertising(advertisingCallback);
                    isAdv = false;
                    after20 = 0;
                }

                write_file("engergy", date, activity, energy1_sum, energy2_sum, energy3_sum);

                total_count = 0;
                if(acc_count >= 4) total_count++;
                if(grav_count >= 4) total_count++;
                if(mag_count >= 4) total_count++;

                if(total_count >= 2)
                    seconds += 1;

                energy1_sum = 0;
                energy2_sum = 0;
                energy3_sum = 0;
                acc_count = 0;
                grav_count = 0;
                mag_count = 0;

                acc1_prev = event.values[0];
                acc2_prev = event.values[1];
                acc3_prev = event.values[2];

//                if((current_time - start_time) == 30 ||(current_time - start_time) == 90 || (current_time - start_time) == 150 || (current_time - start_time) == 210|| (current_time - start_time) == 270|| (current_time - start_time) == 330)
//                    advertiser.stopAdvertising(advertisingCallback);


                if ((current_time - start_time) % 60 == 0 && current_time - start_time <= 1200) { // need to change 300 to 1200
///////////////////////ms                    sendMessage("You are Sitting!");
//                    advertiser.startAdvertising(settings, data, advertisingCallback);

                    if (seconds >= 30) {
                        if(step)
                            write_file("minute", date, activity, seconds, "false");
                        else
                        {
                            write_file("minute", date, activity, seconds, "true");
                            min_count += 1;
                        }
                    }
                    else {
                        write_file("minute", date, activity, seconds, "false");
                    }
                    seconds = 0;
                    step = false;


                    if (((current_time - start_time) / 60) - min_count >= 2) {
                        start_time = current_time;
                        min_count = 0;
                    }
                }

                if ((current_time - start_time) >= 1200) { // need to change 300 to 1200
                    if (min_count >= 15) { // need to change 4 to 19
                        start_time = current_time;
                        min_count = 0;

                        if (current_time < win1 && !iswin1) {
                            if (!isAdv) {
                                //             advertiser.startAdvertising(settings, data, advertisingCallback);
                                after20 = current_time + 60;
                                isAdv = true;
                            }
//                            sendMessage("You are Sitting!");
                            iswin1 = true;
                        } else if (win1 < current_time && current_time < win2 && !iswin2) {
                            if (!isAdv) {
                                //             advertiser.startAdvertising(settings, data, advertisingCallback);
                                after20 = current_time + 60;
                                isAdv = true;
                            }
//                            sendMessage("You are Sitting!");
                            iswin2 = true;
                        } else if (win2 < current_time && current_time < win3 && !iswin3) {
                            if (!isAdv) {
                                //         advertiser.startAdvertising(settings, data, advertisingCallback);
                                after20 = current_time + 60;
                                isAdv = true;
                            }
//                            sendMessage("You are Sitting!");
                            iswin3 = true;
                        } else if (win3 < current_time && current_time < win4 && !iswin4) {
                            if (!isAdv) {
                                //          advertiser.startAdvertising(settings, data, advertisingCallback);
                                after20 = current_time + 60;
                                isAdv = true;
                            }
//                            sendMessage("You are Sitting!");
                            iswin4 = true;
                        } else if (win4 < current_time && current_time < win5 && !iswin5) {
                            if (!isAdv) {
                                //         advertiser.startAdvertising(settings, data, advertisingCallback);
                                after20 = current_time + 60;
                                isAdv = true;
                            }
//                            sendMessage("You are Sitting!");
                            iswin5 = true;
                        }
                    }
                }

                if (current_time == (win1 - 1200) && !iswin1) {
                    if (!isAdv) {
                        //        advertiser.startAdvertising(settings, data, advertisingCallback);
                        after20 = current_time + 60;
                        isAdv = true;
                    }
//                    sendMessage("You are Sitting!");
                    iswin1 = true;
                    start_time = current_time;
                    min_count = 0;
                } else if (current_time == (win2 - 1200) && !iswin2) {
                    if (!isAdv) {
                        //       advertiser.startAdvertising(settings, data, advertisingCallback);
                        after20 = current_time + 60;
                        isAdv = true;
                    }
//                    sendMessage("You are Sitting!");
                    iswin2 = true;
                    start_time = current_time;
                    min_count = 0;
                } else if (current_time == (win3 - 1200) && !iswin3) {
                    if (!isAdv) {
                        //        advertiser.startAdvertising(settings, data, advertisingCallback);
                        after20 = current_time + 60;
                        isAdv = true;
                    }
//                    sendMessage("You are Sitting!");
                    iswin3 = true;
                    start_time = current_time;
                    min_count = 0;
                } else if (current_time == (win4 - 1200) && !iswin4) {
                    if (!isAdv) {
                        //        advertiser.startAdvertising(settings, data, advertisingCallback);
                        after20 = current_time + 60;
                        isAdv = true;
                    }
//                    sendMessage("You are Sitting!");
                    iswin4 = true;
                    start_time = current_time;
                    min_count = 0;
                } else if (current_time == (win5 - 1200) && !iswin5) {
                    if (!isAdv) {
                        //       advertiser.startAdvertising(settings, data, advertisingCallback);
                        after20 = current_time + 60;
                        isAdv = true;
                    }
//                    sendMessage("You are Sitting!");
                    iswin5 = true;
                    start_time = current_time;
                    min_count = 0;
                }



            }

        }
    };

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

    public float acc_x(float acc_x) {
        if (acc_x <= -5.343858) return 0;
        else {
            if (acc_x <= 8.030152) return 1;
            else {
                if (acc_x <= 8.528147) return 0;
                else {
//                    if (acc_x <= 9.320627) return 1;
//                    else {
                    return 0;
//                    }
                }
            }
        }
    }

    public float grav(float grav) {
        if (grav <= -5) // standing with hands down
            return 0;
        else if (-5 < grav && grav <= 3) // sitting
            return 1;
        else if (3 < grav && grav <= 7.5) //
            return 0;
        else if (7.5 < grav && grav <= 8.8) // sitting with phone
            return 1;
        else //
            return 0;
    }

    public float grav2(float grav) {
        if (grav <= 3) // standing with hands down
            return 1;
        else
            return 0;

    }

    // start from here

    public float a_x(float acc_x) {
        if(acc_x <= -5.343858) { return 0;
        }else
        if(acc_x <= 6.624756) { if (acc_x <= 2.75) {
            if (acc_x <= 0.2) {if (acc_x <= -0.95) return 0;
            else return 1;}
            else if (acc_x <= 2.0) {if (acc_x <= 1.55) return 1;
            else return 0;}
            else return 1;
        }
        else {return 1;} }
        else
        if(acc_x <= 8.02297) { return 1;
        }
        else
        if(acc_x <= 8.528147) { return 0; }
        else
        if(acc_x <= 9.320627) { return 1; }
        else { return 0; }
    }



    public float a_z(float acc_z) {
        if (acc_z <= 3.676297)
            if (acc_z <= -0.060454)
                if (acc_z <= -6.503251)
                    if (acc_z <= -8.866327)
                        if (acc_z <= -9.855731) {
                            return 0;
                        } else {
                            return 1;
                        }
                    else {
                        return 0;
                    }
                else {
                    return 0;
                }
            else if (acc_z <= 3.185485)
                if (acc_z <= 2.583344)
                    if (acc_z <= 0.475848) {
                        return 0;
                    } else if (acc_z <= 1.861492)
                        if (acc_z <= 1.465251) {
                            return 1;
                        } else if (acc_z <= 1.596334) {
                            return 0;
                        } else {
                            return 1;
                        }
                    else if (acc_z <= 2.072182) {
                        return 0;
                    } else {
                        return 1;
                    }
                else {
                    return 0;
                }
            else {
                return 1;
            }
        else {
            return 1;
        }
    }

    public float g_x(float grav_x) {
        if(grav_x <= -5.345205)
            if(grav_x <= -7.542184) { return 0; }
            else
            if(grav_x <= -6.938396)
                if(grav_x <= -7.150733)
                    if(grav_x <= -7.221211) { return 0; }
                    else { return 1; }
                else { return 0; }
            else
            if(grav_x <= -6.155493)
                if(grav_x <= -6.242432) { return 0; }
                else { return 1; }
            else { return 0; }
        else
        if(grav_x <= 6.70975)
            if(grav_x <= -3.063381)
                if(grav_x <= -4.494066)
                    if(grav_x <= -5.049072)
                        if(grav_x <= -5.223699)
                            if(grav_x <= -5.282656) { return 1; }
                            else { return 0; }
                        else { return 1; }
                    else
                    if(grav_x <= -4.05) {return 0;}//-4.748899) { return 0; }
                    else { return 1; }
                else { //return 1; }
                    if (grav_x <= -3.1) {return 0;}
                    else {return 1;}
                }
            else {
                if (grav_x <=3) {//return 0;}
                    if (grav_x <= 1) {if (grav_x <= -1) {
                        if (grav_x <= -1.6) {
                            if (grav_x <= -1.9) {return 0;}
                            else return 1;
                        }
                        else return 0;
                    }
                    else return 1;}
                    else {return 0;}
                } else {return 1;}
            }
        else
        if(grav_x <= 7.972691) { return 1; }
        else
        if(grav_x <= 8.525602)
            if(grav_x <= 8.080132)
                if(grav_x <= 8.044218) { return 0; }
                else { return 1; }
            else { return 0; }
        else
        if(grav_x <= 9.333496)
            if(grav_x <= 9.18311)
                if(grav_x <= 8.956409) { return 1; }
                else { return 0; }
            else { return 1; }
        else { return 0; }
    }

    public float g_z(float grav_z) {
        if (grav_z <= 3.671807)
            if (grav_z <= 0.299724)
                if (grav_z <= -6.524649)
                    if (grav_z <= -7.414543)
                        if (grav_z <= -8.557625) {
                            return 1;
                        } else if (grav_z <= -8.196549) {
                            return 0;
                        } else if (grav_z <= -8.125322) {
                            return 1;
                        } else {
                            return 0;
                        }
                    else {
                        return 1;
                    }
                else if (grav_z <= -3.451092)
                    if (grav_z <= -5.176413) {
                        return 0;
                    } else if (grav_z <= -3.806781)
                        if (grav_z <= -4.455759)
                            if (grav_z <= -4.70715)
                                if (grav_z <= -5.108478) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            else if (grav_z <= -4.519804) {
                                return 1;
                            } else if (grav_z <= -4.461894) {
                                return 0;
                            } else {
                                return 1;
                            }
                        else {
                            return 0;
                        }
                    else if (grav_z <= -3.719243) {
                        return 1;
                    } else {
                        return 0;
                    }
                else {
                    return 0;
                }
            else if (grav_z <= 2.230797)
                if (grav_z <= 0.616058) {
                    return 0;
                } else if (grav_z <= 1.804929)
                    if (grav_z <= 1.039233)
                        if (grav_z <= 0.7847) {
                            return 1;
                        } else {
                            return 0;
                        }
                    else {
                        return 1;
                    }
                else if (grav_z <= 2.095226) {
                    return 0;
                } else {
                    return 1;
                }
            else if (grav_z <= 3.176657) {
                return 0;
            } else {
                return 1;
            }
        else {
            return 1;
        }
    }

    public float m_x(float mag_x) {
        mag_x = -mag_x;
        if (mag_x <= 20.765924)
//            if (mag_x <= -37.95767)
            if (mag_x <= 0)
                if (mag_x <= -43.75378)
                    if (mag_x <= -61.993473)
                        if (mag_x <= -75.812164)
                            if (mag_x <= -99.47592) {
                                return 1;
                            } else if (mag_x <= -77.81119) {
                                return 0;
                            } else {
                                return 1;
                            }
                        else {
                            return 1;
                        }
                    else {
                        return 1;
                        //return 0;
                    }
                else if (mag_x <= -40.07376)
                    if (mag_x <= -42.32022)
                        if (mag_x <= -43.14081) {
                            return 1;
                        } else {
                            return 0;
                        }
                    else {
                        return 0;
                    }
                else {
                    //return 0;
                    return 1;
                }
            else {
                return 1;
            }
        else {
            return 0;
        }
    }
}

//    public float acc_y(float acc_y) {
//        if (acc_y <= -4.120421) {
//            if (acc_y <= -9.933543) {
//                if (acc_y <= -10.527305) {
//                    return 0;
//                } else {
//                    return 1;
//                }
//            } else {
//                return 1;
//            }
//        } else {
//            if (acc_y <= 2.312799) {
//                if (acc_y <= -3.236961) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            } else {
//                return 1;
//            }
//        }
//    }
/*

if a < 10
    if a < 9
        if a <8
            if a > 7
                return 1;
            else
                return 0:
        else
            return 1;
    else
        return 0
else
    return 1;

a > 10 : return 1
9 < a < 10 return 0
8 < a < 9 reutnr*/
//    public float acc_z(float acc_z)
//    {
//        if(acc_z <= -0.060454){
//            if(acc_z <= -6.503251){
//                if(acc_z <= -8.866327){
//                    if(acc_z <= -9.855731) { return 0; }
//                    else { return 1; }
//                }
//                else
//                {
//                    if(acc_z <= -7.635109){
//                        if(acc_z <= -8.497621) { return 1; }
//                        else { return 0; }
//                    }
//                    else { return 1; }
//                }
//            }
//            else { return 0; }
//        }
//        else {
//            if(acc_z <= 2.583344) {
//                if(acc_z <= 0.475848) { return 0; }
//                else{
//                    if(acc_z <= 1.861492){
//                        if(acc_z <= 1.465251) { return 1; }
//                        else
//                        {
//                            if(acc_z <= 1.596334) { return 0; }
//                            else { return 1; }
//                        }
//                    }
//                    else{
//                        if(acc_z <= 2.072182) { return 0; }
//                        else {
//                            if(acc_z <= 2.204461) { return 1; }
//                            else { return 0; }
//                        }
//                    }
//                }
//            }
//            else{
//                if(acc_z <= 3.128623) { return 0; }
//                else { return 1; }
//            }
//        }
//    }
//
//    public float gyr_x(float gyr_x)
//    {
//        if(gyr_x <= 0.133158)
//        {
//            if(gyr_x <= -0.093377)
//            {
//                if(gyr_x <= -0.33541)
//                {
//                    if(gyr_x <= -8.202536)
//                    {
//                        if(gyr_x <= -8.64805) { return 0; }
//                        else { return 1; }
//                    }
//                    else { return 0; }
//                }
//                else
//                {
//                    if(gyr_x <= -0.173485) { return 0; }
//                    else
//                    {
//                        if(gyr_x <= -0.12269) { return 0; }
//                        else { return 1; }
//                    }
//                }
//            }
//            else { return 1; }
//        }
//        else { return 0; }
//    }




//    private class MyAdapter extends WearableListView.Adapter {
//        private final LayoutInflater mInflater;
//        private ArrayList<String> data;
//
//        private MyAdapter(Context context, ArrayList<String> listItems) {
//            mInflater = LayoutInflater.from(context);
//            data = listItems;
//        }
//
//        @Override
//        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//    }
