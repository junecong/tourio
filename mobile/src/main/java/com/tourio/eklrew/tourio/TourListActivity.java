package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListActivity extends ActionBarActivity {

    ListView tourListView;
    TourListAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        tourListView = (ListView) findViewById(R.id.tour_list);

        //Hardcoded tour list
        TourListItem tour = TourHelper.hardCodedTour();
        List<TourListItem> tourList = new ArrayList<TourListItem>();
        for (int i=0;i<10;i++) {
            tourList.add(tour);
        }
        tourAdapter = new TourListAdapter(this,tourList);
        tourListView.setAdapter(tourAdapter);

        tourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TourListItem item = (TourListItem) tourAdapter.getItem(position);
                Intent detailIntent = new Intent(TourListActivity.this, DetailTourActivity.class)
                        .putExtra("tour_id", item.getTourId());
                startActivity(detailIntent);
            }
        });
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
