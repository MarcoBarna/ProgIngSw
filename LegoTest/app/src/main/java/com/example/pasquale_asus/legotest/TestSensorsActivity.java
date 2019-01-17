package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3TouchSensor;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;

import static com.example.pasquale_asus.legotest.R.id.spinner_motor1;

public class TestSensorsActivity extends AppCompatActivity {
    private Button set_ports_button;
    private ImageView closePopup;
    private Ev3TouchSensor ev3TouchSensor;
    private Ev3UltrasonicSensor ev3UltrasonicSensor;
    private Ev3GyroSensor ev3GyroSensor;
    private Ev3ColorSensor ev3ColorSensor;
    public static Handler readSensorsHandler;
    private Runnable readSensorsRunnable;
    private boolean readSensorsStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sensors);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        set_ports_button = findViewById(R.id.set_port_sensor);
        set_ports_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonsetPortsClick(view);
            }
        });

        Button buttonTestMotors = findViewById(R.id.button_test_sensors_motors);
        buttonTestMotors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onButtonTestMotors(view);
            }
        });

        ev3TouchSensor = MainActivity.ev3.inputs.touchSensor;
        ev3TouchSensor.SensorPort(MainActivity.ev3.ports.touch_sensor_port);
        ev3TouchSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);

        ev3UltrasonicSensor = MainActivity.ev3.inputs.ultrasonicSensor;
        ev3UltrasonicSensor.SensorPort(MainActivity.ev3.ports.proximity_sensor_port);
        ev3UltrasonicSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);

        ev3ColorSensor = MainActivity.ev3.inputs.colorSensor;
        ev3ColorSensor.SensorPort(MainActivity.ev3.ports.color_sensor_port);
        ev3ColorSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);

        readSensorsStop = false;
        readSensorsHandler = new Handler();
        readSensorsRunnable = new Runnable() {
            @Override
            public void run() {
                TextView touch, prox, color;
                touch = findViewById(R.id.touch_sensor_state);
                prox = findViewById(R.id.proximity_sensor_value);
                color = findViewById(R.id.light_sensor_value);
                touch.setText(MainActivity.ev3.inputs.touchSensor.IsPressed()+"");
                prox.setText(MainActivity.ev3.inputs.ultrasonicSensor.GetDistance()+"");
                color.setText(MainActivity.ev3.inputs.colorSensor.GetColorName());
                if (readSensorsStop == false)
                    readSensorsHandler.postDelayed(readSensorsRunnable, 1000);
            }
        };
        readSensorsHandler.post(readSensorsRunnable);

        Utility.setupToolbar(this, R.id.testSensorsToolbar);
    }

    @Override
    protected void onPause() {
        readSensorsStop = true;
        readSensorsHandler.removeCallbacks(readSensorsRunnable);
        super.onPause();
    }

    public void onButtonTestMotors(View v){
        Intent intent = new Intent(this, TestMotorsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void onButtonsetPortsClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_set_ports);

        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(getApplicationContext(), "Changed TouchSensor port: " + item, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
