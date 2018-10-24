package com.example.pasquale_asus.legotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;

import java.util.ArrayList;

public class BtPaired extends Form {
    @Override
    public void $define(){
        setContentView(R.layout.activity_bt_paired);
        final ListView btList= findViewById(R.id.bt_list);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("First");
        arrayList.add("Second");
        arrayList.add("Third");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        btList.setAdapter(arrayAdapter);
        
    }

    public void goBack(View view){
        Intent intent = new Intent();
        String string = new String("ciao sono YAO");
        intent.putExtra("bluetooth", string);
        setResult(0, intent);
        finish();
    }
}