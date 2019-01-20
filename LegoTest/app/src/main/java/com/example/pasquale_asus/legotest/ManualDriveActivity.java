package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3Sound;
import com.google.appinventor.components.runtime.Form;

public class ManualDriveActivity extends Form {
    private ImageButton up, down, left, right;
    private TextView textView;
    private TextView text_gyro;
    private int number_rotation = 0;
    private int tot_number_rotation = 0;
    private Ev3GyroSensor ev3GyroSensor;
    private Ev3Sound ev3Sound;
    private Ev3ColorSensor colorSensor;
    private Ev3Motors wheels, sensordirection;
    private Button buttonup, buttondown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void $define(){
        setContentView(R.layout.activity_manual_drive);
        textView = findViewById(R.id.textView);
        buttonup = findViewById(R.id.sensorup);
        buttondown = findViewById(R.id.sensordown);
        /*Instance of sensors*/
        ev3Sound = new Ev3Sound(this);
        colorSensor = new Ev3ColorSensor(this);
        ev3GyroSensor = new Ev3GyroSensor(this);
        wheels = new Ev3Motors(this);
        sensordirection = new Ev3Motors(this);
        /*end */
        connectPortSensors();
        connectSensorsToBluetooth();
        colorSensor.SetColorMode();
        ev3GyroSensor.SetRateMode();
        wheels.ResetTachoCount();
        text_gyro = findViewById(R.id.textgyro);

        buttondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensordirection.RotateIndefinitely(-30);
            }
        });
        buttonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensordirection.RotateIndefinitely(60);
            }
        });
        
        left = findViewById(R.id.left);
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        goLeft(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        wheels.Stop(true);
                        wheels.Stop(false);
                }
                return false;
            }
        });

        right = findViewById(R.id.right);
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        goRight(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        wheels.Stop(true);
                        wheels.Stop(false);
                }
                return false;
            }
        });

        /*Button UP (change id button)*/
        up = findViewById(R.id.up);
        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        goForward(view);
                        number_rotation = wheels.GetTachoCount() / 360;
                        tot_number_rotation += number_rotation;
                        text_gyro.setText("Number Rotation " + tot_number_rotation);
                        break;
                    case MotionEvent.ACTION_UP:
                        wheels.Stop(true);
                        wheels.Stop(false);
                        number_rotation = wheels.GetTachoCount() / 360;
                        tot_number_rotation += number_rotation;
                        text_gyro.setText("Number Rotation " + tot_number_rotation);
                        wheels.ResetTachoCount();
                }
                return false;
            }
        });


        /*Button DOWN (change id button)*/
        down = findViewById(R.id.down);
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        goBackward(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        wheels.Stop(false);
                        wheels.ResetTachoCount();
                }
                number_rotation = (-wheels.GetTachoCount() )/ 360;
                tot_number_rotation += number_rotation;
                text_gyro.setText("Number Rotation "+tot_number_rotation);
                return false;
            }
        });

    }
    public void goForward(View v){
        wheels.RotateSyncIndefinitely(200,0);
    }
    public void goBackward(View v){
        wheels.RotateSyncIndefinitely(-200,0);
    }
    public  void goLeft(View v){
        wheels.RotateSyncIndefinitely(100,-55);
    }
    public void goRight(View v){
        wheels.RotateSyncIndefinitely(100,55);
    }
    public void connectPortSensors(){
        wheels.MotorPorts("BC");
        ev3GyroSensor.SensorPort("2");
        colorSensor.SensorPort("1");
        sensordirection.MotorPorts("AD");
    }
    public void connectSensorsToBluetooth(){
        wheels.BluetoothClient(MainActivity.ev3.bluetoothClient);
        ev3Sound.BluetoothClient(MainActivity.ev3.bluetoothClient);
        ev3GyroSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);
        colorSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);
        sensordirection.BluetoothClient(MainActivity.ev3.bluetoothClient);
    }
    @Override
    public void onBackPressed() {
        wheels.Stop(true);
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
