package com.example.pasquale_asus.legotest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3TouchSensor;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestSensorsActivity extends AppCompatActivity {
    private Button set_ports_button;
    private ImageView closePopup;
    EV3 ev3;
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
        ev3 = EV3.getEV3();

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

        ev3TouchSensor = ev3.inputs.touchSensor;
        ev3TouchSensor.BluetoothClient(ev3.bluetoothClient);

        ev3UltrasonicSensor = ev3.inputs.ultrasonicSensor;
        ev3UltrasonicSensor.BluetoothClient(ev3.bluetoothClient);

        ev3ColorSensor = ev3.inputs.colorSensor;
        //ev3ColorSensor.SensorPort(ev3.ports.color_sensor_port);
        ev3ColorSensor.SensorPort(ev3.inputs.colorSensor.SensorPort());
        ev3ColorSensor.Mode("reflected");
        ev3ColorSensor.BluetoothClient(ev3.bluetoothClient);

        readSensorsStop = false;
        readSensorsHandler = new Handler();
        readSensorsRunnable = new Runnable() {
            @Override
            public void run() {
                FutureTask<Boolean> touchTask = ev3.inputs.readTouchSensor();
                touchTask.run();
                FutureTask<Double> proximityTask = ev3.inputs.readProximitySensor();
                proximityTask.run();
                FutureTask<String> colorTask = ev3.inputs.readColorNameSensor();
                colorTask.run();
                TextView touch, prox, color;
                touch = findViewById(R.id.touch_sensor_state);
                prox = findViewById(R.id.proximity_sensor_value);
                color = findViewById(R.id.light_sensor_value);
                //Retrieves the data from the Tasks and shows it
                try {
                    touch.setText(String.format("%b", touchTask.get()));
                    prox.setText(String.format(Locale.ENGLISH,"%.2f", proximityTask.get()));
                    color.setText(""+ev3ColorSensor.GetLightLevel());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (!readSensorsStop)
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
        Utility.onButtonsetPortsClick(this, v);
    }
}
