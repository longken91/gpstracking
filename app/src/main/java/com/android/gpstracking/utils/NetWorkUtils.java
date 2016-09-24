package com.android.gpstracking.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by natuan on 22/09/2016.
 */
public class NetWorkUtils {

    public static boolean isOnline(Context context) {
        Logger.enter();
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (null == networkInfo || !networkInfo.isConnectedOrConnecting()) {
            Logger.d("No network connection");
            return false;
        }
        Logger.exit();
        return true;
    }
}
