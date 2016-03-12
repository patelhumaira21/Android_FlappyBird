/**
 * File Name : FlappyBirdActivity.java
 * Created by: Humaira Patel
 * Date: 02/18/2016
 */
package edu.sdsu.cs.cs646.assign3;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * This class implements a version of Flappy Bird Game.It handles
 * the animation of the bird and pipes and detects collision. This also
 * returns the result back to the main activity.
 *
 */
public class FlappyBirdActivity extends ActionBarActivity implements View.OnTouchListener {

    private AnimationDrawable flappyAnimation;
    private AnimationDrawable baseAnimation;
    private ImageView bird;
    private ImageView base;
    private float screenHeight;
    private DrawingPipe pipeView;
    private boolean gameStarted = false;
    private boolean gameWon = false;
    private ObjectAnimator birdDownAnimation = null;
    private ObjectAnimator birdUpAnimation = null;

    /**
     * Overriding the onCreate() of the parent class.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_bird);

        // Setting up the bird animation.
        bird = (ImageView) findViewById(R.id.bird);
        bird.setBackgroundResource(R.anim.wing_motion);
        this.flappyAnimation = (AnimationDrawable) bird.getBackground();
        this.flappyAnimation.start();

        // Setting up the animation of the base.
        base = (ImageView) findViewById(R.id.base);
        base.setBackgroundResource(R.anim.base_motion);
        this.baseAnimation = (AnimationDrawable) base.getBackground();
        this.baseAnimation.start();

        // Retrieving the screen height.
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = screenSize.y;
        Log.d("Screen height", ""+screenHeight);

        // Setting up the onTouchListener
        View touchView = findViewById(R.id.background);
        touchView.setOnTouchListener(this);

        // Setting up the pipes in the background.
        pipeView = (DrawingPipe)findViewById(R.id.drawingPipe);
    }

    /**
     * This method implements the actions to be performed when the user
     * touches the screen.
     */
    public boolean onTouch(View v, MotionEvent event){

        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        if(actionCode == MotionEvent.ACTION_DOWN){
            // Start pipe animation when the user touches the screen for the first time.
            if(!gameStarted) {
                gameStarted = true;
                pipeView.startAnimation(true);
            }
            // Start the bird up animation.
            startUpAnimation();
        }
        return true;
    }

    /**
     * This method detects collision of the bird with pipes as well as
     * screen top and bottom.
     */
    private boolean isCollision() {

        float birdX = bird.getX();
        float birdY = bird.getY();
        float birdHeight = bird.getHeight();
        float birdWidth = bird.getWidth();

        // Get the coordinates of the pipes.
        RectF [] r = pipeView.getPipes();
        for(int i=0; i<r.length;i++){
            // Check for collision with pipes.
            if(r[i].intersects(birdX, birdY, birdX + birdWidth, birdY + birdHeight)){
                Log.i("Bird Loc on Collision","Left:"+birdX+" Top:"+birdY+" Right:"+(birdX+birdWidth)+" Bottom:"+(birdY+birdHeight));
                Log.i("Pipe Location",""+r[i]);
                return true;
            }
        }
        // Detecting screen top collision or base collision
        return (birdY <= 0) || ( birdY+birdHeight) >= (base.getY());
    }

    /**
     * This method clears all animations and removes all listeners
     * when the game is won/lost.
     */
    private void clearAllAnimations(){

        // clear down animation
        if(birdDownAnimation != null) {
            birdDownAnimation.removeAllListeners();
            birdDownAnimation.removeAllUpdateListeners();
            birdDownAnimation.cancel();
        }
        // clear up animation
        if(birdUpAnimation != null) {
            birdUpAnimation.removeAllListeners();
            birdUpAnimation.removeAllUpdateListeners();
            birdUpAnimation.cancel();
        }
        // clear flappy animation
        flappyAnimation.stop();
        // clear pipe animation
        pipeView.stopAnimation();
    }

    /**
     * This method sends the game result(won/lost) to the HomeActivity.
     */
    public void goToHome() {

        Log.i("Going home, gamewon", ""+gameWon);
        Intent goHome = getIntent();
        goHome.putExtra("message", gameWon ? "You Won!!!":"You Lose...\n Play Again!");
        setResult(RESULT_OK, goHome);
        finish();
    }

    /**
     * This method starts the upward motion of the bird when the user touches the screen.
     */
    public void startUpAnimation() {

        // Stop the current up animation
        if(birdUpAnimation != null && birdUpAnimation.isStarted()){
            birdUpAnimation.cancel();
        }

        // Stop the current down animation
        if(birdDownAnimation != null && birdDownAnimation.isStarted()){
            birdDownAnimation.cancel();
        }

        // Start up animation
        Log.i("Bird Position Downward", "" + bird.getY());
        birdUpAnimation = ObjectAnimator.ofFloat(bird, "y", bird.getY()-150);
        birdUpAnimation.setDuration(500);
        birdUpAnimation.addUpdateListener(myUpdateListener);
        birdUpAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            // Start down animation of the bird when the birdUpAnimation has ended.
            @Override
            public void onAnimationEnd(Animator animation) {
                if (birdDownAnimation != null && birdDownAnimation.isStarted()) {
                    birdDownAnimation.cancel();
                }
                startDownAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        birdUpAnimation.start();
    }

    /**
     * This method starts the downward motion of the bird when the user is not touching the screen.
     */
    private void startDownAnimation() {

        birdDownAnimation = ObjectAnimator.ofFloat(bird, "y", screenHeight);
        birdDownAnimation.setDuration(2000);
        Log.i("Bird Position Downward", "" + bird.getY());
        birdDownAnimation.addUpdateListener(myUpdateListener);
        birdDownAnimation.start();
    }

    /**
     * Setting up the UpdateListener.
     */
    private ValueAnimator.AnimatorUpdateListener myUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // User wins the game if the leftmost pipe has crossed the screen.
            gameWon = pipeView.isEndOfScreen();
            // Clear animation is user loses/wins the game.
            if (gameWon || isCollision()) {
                clearAllAnimations();
                goToHome();
            }
        }
    };
}

