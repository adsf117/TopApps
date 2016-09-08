package com.pruebaandroid.topapps.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andres on 06/09/2016.
 */
public class Utils {

    public static Boolean isConnected(Context context)
    {
        Boolean rtaisConnected  =false;
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            rtaisConnected=true;
        }
        return rtaisConnected;
    }
}
