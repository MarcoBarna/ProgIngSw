package com.example.pasquale_asus.legotest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;
import com.google.appinventor.components.runtime.Form;

import java.nio.charset.MalformedInputException;
import java.text.Normalizer;

public class AutomaticDriveActivity extends Form {
    private android.widget.Button firstaction;
    private Ev3Motors motors;
    private Ev3UltrasonicSensor ultrasonicSensor;
    private boolean interruptAction;

    @Override
    public void $define(){
        setContentView(R.layout.activity_automatic_drive);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        initializeMotors();
        firstaction = findViewById(R.id.firstaction);
        firstaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interruptAction = false;
                new Thread(){
                    @Override
                    public void run() {
                        firstActionAlgorithm();
                    }
                }.start();
            }
        });


    }
    public void initializeMotors(){
        motors = new Ev3Motors(this);
        motors.MotorPorts("BC");
        motors.BluetoothClient(MainActivity.bluetoothClient);
        ultrasonicSensor = new Ev3UltrasonicSensor(this);
        ultrasonicSensor.SensorPort(MainActivity.gyro_sensor_port);
        ultrasonicSensor.BluetoothClient(MainActivity.bluetoothClient);
    }
    synchronized public void firstActionAlgorithm(){
        final int OBSTACLE_DISTANCE = 20;
        try {
            while(interruptAction == false){
                motors.RotateSyncIndefinitely(50, 0);
                if (ultrasonicSensor.GetDistance() < 20 && ultrasonicSensor.GetDistance() > 3) {
                    motors.Stop(false);
                    motors.RotateSyncInDuration(-50, 400, -90, false);
                    wait(500);
                }
                wait(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
