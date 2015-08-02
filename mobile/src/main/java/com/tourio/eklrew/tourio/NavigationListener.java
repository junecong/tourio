package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Shawn on 7/31/2015.
 */
public class NavigationListener extends WearableListenerService {

    private static final String START_TOUR = "start_tour";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("NavigationListener Msg:", ">>>Received<<<");

        /* Starts Google Maps navigation for starting tour. Navigates user from current location
         * to first stop. */
        if (messageEvent.getPath().equals(START_TOUR)) {
            // TODO: Get address to tour's first stop from database

            Intent locationIntent = new Intent(this, LocationService.class);
            startService(locationIntent);

            Uri gmmIntentUri = Uri.parse("google.navigation:q=Exploratorium,+San+Francisco+California");
                // Currently has "bs" destination
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mapIntent);
        }
    }

}
