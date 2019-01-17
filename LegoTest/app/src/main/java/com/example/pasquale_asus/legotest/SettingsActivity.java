package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.pasquale_asus.legotest.R.id.spinner_motor1;

public class SettingsActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private Button set_ports_button;
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
        switch_bluetooth = findViewById(R.id.switch_bluetooth);
        switch_bluetooth.setChecked((BluetoothAdapter.getDefaultAdapter() == null) ? false : BluetoothAdapter.getDefaultAdapter().isEnabled());
        switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter == null)
                    Toast.makeText(getApplicationContext(), "Bluetooth is not present", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void stateOff() {
                switch_bluetooth.setChecked(false);
                switch_bluetooth.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Bluetooth disabled", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void stateTurningOn() {
                switch_bluetooth.setEnabled(false);
                Toast.makeText(getApplicationContext(), "enabling Bluetooth", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void stateTurningOff() {
                switch_bluetooth.setEnabled(false);
                Toast.makeText(getApplicationContext(), "disabling Bluetooth", Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetoothFilter.addAction(BluetoothAdapter.EXTRA_STATE);
        registerReceiver(bluetoothReceiver, bluetoothFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    public void onButtonsetPortsClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_set_ports);

        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSpinnerTouched = false;
                dialog.dismiss();
            }
        });

        Spinner motor1 = dialog.findViewById(spinner_motor1);
        Spinner motor2 = dialog.findViewById(R.id.spinner_motor2);
        Spinner motor3 = dialog.findViewById(R.id.spinner_motor3);
        Spinner color_sensor = dialog.findViewById(R.id.spinner_color_sensor);
        Spinner gyro_sensor = dialog.findViewById(R.id.spinner_gyro_sensor);
        Spinner touch_sensor = dialog.findViewById(R.id.spinner_touch_sensor);
        Spinner proximity_sensor = dialog.findViewById(R.id.spinner_proximity_sensor);

        String[] items_numbers = new String[]{"1", "2", "3", "4"};
        String[] items_letters = new String[]{"A", "B", "C", "D"};

        String items_string_numbers = "";
        for(String item: items_numbers)
            items_string_numbers += item;
        items_string_numbers = items_string_numbers.replace(",","");

        String items_string_letters = "";
        for(String item: items_letters)
            items_string_letters += item;
        items_string_letters = items_string_letters.replace(",","");

        final ArrayAdapter<String> adapter_numbers = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items_numbers);
        adapter_numbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> adapter_letters = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items_letters);
        adapter_letters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        motor1.setAdapter(adapter_letters);
        motor2.setAdapter(adapter_letters);
        motor3.setAdapter(adapter_letters);
        color_sensor.setAdapter(adapter_numbers);
        gyro_sensor.setAdapter(adapter_numbers);
        touch_sensor.setAdapter(adapter_numbers);
        proximity_sensor.setAdapter(adapter_numbers);

        motor1.setSelection(items_string_letters.indexOf(MainActivity.ev3.ports.motor1_port));
        motor2.setSelection(items_string_letters.indexOf(MainActivity.ev3.ports.motor2_port));
        motor3.setSelection(items_string_letters.indexOf(MainActivity.ev3.ports.motor3_port));
        color_sensor.setSelection(items_string_numbers.indexOf(MainActivity.ev3.ports.color_sensor_port));
        gyro_sensor.setSelection(items_string_numbers.indexOf(MainActivity.ev3.ports.gyro_sensor_port));
        touch_sensor.setSelection(items_string_numbers.indexOf(MainActivity.ev3.ports.touch_sensor_port));
        proximity_sensor.setSelection(items_string_numbers.indexOf(MainActivity.ev3.ports.proximity_sensor_port));

        motor1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.motor1_port != item) {
                    MainActivity.ev3.ports.motor1_port = item;
                    Toast.makeText(getApplicationContext(), "Changed Motor1 port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        motor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.motor2_port != item) {
                    MainActivity.ev3.ports.motor2_port = item;
                    Toast.makeText(getApplicationContext(), "Changed Motor2 port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        motor3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.motor3_port != item) {
                    MainActivity.ev3.ports.motor3_port = item;
                    Toast.makeText(getApplicationContext(), "Changed Motor3 port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        color_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.color_sensor_port != item) {
                    MainActivity.ev3.ports.color_sensor_port = item;
                    Toast.makeText(getApplicationContext(), "Changed ColorSensor port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        gyro_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.gyro_sensor_port != item) {
                    MainActivity.ev3.ports.gyro_sensor_port = item;
                    Toast.makeText(getApplicationContext(), "Changed GyroSensor port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        touch_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.touch_sensor_port != item) {
                    MainActivity.ev3.ports.touch_sensor_port = item;
                    Toast.makeText(getApplicationContext(), "Changed TouchSensor port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        proximity_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.ev3.ports.proximity_sensor_port != item) {
                    MainActivity.ev3.ports.proximity_sensor_port = item;
                    Toast.makeText(getApplicationContext(), "Changed Proximity port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
