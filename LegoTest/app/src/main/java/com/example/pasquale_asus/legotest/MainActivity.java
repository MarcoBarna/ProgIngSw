package com.example.pasquale_asus.legotest;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;


public class MainActivity extends Form
{
    private BluetoothClient bluetoothClient1;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect;
    private String bluetoothValue;
    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        setContentView(R.layout.activity_main);
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
    }

    public void selectPairedBluetooth(){
        Intent intent = new Intent(this, BtPaired.class);
        startActivityForResult(intent, 0);
    }

    public void disconnectBluetooth(){
        //Insert what to do when Bluetooth gets disconnected
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textView = findViewById(R.id.textView);
        if (resultCode == RESULT_OK) {
            bluetoothValue = data.getStringExtra("bluetooth");
            //textView.setText(bluetoothValue.subSequence(0,17));
            if(bluetoothClient1.Connect("" + bluetoothValue.subSequence(0,17))){
                visibilityBtConnected();
                textView.setText("Connected");
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