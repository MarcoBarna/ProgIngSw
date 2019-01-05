package com.example.pasquale_asus.legotest;

        import android.app.Dialog;
        import android.content.res.Resources;
        import android.content.res.TypedArray;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

public class GuideActivity extends AppCompatActivity {
    Resources resources;
    TypedArray  GuideButtonsIDs,
                GuideProblems,
                GuideSolutions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.activity_guide);

        GuideButtonsIDs = resources.obtainTypedArray(R.array.GuideButtonsIDs);
        GuideProblems = resources.obtainTypedArray(R.array.GuideProblems);
        GuideSolutions = resources.obtainTypedArray(R.array.GuideSolutions);

        for(Integer i = 0; i < GuideButtonsIDs.length(); i++) {
            final Integer index = i;

            Button questionButton = findViewById(GuideButtonsIDs.getResourceId(index, View.NO_ID));

            questionButton.setText(GuideProblems.getString(i));

            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onProblemClick(view, index);
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.GuideToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void onProblemClick(View v, Integer index){
        String problemText = GuideProblems.getString(index);
        String solutionText = GuideSolutions.getString(index);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_guide);

        TextView problemBody = dialog.findViewById(R.id.guideProblemPopup);
        problemBody.setText(problemText);

        TextView solutionBody = dialog.findViewById(R.id.guideSolutionPopup);
        solutionBody.setText(solutionText);

        ImageView closePopup = dialog.findViewById(R.id.closePopup);
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