package com.example.pasquale_asus.legotest;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;

import java.util.ArrayList;

public class BtPaired extends AppCompatActivity {
    private BluetoothClient bluetoothClient;
    private TextView bluetoothoff, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_paired);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        bluetoothoff = findViewById(R.id.bluetoothoff);
//        title = findViewById(R.id.listTitle);
        bluetoothoff.setVisibility(View.INVISIBLE);
        final ListView btList= findViewById(R.id.bt_list);
        bluetoothClient = MainActivity.ev3.bluetoothClient;
        final ArrayList<String> arrayList = new ArrayList<>(bluetoothClient.AddressesAndNames());
        if(arrayList.size() == 0) {
            bluetoothoff.setVisibility(View.VISIBLE);
            btList.setVisibility(View.INVISIBLE);
        }
//        for (int i = 0; i < 10; i++)
//            arrayList.add("bloatAddress"+(i+1));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        btList.setAdapter(arrayAdapter);
        btList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                String string = arrayList.get(i);
                intent.putExtra("bluetooth", string);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Utility.setupToolbar(this, R.id.BTPairedToolbar);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}