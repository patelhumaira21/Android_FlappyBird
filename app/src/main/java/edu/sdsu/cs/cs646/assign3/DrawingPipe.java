/**
 * File Name : DrawingPipe.java
 * Created by: Humaira Patel
 * Date: 02/18/2016
 */
package edu.sdsu.cs.cs646.assign3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Represents the pipes in the game.
 *
 */
public class DrawingPipe extends View {

    static Paint greenFill;
    private boolean animation = false;
    private float screenWidth = 800f;
    private float leftMostPoint = 0f;
    private static final float PIPE_WIDTH = 150f;
    private static final float PIPE_HEIGHT = 200f;
    private static final float DELTA_X = 5f;
    private RectF topBig, topSmall, bottomBig, bottomSmall;

    static {
        greenFill = new Paint();
        greenFill.setColor(Color.GREEN);
    }

    /**
     *  Constructor to initialize the view
     * @param context
     * @param xmlAttributes
     */
    public DrawingPipe(Context context, AttributeSet xmlAttributes) {

        super(context, xmlAttributes);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        leftMostPoint=(screenWidth);
    }

    /**
     * This method over-rides the onDraw method. It draws the 4 pipes
     * and also support the animation of the pipes.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        float height = canvas.getHeight();

        // Positioning the pipes
        topBig = new RectF(leftMostPoint, 0f , leftMostPoint+PIPE_WIDTH, PIPE_HEIGHT*3);
        topSmall = new RectF(leftMostPoint+(PIPE_WIDTH*3), 0f,leftMostPoint+(PIPE_WIDTH*4), PIPE_HEIGHT*2);
        bottomSmall = new RectF(leftMostPoint, height-(PIPE_HEIGHT*2) ,leftMostPoint+PIPE_WIDTH,height);
        bottomBig = new RectF(leftMostPoint+(PIPE_WIDTH*3), height-(PIPE_HEIGHT*3),leftMostPoint+(PIPE_WIDTH*4), height);

        // drawing the pipe
        canvas.drawRect(topBig, greenFill);
        canvas.drawRect(topSmall, greenFill);
        canvas.drawRect(bottomBig, greenFill);
        canvas.drawRect(bottomSmall,greenFill);

        // for animation
        leftMostPoint -= DELTA_X;

        if(!isEndOfScreen()) {
            startAnimation(animation);
        }
    }

    /**
     * This method starts the pipe animation only when user first touches the screen.
     */
    public void startAnimation(boolean animation){

        if(animation){
            this.animation = animation;
            invalidate();
        }
    }

    /**
     * This method stops the pipe animation only when user wins/loses.
     */
    public void stopAnimation(){
        animation = false;
    }

    /**
     * This method checks if the leftmost point of the pipe has crossed the screen.
     */
    public boolean isEndOfScreen(){
        return (leftMostPoint+(PIPE_WIDTH*4) < 0);
    }

    /**
     * This method gets the rectangles which represents the pipes.
     */
    public RectF[] getPipes(){

        RectF[] rectArray = {topBig,bottomSmall,topSmall,bottomBig};
        return rectArray;
    }
}



