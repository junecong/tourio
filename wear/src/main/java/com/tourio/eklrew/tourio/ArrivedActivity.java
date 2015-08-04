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
public class ArrivedActivity extends Activity {
    StopInfo currStop;
    StopInfo nextStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_wear);

        Intent intent = getIntent();
        currStop = intent.getParcelableExtra("curr_stop");
        nextStop = intent.getParcelableExtra("next_stop");

        setViews();
    }

    public void setViews() {
        TextView stopNameView = (TextView) findViewById(R.id.stop_text);
        stopNameView.setText(currStop.getName());
    }

    /* Called when user presses "Yes!" button when asked if ready for next stop. */
    public void showNextStop(View view) {
        Intent nextStopIntent = new Intent(this, NextStopActivity.class);
        nextStopIntent.putExtra("next_stop",nextStop);
        nextStopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextStopIntent);
    }
}