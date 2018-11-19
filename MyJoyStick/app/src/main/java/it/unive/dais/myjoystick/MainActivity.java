package it.unive.dais.myjoystick;

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
                double sinTETA = 0; //seno dell'angolo che parte da 0 Math.PI
                double cosTETA = 0; //coseno dell'angolo che parte da 0 Math.PI
                double radius = 0; //distanza dal centro al punto di tocco
                double maxRadius = 200; //raggio del cerchio in cui si può muovere la levetta
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //il tocco è al centro del bottone, non più all'angolo in alto a sinistra
                        float eventX = event.getX() - button.getWidth() / 2;
                        float eventY = event.getY() - button.getHeight() / 2;

                        //calcolo del raggio
                        radius = Math.sqrt(
                                Math.pow(button.getTranslationX() + eventX, 2) +
                                        Math.pow(button.getTranslationY() + eventY, 2)
                        );

                        //calcolo della coordinata massima contenuta nel cerchio
                        cosTETA =  (button.getTranslationX() + eventX) / radius;
                        sinTETA =  (button.getTranslationY() + eventY) / radius;

                        //il tocco esce dal cerchio?
                        if (radius > maxRadius) {
                            //mantengo il bottone al'interno del cerchio, ancora controllato dal tocco
                            button.setTranslationX( (float) (cosTETA * maxRadius));
                            button.setTranslationY( (float) (sinTETA * maxRadius));
                        }
                        else {
                            //seguo il tocco completamente
                            button.setTranslationX(button.getTranslationX() + eventX);
                            button.setTranslationY(button.getTranslationY() + eventY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        button.setTranslationX(0);
                        button.setTranslationY(0);
                        break;
                    default:
                        break;
                }
                textView.setText(textView.getText() +
                        "\nsin: " + sinTETA + "\ncos: " + cosTETA +
                        "\nradius: " + radius + "\nmaxRadius: " + maxRadius +
                        "\n(radius < maxRadius) ? " + (radius < maxRadius));
                return false;
            }
        });

    }
}