package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private Button set_ports_button, set_language_system;
    private Switch switch_bluetooth;
    private ImageView closePopup;
    boolean isSpinnerTouched = false;
    BluetoothReceiver bluetoothReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        set_ports_button = findViewById(R.id.set_ports_button);
        set_ports_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonsetPortsClick(view);
            }
        });
        set_language_system = findViewById(R.id.select_language);
        set_language_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonsetLanguageClick(view);
            }
        });

        
        switch_bluetooth = findViewById(R.id.switch_bluetooth);
        switch_bluetooth.setChecked((BluetoothAdapter.getDefaultAdapter() != null) && BluetoothAdapter.getDefaultAdapter().isEnabled());
        switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter == null)
                    Toast.makeText(getApplicationContext(), R.string.bluetooth_not_present, Toast.LENGTH_LONG).show();
                else{
                    boolean isEnabled = bluetoothAdapter.isEnabled();
                    if (isChecked && !isEnabled) {
                        switch_bluetooth.setEnabled(false);
                        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
                        //Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
                    }
                    else if(!isChecked && isEnabled){
                        switch_bluetooth.setEnabled(false);
                        bluetoothAdapter.disable();
                        //Toast.makeText(getApplicationContext(), "Bluetooth disabled", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Sets up the Toolbar
        Utility.setupToolbar(this, R.id.settingsToolbar);

        //Prepares the BluetoothReceiver (BroadcastReceiver) to receive changes in the Bluetooth states
        bluetoothReceiver = new BluetoothReceiver() {
            @Override
            protected void stateOn() {
                switch_bluetooth.setChecked(true);
                switch_bluetooth.setEnabled(true);
                Toast.makeText(getApplicationContext(), R.string.bluetooth_enabled, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void stateOff() {
                switch_bluetooth.setChecked(false);
                switch_bluetooth.setEnabled(true);
                Toast.makeText(getApplicationContext(), R.string.bluetooth_disabled, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void stateTurningOn() {
                switch_bluetooth.setEnabled(false);
                Toast.makeText(getApplicationContext(), R.string.enabling_bluetooth, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void stateTurningOff() {
                switch_bluetooth.setEnabled(false);
                Toast.makeText(getApplicationContext(), R.string.disabling_bluetooth, Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetoothFilter.addAction(BluetoothAdapter.EXTRA_STATE);
        registerReceiver(bluetoothReceiver, bluetoothFilter);
    }

    private void onButtonsetLanguageClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_set_language);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RadioButton radioButtonIT = dialog.findViewById(R.id.language_ita);
        RadioButton radioButtonEN = dialog.findViewById(R.id.language_en);

        radioButtonIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("it");
                dialog.dismiss();
                recreate();
            }
        });
        radioButtonEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("en");
                dialog.dismiss();
                recreate();
            }
        });
        switch (Locale.getDefault().getLanguage()){
            case "en":
                radioButtonEN.setChecked(true);
                break;
            case "it":
                radioButtonIT.setChecked(true);
                break;
            default:
                break;
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void setLanguage(String language){
        String languageToLoad  = language;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        SettingsActivity.this.getResources().updateConfiguration(config,SettingsActivity.this.getResources().getDisplayMetrics());
        Toast.makeText(getApplicationContext(), R.string.language_set, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    public void onButtonsetPortsClick(View v){
        Utility.onButtonsetPortsClick(this, v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK)
                switch_bluetooth.setEnabled(true);
            else
                if (resultCode == RESULT_CANCELED) {
                    switch_bluetooth.setChecked(false);
                    switch_bluetooth.setEnabled(true);
                }
        }
    }
}
