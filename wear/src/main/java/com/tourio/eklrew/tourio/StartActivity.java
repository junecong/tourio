package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
    }

    /* Called when user presses "Start!" button when asked if ready to start tour. */
    public void startTour(View view) {
        WearHelper.skipWithoutDialog(this);

//        Intent transitIntent = new Intent(this, TransitActivity.class);
//        transitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(transitIntent);
    }
}
