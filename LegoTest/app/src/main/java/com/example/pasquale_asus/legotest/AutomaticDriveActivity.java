package com.example.pasquale_asus.legotest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.Ev3UltrasonicSensor;

import java.util.List;
import java.util.Locale;

public class AutomaticDriveActivity extends AppCompatActivity {
    private android.widget.Button firstaction, stopButton, secondaction,thirdaction;
    private Ev3Motors motors;
    private Ev3UltrasonicSensor ultrasonicSensor;
    private Ev3ColorSensor colorSensor;
    private boolean interruptAction;
    public static Thread readSensors;
    private TextView debug;
    Handler handler;
    private boolean handlerStop, threadStop;
    private long anglewheels = 0;
    Runnable r = null;
    public TextToSpeech textToSpeech;
    public android.speech.SpeechRecognizer speechRecognizer;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_drive);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        initializeMotors();
        handler = new Handler();
        debug = findViewById(R.id.textView7);
        debug.setText(R.string.not_pressed);
        firstaction = findViewById(R.id.firstaction);
        secondaction = findViewById(R.id.secondaction);
        thirdaction = findViewById(R.id.thirdaction);
        initializeTextToSpeech();
        initializeSpeechRecognizer();
        firstaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disableAllButtonActions(firstaction);
                stopButton.setVisibility(View.VISIBLE);
                debug.setVisibility(View.VISIBLE);
                stopButton.setActivated(true);
                interruptAction = false;
                debug.setText(R.string.pressed);
                handlerStop = true;
                avoidObstacles();
                //Runnable algorithm = avoidObstacles();
            }
        });
        secondaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disableAllButtonActions(firstaction);
                stopButton.setVisibility(View.VISIBLE);
                debug.setVisibility(View.VISIBLE);
                stopButton.setActivated(true);
                interruptAction = false;
                debug.setText(R.string.pressed);
                handlerStop = true;
                calculateDistance();
                //Runnable algorithm = avoidObstacles();
            }
        });
        thirdaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechRecognizer.startListening(intent);
                stopButton.setVisibility(View.VISIBLE);
                stopButton.setActivated(true);
            }
        });

        stopButton = findViewById(R.id.stop_button);
        //stopButton.setActivated(false);
        debug.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(r);
                handlerStop = true;
                threadStop = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        motors.Stop(false);
                    }
                }, 500);
                stopButton.setVisibility(View.INVISIBLE);
                //stopButton.setActivated(false);
                debug.setVisibility(View.INVISIBLE);
            }
        });

        Utility.setupToolbar(this, R.id.automaticDriveToolbar);
    }

    private void initializeSpeechRecognizer() {
        if(android.speech.SpeechRecognizer.isRecognitionAvailable(this)){
            speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> result = bundle.getStringArrayList(
                            android.speech.SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }
    private void processResult(String command) {
        command = command.toLowerCase();
        debug.setText(command);
        debug.setVisibility(View.VISIBLE);
        if (command.contains(getString(R.string.go_lowercase))) {
            if (command.contains(getString(R.string.forward_lowercase))) {
                speak(getString(R.string.going_forward));
                motors.RotateSyncIndefinitely(50, 0);
            }
            if (command.contains(getString(R.string.backwards_lowercase))) {
                speak(getString(R.string.going_backwards));
                motors.RotateSyncIndefinitely(-50, 0);
            }
        }
        if(command.contains(getString(R.string.turn_lowercase))){
            if(command.contains(getString(R.string.left_lowercase))){
                speak(getString(R.string.turning_left));
                motors.RotateSyncInDuration(50,1000,-90,false);
            }
            if(command.contains(getString(R.string.right_lowercase))){
                speak(getString(R.string.turning_right));
                motors.RotateSyncInDuration(50,1000,90,false);
            }
        }
        if(command.contains(getString(R.string.do_lowercase))) {
            if (command.contains(getString(R.string.three_sixty))) {
                speak(getString(R.string.im_crazy));
                motors.RotateSyncIndefinitely(50, 90);
            }
        }
        if(command.contains(getString(R.string.stop_lowercase))){
            motors.Stop(true);
        }

    }
    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(textToSpeech.getEngines().size() == 0){
                    Toast.makeText(AutomaticDriveActivity.this, "There is no textToSpeech engine on your device", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    textToSpeech.setLanguage(Locale.getDefault());
                   // speak("");
                }
            }
        });
    }
    private void speak(String message){
        if(Build.VERSION.SDK_INT >=21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        textToSpeech.shutdown();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        handlerStop = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.Stop(false);
            }
        }, 500);
        super.onBackPressed();
    }

    public void initializeMotors(){
        motors = MainActivity.ev3.outputs.motor2;
        motors.MotorPorts(MainActivity.ev3.outputs.motor1.MotorPorts()
                            + MainActivity.ev3.outputs.motor2.MotorPorts());
        motors.BluetoothClient(MainActivity.ev3.bluetoothClient);
        ultrasonicSensor = MainActivity.ev3.inputs.ultrasonicSensor;
        ultrasonicSensor.SensorPort(MainActivity.ev3.inputs.ultrasonicSensor.SensorPort());
        ultrasonicSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);
        colorSensor = MainActivity.ev3.inputs.colorSensor;
        colorSensor.SensorPort(MainActivity.ev3.inputs.colorSensor.SensorPort());
        colorSensor.Mode("reflected");
        colorSensor.BluetoothClient(MainActivity.ev3.bluetoothClient);
    }
    public void avoidObstacles(){
        r = new Runnable() {
            public void run() {
                double distance = ultrasonicSensor.GetDistance();
                int lightdistance = colorSensor.GetLightLevel();
                debug.setText(String.format(
                        Locale.ENGLISH,"%s: %f2. | %s", getString(R.string.distance_value), distance, ((lightdistance != -128) ? lightdistance : ""))
                );


                if(distance > 0 && distance < 15 || lightdistance > 0){
                    motors.Stop(false);
                    motors.RotateSyncIndefinitely(-50,90);
                    //motors.RotateSyncInTachoCounts(-50, 2, 90, false);
                }
                else {
                    motors.RotateSyncIndefinitely(100, 0);
                }
                if (!handlerStop)
                    handler.postDelayed(this, 200);
            }
        };
        handlerStop = false;
        handler.postDelayed(r, 500);
    }
    public void calculateDistance(){
        motors.ResetTachoCount();
        anglewheels = 0;
        r = new Runnable() {
            public void run() {
                long temp_angle = (long)(motors.GetTachoCount() * 0.0273);
                anglewheels = (temp_angle > anglewheels) ? temp_angle : (anglewheels);
                debug.setText(String.format(Locale.ENGLISH, "%s: %d cm", getString(R.string.distance_value), anglewheels));
                double distance = ultrasonicSensor.GetDistance();
                if(distance > 0 && distance < 35){
                    motors.Stop(false);
                    motors.RotateSyncIndefinitely(-50,90);
                    //motors.RotateSyncInTachoCounts(-50, 2, 90, false);
                }
                else {
                    motors.RotateSyncIndefinitely(100, 0);
                }
                if (!handlerStop)
                    handler.postDelayed(this, 200);
            }
        };
        handlerStop = false;
        handler.postDelayed(r, 500);
    }
    public void findByColor(){

    }
    @SuppressLint("ResourceAsColor")
    public void disableAllButtonActions(Button actionSelected){
        //firstaction.setBackgroundTintList(ColorStateList.valueOf(R.color.greyButton));
        firstaction.setBackgroundColor(R.color.greyButton);
        //secondaction.setBackgroundTintList(ColorStateList.valueOf(R.color.greyToolbar));
        secondaction.setBackgroundColor(R.color.greyToolbar);
        thirdaction.setBackgroundColor(R.color.colorPrimaryDark);
        actionSelected.setBackgroundColor(R.color.colorPrimaryDark);
    }


//    public Runnable avoidObstacles(){
//        threadStop = false;
//        Runnable algorithm = new Runnable() {
//            @Override
//            public void run() {
//                double distance = 0;
//                try {
//                    distance = MainActivity.ev3.inputs.readProximitySensor().get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//                if (distance > 0 && distance < 20) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            motors.RotateSyncInDuration(-50, 500, 100, false);
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            motors.RotateSyncIndefinitely(50, 0);
//                        }
//                    });
//                }
//                if (!handlerStop)
//                    handler.postDelayed(this, 500);
//            }
//        };
//        return algorithm;
//    }


}
