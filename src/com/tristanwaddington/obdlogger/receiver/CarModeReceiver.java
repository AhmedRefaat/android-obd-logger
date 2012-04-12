package com.tristanwaddington.obdlogger.receiver;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CarModeReceiver extends BroadcastReceiver {
    private static final String TAG = "CarModeReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(UiModeManager.ACTION_ENTER_CAR_MODE)) {
                Log.d(TAG, "Entering car mode!");
            } else if (action.equals(UiModeManager.ACTION_EXIT_CAR_MODE)) {
                Log.d(TAG, "Leaving car mode!");
            }
        }
    }
}
