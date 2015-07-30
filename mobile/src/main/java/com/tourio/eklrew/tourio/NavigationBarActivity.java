package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;


public abstract class NavigationBarActivity extends ActionBarActivity {

    public ListView drawerList;
    public FrameLayout contentFrame;
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        /*
        drawerToggle = new ActionBarDrawerToggle((Activity) this, drawerLayout, R.drawable.ic_drawer, 0, 0)
        {
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(R.string.menu);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        */

        drawerList = (ListView) findViewById(R.id.drawer_list);
        contentFrame = (FrameLayout) findViewById(R.id.content_frame);

        ArrayList<String> drawerItems = new ArrayList<String>();
        drawerItems.add("Profile");
        drawerItems.add("Browse");
        drawerItems.add("Create");
        drawerItems.add("$");
        drawerItems.add("About");
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.drawer_list_item, // The name of the layout ID.
                R.id.drawer_list_item_textview, // The ID of the textview to populate.
                drawerItems);
        drawerList.setAdapter(drawerAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Intent intent = null;
                Context context = NavigationBarActivity.this;
                switch(pos) {
                    case 0: intent = new Intent(context,MyProfileActivity.class); break;
                    case 1: intent = new Intent(context,TourListActivity.class); break;
                    case 2: intent = new Intent(context,CreateTourActivity.class); break;
                    case 3: intent = new Intent(context,CashoutActivity.class); break;
                    case 4: intent = new Intent(context,AboutActivity.class); break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation_bar, menu);
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
