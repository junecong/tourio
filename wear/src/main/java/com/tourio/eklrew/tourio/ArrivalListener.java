package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Shawn on 8/2/2015.
 */
public class ArrivalListener extends WearableListenerService {

//    extends Service implements MessageApi.MessageListener

    private static final String READY_WEAR = "ready_wear";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Log", ">>>ArrivalListener created<<<");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("Arrival msg path:", messageEvent.getPath());

        if (messageEvent.getPath().equals(READY_WEAR)) {
            Log.d("Log", ">>>ArrivalListener msg received<<<");

            Intent readyIntent = new Intent(this, ReadyActivity.class);
            readyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(readyIntent);
        }
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
}
