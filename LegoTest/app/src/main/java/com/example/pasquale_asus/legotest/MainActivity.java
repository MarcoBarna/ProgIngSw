package com.example.pasquale_asus.legotest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;


public class MainActivity extends AppCompatActivity
{
    public static EV3 ev3;
    private Button buttonBluetoothConnect, buttonBluetoothDisconnect;
    private ImageButton manualMode, automaticmode, helpmode, settingsmode;
    public TextView statusBattery, osfirmware;
    public static String MacAddress;


    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ev3 = EV3.getEV3();
        getWindow().setWindowAnimations(R.anim.fadein);
        statusBattery = findViewById(R.id.statusBattery);

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
                disconnectBluetooth(ev3.bluetoothClient);
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
        Intent intent = new Intent(this, TestMotorsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
            MacAddress = macAddressToConnect;
            ev3.bluetoothClient.Connect(macAddressToConnect);
            if(ev3.bluetoothClient.IsConnected()){
                visibilityBtConnected();
                activeUserSections();
                //statusBattery.setText("Battery Level "+(int)(infoBrick.GetBatteryCurrent()*100) +"%");
                if((int)(ev3.extra.commands.GetBatteryCurrent()*100) < 20)
                    //  statusBattery.setTextColor(Color.RED);
                    osfirmware.setText(ev3.extra.commands.GetHardwareVersion());
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
