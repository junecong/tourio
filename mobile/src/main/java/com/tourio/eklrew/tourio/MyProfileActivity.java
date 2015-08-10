package com.tourio.eklrew.tourio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MyProfileActivity extends NavigationBarActivity {

    private ListView profileTourListView;
    private ArrayList<TourListItem> tours;
    private TourListAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_my_profile, null));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_profile, menu);
        initVars();
        return true;
    }

    private void initVars() {
        profileTourListView = (ListView) findViewById(R.id.profile_tour_list);
        tourAdapter = new TourListAdapter(MyProfileActivity.this,
                new ArrayList<TourListItem>());
        profileTourListView.setAdapter(tourAdapter);

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
