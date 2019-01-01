package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;
import java.util.Arrays;

import static com.example.pasquale_asus.legotest.R.id.spinner_motor1;

public class SettingsActivity extends AppCompatActivity {
    private Button set_ports_button;
    private Switch switch_bluetooth;
    private ImageView closePopup;
    boolean isSpinnerTouched = false;

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
                        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intentBtEnabled, 1);
                        Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
                    }
                    else if(!isChecked && isEnabled){
                        bluetoothAdapter.disable();
                        Toast.makeText(getApplicationContext(), "Bluetooth disabled", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
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
        Spinner color_sensor = dialog.findViewById(R.id.spinner_color_sensor);
        Spinner gyro_sensor = dialog.findViewById(R.id.spinner_gyro_sensor);
        Spinner touch_sensor = dialog.findViewById(R.id.spinner_touch_sensor);

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
        color_sensor.setAdapter(adapter_numbers);
        gyro_sensor.setAdapter(adapter_numbers);
        touch_sensor.setAdapter(adapter_numbers);

        motor1.setSelection(items_string_letters.indexOf(MainActivity.motor1_port));
        motor2.setSelection(items_string_letters.indexOf(MainActivity.motor2_port));
        color_sensor.setSelection(items_string_numbers.indexOf(MainActivity.color_sensor_port));
        gyro_sensor.setSelection(items_string_numbers.indexOf(MainActivity.gyro_sensor_port));
        touch_sensor.setSelection(items_string_numbers.indexOf(MainActivity.touch_sensor_port));

        motor1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.motor1_port != item) {
                    MainActivity.motor1_port = item;
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
                if(MainActivity.motor2_port != item) {
                    MainActivity.motor2_port = item;
                    Toast.makeText(getApplicationContext(), "Changed Motor2 port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        color_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                if(MainActivity.color_sensor_port != item) {
                    MainActivity.color_sensor_port = item;
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
                if(MainActivity.gyro_sensor_port != item) {
                    MainActivity.gyro_sensor_port = item;
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
                if(MainActivity.touch_sensor_port != item) {
                    MainActivity.touch_sensor_port = item;
                    Toast.makeText(getApplicationContext(), "Changed TouchSensor port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /*
    * public void onButtonContactsClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_contacts);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
        textView1 = (TextView) dialog.findViewById(R.id.email);
        textView2 = (TextView) dialog.findViewById(R.id.phone);
        textView3 = (TextView) dialog.findViewById(R.id.address);
        textView4 = (TextView) dialog.findViewById(R.id.website);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    * */
}
