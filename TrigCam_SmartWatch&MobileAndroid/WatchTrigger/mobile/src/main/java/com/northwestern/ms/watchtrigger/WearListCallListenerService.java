package com.northwestern.ms.watchtrigger;


import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ms on 8/14/17.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;



import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by ms on 7/27/17.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class WearListCallListenerService extends WearableListenerService {

    private final String LOG_TAG = WearListCallListenerService.class.getSimpleName();

    private static GoogleApiClient googleApiClient;

    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String WEAR_PATH = "/wear";
    private String nodeId;
    Button btn2;


    private Switch led1;
    private Switch led2;
    private Switch led3;
    private EditText ip, port;
    private Button connect;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String ipaddress;
    private int portnum;
    private TextView link;
    private Pattern pattern;
    private Matcher matcher;
    private Handler handler;


    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ipaddr = "";
        Bundle b  = intent.getExtras();
        if (b != null) {
            ipaddr = b.getString("IPADDR");
        }

        return START_STICKY;
    }

    private class btTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Do bluetooth stuff here


            UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
            BluetoothSocket mmSocket = null;
            BluetoothDevice mmDevice = null;
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



            if(!mBluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //      startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("raspberrypi")) //Note, you will need to change this to match the name of your device
                    {
                        Log.e("Aquarium",device.getName());
                        mmDevice = device;
                        break;
                    }
                }
            }


//        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
            try {

                if (mmSocket == null) {
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                }

                if (!mmSocket.isConnected()){
                    mmSocket.connect();
                }

                String msg = "Trigger";
//            msg += "\n";
                OutputStream mmOutputStream = mmSocket.getOutputStream();
                mmOutputStream.write(msg.getBytes());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            return null;
        }
    }


    NotificationManager mNotiManager;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        mNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d("LOG PATH = ", messageEvent.getPath());

        Toast.makeText(this, "Message received.", Toast.LENGTH_SHORT);
        new btTask().execute();
        Toast.makeText(this, "btTask running.", Toast.LENGTH_SHORT);

        ipaddress="10.106.3.166";

        portnum=9091;
        Client client = new Client(ipaddress, portnum);
        client.start();
        final Toast toast = Toast.makeText(this, messageEvent.getPath(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        File root = Environment.getExternalStorageDirectory();
        File csvfile = new File("sdcard/", "SmartWatchTriggersMessages" + ".csv");

        FileWriter buffer = null;
        try {
            buffer = new FileWriter(csvfile, true);

            buffer.append(currentDateandTime + "\n");
//                    buffer.append(a).append(",");
//                    buffer.append(Float.toString(b)).append(",");
            buffer.flush();
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void closeConnection() {
        try {
//            out.writeObject("close");
//            out.close();
//            in.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            showErrorsMessages(ex.getMessage());
        }
    }//end of closeConnection

//    @Override
//    protected void onStop() {
//        super.onStop();
//        closeConnection();
//    }

    //////////////switches related methods ///////////////////
    void checkSwitchStatus() {

    }

    void changeSwitchesSatte(boolean state) {

    }

    ////////////////////// light related methods /////////////
    void lightOn(int lednum) {

    }

    void lightOff(int lednum) {

    }


    void showErrorsMessages(String error) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//        dialog.setTitle("Error!! ").setMessage(error).setNeutralButton("OK", null).create().show();
    }

    public boolean checkIP(final String ip) {
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }


    /////////////// client thread ////////////////////////////
    private class Client extends Thread {
        private String ipaddress;
        private int portnum;

        public Client(String ipaddress, int portnum) {
            this.ipaddress = ipaddress;
            this.portnum = portnum;
        }

        @Override
        public void run() {
            super.run();
            int a = connectToServer(ipaddress, portnum);
            if (a==1)
                closeConnection();


        }


        public int connectToServer(String ip, int port) {

            try {
                socket = new Socket(InetAddress.getByName(ip), port);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                for (int i = 0; i < 1; i++) {
                    System.out.println((String) in.readObject() + "\n");
                }
                checkSwitchStatus();
                //   handler.post(new Runnable() {
                //    public void run() {
                //             connect.setText("Close");
                //       changeSwitchesSatte(true);
                //    }
                //  });

                return 1;
            } catch (IOException e) {
                e.printStackTrace();
//                handler.post(new Runnable() {
//                    public void run() {
//                        showErrorsMessages("Unkown host!!");
//                    }
//                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return 0;
        }

    }//end of client class

}



