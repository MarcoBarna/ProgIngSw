package com.example.pasquale_asus.legotest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.BluetoothConnectionBase;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.LegoMindstormsEv3Base;

import java.io.Serializable;


public class MainActivity extends Form implements Serializable
{
    public BluetoothClient bluetoothClient1;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect,
            manualMode;
    private Ev3Motors ev3Motors, leftMotor,rightMotor;
    public static BluetoothClient globalBluetoothClient1;

    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        setContentView(R.layout.activity_main);
        MainActivity.getActiveForm().BackgroundColor(COLOR_BLACK);
        bluetoothClient1 = new BluetoothClient(this);
        buttonBluetoothConnect = findViewById(R.id.buttonBluetoothConnect);
        buttonBluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPairedBluetooth();
            }
        });
        buttonBluetoothDisconnect = findViewById(R.id.buttonBluetoothDisconnect);
        buttonBluetoothDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectBluetooth();
            }
        });
        buttonBluetoothDisconnect.setVisibility(View.INVISIBLE);
        manualMode = findViewById(R.id.manual_button);
        manualMode.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  manualModeActivity();
              }
          }
        );
        buttonDisable(manualMode);
        ev3Motors = new Ev3Motors(this);
        rightMotor = new Ev3Motors(this);
        leftMotor = new Ev3Motors(this);
        ev3Motors.MotorPorts("BC");
        rightMotor.MotorPorts("C");
        leftMotor.MotorPorts("B");
    }
    private void buttonDisable(Button button){
        button.setEnabled(false);
        button.setBackgroundColor(COLOR_GRAY);
        button.setTextColor(COLOR_LTGRAY);
    }
    private void buttonActivate(Button button){
        button.setEnabled(true);
        button.setTextColor(COLOR_WHITE);
    }
    public void selectPairedBluetooth(){
        Intent intent = new Intent(this, BtPaired.class);
        startActivityForResult(intent, 0);
    }
    public void disconnectBluetooth(){
        //Insert what to do when Bluetooth gets disconnected
        bluetoothClient1.Disconnect();
        visibilityBtDisconnected();
    }
    public void manualModeActivity(){
        Intent intent = new Intent(this, ManualDriveActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textView = findViewById(R.id.textView);
        if (resultCode == RESULT_OK) {
            String userBluetoothDevice = data.getStringExtra("bluetooth");
            String macAddressToConnect = (userBluetoothDevice.subSequence(0,17)).toString();
            bluetoothClient1.Connect(macAddressToConnect);
            if(bluetoothClient1.IsConnected()){
                buttonActivate(manualMode);
                manualMode.setBackgroundColor(COLOR_RED);
                globalBluetoothClient1 = bluetoothClient1;
                visibilityBtConnected();
                textView.setText("Connected");
                BluetoothConnectionBase bs = bluetoothClient1;
                bs.IsConnected();
                textView.setText("connessione" + bs.IsConnected());
                ev3Motors.BluetoothClient(bluetoothClient1);
                rightMotor.BluetoothClient(bluetoothClient1);
                leftMotor.BluetoothClient(bluetoothClient1);
            }
            else{
                visibilityBtDisconnected();
                textView.setText("Disconnected");
            }
        }
        else
            textView.setText("CANCELED");
    }
    public void visibilityBtConnected(){
        buttonBluetoothConnect.setVisibility(View.INVISIBLE);
        buttonBluetoothDisconnect.setVisibility(View.VISIBLE);
    }
    public void visibilityBtDisconnected(){
        buttonBluetoothDisconnect.setVisibility(View.INVISIBLE);
        buttonBluetoothConnect.setVisibility(View.VISIBLE);
    }
}