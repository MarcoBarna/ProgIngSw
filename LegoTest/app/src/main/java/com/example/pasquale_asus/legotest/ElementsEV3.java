package com.example.pasquale_asus.legotest;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3Commands;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3TouchSensor;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;
import com.google.appinventor.components.runtime.Form;

//this class contains the elements for initialize the ev3

public class ElementsEV3 extends Form  {
    public Ev3Motors leftMotors;
    public Ev3Motors rightMotors;
    public BluetoothClient bluetoothClient;
    public Ev3Commands commands;
    public Ev3TouchSensor touchSensor;
    public Ev3ColorSensor colorSensor;
    public Ev3UltrasonicSensor ultrasonicSensor;
    public Ev3GyroSensor gyroSensor;

    public ElementsEV3(){
        $define();
    }
    public void $define(){
        commands = new Ev3Commands(this);
        leftMotors = new Ev3Motors(this);
        rightMotors = new Ev3Motors(this);
        touchSensor = new Ev3TouchSensor(this);
        colorSensor = new Ev3ColorSensor(this);
        ultrasonicSensor = new Ev3UltrasonicSensor(this);
        gyroSensor = new Ev3GyroSensor(this);
        bluetoothClient = new BluetoothClient(this);
    }
}
