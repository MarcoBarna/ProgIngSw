package com.example.pasquale_asus.legotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ListPicker;


public class BtPaired extends Form {
    private ListPicker listBT;
    public void $define(){
        listBT = new ListPicker(this);
    }
}
