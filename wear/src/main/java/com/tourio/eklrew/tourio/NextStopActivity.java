package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Shawn on 7/25/2015.
 */
public class NextStopActivity extends Activity {
    private StopInfo nextStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_wear);

        nextStop = getIntent().getParcelableExtra("next_stop");

        //TODO: TextView stopType = (TextView) findViewById(R.id.TEXTVIEWID);
        if (nextStop.isFirstStop()) {
            //TODO: stopType.setText("First Stop");
        }
        if (nextStop.isLastStop()) {
            //TODO: stopType.setText("Last Stop");
        }
    }

    /* Called when user presses "Skip" button when user is presented with next stop. */
    public void skipTheStop(View view) {
        WearHelper.skipWithDialog(this);
    }

    /* Called when user presses "Go" button when user is presented with next stop. */
    public void goToNextStop(View view) {
        Intent notifyGoIntent = new Intent(this, NotifyPhoneService.class);
        notifyGoIntent.putExtra("go_or_skip","go");
        startService(notifyGoIntent);

        Intent transitIntent = new Intent(this, TransitActivity.class);
        transitIntent.putExtra("next_stop",nextStop);
        transitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(transitIntent);
    }
}
