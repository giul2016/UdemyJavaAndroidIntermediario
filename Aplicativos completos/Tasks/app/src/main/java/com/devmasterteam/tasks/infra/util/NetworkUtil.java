package com.devmasterteam.tasks.infra.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

    /**
     * Verifica se existe conexão com internet
     */
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
