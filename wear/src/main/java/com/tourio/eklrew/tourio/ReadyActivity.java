package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Shawn on 7/25/2015.
 */
public class ReadyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_wear);

        Log.d("Log", ">>>ReadyActivity created<<<");
    }

    /* Called when user presses "Yes!" button when asked if ready for next stop. */
    public void showNextStop(View view) {
        Intent nextStopIntent = new Intent(this, NextStopActivity.class);
        startActivity(nextStopIntent);
    }
}
