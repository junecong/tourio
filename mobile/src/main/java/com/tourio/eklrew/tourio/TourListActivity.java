package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListActivity extends NavigationBarActivity {

    ListView tourListView;
    TourListAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_tour_list,null));

        tourListView = (ListView) findViewById(R.id.tour_list);

        //Hardcoded tour list
        TourListItem tour = TourHelper.hardCodedTourListItem();
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
        final Button near_me = (Button) findViewById(R.id.near_me);
        near_me.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Sort by near me
                Log.e("button clicked", "response");

            }
        });

        final Button rating = (Button) findViewById(R.id.rating);
        rating.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Sort by rating
                Log.e("button clicked", "response");

            }
        });

        final Button duration = (Button) findViewById(R.id.duration);
        near_me.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Sort by duration
                Log.e("button clicked", "response 1");
            }
        });

    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_list, menu);
        Spinner s = (Spinner) menu.findItem(R.id.cities_spinner).getActionView();
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_list, android.R.layout.simple_spinner_dropdown_item); //  create the adapter from a StringArray
        s.setAdapter(mSpinnerAdapter);
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
