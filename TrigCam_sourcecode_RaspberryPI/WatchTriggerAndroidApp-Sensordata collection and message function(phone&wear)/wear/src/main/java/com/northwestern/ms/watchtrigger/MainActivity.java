package com.northwestern.ms.watchtrigger;



import android.app.Activity;
import android.os.Bundle;

import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;

import android.content.Intent;

public class MainActivity extends Activity {
    //implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    private TextView mTextView;
    public static final String WEARABLE_MAIN = "WearableMain";
    private GoogleApiClient mGoogleApiClient;
    private Node mNode;
    private static final String WEAR_PATH = "/from-wear";
    public String messages = "HELLO";
    private Button sm;
    private Button sm2;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "start1", Toast.LENGTH_SHORT).show();

        sm = (Button)findViewById(R.id.btn);
        sm2 = (Button)findViewById(R.id.btn2);
//        if(btn5.getText() == "START") {
//            btn5.setText("STOP");
//            btn6.setEnabled(false);
//        }
//        else {
//            intent = new Intent(this, SensorService.class);
//            stopService(intent);
//            btn5.setText("START");
//            btn6.setEnabled(true);
//        }


      /*  sm = (Button) this.findViewById(R.id.button3);
        if (sm != null) {
            //  sendMesssage();
            sm.setOnClickListener(stress5Listener);
        }*/


    }

    public void service(View v)
    {
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        intent = new Intent(this, SensorService.class);

        startService(intent);
    }

    private View.OnClickListener stress5Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //  DataHolder.getInstance().setStressRank(5);
            Toast.makeText(MainActivity.this, "Thanks for your ranking(5)", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Your toast message",
                    Toast.LENGTH_LONG).show();


        }

    };

}