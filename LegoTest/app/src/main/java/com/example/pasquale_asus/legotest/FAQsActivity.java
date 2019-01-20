package com.example.pasquale_asus.legotest;

        import android.app.Dialog;
        import android.content.res.Resources;
        import android.content.res.TypedArray;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

public class FAQsActivity extends AppCompatActivity {
    Resources resources;
    TypedArray  FAQsButtonsIDs,
                FAQsQuestions,
                FAQsAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        initializeFAQs();
        Utility.setupToolbar(this, R.id.FAQsToolbar);
    }

    private void initializeFAQs(){
        resources = getResources();
        FAQsButtonsIDs = resources.obtainTypedArray(R.array.FAQsButtonsIDs);
        FAQsQuestions = resources.obtainTypedArray(R.array.FAQsQuestions);
        FAQsAnswers = resources.obtainTypedArray(R.array.FAQsAnswers);

        for(int i = 0; i < FAQsButtonsIDs.length(); i++) {
            final Integer index = new Integer(i);

            Button questionButton = findViewById(FAQsButtonsIDs.getResourceId(index, View.NO_ID));

            questionButton.setText(FAQsQuestions.getString(i));

            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onQuestionClick(view, index);
                }
            });
        }
    }

    private void onQuestionClick(View v, Integer index){
        String questionText = FAQsQuestions.getString(index);
        String answerText = FAQsAnswers.getString(index);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.epic_popup_custom_faq);

        TextView questionBody = dialog.findViewById(R.id.FAQquestionPopup);
        questionBody.setText(questionText);

        TextView answerBody = dialog.findViewById(R.id.FAQanswerPopup);
        answerBody.setText(answerText);

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