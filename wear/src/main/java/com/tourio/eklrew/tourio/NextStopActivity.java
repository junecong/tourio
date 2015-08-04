package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        setViews();
    }

    public void setViews() {
        TextView stopType = (TextView) findViewById(R.id.next_stop_text);
        TextView nextStopName = (TextView) findViewById(R.id.next_stop_name);
        TextView nextStopDescription = (TextView) findViewById(R.id.next_stop_description);

        if (nextStop.isFirstStop()) {
            stopType.setText("First Stop");
        }
        if (nextStop.isLastStop()) {
            stopType.setText("Last Stop");
        }
        nextStopName.setText(nextStop.getName());
        nextStopDescription.setText(nextStop.getDescription());

    }

    /* Called when user presses "Skip" button when user is presented with next stop. */
    public void skipTheStop(View view) {
        if (nextStop.isLastStop()) {
            WearHelper.skipLastStop(this);
        }
        else {
            WearHelper.skipWithDialog(this);
        }
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
