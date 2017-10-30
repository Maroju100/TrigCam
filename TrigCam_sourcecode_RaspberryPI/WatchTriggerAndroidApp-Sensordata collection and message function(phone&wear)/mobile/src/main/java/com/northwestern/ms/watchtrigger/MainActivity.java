package com.northwestern.ms.watchtrigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;

//public class  extends AppCompatActivity {
import java.util.concurrent.TimeUnit;

import static android.support.v4.app.NotificationCompat.*;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this,WearListCallListenerService.class);
        this.startService(intent);

//        Intent i= new Intent(this, BackgroundMusic.class);
//        this.startService(i);
    }
}