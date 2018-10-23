package com.example.pasquale_asus.legotest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.util.YailList;


public class MainActivity extends Form
{
    private Ev3Motors motor1;
    private int numbers = 0;
    private ListPicker listPicker1;
    private BluetoothClient BluetoothClient1;
    private Button button;
    private Button buttonBluetooth;
    private TextView text;
    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        buttonBluetooth = findViewById(R.id.buttonBluethoot);
        text = findViewById(R.id.textView);
        listPicker1 = new ListPicker(this);
        listPicker1.Text("Connect");
        BluetoothClient1 = new BluetoothClient(this);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClicked();
            }
        });
        buttonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBluethootClicked();
            }
        });

        motor1 = new Ev3Motors(this);
    }
    public void buttonClicked(){
        numbers += 1;
        button.setText("Clicked " + numbers + " time" + listPicker1.Selection());
    }
    public void buttonBluethootClicked(){
        String macAdress= "00:16:53:61:cb:69 YAO";
        //BluetoothClient1.AddressesAndNames();
        //listPicker1.Elements(YailList.makeList(BluetoothClient1.AddressesAndNames()));
       // BluetoothClient1.Connect(listPicker1.Selection());
        if (BluetoothClient1.Connect(macAdress)) {
          buttonBluetooth.setText("Connesso");
        }
        else {
            buttonBluetooth.setText("Non connesso");
        };

    }

}