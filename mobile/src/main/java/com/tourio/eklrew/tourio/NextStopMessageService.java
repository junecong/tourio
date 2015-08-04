package com.tourio.eklrew.tourio;

import android.app.IntentService;
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
 * Created by Shawn on 8/2/2015.
 */
public class NextStopMessageService extends IntentService {

    private GoogleApiClient mGoogleApiClient;
    private String transcriptionNodeId = null;

    private String message;
    private String messagePath;
    private final String WEAR_RECEIVE_CAPABILITY = TourioHelper.SendMessageHelper.WEAR_RECEIVE_CAPABILITY;

    public NextStopMessageService() {
        super("NextStopMessageService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        boolean skipOrArrived = intent.getExtras().getBoolean("skip_or_arrived");
        messagePath = skipOrArrived? TourioHelper.SendMessageHelper.SKIP_MESSAGE_PATH :
                TourioHelper.SendMessageHelper.ARRIVED_MESSAGE_PATH;
        message = intent.getExtras().getString("stop_info");
        buildGoogleApiClient();
    }

    public void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("Log", ">>>NextStopMessageService GoogleApiClient connected<<<");
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
    }

    /* Establishes GoogleApiClient connection. */
    private void initCapability() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CapabilityApi.GetCapabilityResult capResult = Wearable.CapabilityApi.getCapability(
                        mGoogleApiClient, WEAR_RECEIVE_CAPABILITY, CapabilityApi.FILTER_REACHABLE).await();

                Collection<String> nodes = getNodes();
                Log.d("AS # nodes detected:", String.valueOf(nodes.size()));

                for (String node : nodes) {
                    transcriptionNodeId = node;
                }

                Log.d("AS node id", String.valueOf(transcriptionNodeId));

                Wearable.MessageApi.sendMessage(mGoogleApiClient, transcriptionNodeId,
                        messagePath, message.getBytes()).setResultCallback(
                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.d("Message failed", ""+sendMessageResult.getStatus().getStatusCode());
                                }
                                else {
                                    Log.d("message success",new String(message.getBytes()));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
