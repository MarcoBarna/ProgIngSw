package com.example.pasquale_asus.legotest;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;
import com.google.appinventor.components.runtime.Form;

import java.nio.charset.MalformedInputException;
import java.text.Normalizer;

public class AutomaticDriveActivity extends AppCompatActivity {
    private android.widget.Button firstaction, stopButton;
    private Ev3Motors motors;
    private Ev3UltrasonicSensor ultrasonicSensor;
    private boolean interruptAction;
    public static Thread readSensors;
    private TextView debug;
    Handler handler;
    private boolean handlerStop;
    Runnable r = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_drive);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        initializeMotors();
        handler = new Handler();
        debug = findViewById(R.id.textView7);
        debug.setText("Non cliccato");
        firstaction = findViewById(R.id.firstaction);
        firstaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interruptAction = false;
                debug.setText("Cliccato");
                handlerStop = true;

                r = new Runnable() {
                    public void run() {
                        debug.setText(ultrasonicSensor.GetDistance() + "");
                        if(ultrasonicSensor.GetDistance() > 20){
                            motors.RotateSyncIndefinitely(100, 0);
                        }
                        else {
                            motors.Stop(false);
                            motors.RotateSyncIndefinitely(-50,90);
                            //motors.RotateSyncInTachoCounts(-50, 2, 90, false);
                        }
                        if (handlerStop == false)
                            handler.postDelayed(this, 200);
                    }
                };
                handlerStop = false;
                handler.postDelayed(r, 500);

            }


        });
        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(r);
                handlerStop = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        motors.Stop(false);
                    }
                }, 500);
            }
        });

        setupToolbar();
    }

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.automaticDriveToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        handlerStop = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.Stop(false);
            }
        }, 500);
        super.onBackPressed();
    }

    public void initializeMotors(){
        ElementsEV3 elementsEV3 = new ElementsEV3();
        motors = elementsEV3.leftMotors;
        motors.MotorPorts("BC");
        motors.BluetoothClient(MainActivity.bluetoothClient);
        ultrasonicSensor = elementsEV3.ultrasonicSensor;
        ultrasonicSensor.SensorPort("3");
        ultrasonicSensor.BluetoothClient(MainActivity.bluetoothClient);
    }






}
