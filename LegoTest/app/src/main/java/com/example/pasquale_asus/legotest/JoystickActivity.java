package com.example.pasquale_asus.legotest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.appinventor.components.runtime.Ev3Motors;


public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener
{
    private Ev3Motors motors;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
        motors = MainActivity.ev3.outputs.rightMotors;
        motors.BluetoothClient(MainActivity.ev3.bluetoothClient);
        motors.MotorPorts("BC");
        JoystickView joystick = new JoystickView(this);
//      setContentView(R.layout.activity_main);
        setContentView(joystick);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {

        if (xPercent == 0 && yPercent == 0)
            motors.Stop(true);
        else
            motors.RotateSyncIndefinitely((int)(-yPercent*100), (int)(xPercent*100));
        Log.d("Main Method", "X percent: " + xPercent + " Y percent: " + yPercent);
    }
}