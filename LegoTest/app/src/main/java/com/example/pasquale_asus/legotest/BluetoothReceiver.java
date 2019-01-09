package com.example.pasquale_asus.legotest;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.LinkedList;

public abstract class BluetoothReceiver extends BroadcastReceiver {
//    Collection<AsyncTask>   onBluetoothON,
//                            onBluetoothOFF,
//                            onBluetoothTurningON,
//                            onBluetoothTurningOFF;
//
//    BluetoothReceiver(){
//        onBluetoothON = new LinkedList<AsyncTask>();
//        onBluetoothOFF = new LinkedList<AsyncTask>();
//        onBluetoothTurningON = new LinkedList<AsyncTask>();
//        onBluetoothTurningOFF = new LinkedList<AsyncTask>();
//    }
//
//    public void addOnBluetoothON(AsyncTask asyncTask){
//        onBluetoothON.add(asyncTask);
//    }
//
//    public void addOnBluetoothOFF(AsyncTask asyncTask){
//        onBluetoothOFF.add(asyncTask);
//    }
//
//    public void addOnBluetoothTurningON(AsyncTask asyncTask){
//        onBluetoothTurningON.add(asyncTask);
//    }
//
//    public void addOnBluetoothTurningOFF(AsyncTask asyncTask){
//        onBluetoothTurningOFF.add(asyncTask);
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Estraggo lo stato attuale da intent
        int currentState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
        //Confronto lo stato del Bluetooth con i valori di libreria e di conseguenza cambio il testo nella textView
        switch (currentState) {
            case BluetoothAdapter.STATE_ON: //Acceso
                stateOn();
                break;
            case BluetoothAdapter.STATE_OFF: //Spento
                stateOff();
                break;
            case BluetoothAdapter.STATE_TURNING_ON: //In accensione
                stateTurningOn();
                break;
            case BluetoothAdapter.STATE_TURNING_OFF: //In spegnimento
                stateTurningOff();
                break;
            default:
                break;
        }
    }
    protected abstract void stateOn();

    protected abstract void stateOff();

    protected abstract void stateTurningOn();

    protected abstract void stateTurningOff();
}
