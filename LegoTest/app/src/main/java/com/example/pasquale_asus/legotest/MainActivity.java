package com.example.pasquale_asus.legotest;
import android.view.View;
import android.widget.Button;
import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;


public class MainActivity extends Form
{
    private Ev3Motors motor1;
    private int numbers = 0;
    private ListPicker ListPicker1;
    private BluetoothClient BluetoothClient1;
    private Button button;
    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        ListPicker1 = new ListPicker(this);
        ListPicker1.Text("Connect");
        BluetoothClient1 = new BluetoothClient(this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClicked();
            }
        });
        motor1 = new Ev3Motors(this);
    }
    public void buttonClicked(){
        numbers += 1;
        button.setText("Clicked " + numbers + " time");
    }

}