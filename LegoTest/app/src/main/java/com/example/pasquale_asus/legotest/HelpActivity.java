package com.example.pasquale_asus.legotest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    Dialog dialog;
    ImageView closePopup;
    TextView titlePopup, textView1, textView2, textView3, textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setupButtons();
        Utility.setupToolbar(this, R.id.helpToolbar);
    }

    private void setupButtons(){
        findViewById(R.id.buttonFAQ1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonFAQClick(view);
            }
        });
        findViewById(R.id.buttonFAQ2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonGuideClick(view);
            }
        });
        findViewById(R.id.buttonFAQ3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonContactsClick(view);
            }
        });
        findViewById(R.id.buttonFAQ4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonCreditsClick(view);
            }
        });
    }

    private void onButtonFAQClick(View v){
        Intent FAQsIntent = new Intent(this, FAQsActivity.class);
        startActivity(FAQsIntent);
    }

    private void onButtonGuideClick(View v){
        Intent GuideIntent = new Intent(this, GuideActivity.class);
        startActivity(GuideIntent);
    }

    private void onButtonContactsClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_contacts);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
        textView1 = (TextView) dialog.findViewById(R.id.email);
        textView2 = (TextView) dialog.findViewById(R.id.phone);
        textView3 = (TextView) dialog.findViewById(R.id.address);
        textView4 = (TextView) dialog.findViewById(R.id.website);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void onButtonCreditsClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_credits);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
        textView1 = (TextView) dialog.findViewById(R.id.marco);
        textView2 = (TextView) dialog.findViewById(R.id.pasquale);
        textView3 = (TextView) dialog.findViewById(R.id.riccardo);
        textView4 = (TextView) dialog.findViewById(R.id.alvise);

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
