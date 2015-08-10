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
 * Created by Shawn on 7/31/2015.
 */

/**
sends message to phone
if message path is START_GPS, phone will route to next stop
if message path is SKIP_STOP, phone will skip and send next stop info
*/

//notifies phone if go or skip was pressed
public class NotifyPhoneService extends IntentService {

    private GoogleApiClient mGoogleApiClient;
    private String transcriptionNodeId = null;

    /* Message from wear to mobile to start tour. */
    private static final String START_GPS = "start_gps";
    private static final String SKIP_STOP = "skip_stop";
    private String capabilityPath;
    String intentString;

    public NotifyPhoneService() {
        super("NotifyPhoneService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        intentString = intent.getExtras().getString("go_or_skip");
        capabilityPath = intentString.equals("go")? START_GPS : SKIP_STOP;
        Log.d("capability path",capabilityPath);

        buildGoogleApiClient();
    }

    /* Establishes GoogleApiClient connection. */
//    @Override
//    public int onStartCommand(Intent intent, int f, int id) {
//        Log.d("Log", ">>>Initiated service for sending msg to mobile to start gps<<<");
//
//        return START_STICKY;
//    }

    private void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("Log", ">>>NotifyPhoneService GoogleApiClient connected<<<");
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
                        mGoogleApiClient, capabilityPath, CapabilityApi.FILTER_REACHABLE).await();

                Collection<String> nodes = getNodes();
                Log.d("# nodes detected:", String.valueOf(nodes.size()));

                for (String node : nodes) {
                    transcriptionNodeId = node;
                }

                Log.d("node id detected", String.valueOf(transcriptionNodeId));

                Wearable.MessageApi.sendMessage(mGoogleApiClient, transcriptionNodeId,
                        capabilityPath, null).setResultCallback(
                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.e("Error:", "__________Message Failed__________");
                                }
                                else {
                                    Log.d("message send status","sent");
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
            if (node.isNearby()) {
                results.add(node.getId());
            }
        }
        return results;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
