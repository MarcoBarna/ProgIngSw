package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3Commands;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3Sound;
import com.google.appinventor.components.runtime.Ev3UI;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.GyroscopeSensor;
import com.google.appinventor.components.runtime.LegoMindstormsEv3Base;
import com.google.appinventor.components.runtime.LegoMindstormsEv3Sensor;
import com.google.appinventor.components.runtime.LegoMindstormsNxtBase;
import com.google.appinventor.components.runtime.SpeechRecognizer;
import com.google.appinventor.components.runtime.TextToSpeech;
import com.google.appinventor.components.runtime.util.Ev3BinaryParser;
import com.google.appinventor.components.runtime.util.Ev3Constants;

public class ManualDriveActivity extends Form {
    private Ev3Motors wheels;
    private ImageButton up, down, left, right;
    private Ev3Sound ev3Sound;
    private Ev3ColorSensor colorSensor;
    private TextView textView;
    private Ev3GyroSensor ev3GyroSensor;
    private TextView text_gyro;
    private int number_rotation = 0;
    private int tot_number_rotation = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void $define(){
        setContentView(R.layout.activity_manual_drive);
        textView = findViewById(R.id.textView);

        /*Declaration elements*/
        ev3Sound = new Ev3Sound(this);
        colorSensor = new Ev3ColorSensor(this);
        ev3GyroSensor = new Ev3GyroSensor(this);
        wheels = new Ev3Motors(this);
        connectPortElements();
        connectElementsToBluetooth();
        colorSensor.SetColorMode();
        ev3GyroSensor.SetRateMode();
        wheels.ResetTachoCount();
        text_gyro = findViewById(R.id.textView5);

        left = findViewById(R.id.imageButton3);
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

        right = findViewById(R.id.imageButton4);
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
        up = findViewById(R.id.imageButton);
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
        down = findViewById(R.id.imageButton2);
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        goBackward(view);
                        ev3Sound.PlayTone(20,1000,200);
                        number_rotation = (-wheels.GetTachoCount() )/ 360;
                        tot_number_rotation += number_rotation;
                        text_gyro.setText("Number Rotation "+tot_number_rotation);
                        break;
                    case MotionEvent.ACTION_UP:
                        wheels.Stop(false);
                        number_rotation = (-wheels.GetTachoCount() )/ 360;
                        tot_number_rotation += number_rotation;
                        text_gyro.setText("Number Rotation "+tot_number_rotation);
                        wheels.ResetTachoCount();
                }
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
    public void connectPortElements(){
        wheels.MotorPorts("BC");
        ev3GyroSensor.SensorPort("2");
        colorSensor.SensorPort("1");
    }
    public void connectElementsToBluetooth(){
        wheels.BluetoothClient(MainActivity.bluetoothClient);
        ev3Sound.BluetoothClient(MainActivity.bluetoothClient);
        ev3GyroSensor.BluetoothClient(MainActivity.bluetoothClient);
        colorSensor.BluetoothClient(MainActivity.bluetoothClient);
    }
    @Override
    public void onBackPressed() {
        wheels.Stop(true);
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
