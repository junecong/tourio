package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RateTourActivity extends Activity {

    int tip;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_tour);

        Intent intent = getIntent();
        tip = intent.getIntExtra("int_value", 0);

        star1 = (ImageView) findViewById(R.id.one_star);
        star2 = (ImageView) findViewById(R.id.two_star);
        star3 = (ImageView) findViewById(R.id.three_star);
        star4 = (ImageView) findViewById(R.id.four_star);
        star5 = (ImageView) findViewById(R.id.five_star);

    }

    public void onClickOneStar(View view){
        //Do something

        star1.setImageResource(R.mipmap.star_button);

        star2.setImageResource(R.mipmap.empty_star);
        star3.setImageResource(R.mipmap.empty_star);
        star4.setImageResource(R.mipmap.empty_star);
        star5.setImageResource(R.mipmap.empty_star);
    }
    public void onClickTwoStars(View view){
        //Do something
        star1.setImageResource(R.mipmap.star_button);
        star2.setImageResource(R.mipmap.star_button);

        star3.setImageResource(R.mipmap.empty_star);
        star4.setImageResource(R.mipmap.empty_star);
        star5.setImageResource(R.mipmap.empty_star);
    }
    public void onClickThreeStars(View view){
        //Do something
        star1.setImageResource(R.mipmap.star_button);
        star2.setImageResource(R.mipmap.star_button);
        star3.setImageResource(R.mipmap.star_button);

        star4.setImageResource(R.mipmap.empty_star);
        star5.setImageResource(R.mipmap.empty_star);
    }
    public void onClickFourStars(View view){
        //Do something
        star1.setImageResource(R.mipmap.star_button);
        star2.setImageResource(R.mipmap.star_button);
        star3.setImageResource(R.mipmap.star_button);
        star4.setImageResource(R.mipmap.star_button);

        star5.setImageResource(R.mipmap.empty_star);
    }
    public void onClickFiveStars(View view){
        //Do something
        star1.setImageResource(R.mipmap.star_button);
        star2.setImageResource(R.mipmap.star_button);
        star3.setImageResource(R.mipmap.star_button);
        star4.setImageResource(R.mipmap.star_button);
        star5.setImageResource(R.mipmap.star_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate_tour, menu);
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

    public void nextTime (View view) {
        // Below for testing purposes
        Intent testIntent = new Intent(this, ThankYouActivity.class);
        startActivity(testIntent);
    }
}
