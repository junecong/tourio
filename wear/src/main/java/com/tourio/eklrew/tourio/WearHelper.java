package com.tourio.eklrew.tourio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Prud on 8/3/2015.
 */
public class WearHelper {
    public static void skipWithDialog(final Context context) {
        AlertDialog confirmSkip =
                new AlertDialog.Builder(context)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Do you really want to skip")
                        .setMessage("Do you really want to skip?")
                        .setPositiveButton("Skip", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                skipWithoutDialog(context);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

        TextView alertMsg = (TextView) confirmSkip.findViewById(android.R.id.message);
        alertMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        alertMsg.setTextColor(Color.parseColor("#FF6344"));
        alertMsg.setTypeface(null, Typeface.BOLD);
        alertMsg.setGravity(Gravity.CENTER);

        Button posButton = confirmSkip.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTypeface(null, Typeface.BOLD);
        posButton.setTextColor(Color.parseColor("#FF6344"));

        Button negButton = confirmSkip.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTypeface(null, Typeface.BOLD);
        negButton.setTextColor(Color.parseColor("#FF6344"));
    }

    public static void skipLastStop(final Context context) {
        AlertDialog confirmSkip =
                new AlertDialog.Builder(context)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Do you really want to skip")
                        .setMessage("Do you really want to skip?")
                        .setPositiveButton("Skip", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent doneIntent = new Intent(context,TourDoneActivity.class);
                                context.startActivity(doneIntent);
                                doneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

        TextView alertMsg = (TextView) confirmSkip.findViewById(android.R.id.message);
        alertMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        alertMsg.setTextColor(Color.parseColor("#FF6344"));
        alertMsg.setTypeface(null, Typeface.BOLD);
        alertMsg.setGravity(Gravity.CENTER);

        Button posButton = confirmSkip.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTypeface(null, Typeface.BOLD);
        posButton.setTextColor(Color.parseColor("#FF6344"));

        Button negButton = confirmSkip.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTypeface(null, Typeface.BOLD);
        negButton.setTextColor(Color.parseColor("#FF6344"));
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
        int divider2Index = message.indexOf(DIVIDER, divider1Index + 1);
        String stopName = message.substring(0, divider1Index);
        String stopDescription = message.substring(divider1Index + 1,divider2Index);
        int status = Integer.parseInt(message.substring(divider2Index + 1));
        Log.d("stop info parsed","stop name: "+stopName+".. stop description: "+stopDescription+".. stop status: "+status);
        return new StopInfo(stopName,stopDescription,status);
    }
}
