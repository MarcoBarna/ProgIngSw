package com.example.pasquale_asus.legotest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Commands;

import java.util.Set;



public class MainActivity extends AppCompatActivity
{
    public static String motor1_port, motor2_port, color_sensor_port, gyro_sensor_port, touch_sensor_port, proximity_sensor_port = null;
    public static BluetoothClient bluetoothClient;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect;
    private ImageButton manualMode, automaticmode, helpmode, settingsmode;
    public Ev3Commands infoBrick;
    public TextView statusBattery, osfirmware;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setWindowAnimations(R.anim.fadein);
        initializeLibraryObject();
        buttonBluetoothConnect = findViewById(R.id.buttonBluetoothConnect);
        registerForContextMenu(buttonBluetoothConnect);
        buttonBluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPairedBluetooth();
                /*showBtMenu(view);*/
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
        helpmode = findViewById(R.id.help_button);
        helpmode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                helpModeActivity();
            }
        } );
        settingsmode = findViewById(R.id.settings_button);
        settingsmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsmodeActivity();
            }
        });

        statusBattery = findViewById(R.id.statusBattery);
        osfirmware = findViewById(R.id.osfirmware);
        //disableUserSections();
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(buttonBluetoothConnect);
        super.onDestroy();
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
        //TestMotorsActivity
        Intent intent = new Intent(this, TestSensorsActivity.class);
        startActivity(intent);
    }
    public void automaticModeActivity(){
        Intent intent = new Intent(this, AutomaticDriveActivity.class);
        startActivityForResult(intent, 0);
    }
    public void helpModeActivity(){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }
    public void settingsmodeActivity(){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    public void activeUserSections(){
        manualMode.setEnabled(true);
        automaticmode.setEnabled(true);
    }
    public void disableUserSections(){
        manualMode.setEnabled(false);
        automaticmode.setEnabled(false);
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
                activeUserSections();
                infoBrick.BluetoothClient(bluetoothClient);
                statusBattery.setText("Battery Level "+(int)(infoBrick.GetBatteryCurrent()*100) +"%");
                if((int)(infoBrick.GetBatteryCurrent()*100) < 20)
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
        disableUserSections();
    }
    private void initializeLibraryObject(){
        ElementsEV3 libElements = new ElementsEV3();
        if (bluetoothClient == null)
            this.bluetoothClient = libElements.bluetoothClient;
        this.infoBrick = libElements.commands;
        motor1_port = (motor1_port == null) ? "C" : motor1_port;
        gyro_sensor_port = (gyro_sensor_port == null) ? "3" : gyro_sensor_port;
        color_sensor_port = (color_sensor_port == null) ? "2" : color_sensor_port;
        motor2_port = (motor2_port == null) ? "B" : motor2_port;
        touch_sensor_port = (touch_sensor_port == null) ? "1" : touch_sensor_port;
        proximity_sensor_port = (proximity_sensor_port == null) ? "4" : proximity_sensor_port;
    }
    /*
    public void showBtMenu(View v){
        v.showContextMenu();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Bluetooth Device");
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        for (BluetoothDevice device : pairedDevices)
            menu.add(device.getName());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String macAddressToConnect = "";
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        for (BluetoothDevice pairedDevice : pairedDevices){
            if(item.getTitle().equals(pairedDevice.getName())){
                macAddressToConnect = pairedDevice.getAddress();
            }
        }
        bluetoothClient.Connect(macAddressToConnect);
        if(bluetoothClient.IsConnected()){
            visibilityBtConnected();
            activeUserSections();
            infoBrick.BluetoothClient(bluetoothClient);
            statusBattery.setText("Battery Level "+(int)(infoBrick.GetBatteryCurrent()*100) +"%");
            if((int)(infoBrick.GetBatteryCurrent()*100) < 20)
                statusBattery.setTextColor(Color.RED);
            osfirmware.setText(infoBrick.GetHardwareVersion());
            Toast.makeText(this, "Bluetooth Connected", Toast.LENGTH_LONG);
        }
        else{
            visibilityBtDisconnected();
            Toast.makeText(this, "Bluetooth not Connected", Toast.LENGTH_LONG);
        }
        return true;
    }
    */
}
