package com.example.abatt.mybluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Costanti per gestire le richieste ad altre Activity
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MainActivity ha un BroadcastReceiver per ascoltare il cambiamento di stato del Bluetooth
        MyBluetoothReceiver myBluetoothReceiver = new MyBluetoothReceiver();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.EXTRA_STATE);
        //Abilito la ricezione del Broadcast definito da filter tramite il BroadcastReceiver myBluetoothReceiver
        //IMPORTANTE: ricevo il Broadcast solo quando l'app è attiva
        registerReceiver(myBluetoothReceiver, filter);

        checkBluetooth();

    }

    //Controlla lo stato del Bluetooth e lo comunica su schermo
    private void checkBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView textView = findViewById(R.id.textView);

        if (bluetoothAdapter != null) {
            textView.setText(R.string.bluetoothSupported);
            if (bluetoothAdapter.isEnabled())
                textView.setText(textView.getText() + getString(R.string.bluetoothAndEnabled));
            else
                textView.setText(textView.getText() + getString(R.string.bluetoothButNotEnabled));
        }
        else
            textView.setText(R.string.bluetoothNotSupported);
    }

    //Si occupa di chiedere all'utente di accendere il Bluetooth se è spento, altrimenti avvisa che il Bluetooth è già acceso
    public void askBluetooth(View view){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView textView = findViewById(R.id.textView);
        if (bluetoothAdapter.isEnabled())
            textView.setText(R.string.bluetoothAlreadyEnabled);
        else{
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            textView.setText(R.string.bluetoothAsking);
        }
    }

    @Override
    //Gestisce i risultati delle Activity chiamate da MainActivity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT: //Ho ricevuto il risultato della richiesta al Bluetooth
                if (resultCode == RESULT_OK) { //La richiesta è andata a buon fine (Bluetooth acceso)
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    TextView textView = findViewById(R.id.textView);
                    if (bluetoothAdapter.isEnabled())
                        textView.setText(R.string.bluetoothHasBeenEnabled);
                    else
                        textView.setText(R.string.bluetoothFailedWTF);
                } else
                    //La richiesta è stata annullata (L'utente non ha accettato la richiesta di accendere il Bluetooth)
                    if (resultCode == RESULT_CANCELED) {
                        TextView textView = findViewById(R.id.textView);
                        textView.setText(R.string.bluetoothRequestCANCELED);
                }
                break;
            default:
                break;
        }
    }
    //Monitora lo stato del Bluetooth, è un BroadcastReceiver
    public class MyBluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Estraggo lo stato attuale da intent
            int currentState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            //Prendo il collegamento a textView (visibile perché questa è una inner class)
            TextView textView = findViewById(R.id.textView);
            //Confronto lo stato del Bluetooth con i valori di libreria e di conseguenza cambio il testo nella textView
            switch (currentState) {
                case BluetoothAdapter.STATE_TURNING_ON: //In accensione
                    textView.setText(R.string.bluetoothReceiverTurningON);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF: //In spegnimento
                    textView.setText(R.string.bluetoothReceiverTurningOFF);
                    break;
                case BluetoothAdapter.STATE_ON: //Acceso
                    textView.setText(R.string.bluetoothReceiverON);
                    break;
                case BluetoothAdapter.STATE_OFF: //Spento
                    textView.setText(R.string.bluetoothReceiverOFF);
                    break;
                default:
                    break;
            }
        }
    }
}
