package com.tourio.eklrew.tourio;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class TourListActivity extends ActionBarActivity {

    ListView tourListView;
    TourListAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        tourListView = (ListView) findViewById(R.id.tour_list);

        //Hardcoded tour list
        TourListItem tour = new TourListItem("SanFrancisco","Fun day in SF",2);
        List<TourListItem> tourList = new ArrayList<TourListItem>();
        for (int i=0;i<10;i++) {
            tourList.add(tour);
        }
        tourAdapter = new TourListAdapter(this,tourList);
        tourListView.setAdapter(tourAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
