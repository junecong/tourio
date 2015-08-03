package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by Shawn on 7/31/2015.
 */
public class TransitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_wear);

        Log.d("Log", ">>>(mobile) TransitActivity created<<<");

        Intent arrivalListenerIntent = new Intent(this, ArrivalListener.class);
        startService(arrivalListenerIntent);
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
}
