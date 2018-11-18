package com.example.pasquale_asus.legotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;

import java.util.ArrayList;

public class BtPaired extends Form {
    private BluetoothClient bluetoothClient;
    @Override
    public void $define(){
        setContentView(R.layout.activity_bt_paired);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        final ListView btList= findViewById(R.id.bt_list);
        bluetoothClient = new BluetoothClient(this);
        final ArrayList<String> arrayList = new ArrayList<>(bluetoothClient.AddressesAndNames());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        btList.setAdapter(arrayAdapter);
        btList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                String string = new String(arrayList.get(i));
                intent.putExtra("bluetooth", string);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}