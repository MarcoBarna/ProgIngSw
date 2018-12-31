package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import static com.example.pasquale_asus.legotest.R.id.spinner_motor1;

public class SettingsActivity extends AppCompatActivity {
    private Button set_ports_button;
    private ImageView closePopup;
    private Spinner motor1, motor2, color_sensor, gyro_sensor, touch_sensor;
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
    }
    @SuppressLint("WrongConstant")
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
        
        motor1 = findViewById(spinner_motor1);
        motor2 = findViewById(R.id.spinner_motor2);
        color_sensor = findViewById(R.id.spinner_color_sensor);
        gyro_sensor = findViewById(R.id.spinner_gyro_sensor);
        touch_sensor = findViewById(R.id.spinner_touch_sensor);

        String[] items = new String[]{"1", "2", "3", "4", "A", "B", "C", "D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        motor1.setAdapter(adapter);
        motor2.setAdapter(adapter);
        color_sensor.setAdapter(adapter);
        gyro_sensor.setAdapter(adapter);
        touch_sensor.setAdapter(adapter);



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
