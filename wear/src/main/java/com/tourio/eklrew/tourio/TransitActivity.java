package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Shawn on 7/31/2015.
 */
public class TransitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_wear);
    }

    public void viewMap(View view) {
        Intent viewMapIntent = new Intent(this, WearMapActivity.class);
        startActivity(viewMapIntent);
    }
}
