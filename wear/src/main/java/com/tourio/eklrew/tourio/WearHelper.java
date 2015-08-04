package com.tourio.eklrew.tourio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Prud on 8/3/2015.
 */
public class WearHelper {
    public static void skipWithDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("SKIP")
                .setMessage("Do you really want to skip?")
                .setPositiveButton("Skip", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skipWithoutDialog(context);
                        Toast.makeText(context,"Generated next stop",Toast.LENGTH_LONG).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public static void skipWithoutDialog(Context context) {
        //notify phone of skip
        Intent skipIntent = new Intent(context, NotifyPhoneService.class);
        skipIntent.putExtra("go_or_skip","skip");
        context.startService(skipIntent);

        //wait for phone's response to skip
        Intent skipListenerIntent = new Intent(context,SkipListenerService.class);
        context.startService(skipListenerIntent);
    }

    public static final char DIVIDER = '|';
    public static StopInfo stringToStopInfo(String message) {
        int divider1Index = message.indexOf(DIVIDER);
        int divider2Index = message.indexOf("is", message.indexOf("is") + 1);
        String stopName = message.substring(0, divider1Index);
        String stopDescription = message.substring(divider1Index + 1,divider2Index);
        int status = Integer.parseInt(message.substring(divider2Index + 1));
        return new StopInfo(stopName,stopDescription,status);
    }
}
