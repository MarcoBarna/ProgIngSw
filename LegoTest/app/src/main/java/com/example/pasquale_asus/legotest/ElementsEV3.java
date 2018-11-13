package com.example.pasquale_asus.legotest;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;

//this class contains the elements for initialize the ev3

public class ElementsEV3 extends Form  {
    public Ev3Motors leftMotors;
    public Ev3Motors rightMotors;
    public BluetoothClient bluetoothClient;

    public ElementsEV3(){
        $define();
    }
    public void $define(){
        leftMotors = new Ev3Motors(this);
        rightMotors = new Ev3Motors(this);
        bluetoothClient = new BluetoothClient(this);
    }
}
