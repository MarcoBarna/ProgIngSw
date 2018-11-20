package it.unive.dais.myjoystick;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);
        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Context context = v.getContext();
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                textView.setText(event.toString() + "\nTr X:" + floatingActionButton.getTranslationX() + "Tr Y:" + floatingActionButton.getTranslationY());
                double sinTETA = 0; //seno dell'angolo che parte da 0 Math.PI
                double cosTETA = 0; //coseno dell'angolo che parte da 0 Math.PI
                double radius = 0; //distanza dal centro al punto di tocco
                double maxRadius = size.x/3; //raggio del cerchio in cui si può muovere la levetta
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //il tocco è al centro del bottone, non più all'angolo in alto a sinistra
                        float eventX = event.getX() - floatingActionButton.getWidth() / 2;
                        float eventY = event.getY() - floatingActionButton.getHeight() / 2;

                        //calcolo del raggio
                        radius = Math.sqrt(
                                Math.pow(floatingActionButton.getTranslationX() + eventX, 2) +
                                        Math.pow(floatingActionButton.getTranslationY() + eventY, 2)
                        );

                        //calcolo della coordinata massima contenuta nel cerchio
                        cosTETA =  (floatingActionButton.getTranslationX() + eventX) / radius;
                        sinTETA =  (floatingActionButton.getTranslationY() + eventY) / radius;

                        //il tocco esce dal cerchio?
                        if (radius > maxRadius) {
                            //mantengo il bottone al'interno del cerchio, ancora controllato dal tocco
                            floatingActionButton.setTranslationX( (float) (cosTETA * maxRadius));
                            floatingActionButton.setTranslationY( (float) (sinTETA * maxRadius));
                        }
                        else {
                            //seguo il tocco completamente
                            floatingActionButton.setTranslationX(floatingActionButton.getTranslationX() + eventX);
                            floatingActionButton.setTranslationY(floatingActionButton.getTranslationY() + eventY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        floatingActionButton.setTranslationX(0);
                        floatingActionButton.setTranslationY(0);
                        break;
                    default:
                        break;
                }
                textView.setText(textView.getText() +
                        "\n" + size +
                        //"\nsin: " + sinTETA + "\ncos: " + cosTETA +
                        //"\nradius: " + radius + "\nmaxRadius: " + maxRadius +
                        "\n(radius < maxRadius) ? " + (radius < maxRadius));
                return false;
            }
        });
    }
}