package com.example.abatt.mybluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Costanti per gestire le richieste ad altre Activity
    private final static int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;
    MyBluetoothReceiver myBluetoothReceiver;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        registerForContextMenu(findViewById(R.id.buttonConnectBluetooth));
        Button bluetoothConnecButton = findViewById(R.id.buttonConnectBluetooth);
        bluetoothConnecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });

        myBluetoothReceiver = new MyBluetoothReceiver();
        setupBluetoothReceiver();

        checkBluetooth();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBluetoothReceiver);
        if (bluetoothSocket != null && bluetoothSocket.isConnected())
            findViewById(R.id.button).callOnClick();
    }

    private void setupBluetoothReceiver() {
        //MainActivity ha un BroadcastReceiver per ascoltare il cambiamento di stato del Bluetooth
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.EXTRA_STATE);
        //Abilito la ricezione del Broadcast definito da filter tramite il BroadcastReceiver myBluetoothReceiver
        //IMPORTANTE: ricevo il Broadcast solo quando l'app è attiva
        registerReceiver(myBluetoothReceiver, filter);
    }

    //Controlla lo stato del Bluetooth e lo comunica su schermo
    private void checkBluetooth(){
        TextView textView = findViewById(R.id.bluetoothStatus);

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
        TextView textView = findViewById(R.id.bluetoothStatus);
        if (bluetoothAdapter.isEnabled())
            textView.setText(R.string.bluetoothAlreadyEnabled);
        else{
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            textView.setText(R.string.bluetoothAsking);
        }
    }

    public void showPairedBluetoothDevice(View view){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        TextView textView = findViewById(R.id.listOfPairedDevices);

        if (bluetoothAdapter.isEnabled())
            textView.setTextKeepState(getString(R.string.startPairedBluetoothDevices));
        else
            textView.setTextKeepState(getString(R.string.askEnableBluetoothForPairedBluetoothDevices));

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                textView.setText(textView.getText() + deviceName + " [" + deviceHardwareAddress + "]\n");
            }
        }

    }

    public void connectToPairedBluetoothDevice(){
        ParcelUuid[] parcelUuids = bluetoothDevice.getUuids();

        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(parcelUuids[0].getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final TextView textView = findViewById(R.id.connectionStatus);

        for (int i = 0; i < parcelUuids.length; i++)
            textView.setText(textView.getText() + "\n" + parcelUuids[i].toString());

        new ConnectDevice().execute();
    }

    public void disconnectBluetooth(View view){
        TextView textView = findViewById(R.id.connectionStatus);

        try {
            if (bluetoothSocket != null && bluetoothSocket.isConnected()){
                bluetoothSocket.close();
                textView.setText(getString(R.string.disconnectedBluetooth));
            }
            else
                bluetoothSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices)
            menu.add(device.getName());

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        TextView textView = findViewById(R.id.connectionStatus);

        for (BluetoothDevice pairedDevice : pairedDevices) {
            if (item.getTitle().equals(pairedDevice.getName()))
                bluetoothDevice = pairedDevice;
        }

        textView.setText(bluetoothDevice.getName());

        connectToPairedBluetoothDevice();

        return true;
    }

    @Override
    //Gestisce i risultati delle Activity chiamate da MainActivity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT: //Ho ricevuto il risultato della richiesta al Bluetooth
                if (resultCode == RESULT_OK) { //La richiesta è andata a buon fine (Bluetooth acceso)
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    TextView textView = findViewById(R.id.bluetoothStatus);
                    if (bluetoothAdapter.isEnabled())
                        textView.setText(R.string.bluetoothHasBeenEnabled);
                    else
                        textView.setText(R.string.bluetoothFailedWTF);
                } else
                    //La richiesta è stata annullata (L'utente non ha accettato la richiesta di accendere il Bluetooth)
                    if (resultCode == RESULT_CANCELED) {
                        TextView textView = findViewById(R.id.bluetoothStatus);
                        textView.setText(R.string.bluetoothRequestCANCELED);
                }
                break;
            default:
                break;
        }
    }

    public class ConnectDevice extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                bluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            TextView textView = findViewById(R.id.connectionStatus);
            textView.setText(textView.getText() + "\n" + (bluetoothSocket.isConnected() ? "Connesso" : "Non conesso"));
        }
    }

    //Monitora lo stato del Bluetooth, è un BroadcastReceiver
    public class MyBluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Estraggo lo stato attuale da intent
            int currentState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            //Prendo il collegamento a textView (visibile perché questa è una inner class)
            TextView textView = findViewById(R.id.bluetoothStatus);
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
