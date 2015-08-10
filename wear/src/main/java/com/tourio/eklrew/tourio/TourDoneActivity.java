package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TourDoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_done);
    }

    public void tip(View view) {
        Intent tipIntent = new Intent(this,TipActivity.class);
        startActivity(tipIntent);
    }
}
