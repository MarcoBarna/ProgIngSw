package com.example.pasquale_asus.legotest;

        import android.app.Dialog;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

public class GuideActivity extends AppCompatActivity {
    Dialog dialog;
    ImageView closePopup;
    TextView problem, solution;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        /*TODO Add the same onClickListener to every Problem. {use a loop?}
          TODO In order to use string.xml and change the text on the fly using the Button properties to decide what text has to be shown.
        */

    }

    public void onButtonProblemClick(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_contacts);
        closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
        problem = dialog.findViewById(R.id.guideProblemPopup);
        solution = dialog.findViewById(R.id.guideSolutionPopup);

        //TODO finish problem and solution text modify (add questions and answers to string.xml when test successful)

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