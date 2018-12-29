package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    Dialog dialog;
    ImageView closePopup;
    TextView titlePopup, marco, pasquale, riccardo, alvise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
        marco = (TextView) dialog.findViewById(R.id.marco);
        pasquale = (TextView) dialog.findViewById(R.id.pasquale);
        riccardo = (TextView) dialog.findViewById(R.id.riccardo);
        alvise = (TextView) dialog.findViewById(R.id.alvise);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}
