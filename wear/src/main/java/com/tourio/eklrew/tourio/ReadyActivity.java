package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Shawn on 7/25/2015.
 */
public class ReadyActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_wear);
    }

    /* Called when user presses "Yes!" button when asked if ready for next stop. */
    public void showNextStop(View view) {
        Intent nextStopIntent = new Intent(this, NextStopActivity.class);
        startActivity(nextStopIntent);
    }
}
