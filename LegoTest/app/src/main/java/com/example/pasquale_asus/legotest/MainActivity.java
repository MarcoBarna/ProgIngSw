package com.example.pasquale_asus.legotest;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.TextToSpeech;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Form
{
    private Ev3Motors motor1;
    private int numbers = 0;
    private ListPicker listPicker1;
    private BluetoothClient bluetoothClient1;
    private Button button;
    private Button buttonBluetooth;
    private String bluetoothValue;
    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        buttonBluetooth = findViewById(R.id.buttonBluethoot);
        buttonBluetooth.setText("Connetti");
        listPicker1 = new ListPicker(this);
        listPicker1.Text("Connect");
        bluetoothClient1 = new BluetoothClient(this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClicked();
            }
        });
        buttonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBluetoothClicked();
            }
        });
        motor1 = new Ev3Motors(this);
    }
    public void buttonClicked(){
        numbers += 1;
        button.setText("Clicked " + numbers + " time");
    }
    public void buttonBluetoothClicked(){
        selectPairedBluetooth();
    }
    public void selectPairedBluetooth(){
        Intent intent = new Intent(this, BtPaired.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textView = findViewById(R.id.textView);
        if (resultCode == RESULT_OK) {
            bluetoothValue = data.getStringExtra("bluetooth");
            textView.setText(bluetoothValue + " è connesso");
            if(bluetoothClient1.Connect(bluetoothValue)){
                buttonBluetooth.setText("Disconnetti");
            }
            else{
                buttonBluetooth.setText("Connetti");
            }
        }
        else
            textView.setText("CANCELED");
    }
}