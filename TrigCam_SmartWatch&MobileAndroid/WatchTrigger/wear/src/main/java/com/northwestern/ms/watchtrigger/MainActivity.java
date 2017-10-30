package com.northwestern.ms.watchtrigger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;

import android.content.Intent;

public class MainActivity extends WearableActivity {
    //implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    private TextView mTextView;
    public static final String WEARABLE_MAIN = "WearableMain";
    private GoogleApiClient mGoogleApiClient;
    private Node mNode;
    private static final String WEAR_PATH = "/from-wear";
    public String messages = "HELLO";
    private Button sm;
    private Button sm2;

    float flag=0;
    Intent intent;
    Intent intent2;
    Intent intent3;
    Intent intent4;
    Intent intent5;

    PowerManager pm;
    PowerManager.WakeLock wl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "start1", Toast.LENGTH_SHORT).show();
        setAmbientEnabled();
        sm = (Button)findViewById(R.id.btn);
        sm2 = (Button)findViewById(R.id.btn2);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        wl.acquire();
        Check_Permission();
    }

    public void service(View v)
    {
        Toast.makeText(this, "start1", Toast.LENGTH_SHORT).show();

        intent = new Intent(this, SensorService.class);

        startService(intent);

        if (flag==2) {
            intent2 = new Intent(this, SensorService2.class);
            stopService(intent2);
        }
        else if (flag==3)
            stopService(intent3);
        else if (flag==4)
            stopService(intent4);
        else if (flag==5)
            stopService(intent5);
        flag=1;
    }

    private View.OnClickListener stress5Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Thanks for your ranking(5)", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Your toast message",
                    Toast.LENGTH_LONG).show();
        }

    };
    public void button2(View v)
    {
        Toast.makeText(this, "start2", Toast.LENGTH_SHORT).show();
        if (flag==1)
        stopService(intent);
        else if (flag==3)
            stopService(intent3);
        else if (flag==4)
            stopService(intent4);
        else if (flag==5)
            stopService(intent5);

        intent2 = new Intent(this, SensorService2.class);
        startService(intent2);
        flag=2;

    }

    public void button4(View v)
    {
        Toast.makeText(this, "start3", Toast.LENGTH_SHORT).show();


        if (flag==1)
            stopService(intent);
        else if (flag==2)
            stopService(intent2);
        else if (flag==4)
            stopService(intent4);
        else if (flag==5)
            stopService(intent5);

        intent3 = new Intent(this, SensorService3.class);
        startService(intent3);

        flag=3;
    }


    public void button3(View v)
    {
        Toast.makeText(this, "start3", Toast.LENGTH_SHORT).show();


        if (flag==1)
            stopService(intent);
        else if (flag==2)
            stopService(intent2);
        else if (flag==3)
            stopService(intent3);
        else if (flag==5)
            stopService(intent5);

        intent4 = new Intent(this, SensorService4.class);
        startService(intent4);
        flag=4;
    }



    public void button5(View v)
    {
        Toast.makeText(this, "start3", Toast.LENGTH_SHORT).show();


        if (flag==1)
            stopService(intent);
        else if (flag==2)
            stopService(intent2);
        else if (flag==3)
            stopService(intent3);
        else if (flag==3)
            stopService(intent4);

        intent5 = new Intent(this, SensorService5.class);
        startService(intent5);
        flag=5;
    }

    @Override
    public void onEnterAmbient(Bundle b) {
        super.onEnterAmbient(b);
       // ambientType.setText("Ambient mode: ");
        Toast.makeText(this, "Ambient mode: ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        // Nothing to refresh here
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        //   ambientType.setText("Interactive mode: ");
        Toast.makeText(this, "Interactive mode: ", Toast.LENGTH_SHORT).show();
    }


    public void Check_Permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.MODIFY_AUDIO_SETTINGS))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.BODY_SENSORS))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.KILL_BACKGROUND_PROCESSES) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "WAS NOT INCLUDED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.KILL_BACKGROUND_PROCESSES))
                Log.d("PERMISSION", "ALLOWED");
            else
                Log.d("PERMISSION", "NOT ALLOWED");
        } else
            Log.d("PERMISSION", "WAS INCLUDED");
    }


}