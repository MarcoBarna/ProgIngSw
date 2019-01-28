package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Utility {

    public static String convertToUsablePort(String port){
        String convertedPort;
        switch (port){
            case "49":
                convertedPort = "1";
                break;
            case "50":
                convertedPort = "2";
                break;
            case "51":
                convertedPort = "3";
                break;
            case "52":
                convertedPort = "4";
                break;
            default:
                convertedPort = port;
                break;
        }
        return convertedPort;
    }

    private static class OnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private AppCompatActivity activity;
        private EV3.PortAccess port;
        private String msg;

        public OnSpinnerItemSelectedListener(AppCompatActivity activity, EV3.PortAccess port, String msg) {
            this.activity = activity;
            this.port = port;
            this.msg = msg;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String item = (String) adapterView.getItemAtPosition(i);
            String portValue = convertToUsablePort(port.getPort());
            if(!portValue.equals(item)) {
                port.setPort(item);
                Toast.makeText(activity.getApplicationContext(), msg + item, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}

    }

    public static void setupToolbar(AppCompatActivity activity, int toolbarID){
        Toolbar toolbar = activity.findViewById(toolbarID);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public static void onButtonsetPortsClick(AppCompatActivity activity, View v){
        //TODO rifinire togliendo doppioni di codice
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.epic_popup_set_ports);

        ImageView closePopup = dialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Spinner motor1 = dialog.findViewById(R.id.spinner_motor1);
        Spinner motor2 = dialog.findViewById(R.id.spinner_motor2);
        Spinner motor3 = dialog.findViewById(R.id.spinner_motor3);
        Spinner color_sensor = dialog.findViewById(R.id.spinner_color_sensor);
        Spinner gyro_sensor = dialog.findViewById(R.id.spinner_gyro_sensor);
        Spinner touch_sensor = dialog.findViewById(R.id.spinner_touch_sensor);
        Spinner ultrasonic_sensor = dialog.findViewById(R.id.spinner_proximity_sensor);

        String items_string_numbers = "";
        for(String item: EV3.Ports.input_ports)
            items_string_numbers += item;
        items_string_numbers = items_string_numbers.replace(",","");

        String items_string_letters = "";
        for(String item: EV3.Ports.output_ports)
            items_string_letters += item;
        items_string_letters = items_string_letters.replace(",","");

        final ArrayAdapter<String> adapter_numbers = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, EV3.Ports.input_ports);
        adapter_numbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> adapter_letters = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, EV3.Ports.output_ports);
        adapter_letters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        motor1.setAdapter(adapter_letters);
        motor2.setAdapter(adapter_letters);
        motor3.setAdapter(adapter_letters);
        color_sensor.setAdapter(adapter_numbers);
        gyro_sensor.setAdapter(adapter_numbers);
        touch_sensor.setAdapter(adapter_numbers);
        ultrasonic_sensor.setAdapter(adapter_numbers);

        motor1.setSelection(items_string_letters.indexOf(convertToUsablePort(MainActivity.ev3.outputs.motor1.MotorPorts())));
        motor2.setSelection(items_string_letters.indexOf(convertToUsablePort(MainActivity.ev3.outputs.motor2.MotorPorts())));
        motor3.setSelection(items_string_letters.indexOf(convertToUsablePort(MainActivity.ev3.outputs.motor3.MotorPorts())));
        color_sensor.setSelection(items_string_numbers.indexOf(convertToUsablePort(MainActivity.ev3.inputs.colorSensor.SensorPort())));
        gyro_sensor.setSelection(items_string_numbers.indexOf(convertToUsablePort(MainActivity.ev3.inputs.gyroSensor.SensorPort())));
        touch_sensor.setSelection(items_string_numbers.indexOf(convertToUsablePort(MainActivity.ev3.inputs.touchSensor.SensorPort())));
        ultrasonic_sensor.setSelection(items_string_numbers.indexOf(convertToUsablePort(MainActivity.ev3.inputs.ultrasonicSensor.SensorPort())));

        motor1.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.outputs.motor1.MotorPorts(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.outputs.motor1.MotorPorts();
            }
        }, "Changed Motor1 port: "));

        motor2.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.outputs.motor2.MotorPorts(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.outputs.motor2.MotorPorts();
            }
        }, "Changed Motor2 port: "));

        motor3.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.outputs.motor3.MotorPorts(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.outputs.motor3.MotorPorts();
            }
        }, "Changed Motor3 port: "));

        color_sensor.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.inputs.colorSensor.SensorPort(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.inputs.colorSensor.SensorPort();
            }
        }, "Changed ColorSensor port: "));

        gyro_sensor.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.inputs.gyroSensor.SensorPort(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.inputs.gyroSensor.SensorPort();
            }
        }, "Changed GyroSensor port: "));

        touch_sensor.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.inputs.touchSensor.SensorPort(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.inputs.touchSensor.SensorPort();
            }
        }, "Changed TouchSensor port: "));

        ultrasonic_sensor.setOnItemSelectedListener(new OnSpinnerItemSelectedListener(activity, new EV3.PortAccess() {
            @Override
            public void setPort(String newPort) {
                MainActivity.ev3.inputs.ultrasonicSensor.SensorPort(newPort);
            }

            @Override
            public String getPort() {
                return MainActivity.ev3.inputs.ultrasonicSensor.SensorPort();
            }
        }, "Changed ProximitySensor port: "));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
