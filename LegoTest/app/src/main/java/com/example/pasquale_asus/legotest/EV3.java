package com.example.pasquale_asus.legotest;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3Commands;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3TouchSensor;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;
import com.google.appinventor.components.runtime.Form;

public class EV3 {
    public static class Ports {
        public static String[]
                input_ports = {"1", "2", "3", "4"},
                output_ports = {"A", "B", "C", "D"};

        private static String
                default_gyro_port = input_ports[3],
                default_color_port = input_ports[0],
                default_touch_port = input_ports[0],
                default_proximity_port = input_ports[2],

                default_motor1_port = output_ports[2],
                default_motor2_port = output_ports[1],
                default_motor3_port = output_ports[3];

        public String
                motor1_port = default_motor1_port,
                motor2_port = default_motor2_port,
                motor3_port = default_motor3_port,
                gyro_sensor_port = default_gyro_port,
                color_sensor_port = default_color_port,
                touch_sensor_port = default_touch_port,
                proximity_sensor_port = default_proximity_port;
    }
    public class Outputs{
        Ev3Motors leftMotors;
        Ev3Motors rightMotors;
    }
    public class Inputs{
        Ev3TouchSensor touchSensor;
        Ev3ColorSensor colorSensor;
        Ev3UltrasonicSensor ultrasonicSensor;
        Ev3GyroSensor gyroSensor;
    }
    public class Extra {
        Ev3Commands commands;
    }
    private class ElementsEV3 extends Form {}

    public BluetoothClient bluetoothClient;
    private ElementsEV3 elementsEV3;
    public Ports ports;
    public Inputs inputs;
    public Outputs outputs;
    public Extra extra;

    public EV3(){
        ports = new Ports();
        inputs = new Inputs();
        outputs = new Outputs();
        extra = new Extra();
        elementsEV3 = new ElementsEV3();

        extra.commands = new Ev3Commands(elementsEV3);
        outputs.leftMotors = new Ev3Motors(elementsEV3);
        outputs.rightMotors = new Ev3Motors(elementsEV3);
        inputs.touchSensor = new Ev3TouchSensor(elementsEV3);
        inputs.colorSensor = new Ev3ColorSensor(elementsEV3);
        inputs.ultrasonicSensor = new Ev3UltrasonicSensor(elementsEV3);
        inputs.gyroSensor = new Ev3GyroSensor(elementsEV3);
        bluetoothClient = new BluetoothClient(elementsEV3);


    }
}
