package com.northwestern.ms.watchtrigger;


import android.app.NotificationManager;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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



    NotificationManager mNotiManager;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        mNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d("LOG PATH = ", messageEvent.getPath());

        // ipaddress="10.105.139.174";

        ipaddress="10.106.12.162";

        portnum=9091;
        Client client = new Client(ipaddress, portnum);
        client.start();


//        this.startActivity(intent); // need to do this


//        startActivities(intent);
//        toast.show();
//        if(toast.getView().isShown()) {toast.cancel();}//Toast is cancelled here if currently showing


        final Toast toast = Toast.makeText(this, messageEvent.getPath(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {


//            @Override
//            public void run() {
//                toast.cancel();
//            }
//        }, 1500);

//        if (messageEvent.getPath().equals("/mobile")) {
//            nodeId = messageEvent.getSourceNodeId();
//            Log.v(LOG_TAG, "Node ID of watch: " + nodeId);
//            showToast(messageEvent.getPath());

//            reply(WEAR_PATH);
//        }
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



