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

    @Override
    public void $define(){
        setContentView(R.layout.activity_automatic_drive);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        initializeMotors();
        firstaction = findViewById(R.id.firstaction);
        firstaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstActionAlgorithm();
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
        boolean flag = true;
        motors.RotateIndefinitely(50);
        while(flag) {
            if (ultrasonicSensor.GetDistance() < 15) {
                motors.Stop(true);
                flag = false;
            }
            try {
                wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
