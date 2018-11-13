package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;

public class ManualDriveActivity extends Form {
    private Ev3Motors bMotor;
    private Ev3Motors cMotor;
    private Ev3Motors tempBCMotors;
    private ImageButton up, down;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void $define(){
        setContentView(R.layout.activity_manual_drive);
        bMotor = new Ev3Motors(this);
        cMotor = new Ev3Motors(this);
        tempBCMotors = new Ev3Motors(this);
        bMotor.MotorPorts("B");
        cMotor.MotorPorts("C");
        tempBCMotors.MotorPorts("BC");
        tempBCMotors.BluetoothClient(MainActivity.globalBluetoothClient1);
        bMotor.BluetoothClient(MainActivity.globalBluetoothClient1);
        cMotor.BluetoothClient(MainActivity.globalBluetoothClient1);

        /*Button UP (change id button)*/
        up = findViewById(R.id.imageButton);
        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        goForward(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        //bMotor.Stop(false);
                        //cMotor.Stop(false);
                        tempBCMotors.Stop(true);
                        tempBCMotors.Stop(false);
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
                        break;
                    case MotionEvent.ACTION_UP:
//                        bMotor.Stop(false);
//                        cMotor.Stop(false);
                          tempBCMotors.Stop(true);
                          tempBCMotors.Stop(false);
                }
                return false;
            }
        });
    }
    public void goForward(View v){
        //bMotor.RotateIndefinitely(100);
        //cMotor.RotateIndefinitely(100);
        //tempBCMotors.RotateIndefinitely(100);
        tempBCMotors.RotateSyncIndefinitely(200,0);
    }
    public void goBackward(View v){
      //  bMotor.RotateIndefinitely(-100);
     //   cMotor.RotateIndefinitely(-100);
    //    tempBCMotors.RotateIndefinitely(-100);
        tempBCMotors.RotateSyncIndefinitely(-200,0);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
