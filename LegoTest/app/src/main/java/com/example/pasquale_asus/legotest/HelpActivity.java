package com.example.pasquale_asus.legotest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.buttonFAQ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonFAQClick(view);
            }
        });
        findViewById(R.id.buttonGuide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonGuideClick(view);
            }
        });
        findViewById(R.id.buttonContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonContactsClick(view);
            }
        });
        findViewById(R.id.buttonCredits).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonCreditsClick(view);
            }
        });

    }

    public void onButtonFAQClick(View v){
        //TODO activity and startActivity
    }

    public void onButtonGuideClick(View v){
        //TODO activity and startActivity
    }

    public void onButtonContactsClick(View v){
        //TODO activity and startActivity
    }

    public void onButtonCreditsClick(View v){
        //TODO activity and startActivity
    }


}
