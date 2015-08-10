package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Shawn on 7/31/2015.
 */
public class TransitActivity extends Activity implements MessageApi.MessageListener {
    public static final String ARRIVED_MESSAGE_PATH = "arrived_info";
    public static final String DONE_MESSAGE_PATH = "tour_done";
    private StopInfo currStop;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_wear);

        currStop = getIntent().getParcelableExtra("next_stop");

        setViews();

        startMessageListener();
    }

    public void setViews() {
        TextView stopNameView = (TextView) findViewById(R.id.stop_text);
        stopNameView.setText(currStop.getName());
    }

    private void startMessageListener() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        // Do something
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        // Do something
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // Do something
                    }
                })
                .addApi(Wearable.API)
                .build();
        this.mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    public void viewMap(View view) {
        Intent viewMapIntent = new Intent(this, WearMapActivity.class);
        startActivity(viewMapIntent);
    }

    public void viewDirections(View view) {
        Intent dirIntent = new Intent(Intent.ACTION_MAIN);
        dirIntent.addCategory(Intent.CATEGORY_HOME);
        dirIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dirIntent);
    }

    public void skipThisStop(View view) {
        if (currStop.isLastStop()) {
            WearHelper.skipLastStop(this);
        }
        else {
            WearHelper.skipWithDialog(this);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();
        if (messagePath.equals(ARRIVED_MESSAGE_PATH)) {
            String message = new String(messageEvent.getData());
            StopInfo nextStop = WearHelper.stringToStopInfo(message);
            Intent arriveIntent = new Intent(this,ArrivedActivity.class);
            arriveIntent.putExtra("curr_stop",currStop);
            arriveIntent.putExtra("next_stop",nextStop);
            startActivity(arriveIntent);
        }

        if (messagePath.equals(DONE_MESSAGE_PATH)) {
            Intent doneIntent = new Intent(this,TourDoneActivity.class);
            startActivity(doneIntent);
        }
    }
}