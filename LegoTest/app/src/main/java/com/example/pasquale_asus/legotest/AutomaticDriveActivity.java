package com.example.pasquale_asus.legotest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AutomaticDriveActivity extends AppCompatActivity {
    private android.widget.Button firstaction, stopButton;
    private Ev3Motors motors;
    private Ev3UltrasonicSensor ultrasonicSensor;
    private boolean interruptAction;
    public static Thread readSensors;
    private TextView debug;
    Handler handler;
    private boolean handlerStop, threadStop;
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

                avoidObstacles();

//                r = new Runnable() {
//                    public void run() {
//                        debug.setText(ultrasonicSensor.GetDistance() + "");
//                        if(ultrasonicSensor.GetDistance() > 20){
//                            motors.RotateSyncIndefinitely(100, 0);
//                        }
//                        else {
//                            motors.Stop(false);
//                            motors.RotateSyncIndefinitely(-50,90);
//                            //motors.RotateSyncInTachoCounts(-50, 2, 90, false);
//                        }
//                        if (handlerStop == false)
//                            handler.postDelayed(this, 200);
//                    }
//                };
//                handlerStop = false;
//                handler.postDelayed(r, 500);
            }


        });
        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(r);
                handlerStop = true;
                threadStop = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        motors.Stop(false);
                    }
                }, 500);
            }
        });

        Utility.setupToolbar(this, R.id.automaticDriveToolbar);
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
        motors = MainActivity.ev3.outputs.leftMotors;
        motors.MotorPorts("BC");
        motors.BluetoothClient(MainActivity.ev3.bluetoothClient);
        ultrasonicSensor = MainActivity.ev3.inputs.ultrasonicSensor;
        ultrasonicSensor.SensorPort("3");
        ultrasonicSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);
    }

    public void avoidObstacles(){
        threadStop = false;
        Thread algorithm = new Thread() {
            Handler handler = MainActivity.ev3.handler;
            @Override
            public void run() {
                double distance = 0;
                try {
                    while(!threadStop){
                        distance = MainActivity.ev3.inputs.readProximitySensor().get();
                        if (distance > 0 && distance < 20) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    motors.RotateSyncIndefinitely(-50, 100);
                                }
                            });
                        }
                        else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    motors.RotateSyncIndefinitely(50, 0);
                                }
                            });
                        }

                        sleep(500);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        algorithm.start();
    }




}
