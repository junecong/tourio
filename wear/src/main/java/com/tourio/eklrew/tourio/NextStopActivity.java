package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Shawn on 7/25/2015.
 */
public class NextStopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_wear);
    }

    /* Called when user presses "Skip" button when user is presented with next stop. */
    public void skipTheStop(View view) {
        // Intent skipIntent = new Intent(this, NextStopActivity.class);
        // startActivity(skipIntent);

        // Note: If NextStopActivity grabs info from database & always grabs info of next indexed
        // stop, then starting itself should display next stop's info
    }

    /* Called when user presses "Go" button when user is presented with next stop. */
    public void goToNextStop(View view) {
        // TODO: Starts Google Maps to give user directions from current location to first stop
    }
}
