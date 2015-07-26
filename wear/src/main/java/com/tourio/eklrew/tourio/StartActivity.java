package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.start_tour);
//            }
//        });
    }

    /* Called when user presses "Start!" button when asked if ready to start tour. */
    public void startTour(View view) {
        // Starts a service on phone that:
        // 1) Starts Google Maps to give user directions from current location to first stop
        // 2) Starts listener on phone for arrival confirmation from Google Maps (so can send
        //    user watch notification when user arrives at stop)
        // 3) Starts listener on phone that listens for user pressing "go" button on watch
    }
}
