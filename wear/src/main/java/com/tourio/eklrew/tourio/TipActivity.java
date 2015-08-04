package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class TipActivity extends Activity {

    int tip;
    NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        np= (NumberPicker) findViewById(R.id.tip_numberPicker);
        np.setMaxValue(9);
        np.setMinValue(0);
        np.setValue(5);

        TextView t = (TextView) findViewById(R.id.tip_text);
        t.setText("Tip the tour creator!");

//        Button b = (Button) findViewById(R.id.tip_button);

    }

    public void onClickTip(View view){
        Intent intent = new Intent(this, RateTourActivity.class);
        tip = np.getValue();
        intent.putExtra("int_value", tip);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tip, menu);
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
