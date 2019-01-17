package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.appinventor.components.runtime.Ev3Motors;

public class TestMotorsActivity extends AppCompatActivity{
    private Ev3Motors motors, submotor;
    private FloatingActionButton baseforcursor;
    private Point size = null;
    private ImageButton motorup, motordown;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_motors);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        size = new Point();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        motors = MainActivity.ev3.outputs.leftMotors;
        motors.MotorPorts("BC");
        motors.BluetoothClient(MainActivity.ev3.bluetoothClient);

        submotor = MainActivity.ev3.outputs.rightMotors;
        submotor.MotorPorts("AD");
        submotor.BluetoothClient(MainActivity.ev3.bluetoothClient);


        baseforcursor = findViewById(R.id.floatingActionButton2);
        baseforcursor.setEnabled(false);
        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                return false;
            }
        });

        Utility.setupToolbar(this, R.id.manualToolbar);

        motorup = findViewById(R.id.test_motor_up);
        motordown = findViewById(R.id.test_motor_down);

        /*Button UP (change id button)*/

        motorup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        goForward(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        submotor.Stop(true);
                        submotor.Stop(false);
                }
                return false;
            }
        });

        motordown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        goBackward(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        submotor.Stop(false);
                }
                return false;
            }
        });

        Button buttonTestMotors = findViewById(R.id.button_test_motors_sensors);
        buttonTestMotors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonTestSensors(view);
            }
        });

    }

    private void onButtonTestSensors(View view) {
        Intent intent = new Intent(this, TestSensorsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void goForward(View v){
        submotor.RotateSyncIndefinitely(200,0);
    }
    public void goBackward(View v){
        submotor.RotateSyncIndefinitely(-200,0);
    }
}
