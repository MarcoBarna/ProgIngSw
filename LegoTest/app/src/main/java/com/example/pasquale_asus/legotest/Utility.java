package com.example.pasquale_asus.legotest;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Utility {
    public static void setupToolbar(AppCompatActivity activity, int toolbarID){
        Toolbar toolbar = activity.findViewById(toolbarID);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
