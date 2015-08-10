package com.tourio.eklrew.tourio;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

//Listens for info about the next stop from the phone
//started by the Transit and NextStop activities
public class SkipListenerService extends Service implements MessageApi.MessageListener{
    public static final String SKIP_MESSAGE_PATH = "skip_next_stop_info";
    public static final String DONE_MESSAGE_PATH = "tour_done";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        startMessageListener();
        return START_STICKY;
    }

    private void startMessageListener() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        // Do something
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        // Do something
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // Do something
                    }
                })
                .addApi(Wearable.API)
                .build();
        this.mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();
        Log.d("message received",messagePath);
        if (messagePath.equals(SKIP_MESSAGE_PATH)) {
            Log.d("message received",new String(messageEvent.getData()));
            String message = new String(messageEvent.getData());
            StopInfo nextStop = WearHelper.stringToStopInfo(message);
            Intent nextStopIntent = new Intent(this,NextStopActivity.class);
            nextStopIntent.putExtra("next_stop", nextStop);
            nextStopIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nextStopIntent);
            stopSelf();
        }

        if (messagePath.equals(DONE_MESSAGE_PATH)) {
            Intent doneIntent = new Intent(this,TourDoneActivity.class);
            startActivity(doneIntent);
            doneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
}
