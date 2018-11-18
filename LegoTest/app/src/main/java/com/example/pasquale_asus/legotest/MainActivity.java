package com.example.pasquale_asus.legotest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
    public static BluetoothClient bluetoothClient;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect;
    private ImageButton manualMode, automaticmode;
    public Ev3Commands infoBrick;
    public TextView statusBattery, osfirmware;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setWindowAnimations(R.anim.fadein);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        initializeLibraryObject();

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
                disconnectBluetooth(bluetoothClient);
            }
        });
        buttonBluetoothDisconnect.setVisibility(View.INVISIBLE);
        manualMode = findViewById(R.id.manual_button);
        manualMode.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  manualModeActivity();
              }
          });
        automaticmode = findViewById(R.id.automaticmode);
        automaticmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                automaticModeActivity();
            }
        });
        statusBattery = findViewById(R.id.statusBattery);
        osfirmware = findViewById(R.id.osfirmware);
    }

    public void selectPairedBluetooth(){
        Intent intent = new Intent(this, BtPaired.class);
        startActivityForResult(intent, 0);
    }
    public void disconnectBluetooth(BluetoothClient bluetoothClientToDisconnect){
        bluetoothClientToDisconnect.Disconnect();
        visibilityBtDisconnected();
    }
    public void manualModeActivity(){
        Intent intent = new Intent(this, JoystickActivity.class);
        startActivityForResult(intent, 0);
    }
    public void automaticModeActivity(){
        Intent intent = new Intent(this, AutomaticDriveActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String userBluetoothDevice = data.getStringExtra("bluetooth");
            String macAddressToConnect = (userBluetoothDevice.subSequence(0,17)).toString();
            bluetoothClient.Connect(macAddressToConnect);
            if(bluetoothClient.IsConnected()){
                visibilityBtConnected();
                BluetoothConnectionBase bs = bluetoothClient;
                infoBrick.BluetoothClient(bluetoothClient);
                statusBattery.setText("Battery Level "+(int)infoBrick.GetBatteryVoltage() +"%");
                if((int)infoBrick.GetBatteryVoltage() < 20)
                    statusBattery.setTextColor(Color.RED);
                osfirmware.setText(infoBrick.GetHardwareVersion());
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
    private void initializeLibraryObject(){
        ElementsEV3 libElements = new ElementsEV3();
        this.bluetoothClient = libElements.bluetoothClient;
        this.infoBrick = libElements.commands;
    }
}
