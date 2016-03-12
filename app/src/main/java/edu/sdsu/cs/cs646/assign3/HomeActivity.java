/**
 * File Name : HomeActivity.java
 * Created by: Humaira Patel
 * Date: 02/18/2016
 *
 */
package edu.sdsu.cs.cs646.assign3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * This class is the entry point to the app and serves as the home
 * screen. It is created so that the user can start a new game and
 * know whether game was won or lost.
 */
public class HomeActivity extends ActionBarActivity {

    protected final int REQUEST_CODE = 123;
    private TextView status;

    /**
     * Overriding the onCreate() of the parent class.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("Create method called ", this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting the game status.
        status = (TextView)findViewById(R.id.status);
    }

    /**
     * This method navigates to the FlappyBirdActivity.
     */
    public void startGame(View startButton){

        Intent goIntent = new Intent(this, FlappyBirdActivity.class);
        startActivityForResult(goIntent, REQUEST_CODE);
    }

    /**
     *
     * Overriding the onActivityResult method. This method receives the status whether
     * the user lost/won the game.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(RESULT_OK == resultCode) {
                String result = data.getStringExtra("message");
                Log.i("Game Status",result);
                status.setText(result);
            }
        }
    }
}
