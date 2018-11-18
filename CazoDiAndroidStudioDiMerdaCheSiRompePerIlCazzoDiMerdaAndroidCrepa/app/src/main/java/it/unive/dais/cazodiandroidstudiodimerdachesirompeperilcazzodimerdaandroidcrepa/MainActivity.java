package it.unive.dais.cazodiandroidstudiodimerdachesirompeperilcazzodimerdaandroidcrepa;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);
        final Button button = findViewById(R.id.button);
        final ViewGroup.LayoutParams baseLayoutParams = button.getLayoutParams();
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setText(event.toString() + "\nTr X:" + button.getTranslationX() + "Tr Y:" + button.getTranslationY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        button.setTranslationX(button.getTranslationX() + event.getX() - (button.getWidth() / 2));
                        button.setTranslationY(button.getTranslationY() + event.getY() - (button.getHeight() / 2));
                        //                        float futureX, futureY;
                        //                        float distance = (float) (
                        //                                Math.sqrt(
                        //                                    Math.pow(button.getTranslationX(),2) + Math.pow(button.getTranslationY(),2)
                        //                                )
                        //                        );
                        //                        float ratio = 100/distance;
                        //                        futureX = button.getTranslationX() + event.getX() - (button.getWidth()/2);
                        //                        futureX = futureX * ratio;
                        //                        button.setTranslationX(
                        //                                Math.min(
                        //                                        -futureX,
                        //                                        Math.max(futureX,
                        //                                                button.getTranslationX() + event.getX() - (button.getWidth()/2)
                        //                                        )
                        //                                )
                        //                        );
                        //                        button.setTranslationY(
                        //                                Math.min(
                        //                                        100,
                        //                                        Math.max(-100,
                        //                                                button.getTranslationY() + event.getY() - (button.getHeight()/2)
                        //                                        )
                        //                                )
                        //                        );
                        break;
                    case MotionEvent.ACTION_UP:
                        button.setTranslationX(0);
                        button.setTranslationY(0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }
}