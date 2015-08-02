package com.tourio.eklrew.tourio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Shawn on 8/1/2015.
 */
public class LocationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
