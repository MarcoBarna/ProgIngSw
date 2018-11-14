package com.example.pasquale_asus.legotest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.BluetoothConnectionBase;
import com.google.appinventor.components.runtime.Ev3Commands;

public class MainActivity extends AppCompatActivity
{
    public BluetoothClient bluetoothClient1;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect;
    private ImageButton manualMode;
    public static BluetoothClient globalBluetoothClient1;
    public Ev3Commands commands;
    public TextView statusLego;

    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
        setContentView(R.layout.activity_main);
        //MainActivity.getActiveForm().BackgroundColor(COLOR_BLACK);
       // MainActivity.getActiveForm().BackgroundImage();
        ElementsEV3 libElements = new ElementsEV3();
        bluetoothClient1 = libElements.bluetoothClient;
        commands = libElements.commands;
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
        statusLego = findViewById(R.id.textView3);
    }
    private void buttonDisable(ImageButton button){
        //button.setEnabled(false);
        //button.setBackgroundColor(COLOR_GRAY);
       // button.setTextColor(COLOR_DKGRAY);
    }
    private void buttonActivate(ImageButton button){
       // button.setEnabled(true);
       // button.setTextColor(COLOR_WHITE);
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

        if (resultCode == RESULT_OK) {
            String userBluetoothDevice = data.getStringExtra("bluetooth");
            String macAddressToConnect = (userBluetoothDevice.subSequence(0,17)).toString();
            bluetoothClient1.Connect(macAddressToConnect);
            if(bluetoothClient1.IsConnected()){
                buttonActivate(manualMode);
                globalBluetoothClient1 = bluetoothClient1;
                visibilityBtConnected();
                BluetoothConnectionBase bs = bluetoothClient1;
                bs.IsConnected();
                commands.BluetoothClient(bluetoothClient1);
                statusLego.setText("Battery Level " + commands.GetBatteryCurrent());
            }
            else{
                visibilityBtDisconnected();
            }
        }
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