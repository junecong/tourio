package com.tourio.eklrew.tourio;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Shawn on 7/31/2015.
 */
public class StartNavigationService extends Service {

    private GoogleApiClient mGoogleApiClient;
    private String transcriptionNodeId = null;

    /* Message from wear to mobile to start tour. */
    private static final String START_TOUR = "start_tour";

    /* Establishes GoogleApiClient connection. */
    @Override
    public int onStartCommand(Intent emptyIntent, int f, int id) {
        Log.d("Log", ">>>StartNavigationService started<<<");

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("Log", ">>>GoogleApiClient connected<<<");
                        initCapability();
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        // Do nothing
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // Do nothing
                    }
                })
                .addApi(Wearable.API)
                .build();
        this.mGoogleApiClient.connect();

        return START_STICKY;
    }

    /* Establishes GoogleApiClient connection. */
    private void initCapability() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CapabilityApi.GetCapabilityResult capResult = Wearable.CapabilityApi.getCapability(
                        mGoogleApiClient, START_TOUR, CapabilityApi.FILTER_REACHABLE).await();

                Collection<String> nodes = getNodes();
                Log.d("# nodes detected:", String.valueOf(nodes.size()));

                for (String node : nodes) {
                    transcriptionNodeId = node;
                }

                Log.d("node id", String.valueOf(transcriptionNodeId));

                Wearable.MessageApi.sendMessage(mGoogleApiClient, transcriptionNodeId,
                        START_TOUR, null).setResultCallback(
                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.d("Error:", ">>>Message Failed<<<");
                                }
                            }
                        });
            }
        }).start();
    }

    /* Gets nodes for GoogleApiClient. */
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
