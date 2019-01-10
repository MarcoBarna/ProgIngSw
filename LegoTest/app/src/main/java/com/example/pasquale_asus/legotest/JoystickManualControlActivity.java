package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3Motors;

public class JoystickManualControlActivity extends AppCompatActivity{
    private Ev3Motors motors;
    private FloatingActionButton baseforcursor;
    private Point size = null;
    private Button home_test_motor;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick_manual_control);
        size = new Point();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        ElementsEV3 elementsEV3 = new ElementsEV3();
        motors = elementsEV3.leftMotors;
        motors.MotorPorts("BC");
        motors.BluetoothClient(MainActivity.bluetoothClient);
        baseforcursor = findViewById(R.id.floatingActionButton2);
        baseforcursor.setEnabled(false);
        final TextView textView = findViewById(R.id.textView);
        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //   textView.setText(event.toString() + "\nTr X:" + floatingActionButton.getTranslationX() + "Tr Y:" + floatingActionButton.getTranslationY());
                double sinTETA = 0; //seno dell'angolo che parte da 0 Math.PI
                double cosTETA = 0; //coseno dell'angolo che parte da 0 Math.PI
                double radius = 0; //distanza dal centro al punto di tocco
                double maxRadius = size.x/3; //raggio del cerchio in cui si può muovere la levetta
                int power = 0;
                int turnratio = 0;
                int cache_power = 0;
                int cache_turnratio = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //il tocco è al centro del bottone, non più all'angolo in alto a sinistra
                        float eventX = event.getX() - floatingActionButton.getWidth() / 2;
                        float eventY = event.getY() - floatingActionButton.getHeight() / 2;

                        //calcolo del raggio
                        radius = Math.sqrt(
                                Math.pow(floatingActionButton.getTranslationX() + eventX, 2) +
                                        Math.pow(floatingActionButton.getTranslationY() + eventY, 2)
                        );

                        //calcolo della coordinata massima contenuta nel cerchio
                        cosTETA =  (floatingActionButton.getTranslationX() + eventX) / radius;
                        sinTETA =  (floatingActionButton.getTranslationY() + eventY) / radius;

                        //il tocco esce dal cerchio?
                        if (radius > maxRadius) {
                            //mantengo il bottone al'interno del cerchio, ancora controllato dal tocco
                            floatingActionButton.setTranslationX( (float) (cosTETA * maxRadius));
                            floatingActionButton.setTranslationY( (float) (sinTETA * maxRadius));
                        }
                        else {
                            //seguo il tocco completamente
                            floatingActionButton.setTranslationX(floatingActionButton.getTranslationX() + eventX);
                            floatingActionButton.setTranslationY(floatingActionButton.getTranslationY() + eventY);
                        }
                        power = - (int) (floatingActionButton.getTranslationY() *100 / maxRadius);
                        turnratio = (int) (floatingActionButton.getTranslationX() *100 / maxRadius);
                        if(power != cache_power && turnratio != cache_turnratio) {
                            motors.RotateSyncIndefinitely(power, turnratio);
                            cache_power = power;
                            cache_turnratio = turnratio;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        floatingActionButton.setTranslationX(0);
                        floatingActionButton.setTranslationY(0);
                        motors.Stop(false);
                        break;
                    default:
                        break;
                }
              //  textView.setText(textView.getText() +
              //          "\n" + size +
              //          "\npower:" +power + "\nturnratio:" + turnratio +
                        //"\nsin: " + sinTETA + "\ncos: " + cosTETA +
                        //"\nradius: " + radius + "\nmaxRadius: " + maxRadius +
             //           "\n(radius < maxRadius) ? " + (radius < maxRadius));
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.manualToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
