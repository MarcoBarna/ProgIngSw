package com.example.pasquale_asus.legotest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;

    // Initialization of the constructors
    public JoystickView(Context context){
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }
    public JoystickView(Context context, AttributeSet attributes, int style){
        super(context,attributes,style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }
    public JoystickView(Context context, AttributeSet attributes){
        super(context,attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    // Interfaces Methods implementations
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setupDimensions(); // Call to the method to setup correctly the joystick
        drawJoystick(centerX,centerY); // Draw the joystick
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /* This method serves to make sure that the user moves the joystick when he keeps touching
       and when he release it, the joystick will be brought back to it's center
     */
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if(v.equals(this)){
            if(e.getAction() != e.ACTION_UP){
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if(displacement < baseRadius){
                    drawJoystick(e.getX(),e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - centerX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                }
                else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX,constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX-centerX)/baseRadius, (constrainedY-centerY)/baseRadius, getId());
                }
            }
            else {
                drawJoystick(centerX,centerY);
                joystickCallback.onJoystickMoved(0,0,getId());
            }
        }
        return true;
    }
    /*This method assigns values to each of the variables as ratio of the width and height on
      the surface view. The Math.min ensures that the joystick wil always fit inside the
      surface view.*/
    private void setupDimensions(){
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        baseRadius = Math.min(getWidth(),getHeight())/3;
        hatRadius = Math.min(getWidth(),getHeight())/5;
    }

    private void drawJoystick(Float newX, Float newY){
        if(getHolder().getSurface().isValid()){
            int alpha_base = 255;
            int red_base = 50;
            int green_base = 50;
            int blue_base = 50;
            int alpha_joystick = 255;
            int red_joystick = 0;
            int green_joystick = 0;
            int blue_joystick = 255;
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(alpha_base,red_base,green_base,blue_base); // set color of the base
            myCanvas.drawCircle(centerX,centerY,baseRadius,colors); // Draw the base of the joystick
            colors.setARGB(alpha_joystick,red_joystick,green_joystick,blue_joystick); // set color of the joystick
            myCanvas.drawCircle(newX,newY,hatRadius,colors); // Draw the joystick
            getHolder().unlockCanvasAndPost(myCanvas); // Writes the new drawing in the Surface View
        }
    }
    public interface JoystickListener
    {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }

}
