package com.android.gpstracking.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.android.gpstracking.utils.Logger;

/**
 * Created by natuan on 28/09/2016.
 */

public class TrackerAlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.enter();
        context.startService(new Intent(context, TrackingService.class));
        Logger.exit();
    }
}
